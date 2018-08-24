package com.tarotdt.pas.web.util;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class Utils {
	public static <T> T lastElement(T[] array) {
		return array[(array.length - 1)];
	}

	public static void unsafeSleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException localInterruptedException) {
		}
	}

	public static void setStdoutNotBuffered() {
		System.setOut(new PrintStream(StreamUtils.readFD(FileDescriptor.in, "UTF-8")));
	}

	public static String isoFormat(long ts) {
		return ISODateTimeFormat.basicDateTime().withZone(DateTimeZone.UTC).print(ts);
	}

	public static String isoFormatReadableByDateFormat(long ts) {
		return ISODateTimeFormat.dateHourMinuteSecondMillis().withZone(DateTimeZone.UTC).print(ts) + "Z";
	}

	public static String isoFormatReadableByDateFormatFromTimestampWithCorrectToString(Timestamp ts) {
		return ISODateTimeFormat.dateHourMinuteSecondMillis().withZone(DateTimeZone.getDefault()).print(ts.getTime()) + "Z";
	}

	public static String isoFormatPretty(Date date) {
		return ISODateTimeFormat.dateHourMinuteSecondMillis().withZone(DateTimeZone.UTC).print(date.getTime()) + "Z";
	}

	public static String isoFormatPretty(long ts) {
		return ISODateTimeFormat.dateHourMinuteSecondMillis().withZone(DateTimeZone.UTC).print(ts);
	}

	public static String formatTSFileCompatible(Long ts) {
		if (ts == null)
			ts = Long.valueOf(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf.format(new Date(ts.longValue()));
	}

	public static String indent(int indent) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indent; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

	public static void unsafeClose(Connection conn) {
		try {
			conn.close();
		} catch (Exception e) {
			logger.warn("Could not safely close SQL connection " + conn, e);
		}
	}

	public static String[] parseClassPath(String classPath) {
		List<String> out = new ArrayList<String>();
		String[] jars = classPath.trim().split(":");
		for (String jar : jars) {
			if (jar.endsWith("*")) {
				File[] files = new File(jar.substring(0, jar.length() - 2)).listFiles();
				if (files != null) {
					for (File file : files) {
						out.add(file.getAbsolutePath());
					}
				} else {
					logger.warn("Invalid jar directory:" + jar);
				}
			} else {
				File file = new File(jar);
				if (file.exists()) {
					out.add(jar);
				} else {
					logger.warn("Invalid jar file:" + jar);
				}
			}
		}
		return (String[]) out.toArray(new String[out.size()]);
	}

	public static void unsafeSetAutoCommit(Connection conn, boolean autoCommit) {
		try {
			if (conn.getMetaData().supportsTransactions()) {
				conn.setAutoCommit(autoCommit);
			}
		} catch (SQLException e) {
			logger.warn("Failed to change autoCommit on connection", e);
		}
	}

	public static void unsafeRollbackAndClose(Connection conn) {
		try {
			conn.rollback();
			logger.info("Closing " + conn);
			conn.close();
			logger.info("Conn " + conn + " is now " + conn.isClosed());
		} catch (Exception e) {
			logger.warn("Could not safely close SQL connection " + conn, e);
		}
	}

	public static Map<String, String> getEnvironment() {
		Map<String, String> env = new HashMap();
		env.putAll(System.getenv());
		return env;
	}

	public static String[] environmentArray(Map<String, String> env) {
		String[] result = new String[env.size()];
		int i = 0;
		for (Map.Entry<String, String> e : env.entrySet()) {
			result[i] = ((String) e.getKey() + "=" + (String) e.getValue());
			i++;
		}
		return result;
	}

	public static int execAndLog(String[] args, String[] env) throws IOException, InterruptedException {
		Process p = Runtime.getRuntime().exec(args, env);
		Thread tout = new LoggingStreamEater(p.getInputStream(), Level.INFO);
		tout.start();
		Thread terr = new LoggingStreamEater(p.getErrorStream(), Level.WARN);
		terr.start();
		int rv = p.waitFor();
		tout.join();
		terr.join();
		return rv;
	}

	public static void execAndLogThrows(String[] args, String[] env, File cwd) throws IOException, InterruptedException {
		Process p = Runtime.getRuntime().exec(args, env, cwd);
		Thread tout = new LoggingStreamEater(p.getInputStream(), Level.INFO);
		tout.start();
		Thread terr = new LoggingStreamEater(p.getErrorStream(), Level.INFO);
		terr.start();
		int rv = p.waitFor();
		if (rv != 0) {
			throw new IOException("Return code is non-zero: " + rv);
		}
		tout.join();
		terr.join();
	}

	public static void execAndLogThrows(ProcessBuilder pb) throws IOException, InterruptedException {
		Process p = pb.start();
		LoggingStreamEater outLogger = new LoggingStreamEater(p.getInputStream(), Level.INFO);
		outLogger.start();
		LoggingStreamEater errLogger = new LoggingStreamEater(p.getErrorStream(), Level.INFO);
		errLogger.start();

		int rv = p.waitFor();
		if (rv != 0) {
			if ((pb.command().size() > 0) && (((String) pb.command().get(0)).length() < 100)) {
				throw new IOException(
				    "Process execution failed (" + (String) pb.command().get(0) + ")  (return code " + rv + ")");
			}
			throw new IOException("Process execution failed (return code " + rv + ")");
		}

		outLogger.join();
		errLogger.join();
	}

	public static byte[] execAndGetOutput(String[] args, String[] env) throws IOException, InterruptedException {
		Process p = Runtime.getRuntime().exec(args, env);
		GatheringStreamEater tout = new GatheringStreamEater(p.getInputStream());
		tout.start();
		Thread terr = new LoggingStreamEater(p.getErrorStream(), Level.INFO);
		terr.start();
		int rv = p.waitFor();
		if (rv != 0) {
			throw new IOException("Return code is non-zero: " + rv);
		}
		tout.join();
		terr.join();
		return tout.getOutput().toByteArray();
	}

	public static byte[] execAndGetOutput(String[] args, String[] env, File cwd)
	    throws IOException, InterruptedException {
		Process p = Runtime.getRuntime().exec(args, env, cwd);
		GatheringStreamEater tout = new GatheringStreamEater(p.getInputStream());
		tout.start();
		Thread terr = new LoggingStreamEater(p.getErrorStream(), Level.INFO);
		terr.start();
		int rv = p.waitFor();
		if (rv != 0) {
			throw new IOException("Return code is non-zero: " + rv);
		}
		tout.join();
		terr.join();
		return tout.getOutput().toByteArray();
	}

	public static void execAndWriteOutput(String[] args, String[] env, File cwd, File output)
	    throws IOException, InterruptedException {
		Process p = Runtime.getRuntime().exec(args, env, cwd);
		FileOutputStream fos = new FileOutputStream(output);
		CopyStreamEater tout = new CopyStreamEater(p.getInputStream(), fos);
		tout.start();
		Thread terr = new LoggingStreamEater(p.getErrorStream(), Level.INFO);
		terr.start();
		int rv = p.waitFor();
		if (rv != 0) {
			throw new IOException("Return code is non-zero: " + rv);
		}
		tout.join();
		terr.join();
	}

	public static void execAndWriteOutput(String[] args, String[] env, File cwd, OutputStream output)
	    throws IOException, InterruptedException {
		Process p = Runtime.getRuntime().exec(args, env, cwd);
		CopyStreamEater tout = new CopyStreamEater(p.getInputStream(), output);
		tout.start();
		Thread terr = new LoggingStreamEater(p.getErrorStream(), Level.INFO);
		terr.start();
		int rv = p.waitFor();
		if (rv != 0) {
			throw new IOException("Return code is non-zero: " + rv);
		}
		tout.join();
		terr.join();
	}

	public static ExecutionResults execAndGetOutputAndErrors(String[] args, String[] env)
	    throws InterruptedException, IOException {
		ExecutionResults er = new ExecutionResults();
		Process p = Runtime.getRuntime().exec(args, env);
		GatheringStreamEater tout = new GatheringStreamEater(p.getInputStream());
		tout.start();
		GatheringStreamEater terr = new GatheringStreamEater(p.getErrorStream());
		terr.start();
		er.rv = p.waitFor();
		tout.join();
		terr.join();
		er.err = terr.getOutput().toString();
		er.out = tout.getOutput().toString();
		return er;
	}

	public static ExecutionResults execAndGetOutputAndErrors(String[] args, String[] env, String input)
	    throws InterruptedException, IOException {
		ExecutionResults er = new ExecutionResults();
		Process p = Runtime.getRuntime().exec(args, env);

		PrintStream stream = new PrintStream(p.getOutputStream());
		stream.append(input);
		stream.close();
		p.getOutputStream().close();

		GatheringStreamEater tout = new GatheringStreamEater(p.getInputStream());
		tout.start();
		GatheringStreamEater terr = new GatheringStreamEater(p.getErrorStream());
		terr.start();
		er.rv = p.waitFor();
		tout.join();
		terr.join();
		er.err = terr.getOutput().toString();
		er.out = tout.getOutput().toString();
		return er;
	}

	public static class ExecutionResults {
		public String out;
		public String err;
		public int rv;
	}

	public static class LoggingStreamEater extends Thread {
		public LoggingStreamEater(InputStream is, Level level, String threadName) {
			this.is = is;
			this.level = level;
			this.threadName = threadName;
		}

		public LoggingStreamEater(InputStream is, Level level) {
			this.is = is;
			this.level = level;
		}

		public void run() {
			if (this.threadName == null) {
				Thread.currentThread().setName("Exec-" + Thread.currentThread().getId());
			} else {
				Thread.currentThread().setName(this.threadName + "-" + Thread.currentThread().getId());
			}
			InheritableNDC.inheritNDC();
			try {
				BufferedReader br = StreamUtils.readStream(this.is);
				for (;;) {
					String line = br.readLine();
					if (line == null)
						break;
					handle(line);
				}
				br.close();
			} catch (IOException e) {
				_logger.error("", e);
			}
		}

		protected void handle(String line) {
			Utils.logger.log(this.level, line);
		}

		private Level level;
		InputStream is;
		private String threadName;
		private static Logger _logger = Logger.getLogger("process");
	}

	public static class SmartLogTailBuilder {
		private int maxLines = 80;
		private final List<String> tail = com.google.common.collect.Lists.newArrayList();

		public synchronized void appendLine(String line) {
			if (this.tail.size() >= this.maxLines) {
				this.tail.remove(0);
			}
			this.tail.add(line);
		}

		public synchronized SmartLogTail get() {
			SmartLogTail ret = new SmartLogTail();
			for (String line : this.tail) {
				ret.appendLine(line);
			}
			return ret;
		}
	}

	public static class StreamTailer extends Utils.LoggingStreamEater {
		private final Utils.SmartLogTailBuilder tailBuilder;

		public StreamTailer(InputStream is, Level level, String threadName, Utils.SmartLogTailBuilder tailBuilder) {
			super(is, level, threadName);
			this.tailBuilder = tailBuilder;
		}

		public StreamTailer(InputStream is, Level level, Utils.SmartLogTailBuilder tailBuilder) {
			super(is, level);
			this.tailBuilder = tailBuilder;
		}

		protected synchronized void handle(String line) {
			super.handle(line);
			this.tailBuilder.appendLine(line);
		}
	}

	public static class LineOrientedThreadSafeCopyStreamEaterWithLogging extends Thread {
		OutputStream os;
		InputStream is;

		public LineOrientedThreadSafeCopyStreamEaterWithLogging(InputStream is, OutputStream os) {
			this.is = is;
			this.os = os;
		}

		public void run() {
			Thread.currentThread().setName("Exec-" + Thread.currentThread().getId());
			InheritableNDC.inheritNDC();
			try {
				BufferedReader br = StreamUtils.readStream(this.is);
				for (;;) {
					String line = br.readLine();
					if (line == null)
						break;
					logger.info(line);
					synchronized (this.os) {
						this.os.write((line + "\n").getBytes("utf8"));
						this.os.flush();
					}
				}
				br.close();
			} catch (IOException e) {
				logger.error("", e);
			}
		}

		private static Logger logger = Logger.getLogger("process");
	}

	public static class ThreadSafeCopyStreamEater extends Thread {
		OutputStream os;
		InputStream is;

		public ThreadSafeCopyStreamEater(InputStream is, OutputStream os) {
			this.is = is;
			this.os = os;
		}

		public void run() {
			Thread.currentThread().setName("Exec-" + Thread.currentThread().getId());
			InheritableNDC.inheritNDC();
			try {
				byte[] buf = new byte[4096];
				for (;;) {
					int i = this.is.read(buf);
					if (i <= 0)
						break;
					synchronized (this.os) {
						this.os.write(buf, 0, i);
					}
				}
				this.is.close();
			} catch (IOException e) {
				logger.error("", e);
			}
		}

		private static Logger logger = Logger.getLogger("process");
	}

	public static class CopyStreamEater extends Thread {
		OutputStream os;
		InputStream is;

		public CopyStreamEater(InputStream is, OutputStream os) {
			this.is = is;
			this.os = os;
		}

		public void run() {
			Thread.currentThread().setName("Exec-" + Thread.currentThread().getId());
			InheritableNDC.inheritNDC();
			try {
				byte[] buf = new byte[4096];
				for (;;) {
					int i = this.is.read(buf);
					if (i <= 0)
						break;
					this.os.write(buf, 0, i);
				}
				this.is.close();
			} catch (IOException e) {
				logger.error("", e);
			}
		}

		private static Logger logger = Logger.getLogger("process");
	}

	public static class GatheringStreamEater extends Utils.CopyStreamEater {
		public GatheringStreamEater(InputStream is) {
			super(is, new ByteArrayOutputStream());
			this.is = is;
		}

		public ByteArrayOutputStream getOutput() {
			return (ByteArrayOutputStream) this.os;
		}
	}

	public static String tailFile(File f, int nlines) throws IOException {
		long skip = Math.max(0L, f.length() - (long) nlines * 2000L);
		FileInputStream fis = new FileInputStream(f);
		fis.skip(skip);

		List<String> lines = new ArrayList();
		BufferedReader br = new BufferedReader(new java.io.InputStreamReader(fis, "utf8"));
		try {
			for (;;) {
				String line = br.readLine();
				if (line == null)
					break;
				lines.add(line);
			}
		} finally {
			br.close();
		}
		int keptLines = Math.min(lines.size(), nlines);
		StringBuilder sb = new StringBuilder();
		for (int i = lines.size() - keptLines; i < lines.size(); i++) {
			sb.append((String) lines.get(i));
			sb.append('\n');
		}
		return sb.toString();
	}

	public static Map<String, String> parseKVStringArray(String[] array) {
		Map<String, String> map = new HashMap<String, String>();
		for (String val : array) {
			String[] chunks = val.split("=");
			if (chunks.length < 2) {
				throw new IllegalArgumentException("Illegal param: " + val + ", expected key=value");
			}
			String paramValue = val.substring(chunks[0].length() + 1);
			map.put(chunks[0], paramValue);
		}
		return map;
	}

	public static Map<String, String> parseKVStringArray(Collection<String> array) {
		return parseKVStringArray((String[]) array.toArray(new String[0]));
	}

	public static byte[] getResourceFileContent(String file) throws IOException {
		InputStream is = Utils.class.getClassLoader().getResourceAsStream(file);
		Throwable ex = null;
		try {
			if (is == null) {
				throw new Error("Can't open resource file " + file);
			}
			return IOUtils.toByteArray(is);
		} catch (Throwable e) {
			ex = e;
			throw e;
		} finally {
			if (is != null)
				if (ex != null)
					try {
						is.close();
					} catch (Throwable e) {
						ex.addSuppressed(e);
					}
				else
					is.close();
		}
	}

	public static String getResourceFileContentUTF8(String file) throws IOException {
		InputStream is = Utils.class.getClassLoader().getResourceAsStream(file);
		Throwable ex = null;
		try {
			if (is == null) {
				throw new Error("Can't open resource file " + file);
			}
			return IOUtils.toString(is, "utf8");
		} catch (Throwable e) {
			ex = e;
			throw e;
		} finally {
			if (is != null) {
				if (ex != null)
					try {
						is.close();
					} catch (Throwable e) {
						ex.addSuppressed(e);
					}
				else {
					is.close();
				}
			}
		}
	}

	public static <T> Map<String, T> listToMap(List<T> list, String memberOrFunction) {
		Map<String, T> ret = new HashMap<String, T>();
		if (list.isEmpty()) {
			return ret;
		}
		Class<?> tclazz = list.get(0).getClass();
		try {
			Method m;
			if (memberOrFunction.endsWith("()")) {
				m = tclazz.getMethod(memberOrFunction.replace("()", ""), new Class[0]);
				for (T obj : list) {
					String key = (String) m.invoke(obj, new Object[0]);
					ret.put(key, obj);
				}
			} else {
				Field f = tclazz.getField(memberOrFunction);
				for (T obj : list) {
					String key = (String) f.get(obj);
					ret.put(key, obj);
				}
			}
		} catch (Exception e) {
			throw new Error(e);
		}
		return ret;
	}

	public static <T> void listRemove(List<T> list, String memberOrFunction, String needle) {
		if (list.size() == 0)
			return;
		Class<?> tclazz = list.get(0).getClass();
		try {
			if (memberOrFunction.endsWith("()")) {
				Method m = tclazz.getMethod(memberOrFunction.replace("()", ""), new Class[0]);
				ListIterator<T> it = list.listIterator();
				while (it.hasNext()) {
					String key = (String) m.invoke(it.next(), new Object[0]);
					if (key.equals(needle))
						it.remove();
				}
			} else {
				ListIterator<T> it = list.listIterator();
				Field f = tclazz.getField(memberOrFunction);
				while (it.hasNext()) {
					String key = (String) f.get(it.next());
					if (key.equals(needle))
						it.remove();
				}
			}
		} catch (Exception e) {
			throw new Error(e);
		}
	}

	public static <T> T listFind(List<T> list, String memberOrFunction, String needle) {
		if (list.size() == 0)
			return null;
		Class<?> tclazz = list.get(0).getClass();
		try {
			Method m;
			Field f;
			if (memberOrFunction.endsWith("()")) {
				m = tclazz.getMethod(memberOrFunction.replace("()", ""), new Class[0]);
				for (T obj : list) {
					String key = (String) m.invoke(obj, new Object[0]);
					if (key.equals(needle)) {
						return obj;
					}
				}
			} else {
				f = tclazz.getField(memberOrFunction);
				for (T obj : list) {
					String key = (String) f.get(obj);
					if (key.equals(needle))
						return obj;
				}
			}
			return null;
		} catch (Exception e) {
			throw new Error(e);
		}
	}

	// public static <T> void listSort(List<T> list, String memberOrFunction,
	// final boolean reverse) {
	// if (list.size() == 0)
	// return;
	// Class<?> tclazz = list.get(0).getClass();
	// try {
	// if (memberOrFunction.endsWith("()")) {
	// Method m = tclazz.getMethod(memberOrFunction.replace("()", ""), new
	// Class[0]);
	// Collections.sort(list, new Comparator() {
	// public int compare(T o1, T o2) {
	// try {
	// String s1 = (String) this.val$m.invoke(o1, new Object[0]);
	// String s2 = (String) this.val$m.invoke(o1, new Object[0]);
	// return reverse ? s2.compareTo(s1) : s1.compareTo(s2);
	// } catch (Exception e) {
	// throw new Error(e);
	// }
	// }
	// });
	// } else {
	// Field f = tclazz.getField(memberOrFunction);
	// if (!f.getType().isPrimitive()) {
	// Collections.sort(list, new Comparator() {
	// public int compare(T o1, T o2) {
	// try {
	// String s1 = (String) this.val$f.get(o1);
	// String s2 = (String) this.val$f.get(o1);
	// return reverse ? s2.compareTo(s1) : s1.compareTo(s2);
	// } catch (Exception e) {
	// throw new Error(e);
	// }
	// }
	// });
	// } else if (f.getType() == Integer.TYPE) {
	// Collections.sort(list, new Comparator() {
	// public int compare(T o1, T o2) {
	// try {
	// int i1 = this.val$f.getInt(o1);
	// int i2 = this.val$f.getInt(o2);
	// return reverse ? Ints.compare(i2, i1) : Ints.compare(i1, i2);
	// } catch (Exception e) {
	// throw new Error(e);
	// }
	// }
	// });
	// } else if (f.getType() == Long.TYPE) {
	// Collections.sort(list, new Comparator() {
	// public int compare(T o1, T o2) {
	// try {
	// long i1 = this.val$f.getLong(o1);
	// long i2 = this.val$f.getLong(o2);
	// return reverse ? Longs.compare(i2, i1) : Longs.compare(i1, i2);
	// } catch (Exception e) {
	// throw new Error(e);
	// }
	// }
	// });
	// } else {
	// throw new Error("Unsupported type for sort : " + f.getType());
	// }
	// }
	// } catch (Throwable e) {
	// throw new Error(e);
	// }
	// }



	public static void forceInit(Class<?> cl) {
		try {
			Class.forName(cl.getName(), true, cl.getClassLoader());
		} catch (ClassNotFoundException localClassNotFoundException) {
		}
	}

	public static <T> Iterable<T> iterSafe(Iterable<T> input) {
		if (input != null) {
			return input;
		}
		return new ArrayList();
	}

	public static String md5Base64(String data) {
		return org.apache.commons.codec.binary.Base64
		    .encodeBase64String(org.apache.commons.codec.digest.DigestUtils.md5(data)).substring(0, 22);
	}

	public static Calendar getUTCCalendar() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.setFirstDayOfWeek(2);
		cal.setMinimalDaysInFirstWeek(4);
		cal.setLenient(false);
		return cal;
	}

	public static SimpleDateFormat getSimpleDateFormatUTCStrict(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setLenient(false);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		return sdf;
	}

	public static DateTimeFormatter getDateFormatter(String format) {
		return DateTimeFormat.forPattern(format).withZone(DateTimeZone.UTC);
	}

	public static DateTimeFormatter getISODateFormatter() {
		return ISODateTimeFormat.dateTime().withZone(DateTimeZone.UTC);
	}

	private static final Random fgRandom = new Random(System.currentTimeMillis());

	public static int findUnusedPort() {
		int high = 64000;
		int low = 60000;
		for (int i = 0; i < 10;) {
			Socket s = null;
			int port = (int) (fgRandom.nextFloat() * (float) (high - low)) + low;
			try {
				s = new Socket("localhost", port);

				if (s != null) {
					try {
						s.close();
					} catch (IOException localIOException) {
					}
				}
				i++;

			} catch (ConnectException e) {

				return port;
			} catch (IOException localIOException2) {
			} finally {
				if (s != null) {
					try {
						s.close();
					} catch (IOException localIOException4) {
					}
				}
			}
		}
		return -1;
	}

	public static File nextFileInSequence(File folder, String prefix) {
		if (!folder.isDirectory())
			return new File(folder, prefix + "1");
		Set<String> names = Sets.newHashSet(folder.list());
		int i = 0;
		do {
			i++;
		} while (names.contains(prefix + "" + i));
		return new File(folder, prefix + "" + i);
	}

	private static Map<String, String> extensionToType = Maps.newHashMap();

	static {
		extensionToType.put("txt", "text/plain");
		extensionToType.put("text", "text/plain");
		extensionToType.put("log", "text/plain");

		extensionToType.put("xhtml", "application/xhtml+xml");
		extensionToType.put("xht", "application/xhtml+xml");

		extensionToType.put("xml", "application/xml");
		extensionToType.put("xsl", "application/xml");

		extensionToType.put("dtd", "application/xml-dtd");

		extensionToType.put("xslt", "application/xslt+xml");

		extensionToType.put("html", "text/html");
		extensionToType.put("htm", "text/html");

		extensionToType.put("tar", "application/x-tar");

		extensionToType.put("pdf", "application/pdf");

		extensionToType.put("bz", "application/x-bzip");

		extensionToType.put("bz2", "application/x-bzip2");
		extensionToType.put("boz", "application/x-bzip2");

		extensionToType.put("zip", "application/zip");

		extensionToType.put("gzip", "application/x-gzip");

		extensionToType.put("jar", "application/java-archive");

		extensionToType.put("js", "application/javascript");

		extensionToType.put("json", "application/json");

		extensionToType.put("bmp", "image/bmp");

		extensionToType.put("gif", "image/gif");

		extensionToType.put("jpeg", "image/jpeg");
		extensionToType.put("jpg", "image/jpeg");
		extensionToType.put("jpe", "image/jpeg");

		extensionToType.put("png", "image/png");

		extensionToType.put("svg", "image/svg+xml");
		extensionToType.put("svgz", "image/svg+xml");

		extensionToType.put("tiff", "image/tiff");
		extensionToType.put("tif", "image/tiff");

		extensionToType.put("ico", "image/x-icon");

		extensionToType.put("ico", "image/x-icon");

		extensionToType.put("py", "text/x-python");
		extensionToType.put("r", "text/x-rsrc");
		extensionToType.put("scala", "text/x-scala");
		extensionToType.put("sh", "text/x-sh");
		extensionToType.put("sql", "text/x-sql");
		extensionToType.put("csv", "text/plain");
		extensionToType.put("md", "text/x-markdown");
	}

	public static String probeContentTypeWithFallback(File file) {
		String mimeType = null;
		try {
			mimeType = Files.probeContentType(file.toPath());
			if ((mimeType != null) && (mimeType.equals("text/plain"))) {
				String extensionMimeType = guessMimeTypeFromExtension(file);
				if (isTextLikeMimeType(extensionMimeType)) {
					mimeType = extensionMimeType;
				}
			}
		} catch (IOException e) {
			logger.warn("Could not probe a file's content type", e);
		}
		if (mimeType == null) {
			mimeType = guessMimeTypeFromExtension(file);
		}
		return mimeType;
	}

	public static String guessMimeTypeFromExtension(File file) {
		String mimeType = "application/octet-stream";
		if (file.getName().indexOf('.') > 0) {
			String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1).toLowerCase();
			if (extensionToType.containsKey(extension)) {
				mimeType = (String) extensionToType.get(extension);
			}
		}
		return mimeType;
	}

	public static boolean isTextLikeMimeType(String mimeType) {
		boolean isText = false;
		for (String prefix : new String[] { "text" }) {
			if ((mimeType != null) && (mimeType.startsWith(prefix))) {
				isText = true;
			}
		}
		for (String suffix : new String[] { "/plain", "/json", "/javascript", "python", "scala", "sql" }) {
			if ((mimeType != null) && (mimeType.endsWith(suffix))) {
				isText = true;
			}
		}
		return isText;
	}

	public static void startLogAppender(Appender appender) {
		Logger rootLogger = Logger.getRootLogger();
		if (!rootLogger.isAttached(appender)) {
			String pattern = "%-4r [%t] %-5p %c %x - %m%n";
			Enumeration enumeration = rootLogger.getAllAppenders();

			while (enumeration.hasMoreElements()) {
				Appender a = (Appender) enumeration.nextElement();
				if (a.getLayout() != null && a.getLayout() instanceof PatternLayout) {
					PatternLayout pl = (PatternLayout) a.getLayout();
					pattern = pl.getConversionPattern();
					break;
				}
			}

			appender.setLayout(new PatternLayout(pattern));
			rootLogger.addAppender(appender);
		}

	}

	private static Logger logger = Logger.getLogger(Utils.class);
}
