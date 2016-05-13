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

package ece351.w.svg;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ece351.util.BaseTest351;
import ece351.util.CommandLine;
import ece351.util.ExaminableProperties;
import ece351.util.TestInputs351;
import ece351.w.ast.WProgram;
import ece351.w.rdescent.WRecursiveDescentParser;

/** 
 * Test that your SVG files are equivalent to the staff SVG files.
 * Run this after TestW2SVG.
 */
@RunWith(Parameterized.class)
public final class TestW2SVG2W extends BaseTest351 {

	private final File studentsvgfile;

	public TestW2SVG2W(final File f) {
		this.studentsvgfile = f;
	}
	
	@Parameterized.Parameters
	public static Collection<Object[]> studentSVGfiles() {
		final Collection<Object[]> result = TestInputs351.studentSVGfiles();
		if (result == null || result.isEmpty()) {
			System.err.println("Couldn't find any svg files. Where are they? No tests run.");
		}
		return result;
	}

	@Test
	public void process() throws Exception {

		final String studentPath = studentsvgfile.getAbsolutePath();
		
		// read student SVG file
		System.out.println("reading      " + studentPath);
		final WSVG studentwsvg = WSVG.fromSVG(studentsvgfile.toURI(), TransformW2SVG.USE_DOM_XML_PARSER);

		// read staff SVG file
		final String staffPath = studentPath.replace("student.out", "staff.out");
		final URI staffURI = (new File(staffPath)).toURI();
		System.out.println("reading      " + staffPath);
		final WSVG staffwsvg = WSVG.fromSVG(staffURI, TransformW2SVG.USE_DOM_XML_PARSER);
		
		// infer W from the SVG and then compare the W

		// SVG -> W
		final WProgram staffwp = TransformSVG2W.transform(staffwsvg);
		final WProgram studentwp = TransformSVG2W.transform(studentwsvg);

		// read original W file
		final String originalWPath = studentPath.replace("student.out" + File.separator, "").replace(".svg", ".wave");
		final CommandLine c = new CommandLine(originalWPath);
		final String originalWtxt = c.readInputSpec();
		final WProgram originalWP = WRecursiveDescentParser.parse(originalWtxt);

		// Overall sanity check
		ExaminableProperties.checkAllUnary(originalWP);
		ExaminableProperties.checkAllUnary(studentwp);
		ExaminableProperties.checkAllUnary(staffwp);
		ExaminableProperties.checkAllBinary(staffwp, studentwp);
		ExaminableProperties.checkAllBinary(originalWP, staffwp);
		ExaminableProperties.checkAllBinary(originalWP, studentwp);
		ExaminableProperties.checkAllTernary(originalWP, staffwp, studentwp);

		// W equivalence checks
		assertTrue(originalWP.equivalent(studentwp));
		assertTrue(originalWP.equivalent(staffwp));
		assertTrue(staffwp.equivalent(studentwp));
	}

}
