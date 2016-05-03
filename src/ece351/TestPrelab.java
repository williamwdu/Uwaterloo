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

package ece351;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.parboiled.errors.ErrorUtils.printParseErrors;

import java.io.File;
import java.io.FileReader;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.junit.Test;
import org.parboiled.BaseParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;

import ece351.util.Todo351Exception;

public class TestPrelab {

	@Test
	public void testJUnitConfiguration() {
		boolean x = false;
		assert x=true : "the assignment will always succeed, but will only be executed if assertions are enabled";
		assertTrue("JUnit not adding -ea to the VM arguments for new launch configurations", x);
	}
	
	public static boolean areAssertionsEnabled() {
		boolean x = false;
		assert x=true : "the assignment will always succeed, but will only be executed if assertions are enabled";
		return x;
	}

	@Test
	public void testEclipseJDKConfiguration() {
		final JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
		assertNotNull("Eclipse is running with a JRE rather than a JDK", javac);
	}

	/**
	 * Check that Parboiled is installed properly.
	 */
	@Test 
	public void testParboiledInstall() {
		final PrelabTestParser parser = Parboiled.createParser(PrelabTestParser.class);
    	final ParsingResult<?> result = new ReportingParseRunner<Object>(parser.S()).run("1");
    	if (result.hasErrors()) {
    		System.out.println("Parse errors:");
    		System.out.println(printParseErrors(result));
    		throw new RuntimeException("parse error(s) encountered");
    	}
	}
	
	/**
	 * This test intended to error/fail. The point is to learn how to read
	 * the nested stack trace.
	 */
    @Test
    public void testNestedException() {
        try {
        	final File f = new File("non-existant-file");
        	final FileReader r = new FileReader(f);
        	r.close();
        } catch (final Exception e) {
            throw new RuntimeException("this is not the exception that you are looking for", e);
        }
    }

    /**
     * Wherever you see a Todo351Exception being thrown you should
     * replace it with working code.
     */
    @Test
    public void testTodo351Exception() {
    	throw new Todo351Exception("replace these exceptions with working code");
    }
}

class PrelabTestParser extends BaseParser<Object> {
	public Rule S() {
		return AnyOf("10");
	}
}
