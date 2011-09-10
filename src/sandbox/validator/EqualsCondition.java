package sandbox.validator;

public class EqualsCondition extends Condition {

	public EqualsCondition(Object param) {
		super(param, "=");
	}

	@Override
	public boolean isValid(Object value) {
		return getParam().equals(value);
	}

}
