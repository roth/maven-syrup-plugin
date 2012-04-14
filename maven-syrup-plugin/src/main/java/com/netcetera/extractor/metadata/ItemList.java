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

import org.apache.maven.doxia.sink.Sink;

import static com.netcetera.extractor.metadata.SimpleElementFactory.text;
import static org.apache.maven.doxia.sink.Sink.NUMBERING_DECIMAL;
import static org.apache.maven.doxia.sink.Sink.NUMBERING_LOWER_ALPHA;
import static org.apache.maven.doxia.sink.Sink.NUMBERING_LOWER_ROMAN;
import static org.apache.maven.doxia.sink.Sink.NUMBERING_UPPER_ALPHA;
import static org.apache.maven.doxia.sink.Sink.NUMBERING_UPPER_ROMAN;


/**
 * Apt list element.
 */
public class ItemList extends AbstractAptElement {

  /**
   * list types.
   */
  public enum ListType {
    /** Bullet numbering. */
    BULLET(null),
    /** Decimal numbering: 1, 2, 3, 4, etc. */
    DECIMAL(NUMBERING_DECIMAL),
    /** Lower-alpha numbering: a, b, c, d, etc. */
    LOWER_ALPHA(NUMBERING_LOWER_ALPHA),
    /** Upper-alpha numbering: A, B, C, D, etc. */
    UPPER_ALPHA(NUMBERING_UPPER_ALPHA),
    /** Lower-roman numbering: i, ii, iii, iv, etc. */
    LOWER_ROMAN(NUMBERING_LOWER_ROMAN),
    /** Upper-roman numbering: I, II, III, IV, etc. */
    UPPER_ROMAN(NUMBERING_UPPER_ROMAN);

    private final Integer type;

    private ListType(Integer type) {
      this.type = type;
    }

    private void startList(Sink sink) {
      if (type == null) {
        sink.list();
      } else {
        sink.numberedList(type);
      }
    }
  };


  /**
   * Creates a list item.
   * 
   * @param text .
   */
  public void listItem(SimpleAptElement text) {

    getSink().listItem();
    append(text);
    getSink().listItem_();

    getSink().flush();

  }

  /**
   * start list.
   * 
   * @param type .
   */
  public void startList(ListType type) {
    if (type != null) {
      type.startList(getSink());
    } else {
      getSink().list();
    }
    getSink().flush();
  }

  /**
   * end list.
   */
  public void endList() {
    getSink().list_();
    getSink().flush();
  }

  /**
   * {@link #listItem(SimpleAptElement)}.
   * 
   * @param text .
   */
  public void listItem(String text) {

    listItem(text(text));

  }

  /**
   * see method name.
   * 
   * @param items .
   * @param type .
   * @return .
   */
  public static AptElement toItemList(ListType type, List<SimpleAptElement> items) {

    ItemList list = new ItemList();

    list.startList(type);

    for (Iterator<SimpleAptElement> iterator = items.iterator(); iterator.hasNext();) {

      SimpleAptElement item = iterator.next();

      list.listItem(item);

    }

    return list;
  }

  @Override
  protected void doBeforeClose() {

  }

}
