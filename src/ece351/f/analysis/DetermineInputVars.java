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

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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


public final class DetermineInputVars extends PostOrderExprVisitor {
	private final Set<String> inputVars = new LinkedHashSet<String>();
	private DetermineInputVars(final AssignmentStatement f) { traverseExpr(f.expr); }
	/** Input variables of an AssignmentStatement, in order of occurrence. */
	public static Set<String> inputVars(final AssignmentStatement f) {
		final DetermineInputVars div = new DetermineInputVars(f);
		return Collections.unmodifiableSet(div.inputVars);
	}
	/** Input variables of an FProgram, sorted lexicographically. */
	public static SortedSet<String> inputVars(final FProgram p) {
		final SortedSet<String> vars = new TreeSet<String>();
		for (final AssignmentStatement f : p.formulas) {
			vars.addAll(DetermineInputVars.inputVars(f));
		}
		return vars;
	}
	@Override public Expr visitConstant(final ConstantExpr e) { return e; }
	@Override public Expr visitVar(final VarExpr e) { inputVars.add(e.identifier); return e; }
	@Override public Expr visitNot(final NotExpr e) { return e; }
	@Override public Expr visitAnd(final AndExpr e) { return e; }
	@Override public Expr visitOr(final OrExpr e) { return e; }
	@Override public Expr visitNaryAnd(final NaryAndExpr e) { return e; }
	@Override public Expr visitNaryOr(final NaryOrExpr e) { return e; }
	@Override public Expr visitXOr(final XOrExpr e) { return e; }
	@Override public Expr visitNAnd(final NAndExpr e) { return e; }
	@Override public Expr visitNOr(final NOrExpr e) { return e; }
	@Override public Expr visitXNOr(final XNOrExpr e) { return e; }
	@Override public Expr visitEqual(final EqualExpr e) { return e; }
}
