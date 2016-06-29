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

package ece351.f.simgen;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Set;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ece351.f.FParser;
import ece351.f.analysis.DetermineInputVars;
import ece351.f.ast.FProgram;
import ece351.util.BaseTest351;
import ece351.util.CommandLine;
import ece351.util.Debug;
import ece351.util.ExaminableProperties;
import ece351.util.TestInputs351;
import ece351.w.ast.WProgram;
import ece351.w.parboiled.WParboiledParser;


@RunWith(Parameterized.class)
public class TestSimulatorGenerator extends BaseTest351 {

	/** The test parameter. */
	protected final File input;
	
	// computed from the test parameter in computeFileNames()
	protected String waveName;
	protected String outputWaveName;
	protected String staffWavePath;
	protected String sourcePath;

	public final static String sep = File.separator;

	public TestSimulatorGenerator(final File f) {
		this.input = f;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> files() {
		return TestInputs351.formulaFiles();
	}
	
	@Test
	public void simgen() throws IOException {
		final String inputSpec = input.getAbsolutePath();
		if (inputSpec.contains("jvarty")) {
			// these files have two many variables in them
			// inconvenient to generate the appropriate wave inputs
			return;
		}
		if (inputSpec.contains("opt4") || inputSpec.contains("opt5")) {
			// these optimizations were harder,
			// so some people might not have done them
			return;
		}
		
		final CommandLine c = new CommandLine("-p", inputSpec);
		final String input = c.readInputSpec();
		System.out.println("processing " + inputSpec);
		System.out.println("input: " + inputSpec);
		System.out.println(input);
		
		// parse from the F file
		final FProgram original = FParser.parse(c);
		assertTrue(original.repOk());
		computeFileNames(inputSpec, original);

		// generate the Java output
		final StringWriter sw = new StringWriter();
		final SimulatorGenerator sg = new SimulatorGenerator();
		sg.generate(c.getInputName(), original, new PrintWriter(sw));
		sw.close();
		final String javasim = sw.toString();
		System.out.println("output:");
		System.out.println(javasim);
		// write generated Java source to file
		final File f = new File(sourcePath);
		final PrintWriter pw = new PrintWriter(new FileWriter(sourcePath));
		pw.write(javasim);
		pw.close();

		// compile the generated Java file
		final JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
		assertTrue("Could not get programmatic access to javac compiler, see lab manual section 0", javac != null);
		int compileResult = javac.run(null, null,null, sourcePath);
		assertTrue("Compilation of generated Java program failed.", compileResult==0);

		// remove the old output wave file
		final File owf = new File(outputWaveName);
		if (owf.exists()) {
			owf.delete();
		}
		
		// test the compiled output
		final String classPath = "file:///" + f.getParent() + sep;
		final URLClassLoader loader = new URLClassLoader(new URL[]{new URL(classPath)});
		Class<?> simulatorClass = null;
		try {
			final String className = f.getName().replace(".java", "");
			simulatorClass = Class.forName(className, true, loader);
			simulatorClass.getMethod("main", new Class[] { String[].class })
					.invoke(simulatorClass.newInstance(),
							new Object[] { new String[] { waveName, "-f", outputWaveName } });
			
		} catch (ClassNotFoundException e) {
			Debug.barf(e.toString());
		} catch (IllegalAccessException e) {
			Debug.barf(e.toString());
		} catch (IllegalArgumentException e) {
			Debug.barf(e.toString());
		} catch (InvocationTargetException e) {
			//special case since we're getting a exception during execution of main(),
			//and "Invocation Target Exception" is rather unhelpful.
			Debug.barf(e.getCause().getMessage());
		} catch (NoSuchMethodException e) {
			Debug.barf(e.toString());
		} catch (SecurityException e) {
			Debug.barf(e.toString());
		} catch (InstantiationException e) {
			Debug.barf(e.toString());
		} 
		
		// compare the computed wave outputs
		final CommandLine csw = new CommandLine(outputWaveName);
		final WProgram studentW = WParboiledParser.parse(csw.readInputSpec());

		final CommandLine staffWaveCmd = new CommandLine(staffWavePath);
		final String StaffWave = staffWaveCmd.readInputSpec();
		final WProgram staffWProgram = WParboiledParser.parse(StaffWave);

		// check if staff/student programs are isomorphic
		assertTrue("wave outputs differ for simulation of " + inputSpec,
				staffWProgram.isomorphic(studentW));

		ExaminableProperties.checkAllUnary(staffWProgram);
		ExaminableProperties.checkAllUnary(studentW);
		ExaminableProperties.checkAllBinary(staffWProgram, studentW);

		// success!
		System.out.println("Success! " + inputSpec);
	}

	protected void computeFileNames(final String inputSpec, final FProgram fp) {
		// determine the name of the wave input to use for this formula
		final Set<String> inputVars = DetermineInputVars.inputVars(fp);
		final StringBuilder waveNameBuilder = new StringBuilder("tests/wave/");
		for (final String s : inputVars) {
			waveNameBuilder.append(s);
		}
		if (inputVars.isEmpty()) {
			waveNameBuilder.append("r1");
		}
		waveNameBuilder.append(".wave");
		waveName = waveNameBuilder.toString();
		assert (new File(waveName)).exists() : "input wave file doesn't exist: " + waveName;

		outputWaveName = inputSpec
				.replace(sep + "f" + sep, sep + "f" + sep + "student.out" + sep + "simulator" + sep)
				.replace(".f", ".wave");
		
		staffWavePath = inputSpec
				.replace(sep + "f" + sep, sep + "f" + sep + "staff.out" + sep + "simulator" + sep)
				.replace(".f", ".wave");
		assert ((new File(staffWavePath)).exists()) : "staff wave file does not exist: " + staffWavePath;

		sourcePath = inputSpec.replace(sep + "f" + sep, sep + "f" + sep + "student.out" + sep + "simulator" + sep + "Simulator_").replace(".f", ".java");
		final File f = new File(sourcePath);
		f.getParentFile().mkdirs();

	}

}
