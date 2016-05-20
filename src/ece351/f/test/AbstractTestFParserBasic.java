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

import org.junit.Test;
import org.parboiled.common.ImmutableList;

import ece351.common.ast.AndExpr;
import ece351.common.ast.AssignmentStatement;
import ece351.common.ast.BinaryExpr;
import ece351.common.ast.Expr;
import ece351.common.ast.NotExpr;
import ece351.common.ast.OrExpr;
import ece351.common.ast.VarExpr;
import ece351.f.ast.FProgram;
import ece351.util.BaseTest351;


public abstract class AbstractTestFParserBasic extends BaseTest351 {

	public AbstractTestFParserBasic() { 
		super(); 
	}

	abstract protected FProgram parse(final String input);
	
	/**
	 * x <= a and b;
	 */
	@Test
	public void testAnd() {
		// parse
		final FProgram fp = parse("x <= a and b;");
		// compare
		compareExpectSame(fp, constructAnd());
		compareExpectDifferent(fp, constructOr());
		compareExpectDifferent(fp, constructNot());
		compareExpectDifferent(fp, constructAnd("p", "q"));
		// check isomorphism up to commutativity
		assertTrue("isomorphic() should allow arguments to commute", fp.isomorphic(constructAnd("b", "a")));
	}

	/**
	 * x <= a or b;
	 */
	@Test
	public void testOr() {
		final FProgram fp = parse("x <= a or b;");
		// compare
		compareExpectSame(fp, constructOr());
		compareExpectDifferent(fp, constructAnd());
		compareExpectDifferent(fp, constructNot());
		compareExpectDifferent(fp, constructOr("p", "q"));
		// check isomorphism up to commutativity
		assertTrue("isomorphic() should allow arguments to commute", fp.isomorphic(constructOr("b", "a")));
	}

	/**
	 * x <= not a;
	 */
	@Test
	public void testNot() {
		// parse
		final FProgram fp = parse("x <= not a;");
		// compare
		compareExpectSame(fp, constructNot());
		compareExpectDifferent(fp, constructAnd());
		compareExpectDifferent(fp, constructOr());
		compareExpectDifferent(fp, constructNot("p"));
	}

	/**
	 * x <= a or b or c; should parse as (a or b) or c.
	 */
	@Test
	public void testLeftAssociativeOr() {
		// parse
		final FProgram fp1 = parse("x <= a or b or c;");
		// compare
		compareExpectSame(fp1, constructOr("a", "b", "c"));
		compareExpectDifferent(fp1, constructAnd());
		compareExpectDifferent(fp1, constructOr());
		compareExpectDifferent(fp1, constructNot("p"));

		final FProgram fp2 = constructOrRightAssociative("a", "b", "c");
		assertFalse("unexpectedly isomorphic: " + fp1 + " " + fp2, fp1.isomorphic(fp2));
		assertFalse("unexpectedly equals: " + fp1 + " " + fp2, fp1.equals(fp2));
	}
	
	/**
	 * x <= a and b and c; should parse as (a and b) and c.
	 */
	@Test
	public void testLeftAssociativeAnd() {
		// parse
		final FProgram fp1 = parse("x <= a and b and c;");
		// compare
		compareExpectSame(fp1, constructAnd("a", "b", "c"));
		compareExpectDifferent(fp1, constructAnd());
		compareExpectDifferent(fp1, constructOr());
		compareExpectDifferent(fp1, constructNot("p"));

		final FProgram fp2 = constructAndRightAssociative("a", "b", "c");
		assertFalse("unexpectedly isomorphic: " + fp1 + " " + fp2, fp1.isomorphic(fp2));
		assertFalse("unexpectedly equals: " + fp1 + " " + fp2, fp1.equals(fp2));
	}
	
	private FProgram constructNot() {
		return constructNot("a");
	}
	
	private FProgram constructNot(final String a) {
		final Expr e = new NotExpr(new VarExpr(a));
		final AssignmentStatement s = new AssignmentStatement(new VarExpr("x"), e);
		final FProgram fp = new FProgram(ImmutableList.of(s));
		return fp;
	}

	private static FProgram constructAnd() {
		return constructAnd("a", "b");
	}
	
	private static FProgram constructOr() {
		return constructOr("a", "b");
	}
	
	private static FProgram constructAnd(final String... vars) {
		return constructBinaryExpr(new AndExpr(), vars);
	}
	
	private static FProgram constructOr(final String... vars) {
		return constructBinaryExpr(new OrExpr(), vars);
	}
	
	private static FProgram constructBinaryExpr(final BinaryExpr proto, final String... vars) {
		Expr e = null;
		// left associative (build from the front)
		for (final String s : vars) {
			final VarExpr v = new VarExpr(s);
			if (e == null) {
				e = v;
			} else {
				e = proto.newBinaryExpr(e, v);
			}
		}
		final AssignmentStatement s = new AssignmentStatement(new VarExpr("x"), e);
		final FProgram fp = new FProgram(ImmutableList.of(s));
		return fp;
	}

	private static FProgram constructOrRightAssociative(final String... vars) {
		return constructBinaryExprRightAssociative(new OrExpr(), vars);
	}

	private static FProgram constructAndRightAssociative(final String... vars) {
		return constructBinaryExprRightAssociative(new AndExpr(), vars);
	}

	private static FProgram constructBinaryExprRightAssociative(final BinaryExpr proto, final String... vars) {
		Expr e = null;
		// right associative (build from the back)
		for (int i = vars.length-1; i >= 0; i--) {
			final String s = vars[i];
			final VarExpr v = new VarExpr(s);
			if (e == null) {
				e = v;
			} else {
				e = proto.newBinaryExpr(v, e);
			}
		}
		final AssignmentStatement s = new AssignmentStatement(new VarExpr("x"), e);
		final FProgram fp = new FProgram(ImmutableList.of(s));
		return fp;
	}
	
	public static void compareExpectSame(final FProgram fp1, final FProgram fp2) {
		assertTrue("unexpectedly not equivalent: " + fp1 + " " + fp2, fp1.equivalent(fp2));
		assertTrue("unexpectedly not isomorphic: " + fp1 + " " + fp2, fp1.isomorphic(fp2));
		assertTrue("unexpectedly not equals: " + fp1 + " " + fp2, fp1.equals(fp2));
	}

	public static void compareExpectDifferent(final FProgram fp1, final FProgram fp2) {
		assertFalse("unexpectedly equivalent: " + fp1 + " " + fp2, fp1.equivalent(fp2));
		assertFalse("unexpectedly isomorphic: " + fp1 + " " + fp2, fp1.isomorphic(fp2));
		assertFalse("unexpectedly equals: " + fp1 + " " + fp2, fp1.equals(fp2));
	}

}
