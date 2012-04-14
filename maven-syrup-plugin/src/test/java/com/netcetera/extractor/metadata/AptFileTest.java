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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

import static com.netcetera.extractor.metadata.ItemList.ListType.BULLET;
import static com.netcetera.extractor.metadata.ItemList.ListType.LOWER_ALPHA;
import static com.netcetera.extractor.metadata.Paragraph.toParagraph;
import static com.netcetera.extractor.metadata.SimpleElementFactory.text;
import static com.netcetera.extractor.metadata.Table.Alignment.LEFT;
import static edu.emory.mathcs.backport.java.util.Collections.nCopies;
import static org.codehaus.plexus.PlexusTestCase.getBasedir;

/**
 * test.
 */
public class AptFileTest {


  /**
   * Generates a test Apt file out put.
   * 
   * @throws IOException not expected.
   */
  @Test
  public void testGenerateAptFile() throws IOException {

    File file = new File(getBasedir() + "\\src\\site\\apt\\source.apt");

    AptElement paragraph0 = toParagraph("Paragraph contained in list item 2.");
    AptElement paragraph1 = toParagraph("Paragrapgh 1 in Chapter 1");
    AptElement paragraph2 = toParagraph("Paragrapgh 2 in Chapter 1");
    AptElement paragraph3 = toParagraph("Paragrapgh 1 in Chapter 2");
    AptElement paragraph4 = toParagraph("Paragrapgh 2 in Chapter 2");
    Paragraph paragraph5 = new Paragraph();
    paragraph5.startParagraph("Paragrapgh 2 in Chapter 1.1");
    paragraph5.endParagraph("End Paragraph in Chapter 1.1");

    Table table1 = new Table(null, false);
    table1.row(nCopies(5, text("Header Row")), false);
    table1.row(nCopies(4, text("Row 1")));

    Table table0 = new Table(LEFT, true);
    table0.row(nCopies(5, text("Header Row")), true);
    table0.row(nCopies(4, text("Row 1")));
    table0.row(nCopies(6, text("Row 2")));
    table0.row(nCopies(5, text("Row 3")));

    Table table2 = new Table(null, false);
    table2.row(nCopies(5, text("Header Row")), false);
    table2.row(nCopies(4, text("Row 1")));


    ItemList list = new ItemList();

    list.startList(BULLET);
    list.listItem("List item 1.");
    list.listItem("List item 2.");
    list.append(paragraph0);

    list.startList(LOWER_ALPHA);
    list.listItem("Sub-list item 1.");
    list.append(table1);
    list.listItem("Sub-list item 2.");
    list.endList();

    list.listItem("List item 3.");
    list.endList();

    Section section = new Section();
    section.startSection1("Chapter 1");
    section.append(paragraph1);
    section.append(paragraph2);

    section.startSection2("Chapter 1.1");
    section.append(list);
    section.append(paragraph5);
    section.startSection3("Chapter 1.1.1");
    section.append(table2);
    section.startSection2("Chapter 1.2");

    section.startSection1("Chapter 2");
    section.append(paragraph3);
    section.append(paragraph4);


    FileWriter fileWriter = new FileWriter(file);

    AptFile aptBuilder = new AptFile(fileWriter);
    aptBuilder.head("So ein schöner Title", "dummy");
    aptBuilder.append(table0);
    aptBuilder.append(section);
    aptBuilder.close();

  }
}
