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


public final class OrExpr extends CommutativeBinaryExpr{
	
	public OrExpr(Expr left, Expr right) {
		super(left,right);
	}
	public OrExpr() {this(null, null);}
    
    public OrExpr(Object pop1, Object pop2) {
    	this( (Expr)pop1, (Expr)pop2 );
	}
	public Expr accept(final ExprVisitor v){
    	return v.visitOr(this);
    }

	@Override
	public Expr simplifyOnce() {
		// return a new NaryOrExpr with the same children as this OrExpr
// TODO: short code snippet
throw new ece351.util.Todo351Exception();
	}
	@Override
	public String operator() {
		return Constants.OR;
	}
	@Override
	public BinaryExpr newBinaryExpr(final Expr left, final Expr right) {
		return new OrExpr(left, right);
	}
}
