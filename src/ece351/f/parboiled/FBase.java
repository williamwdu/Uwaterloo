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

package ece351.f.parboiled;

import org.parboiled.Rule;
import ece351.util.BaseParser351;

public abstract class FBase extends BaseParser351 {
	
    Rule Char() {
        return FirstOf(CharRange('a', 'z'),
                       CharRange('A', 'Z'));
    }
	
    Rule Digit() {
    	return CharRange('0', '9');
    }
	
    public Rule Keyword() {
        return FirstOf(AND(),
                       OR(),
                       NOT());
    }
	
    public Rule AND() {
        return Sequence(IgnoreCase("and"),
                        TestNot(FirstOf(Char(),Digit(), "_")));
    }

    public Rule OR() {
        return Sequence(IgnoreCase("or"),
                        TestNot(FirstOf(Char(),Digit(), "_")));
    }

    public Rule NOT() {
        return Sequence(IgnoreCase("not"),
                        TestNot(FirstOf(Char(),Digit(), "_")));
    }

}
