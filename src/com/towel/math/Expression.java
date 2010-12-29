package com.towel.math;

import java.util.HashMap;
import java.util.Map;

import com.towel.math.exp.Node;
import com.towel.math.exp.Operator;
import com.towel.math.exp.Operator.Operands;

/**
 * 
 * Represents and resolves a mathematical formula. This class accepts
 * expressions like '1+1' and '1*2'. It's also possible to use parenthesis, like
 * '1+(8*2)' and '1*(5+5)'.
 * <p>
 * The formula can have spaces; this class cleans it before start parsing.
 * <p>
 * With this new implementation it's possible to use functions, defined by
 * {@link Operator} enum.
 * <p>
 * To use, just put the name and the value inside parenthesis like 'cos(1)'
 * 'sin(1)', etc..
 * <p>
 * With this, expressions can be complex, like '1-cos(8)+6*(tan(2)*log(1))'.
 * <p>
 * There's a especial operator 'random'. To use it: 'rnd(42)', this results in a
 * value between 0 and 41.99.
 * 
 * @author Marcos A. Vasconcelos Junior
 * @see Operator
 * @see Node
 */
public class Expression {
	private String expression = null;
	private Map<String, Double> variables = new HashMap<String, Double>();

	/**
	 * Creates an empty Expression. You need to use
	 * {@link #setExpression(String)} to assign a math expression string to.
	 */
	public Expression() {
		// do nothing
	}

	/**
	 * Creates an Expression and assigns the math expression string.
	 * 
	 * @param s
	 *            the expression string
	 */
	public Expression(String s) {
		setExpression(s);
	}

	/**
	 * Adds a variable and its value in the Expression.
	 * <p>
	 * Something like this can be done:
	 * 
	 * <pre>
	 * Expression e = new Expression(&quot;(x+4)*x&quot;);
	 * e.setVariable(&quot;x&quot, 7);
	 * </pre>
	 * 
	 * @param v
	 *            the variable name
	 * @param val
	 *            the variable value
	 */
	public void setVariable(String v, double val) {
		variables.put(v, new Double(val));
	}

	/**
	 * Sets the expression.
	 * 
	 * @param s
	 *            the expression string
	 */
	public void setExpression(String s) {
		expression = s;
	}

	/**
	 * Resolve and returns the numerical value of this expression.
	 * 
	 * @return the expression value
	 */
	public Double resolve() {
		if (expression == null)
			return null;

		try {
			return evaluate(new Node(this));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Double evaluate(Node n) {
		if (n.hasOperator() && n.hasChild()) {
			if (n.getOperator().getType() == Operands.SINGLE)
				n.setValue(n.getOperator().resolve(evaluate(n.getLeft()), null));
			else if (n.getOperator().getType() == Operands.DOUBLE)
				n.setValue(n.getOperator().resolve(evaluate(n.getLeft()),
						evaluate(n.getRight())));
		}
		return n.getValue();
	}

	/***
	 * Gets the variable's value.
	 * 
	 * @param s
	 *            the variable's name
	 * @return the variable's value
	 */
	public Double getVariable(String s) {
		return variables.get(s);
	}

	/**
	 * Converts a string to a double or, if it's not possible, returns the value
	 * of the variable with the given name.
	 * 
	 * @param s
	 *            the string value or the variable name
	 * @return the double value
	 */
	public Double getDouble(String s) {
		if (s == null)
			return null;
		try {
			return new Double(Double.parseDouble(s));
		} catch (Exception e) {
			return getVariable(s);
		}
	}

	/**
	 * @return a string representation of this expression
	 */
	public String getExpression() {
		return expression;
	}
}
