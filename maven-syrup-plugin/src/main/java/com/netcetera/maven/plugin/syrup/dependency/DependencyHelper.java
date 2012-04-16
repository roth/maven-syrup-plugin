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

import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.model.Repository;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.DefaultProjectBuilderConfiguration;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuilderConfiguration;
import org.apache.maven.project.ProjectBuildingException;


/**
 * Helper class to retrieve Artifacts from configuration.
 */
public class DependencyHelper {

  private ArtifactFactory artifactFactory;

  private ArtifactResolver artifactResolver;

  private ArtifactRepository localRepository;

  private MavenProjectBuilder projectBuilder;

  private List<Repository> remoteRepositories;

  private Log log;

  /**
   * Constructor.
   * 
   * @param log
   * @param artifactFactory artifact factory
   * @param artifactResolver artifact resolver
   * @param localRepository local repositories
   * @param projectBuilder project builder
   * @param remoteRepositories list of remote repositories
   */
  public DependencyHelper(Log log,
      ArtifactFactory artifactFactory,
      ArtifactResolver artifactResolver,
      ArtifactRepository localRepository,
      MavenProjectBuilder projectBuilder,
      List<Repository> remoteRepositories) {
    this.log = log;
    this.artifactFactory = artifactFactory;
    this.artifactResolver = artifactResolver;
    this.localRepository = localRepository;
    this.projectBuilder = projectBuilder;
    this.remoteRepositories = remoteRepositories;

  }

  /**
   * Finds the maven project by the given dependency graph object.
   * 
   * @param graphConfiguration configuration object
   * @return MavenProject representing the artifact given in the configuration
   * @throws MojoExecutionException could not retrieve artifact
   */
  public MavenProject findMavenProject(GraphConfiguration graphConfiguration)
      throws MojoExecutionException {
    Artifact artifact = getArtifactByRootArtifact(graphConfiguration);
    return getProjectByArtifact(artifact);
  }

  private Artifact getArtifactByRootArtifact(GraphConfiguration graphConfig)
      throws MojoExecutionException {
    Artifact artifact = artifactFactory.createArtifact(graphConfig.getGroupId(),
        graphConfig.getArtifactId(), graphConfig.getVersion(), "compile", "pom");
    try {
      artifactResolver.resolve(artifact, remoteRepositories, localRepository);
      return artifact;
    } catch (ArtifactResolutionException e) {
      log.error("Unable to resolve artifact", e);
      throw new MojoExecutionException("Unable to resolve artifact:" + artifact, e);
    } catch (ArtifactNotFoundException e) {
      log.error("Unable to find artifact", e);
      throw new MojoExecutionException("Unable to find artifact:" + artifact, e);
    }

  }


  private MavenProject getProjectByArtifact(Artifact artifact) throws MojoExecutionException {
    // Create a project building request
    ProjectBuilderConfiguration request = new DefaultProjectBuilderConfiguration();
    // Build the project and get the result
    try {
      return projectBuilder.build(artifact.getFile(), request);
    } catch (ProjectBuildingException e) {
      throw new MojoExecutionException("Unable to convert artifact to project:" + artifact, e);
    }
  }

}
