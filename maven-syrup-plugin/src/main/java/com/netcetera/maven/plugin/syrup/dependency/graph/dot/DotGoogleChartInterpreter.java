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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.maven.plugin.logging.SystemStreamLog;

import com.netcetera.maven.plugin.syrup.dependency.GraphConfiguration;

/**
 * Send a request to google do create a dot file.
 */
public class DotGoogleChartInterpreter implements IDotInterpreter {

  private static final SystemStreamLog LOGGER = new SystemStreamLog();

  @Override
  public void convertToImage(GraphConfiguration config) throws IOException {
    File outputDirectory = new File(config.getOutputDirectory());
    if (!outputDirectory.exists()) {
      LOGGER.info("Output directory does not exist yet. Creating it: "
          + outputDirectory.getAbsolutePath());
      FileUtils.forceMkdir(outputDirectory);
    }
    File imageFile = new File(outputDirectory, config.getGraphName() + ".png");
    File dotFile = new File(outputDirectory, config.getGraphName() + ".dot");

    StringBuilder builder = new StringBuilder();
    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dotFile)));
    String line = null;
    while ((line = reader.readLine()) != null) {
      builder.append(line);
    }
    postDotCode(config, builder.toString(), imageFile);
  }

  private int postDotCode(GraphConfiguration config, String dotCode, File imageFile)
      throws IOException {
    HttpClient httpclient = new DefaultHttpClient();
    try {
      HttpPost post = new HttpPost("https://chart.googleapis.com/chart");
      List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

      postParameters.add(new BasicNameValuePair("cht", "gv"));

      postParameters.add(new BasicNameValuePair("chl", dotCode));

      String width = config.getGraphWidth();
      String height = config.getGraphHeight();
      if (StringUtils.isNotEmpty(width) && StringUtils.isNotEmpty(height)) {
        String dimensions = config.getGraphWidth() + "x" + config.getGraphHeight();
        LOGGER.info("Adding dimensions: " + dimensions);
        // postParameters.add(new BasicNameValuePair("chs", dimensions));
      }

      post.setEntity(new UrlEncodedFormEntity(postParameters));

      LOGGER.info("Sending request..");
      HttpResponse response = httpclient.execute(post);
      LOGGER.info("Response: " + response);
      HttpEntity responseEntity = response.getEntity();

      BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(imageFile));
      responseEntity.writeTo(outputStream);
      outputStream.close();
      return response.getStatusLine().getStatusCode();
    } finally {
      // When HttpClient instance is no longer needed,
      // shut down the connection manager to ensure
      // immediate deallocation of all system resources
      httpclient.getConnectionManager().shutdown();
    }
  }
}
