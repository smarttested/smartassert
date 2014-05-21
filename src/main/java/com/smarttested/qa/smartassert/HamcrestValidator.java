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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

/**
 * Validator based on
 * {@link <a href="https://github.com/hamcrest/JavaHamcrest">Hamcrest</a>}
 * matchers.
 * 
 * @author Andrei Varabyeu
 * 
 * @param <T>
 */
class HamcrestValidator<T> extends BaseValidator {

	/**
	 * Object to be validated
	 */
	private T toValidate;

	/**
	 * Hamcrest matcher implementation
	 */
	private Matcher<? super T> matcher;

	/**
	 * Error reason phrase
	 */
	private String reason;

	/**
	 * Default constructor.
	 * 
	 * @param toValidate
	 *            - object to be validated
	 * @param matcher
	 *            - Hamcrest matcher implementation
	 * @param reason
	 *            - Error reason phrase
	 */
	public HamcrestValidator(T toValidate, Matcher<? super T> matcher, String reason) {
		this.toValidate = toValidate;
		this.matcher = matcher;
		this.reason = reason;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.epam.ta.jtestcore.assertions.validation.BaseValidator#validate()
	 */
	@Override
	public AssertionResult performAssert() {

		if (!matcher.matches(toValidate)) {
			Description description = new StringDescription();
			description.appendText(reason).appendText("\nExpected: ").appendDescriptionOf(matcher).appendText("\n     but: ");
			matcher.describeMismatch(toValidate, description);
			return AssertionResult.unsuccessfulResult(description.toString());

		} else {
			return AssertionResult.successfulResult();
		}
	}

}
