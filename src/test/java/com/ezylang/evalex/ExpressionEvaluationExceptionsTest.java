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

import com.ezylang.evalex.parser.ASTNode;
import com.ezylang.evalex.parser.Token;
import com.ezylang.evalex.parser.Token.TokenType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ExpressionEvaluationExceptionsTest {

  @Test
  void testUnexpectedToken() {
    Expression expression = new Expression("1");

    Assertions.assertThatThrownBy(
            () -> {
              ASTNode node = new ASTNode(new Token(1, "(", TokenType.BRACE_OPEN));
              expression.evaluateSubtree(node);
            })
        .isInstanceOf(EvaluationException.class)
        .hasMessage(
            "Unexpected evaluation token: Token(startPosition=1, value=(, type=BRACE_OPEN)");
  }
}
