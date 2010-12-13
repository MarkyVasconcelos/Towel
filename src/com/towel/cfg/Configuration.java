package com.towel.cfg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {
	private Properties props;
	private File file;

	public Configuration(File f) throws IOException {
		if (!f.exists())
			f.createNewFile();
		props = new Properties();
		file = f;
		props.load(new FileInputStream(file));
	}

	public StringConfiguration getCfg(String key) {
		return new StringConfiguration(get(key));
	}

	public String get(String key) {
		return (String) props.get(key);
	}

	public void put(String key, String value) {
		props.put(key, value);
	}

	public void put(String key, StringConfiguration cfg) {
		put(key, cfg.toString());
	}

	public void write() throws IOException {
		props.store(new FileOutputStream(file), "");
	}
}
