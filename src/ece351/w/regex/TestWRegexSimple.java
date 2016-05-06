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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ece351.util.CommandLine;

@RunWith(Parameterized.class)
public class TestWRegexSimple {

	private final String fileName;
	private final String regex;

	public TestWRegexSimple(final String fileName, final String regex) {
		this.fileName = fileName;
		this.regex = regex;
	}
	
	@Parameterized.Parameters(name = "{0} ~ {1}")
	public static Iterable<Object[]> regexs() {
		final List<String> regexs = TestWRegexSimpleData.regexs;
		final int length = regexs.size();
		final List<Object[]> result = new ArrayList<Object[]>();
		final String regex = regexs.get(length-1);
		final int bound = Math.min(length+1, 8);
		for (int i = 0; i < bound; i++) {
			final String file = "tests/wave/r" + (i+1) + ".wave";
			result.add(new Object[]{file, regex});
		}
		return result;
	}

	@Test
	public void recognize() {
		process(true, regex, fileName);
	}
	
	/**
	 * Evaluate regex on inputSpec. InputSpec can be either a string of a
	 * putative W program or it can be the name of a file that contains a
	 * putative W program.
	 * @param regex
	 * @param inputSpec
	 */
	protected static void process(final boolean expect, final String regex, final String inputSpec) {
		final CommandLine c = new CommandLine(inputSpec);
		final Pattern p = Pattern.compile(regex, Pattern.DOTALL);
		final String input = c.readInputSpec();
		final Matcher m = p.matcher(input);
		final boolean result = m.matches();
		if (expect) {
			if (result) {
				// accepted, as expected
				assertTrue(result);
				System.out.println("accepted, as expected:  " + inputSpec);
			} else {
				// rejected, but should not have
				// try to print a helpful error message
				System.err.println("regular expression did not recognize file contents.");
				System.err.println("  file:      " + inputSpec);
				System.err.println("  regex:     " + regex);
				System.err.println("  contents:  " + input);
				// we know this will fail
				assertTrue("regex does not recognize contents of " + inputSpec, result);
			}
		} else {
			assertFalse(result);
			System.out.println("rejected, as expected:  " + inputSpec);
		}
	}

}
