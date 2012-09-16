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

import java.io.Writer;
import java.util.Date;

import org.apache.maven.doxia.module.apt.AptSink;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.apache.commons.lang.time.DateFormatUtils.ISO_DATE_FORMAT;


/**
 * Header.
 */
public class AptFile implements Appendable {

  private final AptSink sink;
  private boolean firstBodyElement = true;

  /**
   * Constructor.
   * 
   * @param writer where the output is written to.
   */
  public AptFile(Writer writer) {
    this.sink = new AptSink(writer);
  }

  /**
   * creates a head section.
   * 
   * @param title .
   * @param author .
   */
  public void head(String title, String author) {

    String date = ISO_DATE_FORMAT.format(new Date());

    sink.head();

    if (isNotEmpty(title)) {
      sink.title();
      sink.text(title);
      sink.title_();
    }

    if (isNotEmpty(author)) {
      sink.author();
      sink.text(author);
      sink.author_();
    }

    sink.date();
    sink.text(date);
    sink.date_();

    sink.head_();

    sink.flush();

  }

  public void append(AptElement element) {

    if (element != null) {

      startBodyIfNeeded();

      sink.rawText(element.getText());
      sink.flush();
    }
  }

  private void startBodyIfNeeded() {
    if (firstBodyElement) {
      sink.body();
      firstBodyElement = false;
    }
  }

  /**
   * close.
   */
  public void close() {

    sink.body_();
    sink.flush();
    sink.close();

  }
}
