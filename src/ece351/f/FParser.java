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

package ece351.f;

import ece351.f.ast.FProgram;
import ece351.f.parboiled.FParboiledParser;
//import ece351.f.parboiled.FParboiledParser;
import ece351.f.rdescent.FRecursiveDescentParser;
import ece351.util.CommandLine;
import ece351.util.Lexer;

public final class FParser {
	
    public static FProgram parse(final String[] args) {
    	final CommandLine c = new CommandLine(args);
    	return parse(c);
    }

	public static FProgram parse(final CommandLine c) {
		if (c.handparser) {
    		return handParse(c.readInputSpec());
    	} else {
    		return parboiledParse(c.readInputSpec());
    	}
	}
    
    private static FProgram handParse(final String input) {
        final Lexer lexer = new Lexer(input);
        final FRecursiveDescentParser parser = new FRecursiveDescentParser(lexer);
        return parser.parse();
    }
    
    private static FProgram parboiledParse(final String input) {
        return FParboiledParser.parse(input);
    }
}
