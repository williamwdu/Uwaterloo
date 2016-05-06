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

package ece351.w.regex;

import java.util.ArrayList;
import java.util.List;

class TestWRegexSimpleData {

	static List<String> regexs = new ArrayList<String>();
	
	static {
		// r1.wave: This regex is exactly the same as the input, so it accepts.
		regexs.add("A: 0;\\s*");

		// r2.wave
		// Copy the regex from above and paste it here.
		// Generalize the regex to accept multiple input values, either 0 or 1.
		regexs.add("A: 0;\\s*");
		// TODO: short code snippet

		// r3.wave
		// Copy the regex from above and paste it here.
		// Generalize the regex to allow whitespace between the last signal and the semi-colon.
// TODO: short code snippet

		// r4.wave
		// Copy the regex from above and paste it here.
		// Generalize the regex to allow multi-character pin names.
// TODO: short code snippet

		// r5.wave
		// Copy the regex from above and paste it here.
		// Generalize the regex to allow lower case in pin names.
// TODO: short code snippet

		// r6.wave
		// Copy the regex from above and paste it here.
		// Generalize the regex to allow multiple spaces between values.
// TODO: short code snippet

		// r7.wave
		// Copy the regex from above and paste it here.
		// Generalize the regex for whitespace again.
// TODO: short code snippet

		// r8.wave
		// Copy the regex from above and paste it here.
		// Generalize the regex to allow multiple pins
// TODO: short code snippet
		
	};
	
}
