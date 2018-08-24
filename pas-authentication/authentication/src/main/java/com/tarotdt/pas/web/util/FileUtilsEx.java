package com.tarotdt.pas.web.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.LinkOption;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

public class FileUtilsEx {
	public static File build(File folder, String... chunks) {
		File f = folder;
		for (String c : chunks) {
			f = new File(f, c);
		}
		return f;
	}

	public static File build(String... chunks) {
		if (chunks.length == 0)
			throw new IllegalArgumentException("empty path");
		File f = new File(chunks[0]);
		for (int i = 1; i < chunks.length; i++) {
			f = new File(f, chunks[i]);
		}
		return f;
	}

	public static void mkdirs(File folder) throws IOException {
		if (folder.exists()) {
			if (folder.isDirectory()) {
				return;
			}
			throw new IOException("Can't create " + folder.getAbsolutePath() + ": is a file");
		}

		FileUtils.forceMkdir(folder);
	}

	public static void mkdirsParent(File file) throws IOException {
		mkdirs(file.getParentFile());
	}

	public static int countLines(File file) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		int lines = 0;
		BufferedReader br = new BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(file), "utf8"));
		Throwable ex = null;
		try {
			while (br.readLine() != null) {
				lines++;
			}
		} catch (Throwable e) {
			ex = e;
			throw e;
		} finally {
			if (br != null)
				if (ex != null)
					try {
						br.close();
					} catch (Throwable e) {
						ex.addSuppressed(e);
					}
				else
					br.close();
		}
		return lines;
	}

	public static void delete(File f) throws IOException {
		if (f.exists()) {
			boolean ret = f.delete();
			if (!ret) {
				throw new IOException("Failed to delete " + f);
			}
		}
	}

	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	public static String readFileToStringUTF8(File file) throws IOException {
		return FileUtils.readFileToString(file, "utf8");
	}

	public static void writeFileUTF8(File file, String content) throws IOException {
		File tmpFile = new File(file.getPath() + "." + content.hashCode());
		FileUtils.write(tmpFile, content, "utf8");
		if (file.exists()) {
			FileUtils.forceDelete(file);
		}
		FileUtils.moveFile(tmpFile, file);
	}

	public static void writeFileUTF8(File file, String content, boolean mkdirs) throws IOException {
		if (mkdirs) {
			mkdirsParent(file);
		}
		writeFileUTF8(file, content);
	}

	// public static List<File> recursiveListFiles(File root) throws IOException {
	// return recursiveListFiles(root, FileFilter.FILES);
	// }

	public static void removeDirectoryRecursive(File root) throws IOException {
		FileUtils.forceDelete(root);
	}

	// public static List<File> recursiveListFiles(File root, FileFilter
	// fileFilter) throws IOException {
	// if (!root.isDirectory()) {
	// throw new IOException("Root " + root + " is not a directory");
	// }
	// List<File> ret = new ArrayList();
	// listRec(root, ret, fileFilter);
	// return ret;
	// }

	public static enum FileFilter implements java.io.FileFilter {
		FILES {
			public boolean accept(File f) {
				return !f.isDirectory();
			}
		},
		DIRECTORIES {
			public boolean accept(File f) {
				return f.isDirectory();
			}
		},
		FILES_AND_DIRECTORIES {
			public boolean accept(File f) {
				return true;
			}
		};

		private FileFilter() {
		}
	}

	// private static void listRec(File f, List<File> ret, FileFilter fileFilter)
	// {
	// if (f.isDirectory()) {
	// for (File child : f.listFiles()) {
	// listRec(child, ret, fileFilter);
	// }
	// }
	// if (fileFilter.accept(f)) {
	// ret.add(f);
	// }
	// }

	public static String fileToString(File path) throws FileNotFoundException, IOException {
		try {
			return fileToString(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new Error("Never appends");
		}
	}

	public static String fileToString(File path, String encoding)
	    throws UnsupportedEncodingException, FileNotFoundException, IOException {
		BufferedReader br = StreamUtils.readFile(path, encoding);
		Throwable ex = null;
		try {
			return streamToString(br);
		} catch (Throwable e) {
			ex = e;
			throw e;
		} finally {
			if (br != null)
				if (ex != null)
					try {
						br.close();
					} catch (Throwable e) {
						ex.addSuppressed(e);
					}
				else
					br.close();
		}
	}

	public static String streamToString(BufferedReader in) throws IOException {
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = in.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}

		return sb.toString();
	}

	public static int highestFileNumber(File root, String prefix) {
		int max = -1;
		if (!root.isDirectory())
			return max;
		for (File f : root.listFiles()) {
			if (f.getName().startsWith(prefix)) {
				try {
					int cur = Integer.parseInt(f.getName().replace(prefix, ""));
					max = Math.max(cur, max);
				} catch (Exception localException) {
				}
			}
		}
		return max;
	}

	public static int nextFileNumber(File root, String prefix) {
		int backupVersion = -1;
		int lastSavedVersion = highestFileNumber(root, prefix);
		if (lastSavedVersion == -1)
			backupVersion = 1;
		else
			backupVersion = lastSavedVersion + 1;
		return backupVersion;
	}

	public static File newestFile(File folder, String prefix) {
		Validate.isTrue(folder.isDirectory(), "folder must be a directory: " + folder);

		Logger.getLogger("pas").info("Newest file in folder " + folder + " ...");
		long latest = -1L;
		File newest = null;
		for (File f : folder.listFiles()) {
			Logger.getLogger("pas").info("Newest file in folder " + f);
			if ((prefix == null) || (f.getName().startsWith(prefix))) {
				long cur = f.lastModified();
				Logger.getLogger("pas").info("  valid " + cur);
				if (cur > latest) {
					newest = f;
					latest = cur;
				}
			} else {
				Logger.getLogger("pas").info("invalid " + prefix);
			}
		}
		return newest;
	}

	public static File transmogrify(File folder, String filename) {
		Validate.isTrue(folder.isDirectory(), "folder must be a directory: " + folder);

		File f = new File(folder, filename);
		if (!f.exists()) {
			return f;
		}
		int suffix = 1;
		for (;;) {
			f = new File(folder, filename + "-" + suffix);
			if (!f.exists())
				return f;
			suffix++;
		}
	}

	public static boolean isWithin(File container, File content) throws IOException {
		String containerCanonical;
		String contentCanonical;
		if (container.exists()) {
			if (content.exists()) {
				containerCanonical = container.toPath().toRealPath(new LinkOption[] { LinkOption.NOFOLLOW_LINKS }).toString();
				contentCanonical = content.toPath().toRealPath(new LinkOption[] { LinkOption.NOFOLLOW_LINKS }).toString();
			} else {
				File last = content.getParentFile();
				while ((last != null) && (!last.exists()))
					last = last.getParentFile();
				if (last == null)
					return false;
				if (isWithin(container, last)) {
					return true;
				}
				return container.toPath().toRealPath(LinkOption.NOFOLLOW_LINKS)
				    .equals(last.toPath().toRealPath(LinkOption.NOFOLLOW_LINKS));
			}
		} else {
			if (content.exists()) {
				return false;
			}
			containerCanonical = container.getCanonicalPath();
			contentCanonical = content.getCanonicalPath();
		}
		return contentCanonical.startsWith(containerCanonical + File.separator);
	}

	public static File getWithin(File parent, String... children) {
		File file = parent;
		for (String child : children) {
			File c = new File(file, child);
			try {
				if (!isWithin(file, c)) {
					throw new IllegalArgumentException("Incorrect path " + c.getCanonicalPath());
				}
			} catch (IOException e) {
				throw new IllegalArgumentException("Incorrect path: " + c.getAbsolutePath(), e);
			}
			file = c;
		}
		return file;
	}

	// public static Collection<File> listSubfoldersOf(File root) {
	// List<File> ret = new ArrayList();
	// if (!root.isDirectory()) {
	// return ret;
	// }
	// for (File sub : root.listFiles()) {
	// if (sub.isDirectory())
	// ret.add(sub);
	// }
	// Collections.sort(ret, new Comparator() {
	// public int compare(File a, File b) {
	// return a.getName().compareTo(b.getName());
	// }
	// });
	// return ret;
	// }
	//
	// public static Collection<File> listSubfoldersContainingFile(File root,
	// String filename) {
	// List<File> ret = new ArrayList();
	// if (!root.isDirectory()) {
	// return ret;
	// }
	// for (File sub : root.listFiles())
	// if (sub.isDirectory()) {
	// File lookup = new File(sub, filename);
	// if (lookup.isFile())
	// ret.add(sub);
	// }
	// Collections.sort(ret, new Comparator() {
	// public int compare(File a, File b) {
	// return a.getName().compareTo(b.getName());
	// }
	// });
	// return ret;
	// }
}
