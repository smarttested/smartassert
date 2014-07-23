# SmartAsserts
*SmartAsserts* is a set of utils which is used to provide the simplest way to perform 'hard' and 'soft' (delayed) assertions.

## Guidline:
 1. **[HOW TO (As simple as it is possible)](#how-to)**
 2. [Hard Assertions](#hard-assertions)
 3. [Soft Assertions](#soft-assertions)
 4. [TestNG integration](#testng-integration)


## HOW-TO
```java
public class YourUnitTest
{


   @Test
   public void testYourBusinessLogic()
   {

   }
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

In this case test will fail on first assertion once provided object is not positive, but after test you don't know whether test has right scale! Another words, **second assert is not performed**. To solve this issue you might do something like:

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
Looks like this is not perfect way to solve issue :) This is actually what [Soft Assertions](#soft-assertions) do for you.

## Soft Assertions

## TestNG Integration
