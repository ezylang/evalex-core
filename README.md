EvalEx - Java Expression Evaluator
==========

![example workflow](https://github.com/ezylang/evalex-core/actions/workflows/maven.yml/badge.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=evalex-core&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=evalex-core)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=evalex-core&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=evalex-core)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=evalex-core&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=evalex-core)

EvalEx is a handy expression evaluator for Java, that allows to evaluate expressions.

Key Features:

- Supports string, boolean and numerical, array and structure expressions and variables.
- Array and structure support: Arrays and structures can be mixed, building arbitrary data
  structures.
- Uses BigDecimal for numerical calculations.
- MathContext and number of decimal places can be configured, with automatic rounding.
- No dependencies to external libraries.
- Easy integration into existing systems to access data.
- Standard boolean and mathematical operators.
- Standard basic mathematical and boolean functions.
- Custom functions and operators can be added.
- Functions can be defined with a variable number of arguments (see MIN, MAX and SUM functions).
- Supports for hexadecimal numbers and scientific notations of numbers.
- Supports implicit multiplication, e.g. (a+b)(a-b) or 2(x-y) which equals to (a+b)\*(a-b) or 2\*(
  x-y)
- Lazy evaluation of function parameters, only if needed (see the IF function).

### Discussion

For announcements, questions and ideas visit
the [Discussions area](https://github.com/ezylang/evalex-core/discussions).

### Usage Examples

````java
// Simple expression with constant values
Expression expression=new Expression("1 + 2 / (4 * SQRT(4))");
    EvaluationValue result=expression.evaluate();
    System.out.println(result.getStringValue()); // prints 1.25

// Boolean expression with number, boolean and string parameter
    Expression expression=
    new Expression("(a > 0) || (b == true) || (name == \"Peter\")")
    .with("a",5)
    .and("b",true)
    .and("name","Peter");
    boolean result=expression.evaluate().getBooleanValue(); // is true

// String support, using + operator to concatenate
    Expression expression=
    new Expression("\"Hello \" + name + \", you are \" + age")
    .with("name","Frank")
    .and("age",38);
    System.out.println(expression.evaluate().getStringValue()); // prints Hello Frank, you are 38
````

````java
// Array support
List<BigDecimal> values=
    Arrays.asList(
    new BigDecimal("2.1"),
    new BigDecimal("3.2"),
    new BigDecimal("4.3"));
    List<Integer> factors=Arrays.asList(3,4,8);

    Expression expression=
    new Expression("values[i] * factors[x]")
    .with("values",values)
    .and("factors",factors)
    .and("i",1)
    .and("x",2);
    BigDecimal result=expression.evaluate().getNumberValue();
    System.out.println(result); // prints 25.6
````

````java
// Structure support: Structures and arrays can be combined to arbitrary data structures
Map<String, Object> order=new HashMap<>();
    order.put("id",12345);
    order.put("name","Mary");
    Map<String, Object> position=new HashMap<>();
    position.put("article",3114);
    position.put("amount",3);
    position.put("price",new BigDecimal("14.95"));
    order.put("positions",Arrays.asList(position));
    Expression expression=
    new Expression("order.positions[x].amount * order.positions[x].price")
    .with("order",order)
    .and("x",0);

    BigDecimal result=expression.evaluate().getNumberValue();
    System.out.println(result); // prints 44.85
````

````java
// Adding a postfix factorial operator
@PostfixOperator
public class PostfixFactorialOperator extends AbstractOperator {

  @Override
  public EvaluationValue evaluate(Expression expression, Token operatorToken,
      EvaluationValue... operands) {
    int number = operands[0].getNumberValue().intValue();
    BigDecimal factorial = BigDecimal.ONE;
    for (int i = 1; i <= number; i++) {
      factorial =
          factorial.multiply(
              new BigDecimal(i, expression.getConfiguration().getMathContext()),
              expression.getConfiguration().getMathContext());
    }
    return new EvaluationValue(factorial);
  }
}

  ExpressionConfiguration config =
      ExpressionConfiguration.defaultConfiguration()
          .withAdditionalOperators(Map.entry("!", new PostfixFactorialOperator()));
  Expression expression = new Expression("2! + 3!"); // prints 8
````

### Author and License

Copyright 2012-2022 by Udo Klimaschewski

**Thanks to all who contributed to this
project: [Contributors](https://github.com/ezylang/evalex-core/graphs/contributors)**

The software is licensed under the Apache License, Version 2.0 (
see [LICENSE](https://raw.githubusercontent.com/evalex-core/evalex-core/master/LICENSE) file).

* The *power of* operator (^) implementation was copied
  from [Stack Overflow](http://stackoverflow.com/questions/3579779/how-to-do-a-fractional-power-on-bigdecimal-in-java)
  Thanks to Gene Marin
