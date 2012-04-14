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

import java.util.ArrayList;
import java.util.List;


/**
 * c.
 */
public final class SimpleElementFactory {

  private static final class SimpleElement extends AbstractAptElement implements SimpleAptElement {
  };

  /**
   * creates a Apt text element.
   * 
   * @param <T> .
   * @param text .
   * @return apt element as text
   */
  public static <T> SimpleAptElement text(T text) {

    SimpleElement element = new SimpleElement();

    if (text != null) {
      element.getSink().text(text.toString());
      element.getSink().flush();
    }

    return element;

  }

  /**
   * {@link #text(Object)}.
   * 
   * @param <T> .
   * @param texts .
   * @return .
   */
  public static <T> List<SimpleAptElement> texts(List<T> texts) {

    List<SimpleAptElement> result = new ArrayList<SimpleAptElement>();

    for (T text : texts) {
      result.add(text(text));
    }

    return result;
  }

  private SimpleElementFactory() {

  }

}
