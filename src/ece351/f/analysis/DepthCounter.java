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

package ece351.f.analysis;

import ece351.common.ast.AssignmentStatement;
import ece351.common.ast.BinaryExpr;
import ece351.common.ast.Expr;
import ece351.common.ast.NaryExpr;
import ece351.common.ast.UnaryExpr;
import ece351.f.ast.FProgram;

/**
 * Measure the depth of an F AST.
 * Similar code to our visitor traversal methods, but with an extra 
 * integer argument to track the depth.
 */
public class DepthCounter {

	/** 
	 * Dispatch to appropriate measure method. 
	 */
	public final int measureExpr(final Expr e, final int depth) {
			// NaryExpr case
			// BinaryExpr case
			// UnaryExpr case
			// nothing more to measure
// TODO: longer code snippet
throw new ece351.util.Todo351Exception();
	}
	
	/** Return the depth of the deepest child. */
	public int measureNaryExpr(final NaryExpr e, final int depth) {
// TODO: short code snippet
throw new ece351.util.Todo351Exception();
	}
	
	/** Return the depth of the deepest child. */
	public int measureBinaryExpr(final BinaryExpr e, final int depth) {
// TODO: short code snippet
throw new ece351.util.Todo351Exception();
	}
	
	/** Return the depth of the child. */
	public int measureUnaryExpr(final UnaryExpr e, final int depth) {
// TODO: short code snippet
throw new ece351.util.Todo351Exception();
	}

	/** Return the depth of the deepest formula in this FProgram. */
	public static int measureFProgram(final FProgram p) {
		int maxDepth = 0;
		for (final AssignmentStatement astmt : p.formulas) {			
			maxDepth = Math.max(maxDepth, measureAssignmentStatement(astmt));
		}
		return maxDepth;
	}

	/** Return the depth of this formula. */
	public static int measureAssignmentStatement(final AssignmentStatement astmt) {
		final DepthCounter dc = new DepthCounter();
		return dc.measureExpr(astmt.expr, 1);
	}
}
