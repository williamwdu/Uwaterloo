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

import ece351.common.ast.ConstantExpr;
import ece351.common.ast.Expr;
import ece351.common.ast.NaryAndExpr;
import ece351.common.ast.NaryExpr;
import ece351.common.ast.NaryOrExpr;
import ece351.common.ast.VarExpr;
import ece351.util.BaseTest351;

public class TestSimplifierNaryExpr extends BaseTest351 {

	@Test
	public void testOrTrue() {
		final ConstantExpr t = ConstantExpr.TrueExpr;
		final VarExpr x = new VarExpr("x");
		final NaryExpr e = new NaryOrExpr(x, t);
		assertTrue(e.repOk());
		final Expr result = e.simplify();
		assertTrue(result.repOk());
		assertEquals(t, result);
	}

	@Test
	public void testAndTrue() {
		final ConstantExpr t = ConstantExpr.TrueExpr;
		final VarExpr x = new VarExpr("x");
		final NaryExpr e = new NaryAndExpr(x, t);
		assertTrue(e.repOk());
		final Expr result = e.simplify();
		assertTrue(result.repOk());
		assertEquals(x, result);
	}


}
