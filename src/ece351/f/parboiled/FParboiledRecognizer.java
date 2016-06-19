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

import ece351.common.ast.Constants;
import ece351.util.CommandLine;

//Parboiled requires that this class not be final
public /*final*/ class FParboiledRecognizer extends FBase implements Constants {

	
	public static void main(final String... args) {
		final CommandLine c = new CommandLine(args);
    	process(FParboiledRecognizer.class, c.readInputSpec());
    }

	@Override
	public Rule Program() {
		//System.out.println("Program");
		return Sequence(
				OneOrMore(Formula()),
				EOI);
	}
	public Rule Formula(){
		//System.out.println("Formula");
		return Sequence(
				W0(),
    			Var(),
    			W0(),
    			"<=",
    			W0(),
    			Expr(),
    			W0(),
    			";",
    			W0()
				);
	}
	public Rule Expr(){
		//System.out.println("Expr");
		return Sequence(
				W0(),
    			Term(),
    			W0(),
    			ZeroOrMore(Sequence(W0(),"or",W0(),Term()))
				);
	}
	public Rule Term(){
		//System.out.println("Term");
		return Sequence(
				W0(),
    			Factor(),
    			W0(),
    			ZeroOrMore(Sequence(W0(),"and",W0(),Factor()))
				);
	}
	public Rule Factor(){
		//System.out.println("Factor");
		return Sequence(
				W0(),
				FirstOf(
						Sequence(W0(),"not",W0(),Factor()),
						Sequence(W0(),"(",W0(),Expr(),W0(),")"),
						Var(),
						Constant()
						)
				);
	}
	public Rule Constant(){
		//System.out.println("Constant");
		return 	FirstOf("'0'","'1'");
	}
	public Rule Var(){
		//System.out.println("Var");
		return Sequence(
				W0(),
				Id(),
				W0()
				);
	}
    public Rule Id() {
    //System.out.println("Id");
	return FirstOf(CharRange('a' , 'z' ), CharRange('A', 'Z'));
    }

	

}
