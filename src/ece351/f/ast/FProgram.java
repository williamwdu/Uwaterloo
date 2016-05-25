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

package ece351.f.ast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.parboiled.common.ImmutableList;

import ece351.common.ast.AssignmentStatement;
import ece351.common.ast.VarExpr;
import ece351.util.Examinable;
import ece351.util.Examiner;
import ece351.util.RunAlloy351;
import ece351.w.ast.Waveform;


public final class FProgram implements Examinable {
	
    public final ImmutableList<AssignmentStatement> formulas;

    public FProgram() {
    	this.formulas = ImmutableList.of();
	}

    /**
     * Construct a new FProgram from a given immutable list of formulas.
     * We can just alias the argument because it's immutable.
     */
    public FProgram(final ImmutableList<AssignmentStatement> formulas) {
    	this.formulas = formulas;
    }
    
    /**
     * Construct a new FProgram from a given list of formulas.
     * We might need to make a defensive copy if the list is mutable.
     */
    public FProgram(final List<AssignmentStatement> formulas) {
    	if (formulas instanceof ImmutableList<?>) {
    		// argument is immutable, alias it
    		this.formulas = (ImmutableList<AssignmentStatement>)formulas;
    	} else {
    		// argument is mutable, make a defensive copy
    		this.formulas = ImmutableList.copyOf(formulas);
    	}
    }
    
	public boolean repOk() {
    	// some formulas
    	assert formulas != null;
    	assert !formulas.isEmpty();
    	// no duplicate output vars
    	assert formulas.size() == outputVars().size();
    	// check each formula
    	for (final AssignmentStatement astmt : formulas) {
    		assert astmt.repOk();
    	}
    	// representation is ok
    	return true;
    }
    
	public FProgram append(final Object formula) {
		return new FProgram(formulas.append((AssignmentStatement)formula));
	}

	public FProgram appendAll(final FProgram p) {
		ImmutableList<AssignmentStatement> result;
		ImmutableList<AssignmentStatement> rest;
		// determine which is longer and which is shorter
		if (formulas.size() > p.formulas.size()) {
			result = this.formulas;
			rest = p.formulas;
		} else {
			result = p.formulas;
			rest = this.formulas;
		}
		// add the shorter one to the longer one
		for (final AssignmentStatement a : rest) {
			result = result.append(a);
		}
		assert result.size() == (formulas.size() + p.formulas.size());
		return new FProgram(result);
	}

    public FProgram simplify() {
    	final List<AssignmentStatement> newformulas = new ArrayList<AssignmentStatement>(formulas.size());
    	for (final AssignmentStatement f : formulas) {
    		newformulas.add(f.simplify());
    	}
    	return new FProgram(newformulas);
    }
    
    public Set<VarExpr> outputVars() {
    	final Set<VarExpr> vars = new TreeSet<VarExpr>();
    	for (final AssignmentStatement f : formulas) {
    		vars.add(f.outputVar);
    	}
    	return Collections.unmodifiableSet(vars);
    }

    @Override
    public String toString() {
		if (formulas == null || formulas.isEmpty()) return "";
		final String sep = System.getProperty("line.separator");
		String str = "";
		for (AssignmentStatement f : formulas){
			str = str + f.toString() + sep;
		}
		return str;
// TODO: longer code snippet
    }
    
	@Override
	public boolean equals(final Object obj) {
		// basics
		if (obj == null) return false;
		if (!obj.getClass().equals(this.getClass())) return false;
		final FProgram that = (FProgram) obj;
		return(Examiner.orderedEquals(formulas, that.formulas));
		// compare field values using Examiner.orderedExamination()
		// no significant differences found, return true
// TODO: short code snippet
	}
	
	@Override
	public boolean isomorphic(final Examinable obj) {
		// basics
		if (obj == null) return false;
		if (!obj.getClass().equals(this.getClass())) return false;
		final FProgram that = (FProgram) obj;
		return(Examiner.unorderedEquals(formulas, that.formulas));
		// compare field values using Examiner.unorderedExamination()
		// no significant differences found, return true
// TODO: short code snippet
	}

	/**
	 * Check that two FPrograms are equivalent by translating them to SAT
	 * and asking a SAT solver to compute the answer.
	 */
	@Override
	public boolean equivalent(final Examinable obj) {
		// basics
		if (obj == null) return false;
		if (!obj.getClass().equals(this.getClass())) return false;
		final FProgram that = (FProgram) obj;
		
		// only run this on well-formed ASTs
		assert repOk();
		assert that.repOk();
		
		// check that we have the same output variables
		final int size = this.formulas.size();
		if (size != that.formulas.size()) return false;
		final Set<VarExpr> thisOutputVars = this.outputVars();
		final Set<VarExpr> thatOutputVars = that.outputVars();
		if (!thisOutputVars.equals(thatOutputVars)) return false;
		// input variables could be different, because some
		// of them might be effectively don't care
		// so don't need to check input vars
		
		// generate the Alloy specification
		// (will be translated to SAT in the next step)
		final String alloy = AlloyConverter.convert(this, that);

		// now the hard part ...
		// ask a SAT solver if these two FPrograms are equivalent
		final boolean result = !RunAlloy351.check(alloy);
		return result;
	}

}
