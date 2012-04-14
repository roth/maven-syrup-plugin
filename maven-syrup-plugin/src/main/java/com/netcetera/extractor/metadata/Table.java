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

import java.util.List;

import static java.util.Collections.nCopies;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang.ArrayUtils.toPrimitive;


/**
 * Apt Table element.
 */
public class Table extends AbstractAptElement {

  private final Alignment alignment;
  private final boolean withGrid;
  private boolean firstRow = true;
  private int lastRowCellSize = 0;

  /**
   * Alignments.
   */
  public static enum Alignment {
    /** {@code |  text  |}. */
    CENTER,
    /** {@code |text    |}. */
    LEFT,
    /** {@code |    text|}. */
    RIGHT;
  };

  /**
   * Constructor.
   * 
   * @param alignment .
   * @param withGrid .
   */
  public Table(Alignment alignment, boolean withGrid) {
    this.alignment = alignment;
    this.withGrid = withGrid;
  }

  /**
   * Row.
   * 
   * @param elements .
   * @param isHeaderRow .
   */
  public void row(List<SimpleAptElement> elements, boolean isHeaderRow) {


    if (isNotEmpty(elements)) {

      startTableIfNeeded(elements.size());
      startTableRowFormatIfNeeded(elements.size());

      getSink().tableRow();


      for (SimpleAptElement aptElement : elements) {
        getSink().tableCell(isHeaderRow);
        append(aptElement);
        getSink().tableCell_();
      }

      getSink().tableRow_();
    }


    getSink().flush();

  }

  /**
   * row.
   * 
   * @param elements .
   */
  public void row(List<SimpleAptElement> elements) {

    row(elements, false);

  }

  private void startTableIfNeeded(int currentRowCellSize) {

    if (firstRow) {
      getSink().table();

      getSink().tableRows(toJustification(currentRowCellSize), withGrid);

      lastRowCellSize = currentRowCellSize;
      firstRow = false;
    }

  }

  private void startTableRowFormatIfNeeded(int currentRowCellSize) {

    if (lastRowCellSize < currentRowCellSize) {
      getSink().tableRows_();
      getSink().tableRows(toJustification(currentRowCellSize), withGrid);
    }

    lastRowCellSize = currentRowCellSize;

  }

  private int[] toJustification(int numberOfCells) {

    if (alignment == null || numberOfCells == 0) {

      return null;

    } else {

      List<Integer> aligments = nCopies(numberOfCells, alignment.ordinal());

      return toPrimitive(aligments.toArray(new Integer[aligments.size()]));

    }

  }

  @Override
  protected void doBeforeClose() {
    getSink().tableRows_();
    getSink().table_();
  }
}
