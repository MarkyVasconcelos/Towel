package test.math;

import com.towel.math.Expression;

/**
 * Tests with expresions.
 * 
 * @author Marcos A. Vasconcelos Junior
 * @author Marco Biscaro
 * @see Expression
 */
public class ExpressionTest {
	private static void testExpressions() {
		Expression exp = new Expression("10+10*((10+10)+cos(10)-sin(x)*tan(8))");
		exp.setVariable("x", 8);
		System.out.println(exp.resolve()); // 268.88279
		exp = new Expression("2&6|5");
		System.out.println(exp.resolve()); // 7
		exp = new Expression("deg(3.1415)");
		System.out.println(exp.resolve()); // 179.99
		exp = new Expression("deg(rad(360)/2)");
		System.out.println(exp.resolve()); // 180
		exp = new Expression("deg(rad(100))");
		System.out.println(exp.resolve()); // 100
		exp = new Expression("rad(deg(10))");
		System.out.println(exp.resolve()); // 10
		exp = new Expression("neg(rnd(10))^3");
		System.out.println(exp.resolve()); // any number between -997 and 0
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		testExpressions();
	}
}
