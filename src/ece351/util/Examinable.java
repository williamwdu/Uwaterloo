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

package ece351.util;

/**
 * Defines three different kinds of equivalence comparisons: computational
 * substitutability (equals), semantic equivalence (equivalent), and
 * structural/syntactic equivalence (isomorphic).
 * 
 * @author drayside
 *
 */
public interface Examinable {

	/**
	 * Compares to objects to see if they can be substituted for each other in the
	 * computation [Liskov]. Should only ever be true of immutable objects.
	 * 
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj);
	
	/**
	 * Structurally (syntactically) the same. Perhaps the ordering of some things has
	 * changed, but otherwise these two objects represent the same AST.
	 * 
	 * @param obj
	 * @return
	 */
	public boolean isomorphic(Examinable obj);
	
	/**
	 * Semantically the same, even if syntactically different.
	 * @param obj
	 * @return
	 */
	public boolean equivalent(Examinable obj);
	
}
