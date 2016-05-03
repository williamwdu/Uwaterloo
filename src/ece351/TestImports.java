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
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Parameterized;

import ece351.util.TestInputs351;
import ece351.util.Utils351;

@RunWith(Parameterized.class)
public class TestImports {

	private final File f;

	public TestImports(final File f) {
		this.f = f;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> files() {
		return TestInputs351.javaFiles();
	}

	private final static String[] strings = new String[]{
		"org.junit",
		"junit",
		"java", 
		"org.parboiled", 
		"ece351", 
		"kodkod",
		"edu.mit.csail.sdg",
		"org.w3c.dom",
		"javax.xml.parsers",
		"org.apache.batik",
		"javax.tools",
		"org.objectweb.asm",
	};
	
	private final static Pattern[] patterns;
	static {
		patterns = new Pattern[strings.length];
		for (int i = 0; i < strings.length; i++) {
			patterns[i] = Pattern.compile("import.*" + strings[i] + "\\..*;");
		}
	}
	
	@Test
	public void test() {
		final List<String> lines = Utils351.readFileLines(f.getAbsolutePath());
		for (final String raw : lines) {
			if (raw.startsWith("import ")) {
				// strip off trailing comments and whitespace
				final String line = raw.replaceAll("//.*", "").replaceAll("/\\*.*\\*/", "").replaceAll("; *", ";");
				boolean legal = false;
				for (final Pattern p : patterns) {
					legal |= p.matcher(line).matches();
				}
				final String msg = "illegal import in \n" + f.getName() + "\n" + line + "\n";
				assertTrue(msg, legal);
			}
		}
	}

	public static boolean check() {
		final JUnitCore core = new JUnitCore();
		final Result r = core.run(TestImports.class);
		if (!r.wasSuccessful()) {
			System.err.println("Problem detected with imports. Run TestImports for details.");
		}
		for (final Failure f : r.getFailures()) {
			System.err.println(f.getMessage());
			System.err.println();
		}
		return r.wasSuccessful();
	}
	
	public static void main(final String[] args) {
		check();
	}
}
