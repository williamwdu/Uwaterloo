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

package ece351.f.analysis;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ece351.f.FParser;
import ece351.f.ast.FProgram;
import ece351.util.BaseTest351;

@RunWith(Parameterized.class)
public class TestInlineIntermediateVars extends BaseTest351 {

	private final FProgram original, expected;
	
	public TestInlineIntermediateVars(final String o, final String e) {
		original = parse(e).simplify();
		expected = parse(e).simplify();
	}
	
	@Parameterized.Parameters
	public static Collection<Object[]> tests() {
		final Object[] a = new Object[] {
			new String[]{
					// original
					"x <= b; b <= c;",
					// expected
					"x <= c;"
			},
			new String[]{
					// original
					"x <= a or b; b <= c or d;",
					// expected
					"x <= a or c or d;"
			},
			new String[]{
					// original
					"x <= a or b; b <= c or d; c <= e and f;",
					// expected
					"x <= a or (e and f) or d;"
			},
		};
		final ArrayList<Object[]> r = new ArrayList<Object[]>(a.length);
		for (int i = 0; i < a.length; i++) {
			r.add((String[]) a[i]);
		}
		return r;
	}


	@Test
	public void test() {
		final FProgram actual = InlineIntermediateVariables.inline(original);
		assertEquals(expected, actual);
	}

	/** Helper. */
	private static FProgram parse(final String s) {
		return FParser.parse(new String[]{s, "-h"});
	}
}
