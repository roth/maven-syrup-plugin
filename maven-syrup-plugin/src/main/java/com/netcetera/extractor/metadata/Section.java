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
  public void startSection1(AptElement title) {

    getSink().section1();
    getSink().sectionTitle1();
    append(title);
    getSink().sectionTitle1_();

    getSink().flush();
  }

  /**
   * {@link #startSection1(AptElement)}.
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
  public void startSection2(AptElement title) {

    getSink().section2();
    getSink().sectionTitle2();
    append(title);
    getSink().sectionTitle2_();

    getSink().flush();
  }

  /**
   * {@link #startSection2(AptElement)}.
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
  public void startSection3(AptElement title) {

    getSink().section3();
    getSink().sectionTitle3();
    append(title);
    getSink().sectionTitle3_();

    getSink().flush();
  }


  /**
   * {@link #startSection3(AptElement)}.
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
  public void startSection4(AptElement title) {

    getSink().section4();
    getSink().sectionTitle4();
    append(title);
    getSink().sectionTitle4_();

    getSink().flush();
  }


  /**
   * {@link #startSection4(AptElement)}.
   * 
   * @param title .
   */
  public void startSection4(String title) {

    startSection3(text(title));

  }

  /**
   * end section1.
   */
  public void endSection1() {

    getSink().section1_();
    getSink().flush();

  }


  /**
   * end section2.
   */
  public void endSection2() {

    getSink().section2_();
    getSink().flush();

  }


  /**
   * end section3.
   */
  public void endSection3() {

    getSink().section3_();
    getSink().flush();

  }


  /**
   * end section4.
   */
  public void endSection4() {

    getSink().section4_();
    getSink().flush();

  }

}
