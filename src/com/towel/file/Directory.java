package com.towel.file;

import java.io.File;
import java.io.FilenameFilter;

import com.towel.collections.CollectionsUtil;

/**
 * Mutable object that represents a directory in local filesystem.
 * 
 * @author Marcos A. Vasconcelos Junior
 */
public class Directory {
	private File current;

	/**
	 * Creates a new directory using the given file.
	 * 
	 * @param file
	 *            the directory in filesystem
	 */
	public Directory(File file) {
		if (!file.isDirectory())
			throw new RuntimeException("File is not a directory: "
					+ file.getAbsolutePath());
		current = file;
	}

	/**
	 * Creates a new directory using the given file name.
	 * 
	 * @param dirName
	 *            the path to the directory
	 */
	public Directory(String dirName) {
		this(new File(dirName));
	}

	/**
	 * Change the current directory to the parent (abstract file
	 * &quot;..&quot;).
	 * 
	 * @return the parent directory, if exists, or this object otherwise
	 */
	public Directory bd() {
		if (current.getParentFile() != null)
			current = current.getParentFile();
		return this;
	}

	/**
	 * Returns the root directory in this filesystem (on Unix systems, it is the
	 * &quot;/&quot; directory; on Windows, it can be units letters - like
	 * &quot;C:&quot;, &quot;D:&quot;, etc.).
	 * 
	 * @return the root directory
	 */
	public Directory root() {
		while (current.getParentFile() != null)
			current = current.getParentFile();
		return this;
	}

	/**
	 * Change working directory, relative to this object.
	 * 
	 * @param path
	 *            the new path (relative to the current directory)
	 * @return the new directory
	 */
	public Directory cd(String path) {
		current = new File(current.getAbsolutePath().concat("/").concat(path));
		return this;
	}

	/**
	 * Gets the directories in the current one (only include directories, not
	 * files).
	 * 
	 * @return the array of directories
	 * @see #getFiles()
	 */
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

	/**
	 * Gets the files in the current directory (only include files, not
	 * directories).
	 * 
	 * @return the array of files
	 * @see #getDirs()
	 */
	public File[] getFiles() {
		File[] files = current.listFiles();
		int i = files.length;
		File[] returnValue = new File[i];
		int currentPos = 0;
		for (int j = 0; j < i; j++)
			if (files[j].isFile())
				returnValue[currentPos++] = files[j];

		return (File[]) CollectionsUtil.trim(returnValue);
	}

	/**
	 * Gets both files and directories in the current directory.
	 * 
	 * @return array of files and directories
	 */
	public File[] allFiles() {
		return current.listFiles();
	}

	/**
	 * Gets the files that have the given extension. The extension is
	 * case-sensitive.
	 * 
	 * @param ext
	 *            the file extension
	 * @return array of files that have the extension
	 */
	public File[] getFilesByExt(String ext) {
		return current.listFiles(new Filter(ext));
	}

	/**
	 * Gets the absolute path of this directory.
	 * 
	 * @return the directory name
	 */
	public String getDirName() {
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
