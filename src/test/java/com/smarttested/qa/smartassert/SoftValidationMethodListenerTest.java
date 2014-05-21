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
package com.smarttested.qa.smartassert;

import com.google.common.base.Predicates;
import com.smarttested.qa.smartassert.SmartAssert;
import com.smarttested.qa.smartassert.SoftAssertException;
import com.smarttested.qa.smartassert.testng.SoftValidationMethodListener;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * @author Andrei Varabyeu
 */
@Listeners(SoftValidationMethodListener.class)
public class SoftValidationMethodListenerTest {

    /**
     * Validates that if test expects soft assertions error, it should be
     * skipped
     */
    @Test(expectedExceptions = {SoftAssertException.class, SkipException.class})
    public void testGuavaSoft() {
        SmartAssert.expect(true, Predicates.alwaysFalse(), "I'm an error!").assertSoft();
        SmartAssert.expect(true, Predicates.alwaysFalse(), "it's ok").assertSoft();
        SmartAssert.expect(true, Predicates.alwaysFalse(), "I'm an second error!").assertSoft();
    }
}
