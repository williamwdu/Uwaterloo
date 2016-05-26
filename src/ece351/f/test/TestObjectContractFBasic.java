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

package ece351.f.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.parboiled.common.ImmutableList;

import ece351.common.ast.AssignmentStatement;
import ece351.common.ast.VarExpr;
import ece351.f.ast.FProgram;

/**
 * If these tests fail, then you are probably not comparing the strings inside VarExpr properly.
 * Read the lab manual to understand different ways to compare objects.
 */
public class TestObjectContractFBasic {

	@Test
	public void test1() {
		final VarExpr v1 = new VarExpr(new String("x"));
		final VarExpr v2 = new VarExpr(new String("x"));
		assertEquals(v1, v2);
	}

	@Test
	public void test2() {
		final FProgram fp1 = simpleFProgramWithFreshStrings();
		final FProgram fp2 = simpleFProgramWithFreshStrings();
		assertEquals(fp1, fp2);
	}

	private FProgram simpleFProgramWithFreshStrings() {
		return new FProgram(ImmutableList.of(new AssignmentStatement(new VarExpr(new String("x")), new VarExpr(new String("a")))));
	}
}
