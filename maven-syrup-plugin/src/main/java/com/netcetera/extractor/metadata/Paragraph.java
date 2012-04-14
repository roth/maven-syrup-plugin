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

import java.util.Iterator;
import java.util.List;

import static com.netcetera.extractor.metadata.SimpleElementFactory.text;

/**
 * Apt paragraph element.
 */
public class Paragraph extends AbstractAptElement {


  /**
   * see method name.
   */
  public void startParagraph() {
    getSink().paragraph();
    getSink().flush();
  }

  /**
   * see method name.
   * 
   * @param textLine .
   */
  public void startParagraph(String textLine) {
    startParagraph();
    appendTextLine(textLine);
  }

  /**
   * see method name.
   */
  public void endParagraph() {
    getSink().paragraph_();
    getSink().flush();
  }

  /**
   * see method name.
   * 
   * @param textLine .
   */
  public void endParagraph(String textLine) {
    append(text(textLine));
    endParagraph();
  }

  /**
   * see method name.
   * 
   * @param textLines .
   * @return .
   */
  public static final AptElement toParagraph(List<SimpleAptElement> textLines) {

    Paragraph paragraph = new Paragraph();
    paragraph.startParagraph();

    for (Iterator<SimpleAptElement> iterator = textLines.iterator(); iterator.hasNext();) {

      SimpleAptElement line = iterator.next();

      paragraph.append(line);

      if (iterator.hasNext()) {
        paragraph.getSink().lineBreak();
      }
    }

    paragraph.endParagraph();
    return paragraph;
  }
}
