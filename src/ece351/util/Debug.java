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

public final class Debug {

	private Debug() {
		throw new UnsupportedOperationException();
	}
	
	/** 
	 * Print a message if the global debug flag is on.
	 * @param s
	 */
	public static void msg(final Object s) {
		if (CommandLine.GLOBAL != null) {
			if (CommandLine.GLOBAL.debug) {
				// the user has asked for debug messages
				System.err.println(s);
			}
		} else {
			// default uninitialized state is to print debug messages
			System.err.println(s);
		}
	}
	
	public static void barf() {
		throw new RuntimeException("something went wrong, probably bad input");
	}

	public static void barf(final String msg) {
		if (msg == null || msg.length() == 0) {
			barf();
		} else {
			throw new RuntimeException(msg);
		}
	}

}
