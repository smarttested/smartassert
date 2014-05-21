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

/**
 * Base validator. Should be overriden by some custom implementation e.g. google
 * guava, hamcrest, etc. There are two types of validation:
 * <ul>
 * <li><b>Hard validation.</b> This is pretty simular to default asserts used in
 * junit and testng. You pass some condition and if this condition isn't true,
 * exception will be throwed</li>
 * <li><b>Soft validation.</b> Soft validation doesn't throw any exceptions in
 * the place where it's defined. If soft validation doesn't pass, error only
 * will be collected into {@link SoftFailuresHolder}</li>
 * </ul>
 * 
 * @author Andrei Varabyeu
 * 
 */
abstract class BaseValidator {

	/**
	 * Soft failures holder. Contains failures on per-thread basis
	 */
	static final SoftFailuresHolder SOFT_FAILURES = new SoftFailuresHolder();

	/**
	 * Should be implemented in child class. Performs validation and returns
	 * {@link AssertionResult} of this validation
	 * 
	 * @return
	 */
	abstract protected AssertionResult performAssert();

	/**
	 * Performs Hard verification. Throws {@link AssertException} in case
	 * validation is not passed
	 */
	public void assertHard() {
		AssertionResult result = performAssert();
		if (!result.successful()) {
			throw new AssertException(result.getErrorMessage());
		}
	}

	/**
	 * Performs Hard verification. Throws provided exception in case validation
	 * is not passed. Be aware that passed error should be subclass of
	 * {@link AssertException} and<br>
	 * <b><u>MUST</u> have constructor with only one {@link String} as
	 * parameter</b>
	 */
	public void assertHard(Class<? extends AssertException> clazz) {
		AssertionResult result = performAssert();
		if (!result.successful()) {
			AssertException toBeThrowed;
			try {
				toBeThrowed = clazz.getConstructor(String.class).newInstance(result.getErrorMessage());
			} catch (Exception e) {
				toBeThrowed = new AssertException(result.getErrorMessage());
			}
			throw toBeThrowed;
		}
	}

	/**
	 * Performs Soft validation. Doesn't throw any exceptions, just holds
	 * validation error (if such present) in {@link SoftFailuresHolder}
	 */
	public void assertSoft() {
		AssertionResult result = performAssert();
		if (!result.successful()) {
			SOFT_FAILURES.addFailure(result.getErrorMessage());
		}
	}

}
