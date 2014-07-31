# SmartAsserts
*SmartAsserts* is a set of utils which is used to provide the simplest way to perform 'hard' and 'soft' (delayed) assertions.

## Guidline:
 1. **[HOW TO (As simple as it is possible)](#how-to)**
 2. [Hard Assertions](#hard-assertions)
 3. [Soft Assertions](#soft-assertions)
 4. [TestNG integration](#testng-integration)
 5. [Predicates and predefined validators](#predicates-and-predefined-validators)


## HOW-TO
```java
/* Put annotation to let TestNG know you are gonna use SmartAssert */
@Listeners(SoftValidationMethodListener.class)
public class YourUnitTest
{


   @Test
   public void testYourBusinessLogic()
   {
      /* Add validation of logic you wonna check */
      
      SmartAssert.expect(Lists.newArrayList("one", "two"), CoreMatchers.hasItem("three"), "There is no 'three'!")
      	.assertSoft();
      SmartAssert.expect(true, CoreMatchers.is(false), "True is not false!").assertSoft();
      
      /* add another validations you need. First one won't fail this test */
      System.out.println("Validations failed, but test still works!")
   }
   
   /* Enjoy! Once test is finished, it will fail with all failed validations it had! */
}
```

## Hard Assertions
Hard assertions is basically assertions you always used before. For example, 

```java
org.testng.Assert.assertTrue(false);
```

Let's assume we have some BigDecimal object and need to validate several fields:

```java
BigDecimal decimal = BigDecimal.valueOf(-0.05);

/* check value is positive. Provided value is negative, so we've got error assertion here */
Assert.assertEquals("Positive value is expected!", 1, decimal.signum());

/* check value has scale == 1. Provided value's scale is '2', so we've got error assertion here */
Assert.assertEquals("Incorrect decimal scale!", 1, decimal.scale());
```

In this case test will fail on first assertion once provided object is not positive, but after test you don't know whether test object has right scale! Another words, **second assert is not performed**. To solve this issue you might do something like:

```java
BigDecimal decimal = BigDecimal.valueOf(-0.05);
StringBuilder errors = new StringBuilder();

/* check value is positive. Provided value is negative, so we've got error assertion here */
if (1 != decimal.signum()){
   errors.append("Positive value is expected!");
}

/* check value has scale == 1. Provided value's scale is '2', so we've got error assertion here */
if (1 != decimal.scale()){
   errors.append("Incorrect decimal scale!");
}

Assert.assertTrue(errors.toString(), 0 == errors.length());
```
This is not perfect way to solve issue obviously :) 

So, problem described above is actually what [Soft Assertions](#soft-assertions) do for you.

## Soft Assertions

Let's try to re-write example above in soft-assert style:

```java
BigDecimal decimal = BigDecimal.valueOf(-0.05);

/* check value is positive. Provided value is negative, but we haven't got error */
SmartAssert.expect(decimal.signum(), CoreMatchers.is(1), "Positive value is expected!").assertSoft();

/* check value has scale == 1. Provided value's scale is '2', so we haven't got error assertion here */
SmartAssert.expect(decimal.scale(), CoreMatchers.is(1), "Incorrect decimal scale!").assertSoft();

/* Here is place were real assertion will be thrown */
SmartAssert.validateSoftAsserts();
```

Using smartassert you are able to write assertions in junit/testNG style, but perform real validation on demand (basically, at the end of the method). All failures will be accumulated into one:
```
com.smarttested.qa.smartassert.SoftAssertException: The following assert has been failed:  
Positive value is expected!  
Expected: is <1>  
     but: was <-1>  
Incorrect decimal scale!  
Expected: is <1>  
     but: was <2>  
	at com.smarttested.qa.smartassert.SoftFailuresHolder.getException(SoftFailuresHolder.java:62)  
	at com.smarttested.qa.smartassert.SoftFailuresHolder.validate(SoftFailuresHolder.java:52)  
	at com.smarttested.qa.smartassert.SmartAssert.validateSoftAsserts(SmartAssert.java:134)  
	at com.smarttested.qa.smartassert.SoftAssertTest.testBigDecimal(SoftAssertTest.java:27)  
```

## TestNG Integration
To avoid calling 
```java
SmartAssert.validateSoftAsserts();
```
we provided TestNG listener which will do that on each test method. Another words, if some test has some faulures, they will be validated after methed/test execution. 
```java
@Listeners(SoftValidationMethodListener.class)
public class SoftValidationMethodListenerTest {

    @Test
    public void testGuavaSoft() {
        SmartAssert.expect(true, Predicates.alwaysFalse(), "I'm an error!").assertSoft();
        SmartAssert.expect(true, Predicates.alwaysFalse(), "it's ok").assertSoft();
        SmartAssert.expect(true, Predicates.alwaysFalse(), "I'm an second error!").assertSoft();
    }
}
```
This method fails with description of all three failed validations. 
Sometimes you need to write negative test which is expects some error. SoftValidationMethodListener support default TestNG behavior so this test pass, because expected exception is specified in TestNG annotation:
```java
@Listeners(SoftValidationMethodListener.class)
public class SoftValidationMethodListenerTest {

    @Test(expectedExceptions = {SoftAssertException.class, SkipException.class})
    public void testGuavaSoft() {
        SmartAssert.expect(true, Predicates.alwaysFalse(), "I'm an error!").assertSoft();
        SmartAssert.expect(true, Predicates.alwaysFalse(), "it's ok").assertSoft();
        SmartAssert.expect(true, Predicates.alwaysFalse(), "I'm an second error!").assertSoft();
    }
}
```

Actually, there is a still way to use hard asserts:
```java
SmartAssert.expect(true, Predicates.alwaysFalse(), "I'm an error!").assertHard();
```

or

```java
SmartAssert.expect(true, Predicates.alwaysFalse(), "I'm an error!").assertHard(SomeYourCustomException.class);
```
if you wonna some custom exception to be raised here. 

## Predicates and predefined validators
A very good style of writing unit and functional tests is to prepare validators you need and do not duplicate them in each test. There is a two ways to do that. First, you can use built-in Hamcrest validators:
```java
SmartAssert.expect("hello world", CoreMatchers.containsString("hello"), "This validation is passing").assertSoft();
```
Hamcrest has a lot of validators which may cover almost all your needs. Of course, you can prepare your own validators by implementing Hamcrest's ```org.hamcrest.Matcher``` interface. More information you can find [here](https://code.google.com/p/hamcrest/) 

Another way to perform validation and making matchers is [Guava's Predicates](https://code.google.com/p/guava-libraries/wiki/FunctionalExplained#Predicates)

Let's consider the following example:
```java
@Listeners(SoftValidationMethodListener.class)
public class SoftAssertTest {

    public static final Predicate<String> STRING_IN_UPPER_CASE = new Predicate<String>() {
        @Override
        public boolean apply(String input) {
            /* validates all symbols in string are whitespaces or upper-case symbols */
            return CharMatcher.JAVA_UPPER_CASE.or(CharMatcher.WHITESPACE).matchesAllOf(input);
        }

        /* add toString just to have pretty error message if validator fail on this predicate */
        @Override
        public String toString() {
            return "Upper case validator";
        }
    };

    @Test
    public void testUpperCase() {
        /* this gonna fail */
        SmartAssert.expect("lower case", STRING_IN_UPPER_CASE, "String is not in upper case").assertSoft();

        /* this gonna pass */
        SmartAssert.expect("UPPER CASE", STRING_IN_UPPER_CASE, "String is not in upper case").assertSoft();
    }
}
```

So, by creating your own matchers and predicates you can move your validation logic into separate logic layer and use across all project. You might also want to use this stuff via some dependency injection container (like Guice or Spring) and replace your validators depending on some conditions:

```java
    
    /* you can have multiple implementations. For example, first one
     * can validate only upper case symbols, second one can pass
     * validation with digits and whitespaces, because they do not
     * have case at all. 
     */
    @Inject
    @Named("upperCaseValidator")
    private Predicate<String> upperCaseValidator;
```
