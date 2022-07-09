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

import com.ezylang.evalex.functions.FunctionIfc;
import com.ezylang.evalex.functions.basic.MaxFunction;
import com.ezylang.evalex.functions.basic.MinFunction;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MapBasedFunctionDictionaryTest {

  @Test
  void testCreationOfFunctions() {
    FunctionIfc min = new MinFunction();
    FunctionIfc max = new MaxFunction();

    @SuppressWarnings({"unchecked", "varargs"})
    FunctionDictionaryIfc dictionary =
        MapBasedFunctionDictionary.ofFunctions(Map.entry("min", min), Map.entry("max", max));

    Assertions.assertThat(dictionary.hasFunction("min")).isTrue();
    Assertions.assertThat(dictionary.hasFunction("max")).isTrue();

    Assertions.assertThat(dictionary.getFunction("min")).isEqualTo(min);
    Assertions.assertThat(dictionary.getFunction("max")).isEqualTo(max);

    Assertions.assertThat(dictionary.hasFunction("medium")).isFalse();
  }

  @Test
  void testCaseInsensitivity() {
    FunctionIfc min = new MinFunction();
    FunctionIfc max = new MaxFunction();

    @SuppressWarnings({"unchecked", "varargs"})
    FunctionDictionaryIfc dictionary =
        MapBasedFunctionDictionary.ofFunctions(Map.entry("Min", min), Map.entry("MAX", max));

    Assertions.assertThat(dictionary.hasFunction("min")).isTrue();
    Assertions.assertThat(dictionary.hasFunction("MIN")).isTrue();
    Assertions.assertThat(dictionary.hasFunction("Min")).isTrue();
    Assertions.assertThat(dictionary.hasFunction("max")).isTrue();
    Assertions.assertThat(dictionary.hasFunction("MAX")).isTrue();
    Assertions.assertThat(dictionary.hasFunction("Max")).isTrue();
  }
}
