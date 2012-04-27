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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.logging.SystemStreamLog;

import com.netcetera.maven.plugin.syrup.dependency.GraphConfiguration;

/**
 * Dot interpreter that uses the 'dot' executable found on the location defined in the
 * {@link GraphConfiguration} to render the dot file into a graphical output.
 */
public class DotExecInterpreter implements IDotInterpreter {

  private static final SystemStreamLog LOGGER = new SystemStreamLog();

  @Override
  public void convertToImage(GraphConfiguration config) throws IOException {
    File outputDirectory = new File(config.getOutputDirectory());
    if (!outputDirectory.exists()) {
      LOGGER.info("output directory " + outputDirectory.getAbsolutePath()
          + " does not exist yet. Creating it");
      FileUtils.forceMkdir(outputDirectory);
    }
    File imageFile = new File(outputDirectory, config.getGraphName() + "." + config.getOutputType());
    File dotFile = new File(outputDirectory, config.getGraphName() + ".dot");

    ProcessBuilder builder = new ProcessBuilder(config.getRendererPath(), "-T"
        + config.getOutputType(), "-o" + imageFile.getAbsolutePath() + "", ""
        + dotFile.getAbsolutePath() + "");
    builder.redirectErrorStream(true);
    Process process = builder.start();
    InputStream is = process.getInputStream();
    InputStreamReader isr = new InputStreamReader(is);
    BufferedReader br = new BufferedReader(isr);
    String line;

    while ((line = br.readLine()) != null) {
      System.out.println("dot: " + line);
    }

  }

}
