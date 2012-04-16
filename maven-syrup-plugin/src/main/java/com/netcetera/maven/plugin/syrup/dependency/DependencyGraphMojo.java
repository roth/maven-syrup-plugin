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

import java.io.IOException;
import java.util.List;

import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactCollector;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.shared.dependency.tree.DependencyNode;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilder;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilderException;

import com.netcetera.maven.plugin.syrup.dependency.graph.GraphRendererFactory;
import com.netcetera.maven.plugin.syrup.dependency.graph.IGraphRenderer;

/**
 * Creates a dependency graph, either starting from the current module or from the module defined in
 * the configuration.
 * 
 * @goal graph
 * @phase process-sources
 */
public class DependencyGraphMojo extends AbstractMojo {

  /**
   * The Root artefact to start with.
   * 
   * @parameter
   * @required
   * @readonly
   */
  private List<GraphConfiguration> graphConfigurations;

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
    DependencyHelper helper = new DependencyHelper(getLog(), artifactFactory, artifactResolver,
        localRepository, projectBuilder, project.getRemoteArtifactRepositories());
    for (GraphConfiguration graphConfiguration : graphConfigurations) {
      getLog().info("Starting with graph " + graphConfiguration.getRenderer());
      MavenProject rootProject = helper.findMavenProject(graphConfiguration);
      getLog().info("Working with " + rootProject.getUrl());
      printDependenciesForArtifact(graphConfiguration, rootProject);
    }
  }

  private void printDependenciesForArtifact(GraphConfiguration graphConfiguration,
      MavenProject rootProject) throws MojoExecutionException {
    try {
      // FIXME the filter does not seem to work
      ArtifactFilter artifactFilter = new ArtifactIdFilter(graphConfiguration.getIncludes());

      DependencyNode rootNode = treeBuilder.buildDependencyTree(rootProject, localRepository,
          artifactFactory, artifactMetadataSource, artifactFilter, artifactCollector);

      GraphRendererFactory factory = new GraphRendererFactory();
      IGraphRenderer graphRenderer = factory.getGraphRenderer(graphConfiguration.getRenderer());
      graphRenderer.createDependencyGraph(graphConfiguration, rootNode);

    } catch (DependencyTreeBuilderException e) {
      throw new MojoExecutionException("Could not create dependencyTree", e);
    } catch (IOException e) {
      throw new MojoExecutionException("Could create graph output", e);
    }
  }

}
