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

package ece351.f.simplifier;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ece351.f.ast.FProgram;
import ece351.f.rdescent.FRecursiveDescentParser;
import ece351.util.BaseTest351;
import ece351.util.CommandLine;
import ece351.util.ExaminableProperties;
import ece351.util.TestInputs351;

/**
 * Just checks that the simplifier always produces equivalent output.
 */
@RunWith(Parameterized.class)
public final class TestSimplifierEquivalence extends BaseTest351 {

	private final File f;

	public TestSimplifierEquivalence(final File f) {
		this.f = f;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> files() {
		return TestInputs351.formulaFiles();
	}

	@Test
	public void simplify() throws IOException {
		final String inputSpec = f.getAbsolutePath();
		final CommandLine c = new CommandLine("-h", "-o4", inputSpec);
		final String input = c.readInputSpec();
		System.out.println("processing " + inputSpec);
		System.out.println("input: ");
		System.out.println(input);
		
		// parse from the file to construct first AST
		final FProgram original = FRecursiveDescentParser.parse(input);
		System.out.println("input after parsing + pretty-printing:");
		System.out.println(original.toString());
		final FProgram simplified = original.simplify();
		System.out.println("ouput: ");
		System.out.println(simplified.toString());

		// equivalence
		assertTrue("simplifier breaks equivalence for " + f.getName(), original.equivalent(simplified));
		
		// idempotence
		final FProgram simplified2 = simplified.simplify();
		assertTrue("simplifier not idempotent for " + f.getName(), simplified.equals(simplified2));
		
		// check examinable sanity
		ExaminableProperties.checkAllUnary(original);
		ExaminableProperties.checkAllUnary(simplified);
		ExaminableProperties.checkAllBinary(original, simplified);
		
		// success!
		System.out.println("success!  " + inputSpec);
	}

}
