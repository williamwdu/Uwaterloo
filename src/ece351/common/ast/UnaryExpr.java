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

import ece351.util.Examinable;
import ece351.util.Examiner;

public abstract class UnaryExpr extends Expr {
	public final Expr expr;

    public UnaryExpr(final Expr e) { 
    	this.expr = e; 
    }

    public UnaryExpr varyExpr(final Expr e) {
    	return newUnaryExpr(e);
    }
    
	@Override
	public final boolean repOk() {
		assert expr != null : "child expr of a UnaryExpr /" + getClass().getName() + " should not be null";
		assert expr.repOk();
		return true;
	}

    public abstract UnaryExpr newUnaryExpr(final Expr expr);

    private final boolean examine(final Examiner e, final Examinable obj) {
		// basics
		if (obj == null) return false;
		if (!obj.getClass().equals(this.getClass())) return false;
		final UnaryExpr that = (UnaryExpr) obj;
		// compare field values using e.examine(x,y)
		return e.examine(expr, that.expr);
// TODO: short code snippet
    }
    
    @Override
    public final int hashCode() {
    	return 17 + expr.hashCode();
    }

	@Override
	public final boolean equals(final Object obj) {
		if (!(obj instanceof Examinable)) return false;
		return examine(Examiner.Equals, (Examinable)obj);
	}

	@Override
	public final boolean isomorphic(final Examinable obj) {
		return examine(Examiner.Isomorphic, obj);
	}

	@Override
	public final String toString() {
// TODO: short code snippet
		String str = "";
		str = this.operator() +" "+ expr.toString();
		return str;
	}

}
