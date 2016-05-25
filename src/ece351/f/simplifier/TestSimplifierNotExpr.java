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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ece351.common.ast.Expr;
import ece351.common.ast.NotExpr;
import ece351.common.ast.VarExpr;
import ece351.util.BaseTest351;

public class TestSimplifierNotExpr extends BaseTest351 {

	@Test
	public void testDoubleNegative() {
		final VarExpr x = new VarExpr("x");
		final NotExpr e = new NotExpr(new NotExpr(x));
		assertTrue(e.repOk());
		final Expr result = e.simplify();
		assertTrue(result.repOk());
		assertEquals(x, result);
	}

}
