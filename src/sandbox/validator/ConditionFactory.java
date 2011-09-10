package sandbox.validator;

import java.util.HashMap;
import java.util.Map;

public class ConditionFactory {
	private Map<String, Class<? extends Condition>> map;

	public ConditionFactory() {
		map = new HashMap<String, Class<? extends Condition>>();

		register(new EqualsCondition(""));
		register(new NotEqualsCondition(""));
	}

	public void register(Condition cond) {
		map.put(cond.getSyntax(), cond.getClass());
	}

	public Condition create(String cond, Object value) {
		Condition condition = null;
		try {
			condition = map.get(cond).getConstructor(Object.class).newInstance(
					value);
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
		return condition;
	}
}
