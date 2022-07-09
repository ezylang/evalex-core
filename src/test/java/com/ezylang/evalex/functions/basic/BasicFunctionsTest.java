/*
  Copyright 2012-2022 Udo Klimaschewski

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
package com.ezylang.evalex.functions.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ezylang.evalex.BaseEvaluationTest;
import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.parser.ParseException;
import java.math.MathContext;
import java.math.RoundingMode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class BasicFunctionsTest extends BaseEvaluationTest {

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "FACT(0) : 1",
        "FACT(1) : 1",
        "FACT(2) : 2",
        "FACT(3) : 6",
        "FACT(5) : 120",
        "FACT(10) : 3628800",
        "FACT(20) : 2432902008176640000"
      })
  void testFactorial(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "IF(1, 4/2, 4/0) : 2",
        "IF(1, 4/IF(0, 5/0, 2*2), 4/0) : 1",
        "IF(1, 6/IF(0, 5/0, 2*IF(1, 3, 6/0)), 4/0) : 1"
      })
  void testIf(String expression, String expectedResult) throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "MAX(99) : 99",
        "MAX(2,1) : 2",
        "MAX(1,9,-5,6,3,7) : 9",
        "MAX(17,88,77,66,609,1567,1876534) : 1876534"
      })
  void testMax(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "MIN(99) : 99",
        "MIN(2,1) : 1",
        "MIN(1,9,-5,6,3,7) : -5",
        "MIN(17,88,77,66,609,1567,1876534) : 17"
      })
  void testMin(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "ROUND(1.1,0) : 1",
        "ROUND(1.5,0) : 2",
        "ROUND(2.34,1) : 2.3",
        "ROUND(2.35,1) : 2.4",
        "ROUND(2.323789,2) : 2.32",
        "ROUND(2.324789,2) : 2.32"
      })
  void testRoundHalfEven(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "ROUND(1.1,0) : 2",
        "ROUND(1.5,0) : 2",
        "ROUND(2.34,1) : 2.4",
        "ROUND(2.35,1) : 2.4",
        "ROUND(2.323789,2) : 2.33",
        "ROUND(2.324789,2) : 2.33"
      })
  void testRoundUp(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    ExpressionConfiguration configuration =
        ExpressionConfiguration.builder().mathContext(new MathContext(32, RoundingMode.UP)).build();
    assertExpressionHasExpectedResult(expression, expectedResult, configuration);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "SUM(1) : 1",
        "SUM(1,2,3,4) : 10",
        "SUM(1,-1) : 0",
        "SUM(1,10,100,1000,10000) : 11111",
        "SUM(1,2,3,-3,-2,5) : 6"
      })
  void testSum(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "SQRT(0) : 0",
        "SQRT(1) : 1",
        "SQRT(2) : 1.4142135623730950488016887242096980785696718753769480731766797379907",
        "SQRT(4) : 2",
        "SQRT(5) : 2.2360679774997896964091736687312762354406183596115257242708972454105",
        "SQRT(10) : 3.1622776601683793319988935444327185337195551393252168268575048527926",
        "SQRT(236769) : 486.58914907753543122473972072155030396245230523850016876894122736411"
      })
  void testSqrt(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @Test
  void testSqrtNegative() {
    assertThatThrownBy(() -> new Expression("SQRT(-1)").evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Parameter to SQRT must not be negative");
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "NOT(0) : true",
        "NOT(1) : false",
        "NOT(20) : false",
        "NOT(\"true\") : false",
        "NOT(\"false\") : true",
        "NOT(2-4/2) : true",
      })
  void testNot(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @Test
  void testRandom() throws EvaluationException, ParseException {
    EvaluationValue r1 = new Expression("RANDOM()").evaluate();
    EvaluationValue r2 = new Expression("RANDOM()").evaluate();

    assertThat(r1).isNotEqualByComparingTo(r2);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "ABS(0) : 0",
        "ABS(1) : 1",
        "ABS(-1) : 1",
        "ABS(20) : 20",
        "ABS(-20) : 20",
        "ABS(2.12345) : 2.12345",
        "ABS(-2.12345) : 2.12345"
      })
  void testAbs(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "FLOOR(0) : 0",
        "FLOOR(1) : 1",
        "FLOOR(-1) : -1",
        "FLOOR(20) : 20",
        "FLOOR(-20) : -20",
        "FLOOR(2.12345) : 2",
        "FLOOR(-2.12345) : -3",
        "FLOOR(-2.97345) : -3"
      })
  void testFloor(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "CEILING(0) : 0",
        "CEILING(1) : 1",
        "CEILING(-1) : -1",
        "CEILING(20) : 20",
        "CEILING(-20) : -20",
        "CEILING(2.12345) : 3",
        "CEILING(-2.12345) : -2",
        "CEILING(-2.97345) : -2"
      })
  void testCeiling(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "LOG(1) : 0.0",
        "LOG(10) : 2.302585092994046",
        "LOG(2.12345) : 0.7530421244614831",
        "LOG(1567) : 7.356918242356021"
      })
  void testLog(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @Test
  void testLogNegative() {
    assertThatThrownBy(() -> new Expression("LOG(-1)").evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Parameter to LOG must be positive and not zero");
  }

  @Test
  void testLogZero() {
    assertThatThrownBy(() -> new Expression("LOG(0)").evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Parameter to LOG must be positive and not zero");
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "LOG10(1) : 0.0",
        "LOG10(10) : 1.0",
        "LOG10(2.12345) : 0.3270420392943239",
        "LOG10(1567) : 3.1950689964685903"
      })
  void testLog10(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @Test
  void testLog10Negative() {
    assertThatThrownBy(() -> new Expression("LOG10(-1)").evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Parameter to LOG10 must be positive and not zero");
  }

  @Test
  void testLog10Zero() {
    assertThatThrownBy(() -> new Expression("LOG10(0)").evaluate())
        .isInstanceOf(EvaluationException.class)
        .hasMessage("Parameter to LOG10 must be positive and not zero");
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "DEG(0) : 0.0",
        "DEG(1) : 57.29577951308232",
        "DEG(90) : 5156.620156177409",
        "DEG(-90) : -5156.620156177409"
      })
  void testDeg(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "RAD(0) : 0.0",
        "RAD(1) : 0.017453292519943295",
        "RAD(45) : 0.7853981633974483",
        "RAD(50) : 0.8726646259971648",
        "RAD(90) : 1.5707963267948966",
        "RAD(-90) : -1.5707963267948966"
      })
  void testRad(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {"SIN(0) : 0.0", "SIN(1) : 0.01745240643728351", "SIN(90) : 1.0", "SIN(-90) : -1.0"})
  void testSin(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "COS(0) : 1.0",
        "COS(1) : 0.9998476951563913",
        "COS(19) : 0.9455185755993168",
        "COS(-19) : 0.9455185755993168"
      })
  void testCos(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "TAN(0) : 0.0",
        "TAN(1) : 0.017455064928217585",
        "TAN(19) : 0.34432761328966527",
        "TAN(-19) : -0.34432761328966527"
      })
  void testTan(String expression, String expectedResult)
      throws EvaluationException, ParseException {
    assertExpressionHasExpectedResult(expression, expectedResult);
  }
}
