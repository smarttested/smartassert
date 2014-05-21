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

import com.google.common.base.Predicate;

/**
 * Validator based on Google Guava libraries. There we have {@link Predicate}
 * which basically means 'condition'. Predicate will be evaluated on object to
 * be validated. This is pretty much useful, since we can create predicate once
 * and use it several times when we need it.
 * 
 * @author Andrei Varabyeu
 * 
 * @param <T>
 *            - Type of object to be validated
 */
class GuavaPredicateValidator<T> extends BaseValidator {

	/**
	 * Object to be validated
	 */
	private T toValidate;

	/**
	 * Predicate which is applicable to object
	 */
	private Predicate<? super T> predicate;

	/**
	 * Error reason phrase
	 */
	private String reason;

	/**
	 * Default constructor
	 * 
	 * @param toValidate
	 *            - Object to be validated
	 * @param predicate
	 *            - Google's Guava Predicate Implementation
	 * @param reason
	 *            - Error reason phrase
	 */
	public GuavaPredicateValidator(T toValidate, Predicate<? super T> predicate, String reason) {
		this.toValidate = toValidate;
		this.predicate = predicate;
		this.reason = reason;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.epam.ta.jtestcore.assertions.validation.BaseValidator#validate()
	 */
	@Override
	public AssertionResult performAssert() {
		if (!predicate.apply(toValidate)) {
			StringBuilder errorMessage = new StringBuilder(this.reason).append("\nPredicate [").append(predicate)
					.append("] expected to be [true], but was [false]");
			return AssertionResult.unsuccessfulResult(errorMessage.toString());
		} else {
			return AssertionResult.successfulResult();
		}
	}
}
