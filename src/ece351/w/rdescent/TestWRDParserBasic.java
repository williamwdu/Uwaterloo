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

import org.junit.Test;
import org.parboiled.common.ImmutableList;

import ece351.TestImports;
import ece351.TestPrelab;
import ece351.util.BaseTest351;
import ece351.util.ExaminableProperties;
import ece351.w.ast.WProgram;
import ece351.w.ast.Waveform;

public class TestWRDParserBasic extends BaseTest351 {

	@Test
	public void test1() {
		WProgram built = new WProgram();
		built = built.append(new Waveform(ImmutableList.of("0"), "A"));
		
		final WProgram parsed = WRecursiveDescentParser.parse("A: 0;");
		
		check(built, parsed);
	}

	@Test
	public void test2() {
		WProgram built = new WProgram();
		built = built.append(new Waveform(ImmutableList.of("0", "1", "0", "1"), "A"));
		
		final WProgram parsed = WRecursiveDescentParser.parse("A: 0 1 0 1;");
		
		check(built, parsed);
	}


	@Test
	public void test3() {
		WProgram built = new WProgram();
		built = built.append(new Waveform(ImmutableList.of("0", "1", "0", "1"), "A"));
		built = built.append(new Waveform(ImmutableList.of("0", "0", "1", "1"), "B"));
		
		final WProgram parsed = WRecursiveDescentParser.parse("A: 0 1 0 1; B: 0 0 1 1;");
		
		check(built, parsed);
	}

	
	private void check(WProgram built, final WProgram parsed) {
		// check that the two ASTs are equals
		assertTrue("ASTs differ", parsed.equals(built));

		// check examinable sanity
		ExaminableProperties.checkAllUnary(parsed);
		ExaminableProperties.checkAllUnary(built);
		ExaminableProperties.checkAllBinary(parsed, built);
	}

}
