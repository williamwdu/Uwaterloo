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
		// TODO: short code snippet
		regexs.add("A: [0 |1 ]+;\\s*");
		// r3.wave
		// Copy the regex from above and paste it here.
		// Generalize the regex to allow whitespace between the last signal and the semi-colon.
		regexs.add("A: [0 |1 ]+;\\s*");
		// r4.wave
		// Copy the regex from above and paste it here.
		// Generalize the regex to allow multi-character pin names.
// TODO: short code snippet
		regexs.add("[A-Z][[A-Z]|[0-9]|'_']*: [0 |1 ]+;\\s*");
		// r5.wave
		// Copy the regex from above and paste it here.
		// Generalize the regex to allow lower case in pin names.
		regexs.add("[A-Za-z][[A-Z]|[0-9]|'_']*: [0 |1 ]+;\\s*");
		// r6.wave
		// Copy the regex from above and paste it here.
		// Generalize the regex to allow multiple spaces between values.
// TODO: short code snippet
		regexs.add("[[A-Za-z][[A-Z]|[0-9]|'_']*: [0 |1 ]+;\\s*]+");
		// r7.wave
		// Copy the regex from above and paste it here.
		// Generalize the regex for whitespace again.
		regexs.add("[[A-Za-z][[A-Z]|[0-9]|'_']*: [0 |1 ]+;\\s*]+");
		// r8.wave
		// Copy the regex from above and paste it here.
		// Generalize the regex to allow multiple pins
		regexs.add("[[A-Za-z][[A-Z]|[0-9]|'_']*: [0 |1 ]+;\\s*]+");
	};
	
}
