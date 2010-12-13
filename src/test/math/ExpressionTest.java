package test.math;

import com.towel.math.Expression;

public class ExpressionTest {
	private static void testExpressions(){
		Expression exp = new Expression("10+10*((10+10)+cos(10)-sin(x)*tan(8))");
		exp.setVariable("x", 8);
		System.out.println(exp.resolve());
	}
	
	public static void main(String[] args) {
		testExpressions();
	}
}
