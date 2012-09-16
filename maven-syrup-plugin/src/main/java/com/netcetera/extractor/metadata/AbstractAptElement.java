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
package com.netcetera.extractor.metadata;

import java.io.StringWriter;

import org.apache.maven.doxia.module.apt.AptSink;

import static com.netcetera.extractor.metadata.SimpleElementFactory.text;


/**
 * abstract apt Element.
 */
abstract class AbstractAptElement implements AptElement, Appendable {


  private final AptSink sink;
  private final StringWriter writer;

  /**
   * Default Constructor.
   */
  public AbstractAptElement() {
    writer = new StringWriter();
    sink = new AptSink(writer);
  }

  public void append(AptElement element) {
    if (element != null) {
      sink.rawText(element.getText());
      sink.flush();
    }
  }


  /**
   * See method name.
   * 
   * @param line .
   */
  public void appendTextLine(String line) {
    append(text(line));
    getSink().lineBreak();
  }

  /**
   * See method name.
   * 
   * @param aptElement .
   */
  public void append(SimpleAptElement aptElement) {
    sink.text(aptElement.getText());
  }

  public String getText() {
    doBeforeClose();
    sink.flush();
    sink.close();
    return writer.toString();
  }

  /**
   * Called before {@link #getText()} is executed.
   */
  protected abstract void doBeforeClose();

  protected AptSink getSink() {
    return sink;
  }

}
