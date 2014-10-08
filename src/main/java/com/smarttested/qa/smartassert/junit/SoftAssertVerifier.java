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
package com.smarttested.qa.smartassert.junit;

import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.smarttested.qa.smartassert.SmartAssert;
import com.smarttested.qa.smartassert.SoftAssertException;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static com.smarttested.qa.smartassert.SmartAssert.validateSoftAsserts;

/**
 * JUnit Verifier for SmartAssert's soft assertions.
 * Will verify and clear soft asserts after each test method
 * <p/>
 * Create instance with double checked locking, but leave
 * constructor public to give a chance to use inheritance if needed
 *
 * @author Andrei Varabyeu
 */
public class SoftAssertVerifier implements TestRule {

    private static Supplier<SoftAssertVerifier> instance = Suppliers.memoize(new Supplier<SoftAssertVerifier>() {
        @Override
        public SoftAssertVerifier get() {
            return new SoftAssertVerifier();
        }
    });

    public static SoftAssertVerifier instance() {
        return instance.get();
    }


    @Override
    public Statement apply(final Statement base, final Description description) {
        return new VerifierStatement(base);

    }

    private class VerifierStatement extends Statement {

        private Statement base;

        private boolean expectsException;

        private VerifierStatement(Statement base) {
            this.base = base;
            this.expectsException = ExpectException.class.isAssignableFrom(base.getClass());
        }


        @Override
        public void evaluate() throws Throwable {
            try {
                base.evaluate();
            } catch (AssertionError e) {
                if (expectsException) {
                    Optional<SoftAssertException> softException = SmartAssert.getSoftFailures().getException();
                    if (softException.isPresent() && e.getMessage().contains(softException.get().getClass().getCanonicalName())) {
                        /* everything is OK, we expect this exception. Leave without error */
                        return;
                    }
                    throw e;
                }
            }
            validateSoftAsserts();
        }
    }
}


