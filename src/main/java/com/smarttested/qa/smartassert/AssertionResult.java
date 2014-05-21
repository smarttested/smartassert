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
 * Assertion Result
 * 
 * @author Andrei Varabyeu
 * 
 */
abstract class AssertionResult {

	/**
	 * @return Successful Validation Result
	 */
	public static AssertionResult successfulResult() {
		return new SuccussfulResult();
	}

	/**
	 * Result of unsuccessful validation. Should contain error message
	 * 
	 * @param message
	 * @return
	 */
	public static AssertionResult unsuccessfulResult(String message) {
		return new UnsuccessfulResult(message);
	}

	/**
	 * Whether result of validation is successful
	 * 
	 * @return
	 */
	abstract boolean successful();

	/**
	 * Returns error message in case of result is unsuccessful or throws error
	 * if result is successul
	 * 
	 * @return
	 */
	abstract String getErrorMessage();

	/**
	 * Successful result implementation
	 * 
	 */
	static class SuccussfulResult extends AssertionResult {

		private SuccussfulResult() {

		}

		@Override
		boolean successful() {
			return true;
		}

		@Override
		String getErrorMessage() {
			throw new RuntimeException("There is no error message in successfull result!");
		}

	}

	/**
	 * Unsuccessful result implementation
	 * 
	 */
	static class UnsuccessfulResult extends AssertionResult {

		private String errorMessage;

		private UnsuccessfulResult(String errorMessage) {
			this.errorMessage = errorMessage;
		}

		@Override
		public String getErrorMessage() {
			return errorMessage;
		}

		@Override
		boolean successful() {
			return false;
		}

	}
}
