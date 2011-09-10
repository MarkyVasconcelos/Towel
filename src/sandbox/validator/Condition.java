package sandbox.validator;

public abstract class Condition {
	private Object param;
	private String syntax;

	public Condition(Object param, String syntax) {
		this.param = param;
		this.syntax = syntax;
	}

	public Object getParam() {
		return param;
	}

	public String getSyntax() {
		return syntax;
	}

	public abstract boolean isValid(Object value);
	
	
}
