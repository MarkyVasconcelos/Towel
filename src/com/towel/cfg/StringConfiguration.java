package com.towel.cfg;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class StringConfiguration {
	private Map<String, String> attrs;
	private String string;

	public StringConfiguration() {
		this("");
	}

	public StringConfiguration(String s) {
		attrs = new HashMap<String, String>();
		string = s;
		map();
	}

	private void map() {
		String split[] = string.split("[\\[\\]]");
		for (String s : split)
			if (!s.isEmpty()) {
				int index = s.indexOf(":");
				attrs.put(s.substring(0, index), s.substring(index + 1));
			}
	}

	public String getAttribute(String name) {
		String result = attrs.get(name);
		return result == null ? "" : result;
	}

	public StringConfiguration setAttribute(String name, String value) {
		attrs.put(name, value);
		return this;
	}

	public boolean hasAttribute(String string) {
		return attrs.containsKey(string);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Entry<String, String> ent : attrs.entrySet())
			builder.append("[").append(ent.getKey()).append(":").append(
					ent.getValue()).append("]");
		return builder.toString();
	}
}
