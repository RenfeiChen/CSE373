package calculator.ast;
import datastructures.interfaces.IList;
import datastructures.concrete.DoubleLinkedList;

import calculator.interpreter.Environment;
import calculator.errors.EvaluationError;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NotYetImplementedException;

/**
 * All of the static methods in this class are given the exact same parameters for
 * consistency. You can often ignore some of these parameters when implementing your
 * methods.
 *
 * Some of these methods should be recursive. You may want to consider using public-private
 * pairs in some cases.
 */
public class ExpressionManipulators {
    /**
     * Takes the given AstNode node and attempts to convert it into a double.
     *
     * Returns a number AstNode containing the computed double.
     *
     * @throws EvaluationError  if any of the expressions contains an undefined variable.
     * @throws EvaluationError  if any of the expressions uses an unknown operation.
     */
    public static AstNode toDouble(Environment env, AstNode node) {
        // To help you get started, we've implemented this method for you.
        return new AstNode(toDoubleHelper(env.getVariables(), node));
    }

    private static double toDoubleHelper(IDictionary<String, AstNode> variables, AstNode node) {
        // There are three types of nodes, so we have three cases.
        if (node.isNumber()) {
            return node.getNumericValue();
        } else if (node.isVariable()) {
            if (!variables.containsKey(node.getName())) {
                // If the expression contains an undefined variable, we give up.
                throw new EvaluationError("Undefined variable: " + node.getName());
            }
            return toDoubleHelper(variables, variables.get(node.getName()));
        } else {
            String name = node.getName();
            
            if (name.equals("toDouble")) {
                return toDoubleHelper(variables, node.getChildren().get(0));
            }


            if (name.equals("+")) {
                double number1 = toDoubleHelper(variables, node.getChildren().get(0));
                double number2 = toDoubleHelper(variables, node.getChildren().get(1));
                return number1 + number2;
            } else if (name.equals("-"))     {
                double number1 = toDoubleHelper(variables, node.getChildren().get(0));
                double number2 = toDoubleHelper(variables, node.getChildren().get(1));
                return number1 - number2;
            } else if (name.equals("*")) {
                double number1 = toDoubleHelper(variables, node.getChildren().get(0));
                double number2 = toDoubleHelper(variables, node.getChildren().get(1));
                return number1 * number2;
            } else if (name.equals("/")) {
                double number1 = toDoubleHelper(variables, node.getChildren().get(0));
                double number2 = toDoubleHelper(variables, node.getChildren().get(1));
                return number1 / number2;
            } else if (name.equals("^")) {
                double number1 = toDoubleHelper(variables, node.getChildren().get(0));
                double number2 = toDoubleHelper(variables, node.getChildren().get(1));
                return Math.pow(number1, number2);
            } else if (name.equals("negate")) {
                return -toDoubleHelper(variables, node.getChildren().get(0));
            } else if (name.equals("sin")) {
                return Math.sin(toDoubleHelper(variables, node.getChildren().get(0)));
            } else if (name.equals("cos")) {
                return Math.cos(toDoubleHelper(variables, node.getChildren().get(0)));
            } else {
                throw new EvaluationError("Unknown operation: " + name);
            }
        }
    }

    public static AstNode simplify(Environment env, AstNode node) {
        if (node.isNumber()) {
            return node;
        } else if (node.isVariable()) {
            if (!env.getVariables().containsKey(node.getName())) {
                return node;
            }
            return simplify(env, env.getVariables().get(node.getName()));
        } else {
            String name = node.getName();
            if (name.equals("simplify")) {
                return simplify(env, node.getChildren().get(0));
            }
            if (name.equals("+") || name.equals("-") || name.equals("*") || name.equals("/") || name.equals("^")) {
                AstNode leftnode = node.getChildren().get(0);
                AstNode rightnode = node.getChildren().get(1);
                leftnode = simplify(env, leftnode);
                rightnode = simplify(env, rightnode);
                if (leftnode.isNumber() && rightnode.isNumber() && !name.equals("/")) {
                    return simplifyhelper(name, leftnode, rightnode);
                }
                IList<AstNode> params = new DoubleLinkedList<>();
                params.add(leftnode);
                params.add(rightnode);
                return new AstNode(name, params);
            }
            else if (name.equals("negate") || name.equals("sin") || name.equals("cos")) {
                AstNode leftnode = node.getChildren().get(0);
                leftnode = simplify(env, leftnode);
                if (leftnode.isNumber() && name.equals("negate")) {
                    return new AstNode(-leftnode.getNumericValue());
                }
                IList<AstNode> params = new DoubleLinkedList<>();
                params.add(leftnode);
                return new AstNode(name, params);
            } else {
                throw new EvaluationError("Unknown operation: " + name);
            }
        }
    }
    
    private static AstNode simplifyhelper(String name, AstNode leftnode, AstNode rightnode) {
        if (name.equals("+")) {
            return new AstNode(leftnode.getNumericValue() + rightnode.getNumericValue());
        }
        if (name.equals("-")) {
            return new AstNode(leftnode.getNumericValue() - rightnode.getNumericValue());
        }
        if (name.equals("*")) {
            return new AstNode(leftnode.getNumericValue() * rightnode.getNumericValue());
        }
        if (name.equals("^")) {
            return new AstNode(Math.pow(leftnode.getNumericValue(), rightnode.getNumericValue()));
        }
        return null;
    }

    /**
     * Expected signature of plot:
     *
     * >>> plot(exprToPlot, var, varMin, varMax, step)
     *
     * Example 1:
     *
     * >>> plot(3 * x, x, 2, 5, 0.5)
     *
     * This command will plot the equation "3 * x", varying "x" from 2 to 5 in 0.5
     * increments. In this case, this means you'll be plotting the following points:
     *
     * [(2, 6), (2.5, 7.5), (3, 9), (3.5, 10.5), (4, 12), (4.5, 13.5), (5, 15)]
     *
     * ---
     *
     * Another example: now, we're plotting the quadratic equation "a^2 + 4a + 4"
     * from -10 to 10 in 0.01 increments. In this case, "a" is our "x" variable.
     *
     * >>> c := 4
     * 4
     * >>> step := 0.01
     * 0.01
     * >>> plot(a^2 + c*a + a, a, -10, 10, step)
     *
     * ---
     *
     * @throws EvaluationError  if any of the expressions contains an undefined variable.
     * @throws EvaluationError  if varMin > varMax
     * @throws EvaluationError  if 'var' was already defined
     * @throws EvaluationError  if 'step' is zero or negative
     */
    public static AstNode plot(Environment env, AstNode node) {
        
        double varMin = toDouble(env, node.getChildren().get(2)).getNumericValue();
        double varMax = toDouble(env, node.getChildren().get(3)).getNumericValue();
        double step = toDouble(env, node.getChildren().get(4)).getNumericValue();
        
        String variablename = node.getChildren().get(1).getName();
        
        AstNode beginnode = node.getChildren().get(0);
        
        if (varMin > varMax || env.getVariables().containsKey(variablename) || step <= 0) {
            throw new EvaluationError("");
        }
        
        IList<Double> xValues = new DoubleLinkedList<>();
        IList<Double> yValues = new DoubleLinkedList<>();
        
        for (double i = varMin; i <= varMax; i += step) {
            xValues.add(i);
            env.getVariables().put(variablename, new AstNode(i));
            yValues.add(toDouble(env, beginnode).getNumericValue());   
        }
        
        env.getVariables().remove(variablename);
        
        env.getImageDrawer().drawScatterPlot("Test", variablename, "y", xValues, yValues);
        
        return new AstNode(1);
    }
}
