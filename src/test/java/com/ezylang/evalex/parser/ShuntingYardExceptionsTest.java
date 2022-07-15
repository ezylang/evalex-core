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
package com.ezylang.evalex.parser;

import static com.ezylang.evalex.parser.Token.TokenType.FUNCTION_PARAM_START;
import static com.ezylang.evalex.parser.Token.TokenType.INFIX_OPERATOR;
import static com.ezylang.evalex.parser.Token.TokenType.PREFIX_OPERATOR;
import static com.ezylang.evalex.parser.Token.TokenType.VARIABLE_OR_CONSTANT;

import com.ezylang.evalex.operators.arithmetic.InfixMultiplicationOperator;
import com.ezylang.evalex.operators.arithmetic.PrefixMinusOperator;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ShuntingYardExceptionsTest extends BaseParserTest {

  @Test
  void testUnexpectedToken() {
    List<Token> tokens = List.of(new Token(1, "x", FUNCTION_PARAM_START));
    Assertions.assertThatThrownBy(
            new ShuntingYardConverter(tokens, configuration)::toAbstractSyntaxTree)
        .isInstanceOf(ParseException.class)
        .hasMessage("Unexpected token of type 'FUNCTION_PARAM_START'");
  }

  @Test
  void testMissingPrefixOperand() {
    List<Token> tokens = List.of(new Token(1, "-", PREFIX_OPERATOR, new PrefixMinusOperator()));
    Assertions.assertThatThrownBy(
            new ShuntingYardConverter(tokens, configuration)::toAbstractSyntaxTree)
        .isInstanceOf(ParseException.class)
        .hasMessage("Missing operand for operator");
  }

  @Test
  void testMissingSecondInfixOperand() {
    List<Token> tokens =
        Arrays.asList(
            new Token(1, "2", VARIABLE_OR_CONSTANT),
            new Token(2, "*", INFIX_OPERATOR, new InfixMultiplicationOperator()));
    Assertions.assertThatThrownBy(
            new ShuntingYardConverter(tokens, configuration)::toAbstractSyntaxTree)
        .isInstanceOf(ParseException.class)
        .hasMessage("Missing second operand for operator");
  }
}