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

import org.apache.maven.plugin.logging.SystemStreamLog;

import com.netcetera.maven.plugin.syrup.dependency.graph.dot.DotRenderer;

/**
 * Factory that instantiates the appropriate renderer given given in rendererType.
 */
public class GraphRendererFactory {

  private static final SystemStreamLog LOGGER = new SystemStreamLog();

  /**
   * Instantiates the given renderer.
   * 
   * @param rendererType renderer to instantiate
   * @return new instance of the renderer
   */
  public IGraphRenderer getGraphRenderer(GraphRendererType rendererType) {
    LOGGER.info("Creating graph renderer: " + rendererType);

    switch (rendererType) {
      case dot:
      case googleDot:
        return new DotRenderer();
    }
    return null;
  }
}
