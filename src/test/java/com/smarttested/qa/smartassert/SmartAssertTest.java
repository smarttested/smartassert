/*
 * Copyright (C) 2014 Andrei Varabyeu
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
package com.smarttested.qa.smartassert;

import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;

/**
 * Tests for {@link com.smarttested.qa.smartassert.SmartAssert}
 *
 * @author Andrei Varabyeu
 */
public class SmartAssertTest {

    @Test
    public void testSoftAssertsValidation() {
        SmartAssert.assertSoft(false, is(true), "This is expected error");

        try {
            SmartAssert.validateSoftAsserts();
        } catch (SoftAssertException e){
            //do nothing, we expect error here
        }

        /* call this method second time. Soft Assert should be cleared after first time and this should be no error */
        SmartAssert.validateSoftAsserts();
    }
}
