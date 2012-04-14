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

import static com.netcetera.extractor.metadata.SimpleElementFactory.text;


/**
 * Apt section element.
 */
public class Section extends AbstractAptElement {


  /**
   * start Section1.
   * 
   * @param title .
   */
  public void startSection1(SimpleAptElement title) {

    getSink().section1();
    getSink().sectionTitle1();
    append(title);
    getSink().sectionTitle1_();

    getSink().flush();
  }

  /**
   * {@link #startSection1(SimpleAptElement)}.
   * 
   * @param title .
   */
  public void startSection1(String title) {

    startSection1(text(title));

  }

  /**
   * start section2.
   * 
   * @param title .
   */
  public void startSection2(SimpleAptElement title) {

    getSink().section2();
    getSink().sectionTitle2();
    append(title);
    getSink().sectionTitle2_();

    getSink().flush();
  }

  /**
   * {@link #startSection2(SimpleAptElement)}.
   * 
   * @param title .
   */
  public void startSection2(String title) {

    startSection2(text(title));

  }


  /**
   * start section3.
   * 
   * @param title .
   */
  public void startSection3(SimpleAptElement title) {

    getSink().section3();
    getSink().sectionTitle3();
    append(title);
    getSink().sectionTitle3_();

    getSink().flush();
  }


  /**
   * {@link #startSection3(SimpleAptElement)}.
   * 
   * @param title .
   */
  public void startSection3(String title) {

    startSection3(text(title));

  }


  /**
   * start section4.
   * 
   * @param title .
   */
  public void startSection4(SimpleAptElement title) {

    getSink().section4();
    getSink().sectionTitle4();
    append(title);
    getSink().sectionTitle4_();

    getSink().flush();
  }


  /**
   * {@link #startSection4(SimpleAptElement)}.
   * 
   * @param title .
   */
  public void startSection4(String title) {

    startSection3(text(title));

  }

  @Override
  protected void doBeforeClose() {
    getSink().section1_();
    getSink().flush();
  }
}
