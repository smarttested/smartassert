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

import org.hamcrest.Matcher;

import com.google.common.base.Predicate;

/**
 * Main validator. Should be used instead of default jUnit/TestNG asserts.
 * 
 * @author Andrei Varabyeu
 * 
 */
public class SmartAssert {

	/**
	 * Returns validator based on Guava Predicate and object to be validated
	 * 
	 * @param object
	 *            - Object to be validated
	 * @param predicate
	 *            - Predicate implementation
	 * @param message
	 *            - Error message
	 * @return - Validator with possibility to use soft and hard validations
	 */
	public static <T> BaseValidator expect(T object, Predicate<? super T> predicate, String message) {
		return new GuavaPredicateValidator<T>(object, predicate, message);
	}

	/**
	 * Returns validator based on Hamcrest Matchers and object to be validated
	 * 
	 * @param object
	 *            - Object to be validated
	 * @param matcher
	 *            - Hamcrest Matcher
	 * @param message
	 *            - Error message
	 * @return
	 */
	public static <T> BaseValidator expect(T object, Matcher<? super T> matcher, String message) {
		return new HamcrestValidator<T>(object, matcher, message);
	}

	/**
	 * Performs hard validation (e.g. assert like jUnit/TestNG) based on Guava
	 * Predicate
	 * 
	 * @param object
	 *            - Object to be validated
	 * @param predicate
	 *            - Predicate implementation
	 * @param message
	 *            - Error message
	 */
	public static <T> void assertHard(T object, Predicate<? super T> predicate, String message) {
		expect(object, predicate, message).assertHard();
	}

	/**
	 * Performs soft validation (DO NOT throw any exceptions until you
	 * explicitly ask about it via) based on Guava Predicate
	 * 
	 * @param object
	 *            - Object to be validated
	 * @param predicate
	 *            - Predicate implementation
	 * @param message
	 *            - Error message
	 */
	public static <T> void assertSoft(T object, Predicate<? super T> predicate, String message) {
		expect(object, predicate, message).assertSoft();
	}

	/**
	 * Performs soft validation (DO NOT throw any exceptions until you
	 * explicitly ask about it via) based on Hamcrest Matchers
	 * 
	 * @param object
	 *            - Object to be validated
	 * @param matcher
	 *            - Hamcrest Matcher
	 * @param message
	 *            - Error Message
	 */
	public static <T> void assertSoft(T object, Matcher<? super T> matcher, String message) {
		expect(object, matcher, message).assertSoft();
	}

	/**
	 * Performs hard validation (e.g. assert like jUnit/TestNG) based on
	 * Hamcrest Matchers
	 * 
	 * @param object
	 *            - Object to be validated
	 * @param matcher
	 *            - Hamcrest Matcher
	 * @param message
	 *            - Error message
	 */
	public static <T> void verifyHard(T object, Matcher<? super T> matcher, String message) {
		expect(object, matcher, message).assertHard();
	}

	/**
	 * Returns object with collected soft validation failures
	 * 
	 * @return
	 */
	public static SoftFailuresHolder getSoftFailures() {
		return BaseValidator.SOFT_FAILURES;
	}

	/**
	 * Check soft failures and if there any throws exception and clears holder
	 * for current thread
	 */
	public static void throwSoftFailures() {
		BaseValidator.SOFT_FAILURES.validate();
	}

}
