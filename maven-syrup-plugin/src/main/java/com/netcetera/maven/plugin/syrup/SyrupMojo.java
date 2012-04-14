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
package com.netcetera.maven.plugin.syrup;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactCollector;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.resolver.filter.ScopeArtifactFilter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilder;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilderException;
import org.apache.maven.shared.dependency.tree.traversal.CollectingDependencyNodeVisitor;

import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * Goal which touches a timestamp file.
 * 
 * @goal extract
 * @phase process-sources
 */
public class SyrupMojo extends AbstractMojo {

  /**
   * The Root artefact to start with.
   * 
   * @parameter
   * @required
   * @readonly
   */
  private DependencyGraph dependencyGraph;

  /**
   * The Maven project.
   * 
   * @parameter default-value="${project}"
   * @required
   * @readonly
   */
  private MavenProject project;

  /**
   * ArtifactFactory.
   * 
   * @component
   */
  private ArtifactFactory artifactFactory;

  /**
   * ArtifactResolver.
   * 
   * @component
   */
  private ArtifactResolver artifactResolver;

  /**
   * Local repos.
   * 
   * @parameter expression="${localRepository}"
   * @readonly
   * @required
   */
  private ArtifactRepository localRepository;

  /**
   * @component
   * @required
   * @readonly
   */
  private ArtifactMetadataSource artifactMetadataSource;


  /**
   * @component
   * @required
   * @readonly
   */
  private ArtifactCollector artifactCollector;


  /**
   * @component
   */
  private DependencyTreeBuilder treeBuilder;

  /** @component */
  protected MavenProjectBuilder projectBuilder;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    DependencyHelper helper = new DependencyHelper(artifactFactory, artifactResolver,
        localRepository, projectBuilder, project.getRemoteArtifactRepositories());
    MavenProject rootProject = helper.findMavenProject(dependencyGraph);
    printDependenciesForArtifact(rootProject);
  }

  private void printDependenciesForArtifact(MavenProject rootProject) {
    try {
      ArtifactFilter artifactFilter = new ScopeArtifactFilter(null);

      DependencyNode rootNode = treeBuilder.buildDependencyTree(rootProject, localRepository,
          artifactFactory, artifactMetadataSource, artifactFilter, artifactCollector);

      CollectingDependencyNodeVisitor visitor = new CollectingDependencyNodeVisitor();

      rootNode.accept(visitor);

      List<DependencyNode> nodes = visitor.getNodes();
      System.out.print("http://yuml.me/diagram/scruffy/class/");
      for (DependencyNode dependencyNode : nodes) {
        int state = dependencyNode.getState();
        Artifact artifact = dependencyNode.getArtifact();
        // if (state == DependencyNode.INCLUDED) {
        for (String include : dependencyGraph.getIncludes()) {
          if (artifact.getArtifactId().matches(include) && dependencyNode.getParent() != null) {
            String edge = "[" + dependencyNode.getParent().getArtifact().getArtifactId() + "]->["
                + artifact.getArtifactId() + "]";
            edge = edge.replaceAll("-", "");
            System.out.println(edge);
          }
          // }
        }
      }
      renderGraph();
    } catch (DependencyTreeBuilderException e) {
      e.printStackTrace();
    }
  }

  private void renderGraph() {
    BufferedImage image = new BufferedImage(350, 350, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = image.createGraphics();
    g2.setBackground(Color.white);
    g2.fillRect(0, 0, 350, 350);
    g2.setBackground(Color.RED);
    g2.setColor(Color.black);
    g2.fillRect(10, 10, 20, 20);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

    Graph g = new DirectedSparseGraph();

    g.addVertex(1);
    g.addVertex(2);
    g.addVertex(3);

    g.addEdge("Edge_1", 1, 2);
    g.addEdge("Edge_2", 2, 3);
    g.addEdge("Edge_3", 3, 1);

    SpringLayout2 layout = new SpringLayout2(g);

    layout.setSize(new Dimension(350, 350));
    VisualizationViewer vv = new VisualizationViewer(layout);
    vv.setPreferredSize(new Dimension(350, 350)); // Sets the viewing area size
    try {
      writeImageFile(new File("target/test.png"), "png", vv, g);
    } catch (IOException e) {

    }
  }

  public void writeImageFile(File file, String format, VisualizationViewer vv, Graph graph)
      throws IOException {
    Color bg = vv.getBackground();
    BufferedImage bi = new BufferedImage(vv.getWidth(), vv.getHeight(), BufferedImage.TYPE_INT_BGR);
    Graphics2D graphics = bi.createGraphics();
    graphics.setColor(bg);
    graphics.fillRect(0, 0, vv.getWidth(), vv.getHeight());

    vv.setVisible(false);

    vv.paint(graphics);

    ImageIO.write(bi, format, file);

  }


}
