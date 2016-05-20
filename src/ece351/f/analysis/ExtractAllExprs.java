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

import kodkod.util.collections.IdentityHashSet;
import ece351.common.ast.AndExpr;
import ece351.common.ast.AssignmentStatement;
import ece351.common.ast.ConstantExpr;
import ece351.common.ast.EqualExpr;
import ece351.common.ast.Expr;
import ece351.common.ast.NAndExpr;
import ece351.common.ast.NOrExpr;
import ece351.common.ast.NaryAndExpr;
import ece351.common.ast.NaryOrExpr;
import ece351.common.ast.NotExpr;
import ece351.common.ast.OrExpr;
import ece351.common.ast.VarExpr;
import ece351.common.ast.XNOrExpr;
import ece351.common.ast.XOrExpr;
import ece351.common.visitor.PostOrderExprVisitor;
import ece351.f.ast.FProgram;

/**
 * Returns a set of all Expr objects in a given FProgram or AssignmentStatement.
 * The result is returned in an IdentityHashSet, which defines object identity
 * by memory address. A regular HashSet defines object identity by the equals()
 * method. Consider two VarExpr objects, X1 and X2, both naming variable X. If
 * we tried to add both of these to a regular HashSet the second add would fail
 * because the regular HashSet would say that it already held a VarExpr for X.
 * The IdentityHashSet, on the other hand, will hold both X1 and X2.
 */
public final class ExtractAllExprs extends PostOrderExprVisitor {
	
	private final IdentityHashSet<Expr> exprs = new IdentityHashSet<Expr>();
	
	private ExtractAllExprs(final Expr e) { traverseExpr(e); }
	
	public static IdentityHashSet<Expr> allExprs(final AssignmentStatement f) {
		final ExtractAllExprs cae = new ExtractAllExprs(f.expr);
		return cae.exprs;
	}
	
	public static IdentityHashSet<Expr> allExprs(final FProgram p) {
		final IdentityHashSet<Expr> allExprs = new IdentityHashSet<Expr>();
		for (final AssignmentStatement f : p.formulas) {
			allExprs.add(f.outputVar);
			allExprs.addAll(ExtractAllExprs.allExprs(f));
		}
		return allExprs;
	}
	
	@Override public Expr visitConstant(ConstantExpr e) { exprs.add(e); return e; }
	@Override public Expr visitVar(VarExpr e) { exprs.add(e); return e; }
	@Override public Expr visitNot(NotExpr e) { exprs.add(e); return e; }
	@Override public Expr visitAnd(AndExpr e) { exprs.add(e); return e; }
	@Override public Expr visitOr(OrExpr e) { exprs.add(e); return e; }
	@Override public Expr visitXOr(XOrExpr e) { exprs.add(e); return e; }
	@Override public Expr visitNAnd(NAndExpr e) { exprs.add(e); return e; }
	@Override public Expr visitNOr(NOrExpr e) { exprs.add(e); return e; }
	@Override public Expr visitXNOr(XNOrExpr e) { exprs.add(e); return e; }
	@Override public Expr visitEqual(EqualExpr e) { exprs.add(e); return e; }
	@Override public Expr visitNaryAnd(NaryAndExpr e) { exprs.add(e); return e; }
	@Override public Expr visitNaryOr(NaryOrExpr e) { exprs.add(e); return e; }
}
