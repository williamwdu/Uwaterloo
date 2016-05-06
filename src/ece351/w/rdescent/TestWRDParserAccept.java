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

package ece351.w.rdescent;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ece351.util.BaseTest351;
import ece351.util.CommandLine;
import ece351.util.ExaminableProperties;
import ece351.util.TestInputs351;
import ece351.w.ast.WProgram;

@RunWith(Parameterized.class)
public final class TestWRDParserAccept extends BaseTest351 {

	private final File wave;

	public TestWRDParserAccept(final File wave) {
		this.wave = wave;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> waveFiles() {
		return TestInputs351.waveFiles();
	}

	@Test
	public void parse() {
		final String inputSpec = wave.getAbsolutePath();
		final CommandLine c = new CommandLine(inputSpec);
		final String input = c.readInputSpec();
		System.out.println("processing " + inputSpec);
		System.out.println("input: ");
		System.out.println(input);
		// parse from the file to construct first AST
		final WProgram wp1 = WRecursiveDescentParser.parse(input);
		// pretty-print the first AST
		final String pp = wp1.toString();
		System.out.println("pretty-print: ");
		System.out.println(pp);
		// construct a second AST from the pretty-print
		final WProgram wp2 = WRecursiveDescentParser.parse(pp);
		// check that the two ASTs are isomorphic (syntactically the same)
		assertTrue("ASTs differ for " + inputSpec, wp1.isomorphic(wp2));
		// check examinable sanity
		ExaminableProperties.checkAllUnary(wp1);
		ExaminableProperties.checkAllUnary(wp2);
		ExaminableProperties.checkAllBinary(wp1, wp2);
		// success!
		System.out.println("accepted, as expected:  " + inputSpec);
	}

}
