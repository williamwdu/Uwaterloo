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

public final class VarExpr extends Expr {
	
	public String identifier;
	
	public VarExpr(final String name){
		this.identifier = name;
	}
	
	public VarExpr(final Object name) {
		this((String)name);
	}

	@Override
	public final boolean repOk() {
		assert identifier != null : "identifier should not be null";
		assert identifier.length() > 0 : "identifier should be a non-empty string";
		return true;
	}


    public String toString() {
    	return this.identifier;
    }
    
    public Expr accept(final ExprVisitor v){
    	return v.visitVar(this);
    }

    @Override
    public int hashCode() {
    	return identifier.hashCode();
    }

	@Override
	public boolean equals(final Object obj) {
		// basics
		if (obj == null) return false;
		if (!getClass().equals(obj.getClass())) return false;
		final VarExpr that = (VarExpr) obj;
		// compare field values
// TODO: short code snippet
		if (that.identifier.equals(identifier)){
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

	public String operator() {
		return "var";
	}
}
