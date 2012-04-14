/*
 * Copyright (C) 2012 by Netcetera AG.
 * All rights reserved.
 *
 * The copyright to the computer program(s) herein is the property of
 * Netcetera AG, Switzerland.  The program(s) may be used and/or copied
 * only with the written permission of Netcetera AG or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the program(s) have been supplied.
 */
package com.netcetera;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.Ignore;

/**
 * Example test for running a Mojo.
 */
@Ignore
public class MyMojoTest extends AbstractMojoTestCase {

  /**
   * @throws Exception if any
   */
  public void testSomething() throws Exception {
    File pom = getTestFile("src/test/resources/test-pom.xml");
    assertNotNull(pom);
    assertTrue(pom.exists());

    MyMojo myMojo = (MyMojo) lookupMojo("touch", pom);
    assertNotNull(myMojo);
    myMojo.execute();

  }
}
