package com.towel.math;

import java.util.HashMap;
import java.util.Map;

import com.towel.math.exp.Node;
import com.towel.math.exp.Operator.Operands;



/**
 * 
 * Java class to represent and resolve a mathematical formula this class accept
 * expressions like '1+1', '1*2' There's possible to use brackets too like
 * '1+(8*2)', '1*(5+5)'
 * 
 * The formula can have spaces, this class clean it before start parsing.
 * 
 * With this new implementation there's possible to use functions, these are:
 * cos, sin, tan, acos, asin, atan, sqrt, sqr, log, exp, floor, ceil, abs and
 * neg As theses of the Math class.
 * 
 * To use, just put the name and the value inside brackets like 'cos(1)'
 * 'sin(1)', etc..
 * 
 * With this, the expressions can be something like '1-cos(8)+6*(tan(2)*log(1))'
 * 
 * Theres a especial operator 'random' to use: 'rnd(42)', this results in a
 * value between 0 and 41.99 Eg. '1+rnd(3)'
 * 
 * @author Marcos.Vasconcelos
 */
public class Expression {
	private String expression = null;
	private Map<String, Double> variables = new HashMap<String, Double>();

	/**
	 * Creates an empty Expression. You need to use setExpression(String s) to
	 * assign a math expression string to it.
	 */
	public Expression() {
	}

	/**
	 * Creates an Expression and assign the math expression string.
	 */
	public Expression(String s) {
		setExpression(s);
	}

	/**
	 * Adds a variable and its value in the Expression
	 */
	public void setVariable(String v, double val) {
		variables.put(v, new Double(val));
	}

	/**
	 * Sets the expression
	 */
	public void setExpression(String s) {
		expression = s;
	}

	/**
	 * Resolve and returns the value of the expression
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
	 * Gets the variable's value
	 */
	public Double getVariable(String s) {
		return variables.get(s);
	}

	public Double getDouble(String s) {
		if (s == null)
			return null;
		try {
			return new Double(Double.parseDouble(s));
		} catch (Exception e) {
			return getVariable(s);
		}
	}

	public String getExpression() {
		return expression;
	}
}
