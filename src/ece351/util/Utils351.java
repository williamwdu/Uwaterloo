/* *********************************************************************
 * ECE351 
 * Department of Electrical and Computer Engineering 
 * University of Waterloo 
 * Term: Summer 2016 (1165)
 *
 * The base version of this file is the intellectual property of the
 * University of Waterloo. Redistribution is prohibited.
 *
 * By pushing changes to this file I affirm that I am the author of
 * all changes. I affirm that I have complied with the course
 * collaboration policy and have not plagiarized my work. 
 *
 * I understand that redistributing this file might expose me to
 * disciplinary action under UW Policy 71. I understand that Policy 71
 * allows for retroactive modification of my final grade in a course.
 * For example, if I post my solutions to these labs on GitHub after I
 * finish ECE351, and a future student plagiarizes them, then I too
 * could be found guilty of plagiarism. Consequently, my final grade
 * in ECE351 could be retroactively lowered. This might require that I
 * repeat ECE351, which in turn might delay my graduation.
 *
 * https://uwaterloo.ca/secretariat-general-counsel/policies-procedures-guidelines/policy-71
 * 
 * ********************************************************************/

package ece351.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

import org.parboiled.common.ImmutableList;

// TODO: remove class Utils351
public final class Utils351 {

	private Utils351() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Read input file to String.
	 * @param inputFileName
	 * @return
	 */
	public static String readFile(final String inputFileName) {
		try {
			final File f = new File(inputFileName);
			final long length = f.length();
			if (length < 1000000) {
				final FileReader fr = new FileReader(inputFileName);
				final BufferedReader br = new BufferedReader(fr);
				final char[] c = new char[(int)length];
				br.read(c);
				br.close();
				return new String(c);
			} else {
				throw new RuntimeException("File is too big! " + inputFileName);
			}
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	/**
	 * Read non-blank lines from file into a list of strings.
	 * @param inputFileName
	 * @return
	 */
	// TODO: remove these methods from Utils351
	public static List<String> readFileLines(final String inputFileName) {
		try {
			
			final BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFileName));
			final List<String> lines = new ArrayList<String>();
			
			String line = null;
			while ((line= bufferedReader.readLine()) != null) {
				if (line.trim().length() > 0) {
					lines.add(line);
				}
			}
			bufferedReader.close();
			
			return lines;
			
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static File[] files(final String dirName, final String regex) {
		final File dir = new File(dirName);
		if (!dir.exists()) {
			System.err.println("directory does not exist: " + dirName);
			System.err.println("proceeding anyway ...");
			return new File[]{};
		}
		assert dir.isDirectory() : "not a directory: " + dir;
		final Pattern p = Pattern.compile(regex);
		final File[] result = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(final File f, final String s) {
				final boolean m = p.matcher(s).matches();
				return m;
			}
		});
		assert result != null;
		Arrays.sort(result);
		return result;

	}

	public static File[] filesR(final String dirName, final String regex) {
		// preconditions
		final File dir = new File(dirName);
		assert dir.isDirectory() : "not a directory: " + dir;
		// get all subdirs (recursively)
		final File[] subdirs = subdirsR(dir);
		// get all matching files in all subdirs
		final List<File> result = new ArrayList<File>();
		for (final File d : subdirs) {
			final File[] files = files(d.getAbsolutePath(), regex);
			for (final File f : files) { result.add(f); }
		}
		return result.toArray(new File[]{});
	}
	
	public static File[] subdirs(final File dir) {
		// precondition
		assert dir.isDirectory() : 
			"not a directory: " + dir;
		// subdirs
		final File[] subdirs = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(final File f) {
				final boolean b = f.isDirectory();
				return b;
			}
		});
		return subdirs;
	}

	public static File[] subdirsR(final File root) {
		// precondition
		assert root.isDirectory() : "not a directory: " + root;
		// storage
		final List<File> result = new ArrayList<File>();
		result.add(root);
		final Stack<File> worklist = new Stack<File>();
		worklist.push(root);
		// subdirs
		while (!worklist.isEmpty()) {
			final File d = worklist.pop();
			final File[] subdirs = subdirs(d);
			for (final File s : subdirs) { 
				result.add(s);
				worklist.push(s); 
			}
		}
		Collections.sort(result);
		return result.toArray(new File[]{});
	}

	public static String bitListToString(final ImmutableList<String> list) {
		StringBuilder s = new StringBuilder();
		for(final String item : list) {
			s.append(item); s.append(", ");
		}
		if (s.length() > 0) {s.delete(s.length()-2,	s.length()-1);}
		return s.toString();
	}

}
