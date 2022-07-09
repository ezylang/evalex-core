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

import com.ezylang.evalex.data.DataAccessorIfc;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.data.MapBasedDataAccessor;
import com.ezylang.evalex.functions.FunctionIfc;
import com.ezylang.evalex.functions.basic.AbsFunction;
import com.ezylang.evalex.functions.basic.CeilingFunction;
import com.ezylang.evalex.functions.basic.FactFunction;
import com.ezylang.evalex.functions.basic.FloorFunction;
import com.ezylang.evalex.functions.basic.IfFunction;
import com.ezylang.evalex.functions.basic.Log10Function;
import com.ezylang.evalex.functions.basic.LogFunction;
import com.ezylang.evalex.functions.basic.MaxFunction;
import com.ezylang.evalex.functions.basic.MinFunction;
import com.ezylang.evalex.functions.basic.NotFunction;
import com.ezylang.evalex.functions.basic.RandomFunction;
import com.ezylang.evalex.functions.basic.RoundFunction;
import com.ezylang.evalex.functions.basic.SqrtFunction;
import com.ezylang.evalex.functions.basic.SumFunction;
import com.ezylang.evalex.functions.string.UpperFunction;
import com.ezylang.evalex.functions.trigonometric.CosFunction;
import com.ezylang.evalex.functions.trigonometric.DegFunction;
import com.ezylang.evalex.functions.trigonometric.RadFunction;
import com.ezylang.evalex.functions.trigonometric.SinFunction;
import com.ezylang.evalex.functions.trigonometric.TanFunction;
import com.ezylang.evalex.operators.OperatorIfc;
import com.ezylang.evalex.operators.arithmetic.InfixDivisionOperator;
import com.ezylang.evalex.operators.arithmetic.InfixMinusOperator;
import com.ezylang.evalex.operators.arithmetic.InfixModuloOperator;
import com.ezylang.evalex.operators.arithmetic.InfixMultiplicationOperator;
import com.ezylang.evalex.operators.arithmetic.InfixPlusOperator;
import com.ezylang.evalex.operators.arithmetic.InfixPowerOfOperator;
import com.ezylang.evalex.operators.arithmetic.PrefixMinusOperator;
import com.ezylang.evalex.operators.arithmetic.PrefixPlusOperator;
import com.ezylang.evalex.operators.booleans.InfixAndOperator;
import com.ezylang.evalex.operators.booleans.InfixEqualsOperator;
import com.ezylang.evalex.operators.booleans.InfixGreaterEqualsOperator;
import com.ezylang.evalex.operators.booleans.InfixGreaterOperator;
import com.ezylang.evalex.operators.booleans.InfixLessEqualsOperator;
import com.ezylang.evalex.operators.booleans.InfixLessOperator;
import com.ezylang.evalex.operators.booleans.InfixNotEqualsOperator;
import com.ezylang.evalex.operators.booleans.InfixOrOperator;
import com.ezylang.evalex.operators.booleans.PrefixNotOperator;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

/**
 * The expression configuration can be used to configure various aspects of expression parsing and
 * evaluation. <br>
 * A <code>Builder</code> is provided to create custom configurations, e.g.: <br>
 *
 * <pre>
 *   ExpressionConfiguration config = ExpressionConfiguration.builder().mathContext(MathContext.DECIMAL32).dataAccessor(myDataAccessor).build();
 * </pre>
 *
 * <br>
 * Additional operators and functions can be added to an existing configuration:<br>
 *
 * <pre>
 *     ExpressionConfiguration.defaultConfiguration()
 *        .withAdditionalOperators(
 *            Map.entry("++", new PrefixPlusPlusOperator()),
 *            Map.entry("++", new PostfixPlusPlusOperator()))
 *        .withAdditionalFunctions(Map.entry("save", new SaveFunction()),
 *            Map.entry("update", new UpdateFunction()));
 * </pre>
 */
@SuppressWarnings("UnusedAssignment")
@Builder
public class ExpressionConfiguration {

  @SuppressWarnings({"unchecked", "varargs"})
  public static final OperatorDictionaryIfc StandardOperatorsDictionary =
      MapBasedOperatorDictionary.ofOperators(
          // arithmetic
          Map.entry("+", new PrefixPlusOperator()),
          Map.entry("-", new PrefixMinusOperator()),
          Map.entry("+", new InfixPlusOperator()),
          Map.entry("-", new InfixMinusOperator()),
          Map.entry("*", new InfixMultiplicationOperator()),
          Map.entry("/", new InfixDivisionOperator()),
          Map.entry("^", new InfixPowerOfOperator()),
          Map.entry("%", new InfixModuloOperator()),
          // booleans
          Map.entry("=", new InfixEqualsOperator()),
          Map.entry("==", new InfixEqualsOperator()),
          Map.entry("!=", new InfixNotEqualsOperator()),
          Map.entry("<>", new InfixNotEqualsOperator()),
          Map.entry(">", new InfixGreaterOperator()),
          Map.entry(">=", new InfixGreaterEqualsOperator()),
          Map.entry("<", new InfixLessOperator()),
          Map.entry("<=", new InfixLessEqualsOperator()),
          Map.entry("&&", new InfixAndOperator()),
          Map.entry("||", new InfixOrOperator()),
          Map.entry("!", new PrefixNotOperator()));

  @SuppressWarnings({"unchecked", "varargs"})
  public static final FunctionDictionaryIfc StandardFunctionsDictionary =
      MapBasedFunctionDictionary.ofFunctions(
          // basic functions
          Map.entry("ABS", new AbsFunction()),
          Map.entry("CEILING", new CeilingFunction()),
          Map.entry("FACT", new FactFunction()),
          Map.entry("FLOOR", new FloorFunction()),
          Map.entry("IF", new IfFunction()),
          Map.entry("LOG", new LogFunction()),
          Map.entry("LOG10", new Log10Function()),
          Map.entry("MAX", new MaxFunction()),
          Map.entry("MIN", new MinFunction()),
          Map.entry("NOT", new NotFunction()),
          Map.entry("ROUND", new RoundFunction()),
          Map.entry("SUM", new SumFunction()),
          Map.entry("SQRT", new SqrtFunction()),
          Map.entry("RANDOM", new RandomFunction()),
          // trigonometric
          Map.entry("DEG", new DegFunction()),
          Map.entry("RAD", new RadFunction()),
          Map.entry("SIN", new SinFunction()),
          Map.entry("COS", new CosFunction()),
          Map.entry("TAN", new TanFunction()),
          // string functions
          Map.entry("STR_UPPER", new UpperFunction()));

  public static final Map<String, EvaluationValue> StandardConstants =
      Collections.unmodifiableMap(getStandardConstants());

  private static Map<String, EvaluationValue> getStandardConstants() {

    Map<String, EvaluationValue> constants = new HashMap<>();

    constants.put("TRUE", new EvaluationValue(true));
    constants.put("FALSE", new EvaluationValue(false));
    constants.put(
        "PI",
        new EvaluationValue(
            new BigDecimal(
                "3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679")));
    constants.put(
        "E",
        new EvaluationValue(
            new BigDecimal(
                "2.71828182845904523536028747135266249775724709369995957496696762772407663")));

    return constants;
  }

  /** The default math context has a precision of 68 and {@link RoundingMode#HALF_EVEN}. */
  public static final MathContext DEFAULT_MATH_CONTEXT =
      new MathContext(68, RoundingMode.HALF_EVEN);

  /** The operator dictionary holds all operators that will be allowed in an expression. */
  @SuppressWarnings("UnusedAssignment")
  @Builder.Default
  @Getter
  private OperatorDictionaryIfc operatorDictionary = StandardOperatorsDictionary;

  /** The function dictionary holds all functions that will be allowed in an expression. */
  @SuppressWarnings("UnusedAssignment")
  @Builder.Default
  @Getter
  private FunctionDictionaryIfc functionDictionary = StandardFunctionsDictionary;

  /** The math context to use. */
  @SuppressWarnings("UnusedAssignment")
  @Builder.Default
  @Getter
  private MathContext mathContext = DEFAULT_MATH_CONTEXT;

  /**
   * The data accessor is responsible for accessing variable and constant values in an expression.
   */
  @SuppressWarnings("UnusedAssignment")
  @Builder.Default
  @Getter
  private DataAccessorIfc dataAccessor = new MapBasedDataAccessor();

  /**
   * Default constants will be added automatically to each expression and can be used in expression
   * evaluation.
   */
  @Builder.Default @Getter
  private Map<String, EvaluationValue> defaultConstants = StandardConstants;

  /** Support for arrays in expressions are allowed or not. */
  @Builder.Default @Getter private boolean arraysAllowed = true;

  /** Support for structures in expressions are allowed or not. */
  @Builder.Default @Getter private boolean structuresAllowed = true;

  /** Support for implicit multiplication, like in (a+b)(b+c) are allowed or not. */
  @Builder.Default @Getter private boolean implicitMultiplicationAllowed = true;

  /**
   * The power of operator precedence, can be set higher {@link
   * OperatorIfc#OPERATOR_PRECEDENCE_POWER_HIGHER} or to a custom value.
   */
  @Builder.Default @Getter private int powerOfPrecedence = OperatorIfc.OPERATOR_PRECEDENCE_POWER;

  /**
   * Convenience method to create a default configuration.
   *
   * @return A configuration with default settings.
   */
  public static ExpressionConfiguration defaultConfiguration() {
    return ExpressionConfiguration.builder().build();
  }

  /**
   * Adds additional operators to this configuration.
   *
   * @param operators variable number of arguments with a map entry holding the operator name and
   *     implementation. <br>
   *     Example:
   *     <pre>
   *     ExpressionConfiguration.defaultConfiguration()
   *        .withAdditionalOperators(
   *            Map.entry("++", new PrefixPlusPlusOperator()),
   *            Map.entry("++", new PostfixPlusPlusOperator()));
   *     </pre>
   *
   * @return The modified configuration, to allow chaining of methods.
   */
  @SafeVarargs
  public final ExpressionConfiguration withAdditionalOperators(
      Map.Entry<String, OperatorIfc>... operators) {
    Arrays.stream(operators)
        .forEach(entry -> operatorDictionary.addOperator(entry.getKey(), entry.getValue()));
    return this;
  }

  /**
   * Adds additional functions to this configuration.
   *
   * @param functions variable number of arguments with a map entry holding the functions name and
   *     implementation. <br>
   *     Example:
   *     <pre>
   *     ExpressionConfiguration.defaultConfiguration()
   *        .withAdditionalFunctions(
   *            Map.entry("save", new SaveFunction()),
   *            Map.entry("update", new UpdateFunction()));
   *     </pre>
   *
   * @return The modified configuration, to allow chaining of methods.
   */
  @SafeVarargs
  public final ExpressionConfiguration withAdditionalFunctions(
      Map.Entry<String, FunctionIfc>... functions) {
    Arrays.stream(functions)
        .forEach(entry -> functionDictionary.addFunction(entry.getKey(), entry.getValue()));
    return this;
  }
}
