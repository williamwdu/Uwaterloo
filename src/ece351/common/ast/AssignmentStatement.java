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

package ece351.common.ast;

import org.parboiled.common.ImmutableList;

import ece351.f.ast.FProgram;
import ece351.util.Examinable;

public final class AssignmentStatement extends Statement implements Examinable {

	public final VarExpr outputVar;
	public final Expr expr;
	
	public AssignmentStatement() {
		outputVar = null;
		expr = null;
	}
	
	public AssignmentStatement(String var, Expr expr)
	{
		this.outputVar = new VarExpr(var);
		this.expr = expr;
	}
	
	public AssignmentStatement(VarExpr var, Expr expr) {
		this.outputVar = var;
		this.expr = expr;
	}

	public AssignmentStatement(Object var) {
		this((VarExpr)var, null);
	}

	public boolean repOk() {
		assert outputVar != null;
		assert expr != null : "expr is null for " + outputVar;
		assert outputVar.repOk();
		assert expr.repOk();
		return true;
	}
	
    @Override
    public String toString() {
// TODO: short code snippet
    	String str = "";
    	str = outputVar.toString() + " <= " + expr.toString() + " ;";
    	return str;
    }

	@Override
	public boolean equals(final Object obj) {
		// basics
		if (obj == null) return false;
		if (!obj.getClass().equals(getClass())) return false;
		final AssignmentStatement that = (AssignmentStatement) obj;

		// compare fields
		if (!this.outputVar.equals(that.outputVar)) return false;
		if (!this.expr.equals(that.expr)) return false;

		// no significant differences found, return true
		return true;
	}

	@Override
	public boolean isomorphic(final Examinable obj) {
		// basics
		if (obj == null) return false;
		if (!obj.getClass().equals(this.getClass())) return false;
		final AssignmentStatement that = (AssignmentStatement) obj;
		
		// TODO: compare field values
		if(!this.expr.operator().equals(that.expr.operator())) return false;
		if (!this.outputVar.equals(that.outputVar)) return false;
		
		// no significant differences found, return true
		return true;
// TODO: short code snippet
	}

	/**
	 * Call a SAT solver to compute logical equivalence.
	 */
	@Override
	public boolean equivalent(final Examinable obj) {
		if (!(obj instanceof AssignmentStatement)) return false;
		final FProgram fp1 = new FProgram(ImmutableList.of(this));
		final FProgram fp2 = new FProgram(ImmutableList.of((AssignmentStatement)obj));
		return fp1.equivalent(fp2);
	}
	
	public AssignmentStatement simplify() {
		return new AssignmentStatement(outputVar, expr.simplify());
	}

	public AssignmentStatement varyExpr(final Object e) {
		return new AssignmentStatement(outputVar, (Expr)e);
	}
	
	public AssignmentStatement varyOutputVar(final VarExpr v) {
		return new AssignmentStatement(v, expr);
	}

	public AssignmentStatement varyOutputVar(final String s) {
		return new AssignmentStatement(new VarExpr(s), expr);
	}
}
