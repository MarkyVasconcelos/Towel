package com.towel.file;

import java.io.File;
import java.io.FilenameFilter;

import com.towel.collections.CollectionsUtil;



public class Directory {
	private File current;

	public Directory(File file) {
		if (!file.isDirectory())
			throw new RuntimeException("File is not a directory: "
					+ file.getAbsolutePath());
		current = file;
	}

	public Directory(String dirName) {
		this(new File(dirName));
	}

	public Directory bd() {
		if (current.getParentFile() != null)
			current = current.getParentFile();
		return this;
	}

	public Directory root() {
		while (current.getParentFile() != null)
			current = current.getParentFile();
		return this;
	}

	public Directory cd(String path) {
		current = new File(current.getAbsolutePath().concat("/").concat(path));
		return this;
	}

	public Directory[] getDirs() {
		File[] files = current.listFiles();
		int i = files.length;
		Directory[] dirs = new Directory[i];
		int currentPos = 0;
		for (int j = 0; j < i; j++)
			if (files[j].isDirectory())
				dirs[currentPos++] = new Directory(files[j]);

		return (Directory[]) CollectionsUtil.trim(dirs);
	}

	public File[] getFiles() {
		File[] files = current.listFiles();
		int i = files.length;
		File[] returnValue = new File[i];
		int currentPos = 0;
		for (int j = 0; j < i; j++)
			if (!files[j].isDirectory())
				returnValue[currentPos++] = files[j];

		return (File[]) CollectionsUtil.trim(returnValue);
	}

	public File[] allFiles() {
		return current.listFiles();
	}

	public File[] getFilesByExt(String ext) {
		return current.listFiles(new Filter(ext));
	}
	
	public String getDirName(){
		return current.getAbsolutePath();
	}

	private class Filter implements FilenameFilter {
		private String ext;

		public Filter(String ext) {
			this.ext = ext;
		}

		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(ext);
		}
	}
}
