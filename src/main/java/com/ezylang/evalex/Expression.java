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

import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.parser.ASTNode;
import com.ezylang.evalex.parser.ParseException;
import com.ezylang.evalex.parser.ShuntingYardConverter;
import com.ezylang.evalex.parser.Token;
import com.ezylang.evalex.parser.Tokenizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;

/**
 * Main class that allow creating, parsing, passing parameters and evaluating an expression string.
 *
 * @see <a href="https://github.com/ezylang/EvalEx">EvalEx Homepage</a>
 */
public class Expression {
  @Getter private final ExpressionConfiguration configuration;

  @Getter private final String expressionString;

  private ASTNode abstractSyntaxTree;

  /**
   * Creates a new expression with the default configuration. The expression is not parsed until it
   * is first evaluated or validated.
   *
   * @param expressionString A string holding an expression.
   */
  public Expression(String expressionString) {
    this(expressionString, ExpressionConfiguration.defaultConfiguration());
  }

  /**
   * Creates a new expression with a custom configuration. The expression is not parsed until it is
   * first evaluated or validated.
   *
   * @param expressionString A string holding an expression.
   */
  public Expression(String expressionString, ExpressionConfiguration configuration) {
    this.expressionString = expressionString;
    this.configuration = configuration;

    // add default constants to data
    for (Map.Entry<String, EvaluationValue> constant :
        configuration.getDefaultConstants().entrySet()) {
      configuration.getDataAccessor().setData(constant.getKey(), constant.getValue());
    }
  }

  /**
   * Evaluates the expression by parsing it (if not done before) and the evaluating it.
   *
   * @return The evaluation result value.
   * @throws EvaluationException If there were problems while evaluating the expression.
   * @throws ParseException If there were problems while parsing the expression.
   */
  public EvaluationValue evaluate() throws EvaluationException, ParseException {
    return evaluateSubtree(getAbstractSyntaxTree());
  }

  /**
   * Evaluates only a subtree of the abstract syntax tree.
   *
   * @param startNode The {@link ASTNode} to start evaluation from.
   * @return The evaluation result value.
   * @throws EvaluationException If there were problems while evaluating the expression.
   */
  public EvaluationValue evaluateSubtree(ASTNode startNode) throws EvaluationException {
    Token token = startNode.getToken();
    switch (token.getType()) {
      case NUMBER_LITERAL:
        return EvaluationValue.numberOfString(token.getValue(), configuration.getMathContext());
      case STRING_LITERAL:
        return new EvaluationValue(token.getValue());
      case VARIABLE_OR_CONSTANT:
        return configuration.getDataAccessor().getData(token.getValue());
      case PREFIX_OPERATOR:
      case POSTFIX_OPERATOR:
        return token
            .getOperatorDefinition()
            .evaluate(this, token, evaluateSubtree(startNode.getParameters().get(0)));
      case INFIX_OPERATOR:
        return token
            .getOperatorDefinition()
            .evaluate(
                this,
                token,
                evaluateSubtree(startNode.getParameters().get(0)),
                evaluateSubtree(startNode.getParameters().get(1)));
      case ARRAY_INDEX:
        return evaluateArrayIndex(startNode);
      case STRUCTURE_SEPARATOR:
        return evaluateStructureSeparator(startNode);
      case FUNCTION:
        List<EvaluationValue> parameterResults = new ArrayList<>();
        for (int i = 0; i < startNode.getParameters().size(); i++) {
          if (token.getFunctionDefinition().isParameterLazy(i)) {
            parameterResults.add(new EvaluationValue(startNode.getParameters().get(i)));
          } else {
            parameterResults.add(evaluateSubtree(startNode.getParameters().get(i)));
          }
        }
        return token
            .getFunctionDefinition()
            .evaluate(this, token, parameterResults.toArray(new EvaluationValue[0]));
      default:
        throw new EvaluationException(token, "Unexpected evaluation token: " + token);
    }
  }

  private EvaluationValue evaluateArrayIndex(ASTNode startNode) throws EvaluationException {
    EvaluationValue array = evaluateSubtree(startNode.getParameters().get(0));
    EvaluationValue index = evaluateSubtree(startNode.getParameters().get(1));

    if (array.isArrayValue() && index.isNumberValue()) {
      return array.getArrayValue().get(index.getNumberValue().intValue());
    } else {
      throw EvaluationException.ofUnsupportedDataTypeInOperation(startNode.getToken());
    }
  }

  private EvaluationValue evaluateStructureSeparator(ASTNode startNode) throws EvaluationException {
    EvaluationValue structure = evaluateSubtree(startNode.getParameters().get(0));
    String name = startNode.getParameters().get(1).getToken().getValue();

    if (structure.isStructureValue()) {
      return structure.getStructureValue().get(name);
    } else {
      throw EvaluationException.ofUnsupportedDataTypeInOperation(startNode.getToken());
    }
  }

  /**
   * Returns the root ode of the parsed abstract syntax tree.
   *
   * @return The abstract syntax tree root node.
   * @throws ParseException If there were problems while parsing the expression.
   */
  public ASTNode getAbstractSyntaxTree() throws ParseException {
    if (abstractSyntaxTree == null) {
      Tokenizer tokenizer = new Tokenizer(expressionString, configuration);
      ShuntingYardConverter converter = new ShuntingYardConverter(tokenizer.parse(), configuration);
      abstractSyntaxTree = converter.toAbstractSyntaxTree();
    }

    return abstractSyntaxTree;
  }

  /**
   * Validates the expression by parsing it and throwing an exception, if the parser fails.
   *
   * @throws ParseException If there were problems while parsing the expression.
   */
  public void validate() throws ParseException {
    getAbstractSyntaxTree();
  }

  /**
   * Adds a variable value to the expression data storage. If a value with the same name already
   * exists, it is overridden. The data type will be determined by examining the passed value
   * object. An exception is thrown, if he found data type is not supported.
   *
   * @param variable The variable name.
   * @param value The variable value.
   * @return The Expression instance, to allow chaining of methods.
   */
  public Expression with(String variable, Object value) {
    configuration.getDataAccessor().setData(variable, new EvaluationValue(value));
    return this;
  }

  /**
   * Adds a variable value to the expression data storage. If a value with the same name already
   * exists, it is overridden. The data type will be determined by examining the passed value
   * object. An exception is thrown, if he found data type is not supported.
   *
   * @param variable The variable name.
   * @param value The variable value.
   * @return The Expression instance, to allow chaining of methods.
   */
  public Expression and(String variable, Object value) {
    return with(variable, value);
  }
}
