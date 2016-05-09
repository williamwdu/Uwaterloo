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

package ece351.w.regex;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ece351.util.CommandLine;
import ece351.util.TestInputs351;
import ece351.util.Utils351;

@RunWith(Parameterized.class)
public final class TestWRegexAccept {

	/**
	 * Now you have a fairly general regex. Copy and paste it into this static
	 * variable and let's try to match it against all of the test wave files.
	 */
	final static String REGEX = "([A-Za-z][[A-Z]|[0-9]|\\s]*:[\\s]*[0 |1 |\\s]+;\\s*)+";
// TODO: short code snippet


	
	private final File wave;

	public TestWRegexAccept(final File wave) {
		this.wave = wave;
	}

	@Parameterized.Parameters(name = "{0}")
	public static Collection<Object[]> waveFiles() {
		return TestInputs351.waveFiles();
	}

	@Test
	public void accept() {
		TestWRegexSimple.process(true, REGEX, wave.getAbsolutePath());
	}
	
}
