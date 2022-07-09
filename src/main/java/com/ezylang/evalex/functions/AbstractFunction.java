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
package com.ezylang.evalex.functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

/**
 * Abstract implementation of the {@link FunctionIfc}, used as base class for function
 * implementations.
 */
public abstract class AbstractFunction implements FunctionIfc {

  @Getter
  private final List<FunctionParameterDefinition> functionParameterDefinitions = new ArrayList<>();

  /**
   * Creates a new function and uses the {@link FunctionParameter} annotations to create the
   * parameter definitions.
   */
  protected AbstractFunction() {
    FunctionParameter[] parameterAnnotations =
        getClass().getAnnotationsByType(FunctionParameter.class);

    Arrays.stream(parameterAnnotations)
        .forEach(
            parameter ->
                functionParameterDefinitions.add(
                    new FunctionParameterDefinition(
                        parameter.name(), parameter.isVarArg(), parameter.isLazy())));
  }
}
