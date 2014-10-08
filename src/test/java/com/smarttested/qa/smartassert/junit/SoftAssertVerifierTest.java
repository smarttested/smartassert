/*
 * Copyright (C) 2014 The original author
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.smarttested.qa.smartassert.junit;

import com.smarttested.qa.smartassert.SmartAssert;
import com.smarttested.qa.smartassert.SoftAssertException;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

/**
 * @author Andrei Varabyeu
 */
public class SoftAssertVerifierTest {

    @Rule
    public SoftAssertVerifier verifier = SoftAssertVerifier.instance();

    /**
     * Checks that user is able to expect soft assertion error
     */
    @Test(expected = SoftAssertException.class)
    public void testVerifier() {
        SmartAssert.assertSoft(true, CoreMatchers.is(false), "Some expected error. Should be thrown by verifier");
    }

    /**
     * Test have correct expected exception, but still have soft assertion errors. Check that such test fails
     *
     * @throws Throwable
     */
    @Test(expected = SoftAssertException.class)
    public void testVerifierWithExpectedException() throws Throwable {
        Result result = new JUnitCore().run(TestClassWithExpectedRuntimeException.class);

        if (!result.getFailures().isEmpty()) {
            throw result.getFailures().get(0).getException();
        }
    }

    /**
     * Test have unexpected exception, there is no need to check soft validations
     *
     * @throws Throwable
     */
    @Test(expected = SoftAssertException.class)
    public void testVerifierWithUnexpectedException() throws Throwable {
        Result result = new JUnitCore().run(TestClassWithUnexpectedRuntimeException.class);
        if (!result.getFailures().isEmpty()) {
            Assert.assertThat(result.getFailures().get(0).getException().getMessage(),
                    Matchers.containsString("Unexpected exception, expected<java.lang.IllegalArgumentException> but was<java.lang.RuntimeException>"));
        }
    }


    public static class TestClassWithUnexpectedRuntimeException {

        @Rule
        public SoftAssertVerifier verifier = SoftAssertVerifier.instance();

        @Test(expected = IllegalArgumentException.class)
        public void testMethodWithSoftError() {
            SmartAssert.assertSoft(true, CoreMatchers.is(false), "Some expected error. Should NOT be thrown by verifier");
            throw new RuntimeException("some error during test");
        }
    }

    public static class TestClassWithExpectedRuntimeException {

        @Rule
        public SoftAssertVerifier verifier = SoftAssertVerifier.instance();

        @Test(expected = RuntimeException.class)
        public void testMethodWithSoftError() {
            SmartAssert.assertSoft(true, CoreMatchers.is(false), "Some expected error. Should be thrown by verifier");
            throw new RuntimeException("some error during test");
        }
    }
}
