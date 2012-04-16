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
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;


public class ArtifactIdFilter implements ArtifactFilter {

  private List<String> regexPatterns;

  public ArtifactIdFilter(List<String> regexPatterns) {
    this.regexPatterns = regexPatterns;
  }

  @Override
  public boolean include(Artifact artifact) {

    String id = artifact.getGroupId() + ":" + artifact.getArtifactId();
    System.out.println("Testing: " + id);
    boolean matched = false;
    for (String regexPattern : regexPatterns) {
      if (id.matches(regexPattern)) {
        matched = true;
      }
    }
    System.out.println("Including: " + matched);
    return matched;
  }

}
