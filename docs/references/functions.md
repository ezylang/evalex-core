---
layout: default
title: Standard Functions
nav_order: 3
parent: References
---

## Standard Functions

Available through the _ExpressionConfiguration.StandardFunctionsDictionary_ constant:

### Basic Functions

| Name                                       | Value                                                                                                                   |
|--------------------------------------------|-------------------------------------------------------------------------------------------------------------------------|
| ABS(value)                                 | Absolute (non-negative) value                                                                                           |
| CEILING(value)                             | Rounds the given value an integer using the rounding mode CEILING                                                       |
| FACT(base)                                 | Calculates the factorial of a base value                                                                                |
| FLOOR(value)                               | Rounds the given value an integer using the rounding mode FLOOR                                                         |
| IF(condition, resultIfTrue, resultIfFalse) | Conditional evaluation function. If _condition_ is true, the _resultIfTrue_ is returned, else the _resultIfFalse_ value |
| LOG(value)                                 | The natural logarithm (base e) of a value                                                                               |
| LOG10(value)                               | The base 10 logarithm of a value                                                                                        |
| MAX(value, ...)                            | Returns the maximum value of all parameters                                                                             |
| MIN(value, ...)                            | Returns the maximum value of all parameters                                                                             |
| NOT(value)                                 | Boolean negation, implemented as a function (for compatibility)                                                         |
| RANDOM()                                   | Produces a random value between 0 and 1                                                                                 |
| ROUND(value, scale)                        | Rounds the given value to the specified scale, using the current rounding mode                                          |
| SQRT(value)                                | Square root function                                                                                                    |
| SUM(value, ...)                            | Returns the sum of all parameters                                                                                       |

### String Functions

| Name                            | Value                                                                 |
|---------------------------------|-----------------------------------------------------------------------|
| STR_CONTAINS(string, substring) | Returns true, if the string contains the substring (case-insensitive) |
| STR_LOWER(value)                | Converts the given value to lower case                                |
| STR_UPPER(value)                | Converts the given value to upper case                                |

### Trigonometric Functions

| Name         | Value                                                                                          |
|--------------|------------------------------------------------------------------------------------------------|
| COS(degrees) | Returns the trigonometric cosine of an angle (in degrees)                                      |
| DEG(rad)     | Converts an angle measured in radians to an approximately equivalent angle measured in degrees |
| RAD(degrees) | Converts an angle measured in degrees to an approximately equivalent angle measured in radians |
| SIN(degrees) | Returns the trigonometric sine of an angle (in degrees)                                        |
| TAN(degrees) | Returns the trigonometric tangent of an angle (in degrees)                                     |

