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
package com.netcetera.maven.plugin.syrup.dependency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.maven.plugin.logging.SystemStreamLog;

/**
 * Configuration object.
 */
public class GraphConfiguration {

  private static final SystemStreamLog LOGGER = new SystemStreamLog();

  private String groupId;

  private String artifactId;

  private String version;

  private List<String> includes;

  private String outputDirectory;

  private String graphName;

  private String renderer;

  private String rendererPath;

  private String outputType;

  private String graphWidth;

  private String graphHeight;

  private Boolean recursive;

  /**
   * Default constructor for maven.
   */
  public GraphConfiguration() {

  }

  /**
   * Copy constructor.
   * 
   * @param config configuration to copy.
   */
  public GraphConfiguration(GraphConfiguration config) {
    this.groupId = config.groupId;
    this.artifactId = config.artifactId;
    this.version = config.version;
    this.includes = Collections.unmodifiableList(config.includes);
    this.outputDirectory = config.outputDirectory;
    this.graphName = config.graphName;
    this.renderer = config.renderer;
    this.rendererPath = config.rendererPath;
    this.outputType = config.outputType;
    this.graphWidth = config.graphWidth;
    this.graphHeight = config.graphHeight;
    this.recursive = config.recursive;

  }

  public String getGroupId() {
    return groupId;
  }


  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }


  public String getArtifactId() {
    return artifactId;
  }


  public void setArtifactId(String artifactId) {
    this.artifactId = artifactId;
  }


  public String getVersion() {
    return version;
  }


  public void setVersion(String version) {
    this.version = version;
  }

  public List<String> getIncludes() {
    if (includes == null) {
      return new ArrayList<String>();
    }
    return includes;
  }


  public void setIncludes(List<String> includes) {
    this.includes = includes;
  }


  /**
   * @return Returns the renderer.
   */
  public String getRenderer() {
    return renderer;
  }


  /**
   * @param renderer The renderer to set.
   */
  public void setRenderer(String renderer) {
    this.renderer = renderer;
  }


  /**
   * @return Returns the outputDirectory.
   */
  public String getOutputDirectory() {
    return outputDirectory;
  }


  /**
   * @param outputDirectory The outputDirectory to set.
   */
  public void setOutputDirectory(String outputDirectory) {
    this.outputDirectory = outputDirectory;
  }


  /**
   * @return Returns the graphName.
   */
  public String getGraphName() {
    return graphName;
  }


  /**
   * @param graphName The graphName to set.
   */
  public void setGraphName(String graphName) {
    this.graphName = graphName;
  }


  /**
   * @return Returns the outputType.
   */
  public String getOutputType() {
    return outputType;
  }


  /**
   * @param outputType The outputType to set.
   */
  public void setOutputType(String outputType) {
    this.outputType = outputType;
  }


  /**
   * @return Returns the rendererPath.
   */
  public String getRendererPath() {
    return rendererPath;
  }


  /**
   * @param rendererPath The rendererPath to set.
   */
  public void setRendererPath(String rendererPath) {
    this.rendererPath = rendererPath;
  }


  public String getGraphWidth() {
    return graphWidth;
  }

  public String getGraphHeight() {
    return graphHeight;
  }


  public Boolean isRecursive() {
    return recursive != null && recursive;
  }


  public void setRecursive(Boolean recursive) {
    this.recursive = recursive;
  }


}
