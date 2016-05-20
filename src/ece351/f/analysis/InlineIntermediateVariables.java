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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.parboiled.common.ImmutableList;

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

public final class InlineIntermediateVariables extends PostOrderExprVisitor {

	private final Set<String> intermediateVars = new TreeSet<String>();
	private final Map<String,AssignmentStatement> defns = new TreeMap<String,AssignmentStatement>();
	private int count = 0;

	/**
	 * Maximum depth that nesting is allowed before inlining stops working.
	 * This approach is less expensive than explicit cycle detection.
	 */
	private final int MAX = 5;
	
	private InlineIntermediateVariables(final FProgram p1) {
		// defns
		for (final AssignmentStatement a : p1.formulas) {
			defns.put(a.outputVar.identifier, a);
		}		
	}
	
	/**
	 * Assume that there are no cycles.
	 * @param p1
	 * @return
	 */
	public static FProgram inline(final FProgram p1) {
		final InlineIntermediateVariables fiv = new InlineIntermediateVariables(p1);
		return fiv.doit(0);
	}

	private final FProgram doit(final int iteration) {
		final int oldestCount = count;

		// replace uses of intermediate vars with their defns
		final List<AssignmentStatement> as = new ArrayList<AssignmentStatement>(defns.values());
		for (final AssignmentStatement a : as) {
			final int oldCount = count;
			final Expr e = traverseExpr(a.expr);
			if (oldCount != count) {
				// something changed: save it
				final String n = a.outputVar.identifier;
				defns.put(n, new AssignmentStatement(a.outputVar, e));
			}
		}
		
		if (oldestCount == count || iteration >= MAX) {
			// nothing changed, or we've run out of budget: we're done
			// construct a new FProgram to return
			ImmutableList<AssignmentStatement> result = ImmutableList.of();
			for (final Map.Entry<String, AssignmentStatement> me : defns.entrySet()) {
				final String n = me.getKey();
				if (!intermediateVars.contains(n)) {
					// not an intermediate var, so must be an output
					result = result.append(me.getValue());
				}
			}
			return new FProgram(result);
		} else {
			// something changed, and we have budget: keep working
			return doit(iteration+1);
		}
	}
	
	/** 
	 * Rewrite VarExprs with their definitions, if available.
	 */
	@Override
	public Expr visitVar(final VarExpr e) {
		final String n = e.identifier;
		final AssignmentStatement a = defns.get(n);
		if (a == null) {
			// a true input variable
			return e;
		} else {
			// an intermediate variable: replace with defn
			intermediateVars.add(n);
			count++;
			return a.expr;
		}
	}

	// no-ops
	@Override public Expr visitConstant(final ConstantExpr e) { return e; }
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
