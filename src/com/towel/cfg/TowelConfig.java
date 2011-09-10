package com.towel.cfg;

import java.util.Locale;

public class TowelConfig {
	private Locale locale;

	public static TowelConfig instance;

	public static TowelConfig getInstance() {
		if (instance == null)
			instance = new TowelConfig();
		return instance;
	}

	public TowelConfig() {
		locale = Locale.getDefault();
	}

	public Locale getDefaultLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
