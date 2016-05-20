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

package ece351.f.test;

import ece351.f.ast.FProgram;
import ece351.util.ExaminableProperties;

/**
 * Testing equations: 
 * 		AST.equals(parse(prettyprint(AST)))
 * 		AST.isomorphic(parse(prettyprint(AST)))
 * 		AST.equivalent(parse(prettyprint(AST)))
 * 
 * Note that if equals/isomorphic/equivalent always return true then this
 * equation will succeed regardless of whether the parser and pretty printer are
 * correct (as long as they don't throw exceptions).
 * 
 * TestObjectContractF checks that equals/isomorphic/equivalent are doing
 * the right thing without engaging the parser and pretty printer.
 * 
 * 
 */
public abstract class AbstractTestFParser extends AbstractTestF {

	protected abstract FProgram parse(final String s);
	
	@Override
	protected void test(final String name, final FProgram fp1) {
		// pretty-print the input AST
		final String pp = fp1.toString();
		// parse the pretty-print
		final FProgram fp2 = parse(pp);
		// check that they are the same
		AbstractTestFParserBasic.compareExpectSame(fp1, fp2);
		// check object contract
		ExaminableProperties.checkAllUnary(fp1);
		ExaminableProperties.checkAllUnary(fp2);
		ExaminableProperties.checkAllBinary(fp1, fp2);
		// success!
		System.out.println("accepted, as expected:  " + name);
	}

}
