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

import ece351.common.visitor.ExprVisitor;
import ece351.util.Examinable;

/**
 * Represents either true (1) or false (0).
 * The Singleton design pattern is used here so that this class is
 * instantiated exactly twice, regardless of the program input.
 * @see http://en.wikipedia.org/wiki/Singleton_pattern
 */
public final class ConstantExpr extends Expr {
	public final Boolean b;

	/** The one true instance. To be shared/aliased wherever necessary. */
	public final static ConstantExpr TrueExpr = new ConstantExpr(true);
	/** The one false instance. To be shared/aliased wherever necessary. */
	public final static ConstantExpr FalseExpr = new ConstantExpr(false);

	/** Private constructor prevents clients from instantiating. */
	private ConstantExpr(final Boolean b) { this.b = b; }

	/** To be used by clients instead of the constructor. 
	  * Returns a reference to one of the shared objects. */
	public static ConstantExpr make(final Boolean b) {
		if (b) { return TrueExpr; } else { return FalseExpr; }
	}

	public static ConstantExpr make(final String s) {
		return make(s.startsWith("1"));
	}

	@Override
	public final boolean repOk() {
		assert b != null : "b should not be null";
		return true;
	}

	public String toString() {
// TODO: short code snippet
	String str = "";
	if (b == true){
		str = "'1'";
		return str;
	}
	else{
		str = "'0'";
		return str;
	}
	}

	public Expr accept(final ExprVisitor v){
		return v.visitConstant(this);
	}

	@Override
	public String operator() {
		return "Const";
	}

	@Override
	public int hashCode() {
		return Boolean.valueOf(b).hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		// basics
		if (obj == null) return false;
		if (!obj.getClass().equals(this.getClass())) return false;
		final ConstantExpr that = (ConstantExpr) obj;

		// compare field values
// TODO: short code snippet
		if (that.b.equals(b)){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public boolean isomorphic(final Examinable obj) {
		return equals(obj);
	}

}
