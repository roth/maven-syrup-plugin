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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.netcetera.maven.plugin.syrup.dependency.GraphConfiguration;


public class DotGoogleChartInterpreter implements IDotInterpreter {

  @Override
  public void convertToImage(GraphConfiguration config) throws IOException {
    File outputDirectory = new File(config.getOutputDirectory());
    File imageFile = new File(outputDirectory, config.getGraphName() + ".png");
    File dotFile = new File(outputDirectory, config.getGraphName() + ".dot");

    StringBuilder builder = new StringBuilder();
    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dotFile)));
    String line = null;
    while ((line = reader.readLine()) != null) {
      builder.append(line);
    }
    postDotCode(builder.toString(), imageFile);
  }

  private int postDotCode(String dotCode, File imageFile) throws IOException {
    HttpClient httpclient = new DefaultHttpClient();
    try {
      HttpPost post = new HttpPost("https://chart.googleapis.com/chart");
      List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

      postParameters.add(new BasicNameValuePair("cht", "gv"));

      // System.out.println("https://chart.googleapis.com/chart?cht=gv&chl=" + dotEncoded);
      postParameters.add(new BasicNameValuePair("chl", dotCode));

      post.setEntity(new UrlEncodedFormEntity(postParameters));

      HttpResponse response = httpclient.execute(post);
      System.out.println("Response: " + response.getStatusLine());
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
