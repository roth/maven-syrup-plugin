/*
 * Copyright 2001-2012 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netcetera.maven.plugin.syrup.dependency.graph.dot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.apache.maven.shared.dependency.tree.traversal.CollectingDependencyNodeVisitor;

import com.netcetera.maven.plugin.syrup.dependency.DependencyHelper;
import com.netcetera.maven.plugin.syrup.dependency.GraphConfiguration;
import com.netcetera.maven.plugin.syrup.dependency.graph.GraphRendererType;
import com.netcetera.maven.plugin.syrup.dependency.graph.IGraphRenderer;

/**
 * Renderer that creates a dot output file and calls one of the given interpreters to create a
 * graphical output file.
 */
public class DotRenderer implements IGraphRenderer {

  private static final SystemStreamLog LOGGER = new SystemStreamLog();

  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm");

  public void createDependencyGraph(GraphConfiguration config, DependencyNode rootNode)
      throws IOException {
    CollectingDependencyNodeVisitor visitor = new CollectingDependencyNodeVisitor();
    rootNode.accept(visitor);

    List<DependencyNode> nodes = visitor.getNodes();

    File outputDirectory = new File(config.getOutputDirectory());
    if (!outputDirectory.exists()) {
      LOGGER.info("output directory " + outputDirectory.getAbsolutePath()
          + " does not exist yet. Creating it");
      FileUtils.forceMkdir(outputDirectory);
    }
    File dotFile = new File(outputDirectory, config.getGraphName() + ".dot");
    BufferedWriter writer = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(dotFile)));
    Map<String, Artifact> artifactMap = new HashMap<String, Artifact>();

    String title = config.getGroupId() + ":" + config.getArtifactId() + ":" + config.getVersion()
        + " from " + DATE_FORMAT.format(new Date());
    writer.write("digraph  {\n");
    writer.write("   label=\"" + title + "\";\n");
    writer.write("   labelloc=t;\n");
    writer.write("   fontsize=18.0;\n");
    for (DependencyNode dependencyNode : nodes) {
      DependencyNode parent = dependencyNode.getParent();
      if (parent != null && DependencyHelper.isIncluded(config, dependencyNode.getArtifact())
          && DependencyHelper.isIncluded(config, parent.getArtifact())) {
        Artifact toArtifact = dependencyNode.getArtifact();
        Artifact fromArtifact = parent.getArtifact();
        String toNodeName = getNodeName(toArtifact);
        artifactMap.put(toNodeName, toArtifact);
        String fromNodeName = getNodeName(fromArtifact);
        artifactMap.put(fromNodeName, fromArtifact);
        writer.write("   " + fromNodeName + " -> " + toNodeName + ";\n");
        LOGGER.debug("Edge from " + fromNodeName + " to " + toNodeName);
      }
    }
    for (Map.Entry<String, Artifact> entry : artifactMap.entrySet()) {
      writer.write("   " + entry.getKey() + " [color=blue shape=box label=\""
          + entry.getValue().getArtifactId() + "\"]\n");
    }
    writer.write("}");
    writer.close();

    DotInterpreterFactory factory = new DotInterpreterFactory();
    GraphRendererType rendererType = GraphRendererType.valueOf(config.getRenderer());
    IDotInterpreter interpreter = factory.getInterpreter(rendererType);
    interpreter.convertToImage(config);
  }

  private String getNodeName(Artifact artifact) {
    return artifact.getArtifactId().replaceAll("[.-]", "_");
  }

}
