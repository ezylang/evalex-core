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
package com.ezylang.evalex.config;

import com.ezylang.evalex.config.TestConfigurationProvider.PostfixQuestionOperator;
import com.ezylang.evalex.config.TestConfigurationProvider.PrefixPlusPlusOperator;
import com.ezylang.evalex.operators.OperatorIfc;
import com.ezylang.evalex.operators.arithmetic.InfixModuloOperator;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MapBasedOperatorDictionaryTest {

  @Test
  void testCreationOfOperators() {
    OperatorIfc prefix = new PrefixPlusPlusOperator();
    OperatorIfc postfix = new PostfixQuestionOperator();
    OperatorIfc infix = new InfixModuloOperator();

    @SuppressWarnings({"unchecked", "varargs"})
    OperatorDictionaryIfc dictionary =
        MapBasedOperatorDictionary.ofOperators(
            Map.entry("++", prefix), Map.entry("?", postfix), Map.entry("%", infix));

    Assertions.assertThat(dictionary.hasPrefixOperator("++")).isTrue();
    Assertions.assertThat(dictionary.hasPostfixOperator("?")).isTrue();
    Assertions.assertThat(dictionary.hasInfixOperator("%")).isTrue();

    Assertions.assertThat(dictionary.getPrefixOperator("++")).isEqualTo(prefix);
    Assertions.assertThat(dictionary.getPostfixOperator("?")).isEqualTo(postfix);
    Assertions.assertThat(dictionary.getInfixOperator("%")).isEqualTo(infix);

    Assertions.assertThat(dictionary.hasPrefixOperator("A")).isFalse();
    Assertions.assertThat(dictionary.hasPostfixOperator("B")).isFalse();
    Assertions.assertThat(dictionary.hasInfixOperator("C")).isFalse();
  }

  @Test
  void testCaseInsensitivity() {
    OperatorIfc prefix = new PrefixPlusPlusOperator();
    OperatorIfc postfix = new PostfixQuestionOperator();
    OperatorIfc infix = new InfixModuloOperator();

    @SuppressWarnings({"unchecked", "varargs"})
    OperatorDictionaryIfc dictionary =
        MapBasedOperatorDictionary.ofOperators(
            Map.entry("PlusPlus", prefix),
            Map.entry("Question", postfix),
            Map.entry("Percent", infix));

    Assertions.assertThat(dictionary.hasPrefixOperator("PlusPlus")).isTrue();
    Assertions.assertThat(dictionary.hasPrefixOperator("plusplus")).isTrue();
    Assertions.assertThat(dictionary.hasPrefixOperator("PLUSPLUS")).isTrue();

    Assertions.assertThat(dictionary.hasPostfixOperator("Question")).isTrue();
    Assertions.assertThat(dictionary.hasPostfixOperator("question")).isTrue();
    Assertions.assertThat(dictionary.hasPostfixOperator("QUESTION")).isTrue();

    Assertions.assertThat(dictionary.hasInfixOperator("Percent")).isTrue();
    Assertions.assertThat(dictionary.hasInfixOperator("percent")).isTrue();
    Assertions.assertThat(dictionary.hasInfixOperator("PERCENT")).isTrue();
  }
}
