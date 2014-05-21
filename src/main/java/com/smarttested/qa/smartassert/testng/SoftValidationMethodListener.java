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
package com.smarttested.qa.smartassert.testng;

import java.util.Arrays;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.smarttested.qa.smartassert.SoftAssertException;
import com.smarttested.qa.smartassert.SmartAssert;

/**
 * TestNG Method listener which performs soft-asserts validation after each
 * method
 * 
 * @author Andrei Varabyeu
 * 
 */
public class SoftValidationMethodListener implements IInvokedMethodListener {

	private static final Predicate<IInvokedMethod> EXPECTS_SOFT_ERROR = new ExpectsSoftErrorPredicate();

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		/*
		 * Be sure we have clean soft failures holder
		 */
		SmartAssert.getSoftFailures().cleanupFailures();

		if (EXPECTS_SOFT_ERROR.apply(method)) {
			String errorMessage = String
					.format("Test Method with name '%s' of class '%s' expects SoftValidationException. You should not expect exceptions from soft assertions",
							method.getTestMethod().getMethodName(), method.getTestMethod().getTestClass().getRealClass().getSimpleName());
			throw new SkipException(errorMessage);
		}

	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {

		Optional<SoftAssertException> exception = SmartAssert.getSoftFailures().getException();

		/*
		 * If this result is successful let's validate soft failures
		 */
		if (testResult.isSuccess()) {

			exception = SmartAssert.getSoftFailures().getException();
			if (exception.isPresent()) {
				testResult.setStatus(ITestResult.FAILURE);
				testResult.setThrowable(exception.get());
			}

		} else {

			/*
			 * There are some hard assertions are already failed this test
			 */
			if (null != testResult.getThrowable().getClass()) {
				if (exception.isPresent()) {
					Throwable throwable = testResult.getThrowable();
					String appendMsg = System.getProperty("line.separator") + "Supressed exceptions: " + exception.get().getMessage();
					Throwable extended = new Throwable(throwable.getMessage() + appendMsg, throwable.getCause());
					testResult.setThrowable(extended);
				}
			}
		}
	}

	private static final class ExpectsSoftErrorPredicate implements Predicate<IInvokedMethod> {

		@Override
		public boolean apply(IInvokedMethod method) {
			if (!method.isTestMethod()) {
				return false;
			}
			Test testAnnotation = method.getTestMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class);

			if (null == testAnnotation) {
				return false;
			}

			if (testAnnotation.expectedExceptions().length == 0) {
				return false;
			}
			return Arrays.asList(testAnnotation.expectedExceptions()).contains(SoftAssertException.class);
		}
	}

}
