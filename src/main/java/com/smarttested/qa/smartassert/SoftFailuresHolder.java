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

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;

/**
 * Holder of failures from soft validations. Contains list of failures for each
 * thread. So, implementation is thread-safe
 * 
 * @author Andrei Varabyeu
 * 
 */
public class SoftFailuresHolder {

	private ThreadLocal<List<String>> failures;

	SoftFailuresHolder() {
		failures = new ThreadLocal<List<String>>() {
			@Override
			protected java.util.List<String> initialValue() {
				return new ArrayList<String>();
			};
		};
	}

	public void addFailure(String failureMessage) {
		failures.get().add(failureMessage);
	}

	/**
	 * Validates whether there are failures and throws exception if there are.
	 */
	public void validate() {
		Optional<SoftAssertException> exception = getException();
		if (exception.isPresent()) {
			throw exception.get();
		}
	}

	public Optional<SoftAssertException> getException() {
		if (!failures.get().isEmpty()) {
			StringBuilder error = new StringBuilder();
			error.append("The following assert has been failed:\n").append(Joiner.on('\n').join(failures.get()));
			return Optional.of(new SoftAssertException(error.toString()));
		} else {
			return Optional.absent();
		}
	}

	/**
	 * Cleans-up failures
	 */
	public void cleanupFailures() {
		failures.get().clear();
	}

}
