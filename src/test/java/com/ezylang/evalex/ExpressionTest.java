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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.parser.ASTNode;
import com.ezylang.evalex.parser.ParseException;
import org.junit.jupiter.api.Test;

class ExpressionTest {

  @Test
  void testExpressionDefaults() {
    Expression expression = new Expression("a+b");

    assertThat(expression.getExpressionString()).isEqualTo("a+b");
    assertThat(expression.getConfiguration().getMathContext())
        .isEqualTo(ExpressionConfiguration.DEFAULT_MATH_CONTEXT);
    assertThat(expression.getConfiguration().getFunctionDictionary().hasFunction("SUM")).isTrue();
    assertThat(expression.getConfiguration().getOperatorDictionary().hasInfixOperator("+"))
        .isTrue();
    assertThat(expression.getConfiguration().getOperatorDictionary().hasPrefixOperator("+"))
        .isTrue();
    assertThat(expression.getConfiguration().getOperatorDictionary().hasPostfixOperator("+"))
        .isFalse();
  }

  @Test
  void testValidateOK() throws ParseException {
    new Expression("1+1").validate();
  }

  @Test
  void testValidateFail() {
    assertThatThrownBy(() -> new Expression("2#3").validate())
        .isInstanceOf(ParseException.class)
        .hasMessage("Undefined operator '#'");
  }

  @Test
  void testExpressionNode() throws ParseException, EvaluationException {
    Expression expression = new Expression("a*b");
    ASTNode subExpression = expression.createExpressionNode("4+3");

    EvaluationValue result = expression.with("a", 2).and("b", subExpression).evaluate();

    assertThat(result.getStringValue()).isEqualTo("14");
  }
}
