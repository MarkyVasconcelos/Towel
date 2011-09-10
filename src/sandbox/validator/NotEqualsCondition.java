package sandbox.validator;

public class NotEqualsCondition extends Condition {

	public NotEqualsCondition(Object param) {
		super(param, "!=");
	}

	@Override
	public boolean isValid(Object value) {
		return !getParam().equals(value);
	}

}
