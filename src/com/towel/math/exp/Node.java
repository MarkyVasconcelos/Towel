package com.towel.math.exp;

import com.towel.cfg.StringUtil;
import com.towel.math.Expression;
import com.towel.math.exp.Operator.Operands;

/**
 * Represents one or more operation nodes (a group of one or two operands and an
 * operator).
 * 
 * @author Marcos A. Vasconcelos Junior
 */
public class Node {
	private Operator operator = null;
	private Node leftNode = null;
	private Node rightNode = null;
	private Double value = null;

	/**
	 * Create a Node using the given expression.
	 * 
	 * @param s
	 *            the expression
	 */
	public Node(Expression s) {
		this(s.getExpression(), s);
	}

	private Node(String s, Expression exp) {
		s = StringUtil.removeCharacters(s, ' ');
		s = removeBrackets(s);
		s = addZero(s);
		if (!checkBrackets(s))
			throw new IllegalArgumentException("Wrong number of brackets in '"
					+ s + "'");

		value = exp.getDouble(s);
		int sLength = s.length();
		int inBrackets = 0;
		int startOperator = 0;

		for (int i = 0; i < sLength; i++) {
			if (s.charAt(i) == '(')
				inBrackets++;
			else if (s.charAt(i) == ')')
				inBrackets--;
			else {
				if (inBrackets == 0) {
					Operator o = getOperator(s, i);
					if (o != null)
						if (operator == null
								|| operator.getPriority() >= o.getPriority()) {
							operator = o;
							startOperator = i;
						}
				}
			}
		}

		if (operator != null) {
			// one operand, should always be at the beginning
			if (startOperator == 0 && operator.getType() == Operands.SINGLE) {
				if (checkBrackets(s.substring(operator.getOperator().length()))) {
					leftNode = new Node(s.substring(operator.getOperator()
							.length()), exp);
					return;
				}
				throw new IllegalArgumentException(
						"Error parsing. Missing brackets in '" + s + "'");
			} else if (startOperator > 0
					&& operator.getType() == Operands.DOUBLE) {
				leftNode = new Node(s.substring(0, startOperator), exp);
				rightNode = new Node(s.substring(startOperator
						+ operator.getOperator().length()), exp);
			}
		}
	}

	/**
	 * Returns the operator starting at given index.
	 * 
	 * @param s
	 *            the expression part
	 * @param start
	 *            the start index
	 * @return the operator
	 */
	public Operator getOperator(String s, int start) {
		Operator[] operators = Operator.values();
		String next = getNextWord(s.substring(start));
		for (int i = 0; i < operators.length; i++)
			if (next.startsWith(operators[i].getOperator()))
				return operators[i];

		return null;
	}

	/**
	 * @param s
	 *            the part of expression
	 * @return the first word of this expression
	 */
	public String getNextWord(String s) {
		int sLength = s.length();
		for (int i = 1; i < sLength; i++) {
			char c = s.charAt(i);
			if ((c > 'z' || c < 'a') && (c > '9' || c < '0'))
				return s.substring(0, i);
		}
		return s;
	}

	/**
	 * Checks if there is any missing brackets
	 * 
	 * @param s
	 *            the operation to check
	 * @return true if the operation is valid
	 */
	public boolean checkBrackets(String s) {
		int brackets = 0;
		for (int i = 0; i < s.length(); i++)
			if (s.charAt(i) == '(' && brackets >= 0)
				brackets++;
			else if (s.charAt(i) == ')')
				brackets--;

		return brackets == 0;
	}

	/**
	 * Returns a string that doesn't start with '+' or '-'
	 * 
	 * @param s
	 *            the original String
	 * @return the node with zero at start
	 */
	public String addZero(String s) {
		if (s.startsWith("+") || s.startsWith("-"))
			return "0" + s;
		return s;
	}

	/**
	 * Trace the expression graph.
	 */
	public void trace() {
		System.out.println(value != null ? value : operator.getOperator());
		if (hasChild()) {
			if (hasLeft())
				getLeft().trace();
			if (hasRight())
				getRight().trace();
		}
	}

	/**
	 * @return <code>true</code> if there is one or more children,
	 *         <code>false</code> otherwise
	 */
	public boolean hasChild() {
		return leftNode != null || rightNode != null;
	}

	/**
	 * @return <code>true</code> if there the operator is set,
	 *         <code>false</code> otherwise
	 */
	public boolean hasOperator() {
		return operator != null;
	}

	/**
	 * @return <code>true</code> if there is one a left node, <code>false</code>
	 *         otherwise
	 */
	public boolean hasLeft() {
		return leftNode != null;
	}

	/**
	 * @return the left node
	 */
	public Node getLeft() {
		return leftNode;
	}

	/**
	 * @return <code>true</code> if there is one a right node,
	 *         <code>false</code> otherwise
	 */
	public boolean hasRight() {
		return rightNode != null;
	}

	/**
	 * @return the right node
	 */
	public Node getRight() {
		return rightNode;
	}

	/**
	 * @return the operator
	 */
	public Operator getOperator() {
		return operator;
	}

	/**
	 * @return the value of this node
	 */
	public Double getValue() {
		return value;
	}

	/**
	 * @param f
	 *            the new node's value
	 */
	public void setValue(Double f) {
		value = f;
	}

	private String removeBrackets(String s) {
		String res = s;
		if (s.length() > 2 && res.startsWith("(") && res.endsWith(")")
				&& checkBrackets(s.substring(1, s.length() - 1)))
			res = res.substring(1, res.length() - 1);

		// In case the String was something like '((1+1))'
		// only '1+1' is returned.
		if (res != s)
			return removeBrackets(res);
		return res;
	}
}