package calculator.ast;

import calculator.interpreter.Environment;

import calculator.errors.EvaluationError;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;

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
    	if (node.getName().equals("toDouble")) {
        	return new AstNode(toDoubleHelper(env.getVariables(), node.getChildren().get(0)));
        } else {
        	return new AstNode(toDoubleHelper(env.getVariables(), node));
        }
    }

    private static double toDoubleHelper(IDictionary<String, AstNode> variables, AstNode node) {
        // There are three types of nodes, so we have three cases.
        if (node.isNumber()) {
        	return node.getNumericValue();
        } else if (node.isVariable()) {
            if (!variables.containsKey(node.getName())) {
                throw new EvaluationError("Undefined variable: " + node.getName());
            }
            return toDoubleHelper(variables, variables.get(node.getName()));
        } else {
            String name = node.getName();

            IList<AstNode> operands = node.getChildren();
            double operand1 = toDoubleHelper(variables, operands.get(0));
            double operand2 = 0;
            if (operands.size() == 2) {
            	operand2 = toDoubleHelper(variables, operands.get(1));
            }
            
            if (name.equals("+")) {
            	return operand1 + operand2;
            } else if (name.equals("-")) {
            	return operand1 - operand2;
            } else if (name.equals("*")) {
            	return operand1 * operand2;
            } else if (name.equals("/")) {
            	return operand1 / operand2;
            } else if (name.equals("^")) {
            	return Math.pow(operand1, operand2);
            } else if (name.equals("negate")) {
            	return operand1 * -1;
            } else if (name.equals("sin")) {
            	return Math.sin(operand1);
            } else if (name.equals("cos")) {
            	return Math.cos(operand1);
            } else {
                throw new EvaluationError("Unknown operation: " + name);
            }
        }
    }

    public static AstNode simplify(Environment env, AstNode node) {
        // Try writing this one on your own!
        // Hint 1: Your code will likely be structured roughly similarly
        //         to your "toDouble" method
        // Hint 2: When you're implementing constant folding, you may want
        //         to call your "toDouble" method in some way
    	
    	if (node.getName().equals("simplify")) {
        	return simplifyHelper(env, node.getChildren().get(0));
        } else {
        	return simplifyHelper(env, node);
        }
    }
    
    private static AstNode simplifyHelper(Environment env, AstNode node) {
        IDictionary<String, AstNode> variables = env.getVariables();
        if (node.isNumber()) {
        	return node;
        } else if (node.isVariable()) {
            if (!variables.containsKey(node.getName())) {
                return node;
            }
            return simplifyHelper(env, variables.get(node.getName()));
        } else {
        	return handleOperation(env, node);
        }
    }
    
    private static AstNode handleOperation(Environment env, AstNode node) {
    	IList<AstNode> operands = node.getChildren();
    	String name = node.getName();
        IList<AstNode> newChildren = new DoubleLinkedList<>();
        AstNode operand1 = simplifyHelper(env, operands.get(0));
        newChildren.add(operand1);
        AstNode operand2 = null;
        if (operands.size() == 2) {
            operand2 = simplifyHelper(env, operands.get(1));
            newChildren.add(operand2);
        }
        AstNode newNode = new AstNode(node.getName(), newChildren);
        if (name.equals("+") || name.equals("-") || name.equals("*") || name.equals("^")) {
        	if (operand1.isNumber() && operand2.isNumber()) {
        		return toDouble(env, newNode);
        	} else {
        		return newNode;
        	}
        } else if (name.equals("/") || name.equals("sin") || name.equals("cos")) {
        	return newNode;
        } else if (name.equals("negate")) {
        	if (operand1.isNumber()) {
        		return toDouble(env, newNode);
        	} else {
        		return newNode;
        	}
        } else {
        	throw new EvaluationError("Unknown operation: " + name);
        }
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
     * @throws EvaluationError  if varMin > varMax                       check
     * @throws EvaluationError  if 'var' was already defined
     * @throws EvaluationError  if 'step' is zero or negative            check
     */
    public static AstNode plot(Environment env, AstNode node) {
    	// Note: every single function we add MUST return an
        // AST node that your "simplify" function is capable of handling.
        // However, your "simplify" function doesn't really know what to do
        // with "plot" functions (and what is the "plot" function supposed to
        // evaluate to anyways?) so we'll settle for just returning an
        // arbitrary number.
        //
        // When working on this method, you should uncomment the following line:
       	plotHelper(env, node);
        return new AstNode(0);
    }
    
    private static void plotHelper(Environment env, AstNode node) {
    	AstNode exprToPlot = node.getChildren().get(0);
    	String var = node.getChildren().get(1).getName();
    	AstNode varMin = node.getChildren().get(2);
    	AstNode varMax = node.getChildren().get(3);
    	AstNode step = node.getChildren().get(4);
    	IDictionary<String, AstNode> variables = env.getVariables();

    	// throws EvaluationError  if "var" was already defined
    	try { 
    		toDoubleHelper(variables, variables.get(var));
    		throw new EvaluationError("EvaluationError");
    	} catch (Exception e) {
    		// do nothing
    	}
    	
    	double varMinDouble;
    	double varMaxDouble; 
    	double stepDouble;    	
    	
    	// throws EvaluationError  if "varMin" was not defined
    	varMinDouble = checkValidity(variables, varMin);    	
    	// throws EvaluationError  if "varMax" was not defined
    	varMaxDouble = checkValidity(variables, varMax);
    	// throws EvaluationError  if "step" was not defined
    	stepDouble = checkValidity(variables, step);
    	if (!(stepDouble > 0) || varMinDouble > varMaxDouble) {
    		throw new EvaluationError("EvaluationError");
    	}
    	
    	IList<Double> xValues = new DoubleLinkedList<Double>();
    	IList<Double> yValues = new DoubleLinkedList<Double>();
     	double curr = varMinDouble;
    	// filling up the elements in list xValues and yValues
    	while (!(curr > varMaxDouble)) {
    		xValues.add(new Double(curr));
    		variables.put(var, new AstNode(curr));
    		try {
    			yValues.add(new Double(toDoubleHelper(variables, exprToPlot)));
    		} catch (Exception e) {
    			throw new EvaluationError("EvaluationError");
    		}	
    		curr += stepDouble;
    	}
    	variables.remove(var);
    	env.getImageDrawer().drawScatterPlot(exprToPlot.toString() + " vs " + var.toString(),
    						 var.toString(), exprToPlot.toString(), xValues, yValues);
    }
    
    private static double checkValidity(IDictionary<String, AstNode> variables, AstNode node) {
    	try {
    		if (node.isVariable()) {
    			return toDoubleHelper(variables, variables.get(node.getName()));
    		} else if (node.isOperation()) {
    			return toDoubleHelper(variables, node);
    		}else {
    			return node.getNumericValue();
    		}
    	} catch (Exception e) {
    		throw new EvaluationError("EvaluationError");
    	}
    }
}
