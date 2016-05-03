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

public final class Boolean351 {

	private Boolean351() {
		throw new UnsupportedOperationException();
	}
	
	/** Supports Nary-And. */
	public static boolean and(final boolean... b) {
		if (b.length == 1) {
			return b[0];
		}
		boolean returnValue = b[0];
		for (int i = 1; i < b.length; i++) {
			returnValue &= b[i];
		}
		return returnValue;
	}

	/** Supports Nary-Or. */
	public static boolean or(final boolean... b) {
		if (b.length == 1) {
			return b[0];
		}
		boolean returnValue = b[0];
		for (int i = 1; i < b.length; i++) {
			returnValue |= b[i];
		}
		return returnValue;
	}

//	public static boolean and(final boolean b1, final boolean b2) {
//		return b1 && b2;
//	}
//	
//	public static boolean or(final boolean b1, final boolean b2) {
//		return b1 || b2;
//	}
	
	public static boolean not(final boolean b) {
		return !b;
	}

	public static boolean xor(final boolean b1, final boolean b2) {
		return or(b1,b2) && not(and(b1,b2));
	}
	
	public static boolean implies(final boolean antecedant, final boolean consequent) {
		return or(antecedant, not(consequent));
	}

	public static boolean nand(final boolean b1, final boolean b2) {
		return not(and(b1,b2));
	}

	public static boolean nor(final boolean b1, final boolean b2) {
		return not(or(b1,b2));
	}
}
