---
layout: default
title: Configuration
nav_order: 3
---

## Configuration

EvalEx can be configured through an _ExpressionConfiguration_ object, which can be passed as a
parameter to the _Expression_ constructor.

> **Warning:** Be careful when using the same configuration object for different expressions.
> When using the _MapBasedDataAccessor_, they will share the same variable values.

Example usage, showing all default configuration values:

```java
ExpressionConfiguration configuration = ExpressionConfiguration.builder()
    .arraysAllowed(true)
    .dataAccessor(new MapBasedDataAccessor())
    .decimalPlacesRounding(ExpressionConfiguration.DECIMAL_PLACES_ROUNDING_UNLIMITED)
    .defaultConstants(ExpressionConfiguration.StandardConstants)
    .functionDictionary(ExpressionConfiguration.StandardFunctionsDictionary)
    .implicitMultiplicationAllowed(true)
    .mathContext(ExpressionConfiguration.DEFAULT_MATH_CONTEXT)
    .operatorDictionary(ExpressionConfiguration.StandardOperatorsDictionary)
    .powerOfPrecedence(OperatorIfc.OPERATOR_PRECEDENCE_POWER)
    .structuresAllowed(true)
    .build();

Expression expression = new Expression("2.128 + a", configuration);
```

### Arrays allowed

Specifies if the array index function is allowed (default is true). If set to false, the expression
will throw a _ParseException_, if there is a '[' is encountered in the expression and also no
operator or function is defined for this character.

### Data Accessor

The Data Accessor is responsible for storing and retrieving variable values.

See chapter [Data Access](../customization/data_access.html) for details.

The default implementation is the _MapBasedDataAccessor_, which stores all variables in a
**case-insensitive** _Map_.

### Decimal Places Rounding

Specifies the amount of decimal places to round to in each operation or function.
See chapter [Precision, Scale and Rounding](../concepts/rounding.html) for details.

### Default Constants

Specifies the default constants that can be used in every expression as a _Map_ with the constant
name and _EvaluationValue_ as value.
See the reference chapter for a
list: [Default Constants](../references/constants.html)

### Function Dictionary

The function dictionary is used to look up the functions that are used in an expression.

See chapter [Function Dictionary](../customization/function_dictionary.html) for details.

The default implementation is the _MapBasedFunctionDictionary_, which stores all variables in a
**case-insensitive** _Map_.

### Implicit Multiplication

Implicit multiplication automatically adds in expression like "(a+b)(b+c)" the missing
multiplication operator, so that the expression reads "(a+b) * (b+c)".

By default, implicit multiplication is enabled. It can be disabled with this configuration
parameter.

### Math Context

The math context is used throughout all operations and functions. The default has a precision of 68
and a rounding mode of _HALF_EVEN_.

See chapter [Precision, Scale and Rounding](../concepts/rounding.html) for details.

### Operator Dictionary

The operator dictionary is used to look up the operators that are used in an expression.

See chapter [Operator Dictionary](../customization/operator_dictionary.html) for details.

The default implementation is the _MapBasedOperatorDictionary_, which stores all variables in a
**case-insensitive** _Map_.

### Power Of Precedence

In mathematics, there is no general rule which precedence the power-of operator has.
Consider the expression "-2^2". The expression can have two different results, depending on the
precedence:

- If the precedence is lower than the unary minus, the result will be 4 (-2 * -2).
- If the precedence is higher than the unary minus, the result will be -4 -(2 * 2).

By default, EvalEx uses a lower precedence. You can configure to use a higher precedence by
specifying it here, or by using a predefined constant:

```java
ExpressionConfiguration configuration = ExpressionConfiguration.builder()
    .powerOfPrecedence(OperatorIfc.OPERATOR_PRECEDENCE_POWER_HIGHER)
    .build();

// will now result in -4, imstead of 4:
Expression expression = new Expression("-2^2", configuration);
```

### Structures Allowed

Specifies if the structure separator ('.') operator is allowed (default is true). If set to false,
the expression will throw a _ParseException_, if the a '.' is encountered in the expression and also
no operator or function is defined for this character.
