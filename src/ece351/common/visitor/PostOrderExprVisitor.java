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

package ece351.common.visitor;

import org.parboiled.common.ImmutableList;

import ece351.common.ast.BinaryExpr;
import ece351.common.ast.Expr;
import ece351.common.ast.NaryExpr;
import ece351.common.ast.UnaryExpr;

/**
 * This visitor rewrites the AST from the bottom up.
 * Optimized to only create new parent nodes if children have changed.
 */
public abstract class PostOrderExprVisitor extends ExprVisitor {

	@Override
	public final Expr traverseUnaryExpr(UnaryExpr u) {
		// child first
		final Expr child = traverseExpr(u.expr);
		// only rewrite if something has changed
		if (child != u.expr) {
			u = u.newUnaryExpr(child);
		}
		// now parent
		return u.accept(this);
	}
	
	@Override
	public final Expr traverseBinaryExpr(BinaryExpr b) {
		// children first
		final Expr left = traverseExpr(b.left);
		final Expr right = traverseExpr(b.right);
		// only rewrite if something has changed
		if (left != b.left || right != b.right) {
			b = b.newBinaryExpr(left, right);
		}
		// now parent
		return b.accept(this);
	}

	@Override
	public final Expr traverseNaryExpr(NaryExpr e) {
		// children first
		ImmutableList<Expr> children = ImmutableList.of();
		boolean change = false;
		for (final Expr c1 : e.children) {
			final Expr c2 = traverseExpr(c1);
			children = children.append(c2);
			if (c2 != c1) { change = true; }
		}
		// only rewrite if something changed
		if (change) {
			e = e.newNaryExpr(children);
		}
		// now parent
		return e.accept(this);
	}
}
