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
package com.netcetera.maven.plugin.syrup.dependency.graph;

import java.io.IOException;
import java.util.List;

import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.apache.maven.shared.dependency.tree.traversal.CollectingDependencyNodeVisitor;

import com.netcetera.maven.plugin.syrup.dependency.GraphConfiguration;

/**
 * 
 */
public class RecursiveGraphRender implements IGraphRenderer {

  public void createDependencyGraph(GraphConfiguration config, DependencyNode rootNode)
      throws IOException {
    CollectingDependencyNodeVisitor visitor = new CollectingDependencyNodeVisitor();
    rootNode.accept(visitor);

    List<DependencyNode> nodes = visitor.getNodes();

    for (DependencyNode node : nodes) {

    }
  }

}
