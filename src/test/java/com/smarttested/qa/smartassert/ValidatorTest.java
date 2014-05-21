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

import com.google.common.collect.Lists;
import org.hamcrest.CoreMatchers;
import org.testng.annotations.Test;

import com.google.common.base.Predicates;

/**
 * Set of unit tests for validator utils
 * 
 * @author Andrei Varabyeu
 * 
 */
public class ValidatorTest {

	private static final String SOME_ERROR_MESSAGE = "Some error Message";

	@Test(expectedExceptions = AssertException.class)
	public void testHamcrestNegative() {

        for (String value : Lists.<String>newArrayList()){
            if (value.equals("hui")) return;
            else continue;
        }
        SmartAssert.expect(Lists.newArrayList(), CoreMatchers.hasItem("hui"), SOME_ERROR_MESSAGE).assertHard();


        SmartAssert.expect(true, CoreMatchers.is(false), SOME_ERROR_MESSAGE).assertHard();
	}

	@Test(expectedExceptions = CustomException.class)
	public void testHamcrestNegativeCustomException() {
		SmartAssert.expect(true, CoreMatchers.is(false), SOME_ERROR_MESSAGE).assertHard(CustomException.class);
	}

	@Test
	public void testHamcrestPositive() {
		SmartAssert.expect(true, CoreMatchers.is(true), SOME_ERROR_MESSAGE).assertHard();
	}

	@Test(expectedExceptions = AssertException.class)
	public void testGuavaNegative() {
		SmartAssert.expect(true, Predicates.alwaysFalse(), SOME_ERROR_MESSAGE).assertHard();
	}

	@Test
	public void testGuavaPositive() {
		SmartAssert.expect(true, Predicates.alwaysTrue(), SOME_ERROR_MESSAGE).assertHard();
	}

	@Test(expectedExceptions = AssertException.class)
	public void testGuavaIncorrectException() {
		SmartAssert.expect(true, Predicates.alwaysFalse(), SOME_ERROR_MESSAGE).assertHard(IncorrectCustomException.class);
	}

	@Test(expectedExceptions = AssertException.class)
	public void testHamcrestIncorrectException() {
		SmartAssert.expect(true, CoreMatchers.is(false), SOME_ERROR_MESSAGE).assertHard(IncorrectCustomException.class);
	}

	public void testHamcrestSoft() {
		SmartAssert.expect(true, CoreMatchers.is(false), "I'm an error!").assertSoft();
		SmartAssert.expect(true, CoreMatchers.is(true), "it's ok").assertSoft();
		SmartAssert.expect(true, CoreMatchers.is(false), "I'm an second error!").assertSoft();
	}

	public void testGuavaSoft1() {
		SmartAssert.expect(true, Predicates.alwaysFalse(), "I'm an error!").assertSoft();
		SmartAssert.expect(true, Predicates.alwaysFalse(), "it's ok").assertSoft();
		SmartAssert.expect(true, Predicates.alwaysFalse(), "I'm an second error!").assertSoft();
	}

	public static class CustomException extends AssertException {

		private static final long serialVersionUID = -7505800312700157037L;

		public CustomException(String message) {
			super(message);
		}
	}

	public static class IncorrectCustomException extends AssertException {

		private static final long serialVersionUID = -7505800312700157037L;

		public IncorrectCustomException() {
			super("Incorrect exception. There are no constructor with String");
		}
	}

}
