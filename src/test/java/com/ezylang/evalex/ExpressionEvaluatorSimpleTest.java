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
package com.ezylang.evalex;

import static org.assertj.core.api.Assertions.assertThat;

import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.operators.AbstractOperator;
import com.ezylang.evalex.operators.PostfixOperator;
import com.ezylang.evalex.parser.ParseException;
import com.ezylang.evalex.parser.Token;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ExpressionEvaluatorSimpleTest extends BaseExpressionEvaluatorTest {

  @PostfixOperator
  public class PostfixFactorialOperator extends AbstractOperator {
    @Override
    public EvaluationValue evaluate(
        Expression expression, Token operatorToken, EvaluationValue... operands)
        throws EvaluationException {
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

  @Test
  void doc() throws EvaluationException, ParseException {
    ExpressionConfiguration config =
        ExpressionConfiguration.defaultConfiguration()
            .withAdditionalOperators(Map.entry("!", new PostfixFactorialOperator()));
    Expression expression = new Expression("2! + 3!");
    System.out.println(expression.evaluate().getNumberValue()); // prints
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "1 : 1",
        "1+1 : 2",
        "1-1 : 0",
        "1-2 :  -1",
        "5-3-1 : 1",
        "-1+1 : 0",
        "1+ -1 : 0",
        "-1 + -1 : -2",
        "1+2+3 : 6",
        "8-4+1-2+4-2 : 5"
      })
  void testSimpleAdditiveEvaluation(String expression, String expectedResult)
      throws ParseException, EvaluationException {
    assertThat(evaluate(expression)).isEqualTo(expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "2/2 : 1",
        "4/2 : 2",
        "2*2 : 4",
        "2*4 : 8",
        "6/3*2 : 4",
        "6*2/3 : 4",
        "-1 * -1 : 1",
        "-2 * 2 : -4",
        "4*2/4 : 2",
        "4*2*3/2/2 : 6"
      })
  void testSimpleMultiplicativeEvaluation(String expression, String expectedResult)
      throws ParseException, EvaluationException {
    assertThat(evaluate(expression)).isEqualTo(expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "2+3*2 : 8",
        "2+3*2+2 : 10",
        "2+6/3+1 : 5",
        "2+6/3-1 : 3",
        "1+2+3+4/2+4-2 : 10",
        "2*2*2+4/2 : 10",
        "1*2-2*2/2+8 : 8"
      })
  void testSimpleMixedPrecedence(String expression, String expectedResult)
      throws ParseException, EvaluationException {
    assertThat(evaluate(expression)).isEqualTo(expectedResult);
  }

  @ParameterizedTest
  @CsvSource(
      delimiter = ':',
      value = {
        "(2+3)*2 : 10",
        "(2+3)*(2+2) : 20",
        "(2+6)/2+1 : 5",
        "(2+6)/(3-1) : 4",
        "(((1+2)+3)+4)/(2+4-4) : 5",
        "2*2*((2+4)/2) : 12",
        "1*(2-2*2)/2+8 : 7"
      })
  void testSimpleBraces(String expression, String expectedResult)
      throws ParseException, EvaluationException {
    assertThat(evaluate(expression)).isEqualTo(expectedResult);
  }
}
