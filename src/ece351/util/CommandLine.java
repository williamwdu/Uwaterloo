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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.EnumSet;

import org.parboiled.common.FileUtils;


public final class CommandLine {
	
	public enum FSimplifierOptions { 
		STANDARDIZE, 
		CONSTANT,
		COMPLEMENT,
		DEDUPLICATION,
		ABSORPTION 
	}

	public final int argcount;
	public final boolean debug;
	public final boolean handparser;
	public final boolean parbparser;
	public final boolean parseDOM;
	
	public final int simplifierOptLevel;
	public final EnumSet<FSimplifierOptions> simplifierOpts;
	
	private String outputSpec = UNRESOLVED;
	
	/**
	 * Stores the actual input specification provided on the command line.
	 * Could either be a file name or a legal input string.
	 */
	private final ArrayList<String> inputSpecs;
	
	/**
	 * Distinguished value to store in inputPaths if the inputSpec is 
	 * a sentence in the language rather than a file path.
	 */
	public final static String SENTENCE = "SENTENCE";
	
	/**
	 * Distinguished value to store in inputPaths if the inputSpec has
	 * not yet been resolved.
	 */
	public final static String UNRESOLVED = "UNRESOLVED";
	
	public static CommandLine GLOBAL = null;
	
	public CommandLine(final String a) {
		this(new String[]{a});
	}
	
	public CommandLine(final String... args) {
		argcount = args.length;
		inputSpecs = new ArrayList<String>();
		boolean h = false;
		boolean p = false;
		boolean v = true; // debug flag is on by default
		boolean d = false;
		int o = 1; // Default level for simplifier
		for (int i = 0; i < args.length; i++) {
			if (args[i].length() == 0 || args[i].replaceAll(" *", "").length() == 0) {
				// ignore empty strings
				continue;
			} else if (args[i].startsWith("-h")) { 
				h = true;
				continue;
			} else if (args[i].startsWith("-p")) { 
				p = true;
				continue;
			} else if (args[i].equals("-v")) { // turn debug on
				v = true;
				continue;
			} else if (args[i].equals("-v0")) { // turn debug off
				v = false;
				continue;
			} else if (args[i].equals("-d")) {
				d = true;
				continue;
			} else if (args[i].equals("-f")) {
				i++;
				outputSpec = args[i];
				continue;
			} else if (args[i].startsWith("-o")) {
				try {
					if (!args[i].equals("-o")) {	// extract optimization level only if specified
						o = Integer.parseInt(args[i].substring(2));
					}
				} catch (final NumberFormatException e) { /* Default level for simplifier */ }
				continue;
			} else {
				inputSpecs.add(args[i]);
			}
		}

		if (!h && !p) { 
			// we need to choose a parser: none was specified
			p = true;
		}
		
		assert !(h && p) : "using -h and -p together does not make sense: choose one parser or the other";
		
		debug = v;
		handparser = h;
		parbparser = p;
		parseDOM = d;
		
		simplifierOptLevel = o;
		simplifierOpts = EnumSet.noneOf(FSimplifierOptions.class);
		switch(simplifierOptLevel) {
			case 4:
				simplifierOpts.add(FSimplifierOptions.ABSORPTION);
			case 3:
				simplifierOpts.add(FSimplifierOptions.DEDUPLICATION);
			case 2:
				simplifierOpts.add(FSimplifierOptions.COMPLEMENT);
			case 1:
				simplifierOpts.add(FSimplifierOptions.CONSTANT);
			case 0:
			default:
				simplifierOpts.add(FSimplifierOptions.STANDARDIZE);
		}
		
		if (GLOBAL == null) {
			GLOBAL = this;
		}
	}

	public String getInputName() {
		return getInputName(0);
	}
	
	private String getInputName(int i) {
		assert preconditions(i);
		final File f = getInputFile(i);
		if (f == null) {
			return SENTENCE;
		} else {
			final String n = f.getName();
			// trim the suffix
			final int lastDot = n.lastIndexOf('.');
			return n.substring(0, lastDot);
		}
	}

	public String readInputSpec() {
		return readInputSpec(0);
	}
	
	/**
	 * Resolve inputSpec to the contents of the file it names, or itself
	 * if it is not a file path.
	 * @param i
	 * @return
	 */
	private String readInputSpec(final int i) {
		assert preconditions(i);
		final File f = getInputFile(i);
		if (f == null) {
			return inputSpecs.get(i);
		} else {
			return FileUtils.readAllText(f);
		}
	}

	private boolean preconditions(final int i) {
		assert inputSpecs.size() > 0 : "no inputSpec";
		assert i < inputSpecs.size() : "specified inputSpec does not exist";
		return true;
	}
	
	public File getInputFile() {
		return getInputFile(0);
	}

	/**
	 * If inputSpec names a file then returns that file.
	 * If inputSpec is program text rather than a file then returns null.
	 * If inputSpec names a non-existent file then throws an exception.
	 * Non-existent status concluded if parent of inputSpec is an existing file.
	 * @param i
	 * @return
	 */
	private File getInputFile(final int i) {
		assert preconditions(i);
		final File fcheck = new File(inputSpecs.get(i));
		if (fcheck.exists()) {
			return fcheck;
		} else {
			final File parent = fcheck.getParentFile();
			if (parent == null) {
				// parent doesn't exist
				return null;
			} else {
				if (!parent.exists()) {
					// parent doesn't exist
					return null;
				} else {
					// parent exists
					throw new RuntimeException(new FileNotFoundException(fcheck.getAbsolutePath()));
				}
			}
		}
	}
	
	public PrintWriter resolveOutputSpec() {
		if (UNRESOLVED.equals(outputSpec)) {
			return new PrintWriter(System.out);
		} else {
			final File f = new File(outputSpec);
			f.getParentFile().mkdirs();
			try {
				return new PrintWriter(new FileWriter(f));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public File getOutputFile() {
		return new File(outputSpec);
	}

}
