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
import static org.junit.Assert.assertNotEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ece351.common.ast.AssignmentStatement;
import ece351.f.FParser;
import ece351.f.analysis.DetermineInputVars;
import ece351.f.ast.FProgram;
import ece351.util.BaseTest351;
import ece351.util.CommandLine;
import ece351.util.ExaminableProperties;
import ece351.util.TestInputs351;
import ece351.w.ast.WProgram;
import ece351.w.ast.Waveform;
import ece351.w.parboiled.WParboiledParser;

@RunWith(Parameterized.class)
public class TestSimulatorGenerator_x86_64 extends BaseTest351 {

	/** The test parameter. */
	protected final File input;
	
	// computed from the test parameter in computeFileNames()
	protected String waveName;
	protected String outputWaveName;
	protected String staffWavePath;
	protected String sourcePath;

	public final static String sep = File.separator;

	public TestSimulatorGenerator_x86_64(final File f) {
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
		
		//GetTestWprogram
		final CommandLine staffWaveCmd = new CommandLine(staffWavePath);
		final String StaffWave = staffWaveCmd.readInputSpec();
		final WProgram fullStaffWProgram = WParboiledParser.parse(StaffWave);
		
		//Create Reference WPrograms with only inputs/outputs
		WProgram staffWProgram = new WProgram();
		WProgram onlyInputWProgram = new WProgram();
		Set<String> fprogOutputs = outputVars(original);
		for (Waveform wave : fullStaffWProgram.waveforms)
		{
			if (fprogOutputs.contains(wave.name))
				staffWProgram = staffWProgram.append(wave);
			else
				onlyInputWProgram = onlyInputWProgram.append(wave);
		}
		
		//Generate the x86 output
		final StringWriter sw = new StringWriter();
		final SimulatorGenerator_x86_64 sg = new SimulatorGenerator_x86_64();
		sg.generate(onlyInputWProgram, original, new PrintWriter(sw));
		sw.close();
		final String x86Code = sw.toString();
		System.out.println("Output:");
		System.out.println(x86Code);
		//Write to file
		final PrintWriter pw = new PrintWriter(new FileWriter(sourcePath));
		pw.write(x86Code);
		pw.close();
		

		
		//MACHINE SPECIFIC CODE TO COMPILE TO x86_64, NOT SURE HOW YOU WANT TO HANDLE THIS, MAYBE BRING YOUR OWN x86 JAVA COMPILER?
		//LOOK INTO ASMJIT
		//JUST GONNA HACK FOR NOW AND CALL GCC DIRECT
		//First step, turn into object code
		Path currentRelativePath = Paths.get("");
		String execPath = currentRelativePath.toAbsolutePath().toString();
		String relPath = sourcePath.replace(execPath, ".");
		String outObj = relPath.replace(".s", ".o");
		
		String objCommand = "gcc -g -c -o " + outObj + " " + relPath;
		System.out.println("Executing:");
		System.out.println(objCommand);
		final int assemblingResult = compileWithError(objCommand);
		assertNotEquals("assembling failed", -1, assemblingResult);
		
		String outExecutable = relPath.replace(".s", "");
		String linkCommand = "gcc -o " + outExecutable + " " + outObj;
		System.out.println(linkCommand);
		final int linkResult = compileWithError(linkCommand);
		assertNotEquals("linking failed", -1, linkResult);
		
		//Execute Created File
		String execCommand = outExecutable;
		Process execProc = Runtime.getRuntime().exec(execCommand);
		BufferedReader execOutput = new BufferedReader(new InputStreamReader(execProc.getInputStream()));
		String genOutput = "", genLine;
		while ((genLine = execOutput.readLine()) != null) { genOutput += genLine + "\n"; }
		
		System.out.println("Student Output:");
		System.out.println(genOutput);
		
		WProgram studentW = WParboiledParser.parse(genOutput);
		
		assertTrue("wave outputs differ for simulation of " + inputSpec,
				staffWProgram.isomorphic(studentW));

		ExaminableProperties.checkAllUnary(staffWProgram);
		ExaminableProperties.checkAllUnary(studentW);
		ExaminableProperties.checkAllBinary(staffWProgram, studentW);

		// success!
		System.out.println("Success! " + inputSpec);
		
	}
	
	private Set<String> outputVars(FProgram original) {
		Set<String> vals = new TreeSet<String>();
		for (AssignmentStatement stmt : original.formulas)
		{
			vals.add(stmt.outputVar.identifier);
		}
		return vals;
	}

	protected int compileWithError(String command) throws IOException
	{
		Process compileProc = Runtime.getRuntime().exec(command);
		InputStream objErrorStream = compileProc.getErrorStream();
		BufferedReader objErrorStreamReader = new BufferedReader(new InputStreamReader(objErrorStream));
		String objErrorMessages = "", objErrorLine = "";
		while ((objErrorLine = objErrorStreamReader.readLine()) != null) 
		{
			objErrorMessages += objErrorLine + "\n";
		}
		
		if (!objErrorMessages.isEmpty())
		{
			System.out.println("Error during executing \"" + command + "\"");
			System.out.println(objErrorMessages);
			return -1;
		}
		return 0;
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
				.replace(".f", "_x86_64.wave");
		
		staffWavePath = inputSpec
				.replace(sep + "f" + sep, sep + "f" + sep + "staff.out" + sep + "simulator" + sep)
				.replace(".f", ".wave");
		assert ((new File(staffWavePath)).exists()) : "staff wave file does not exist: " + staffWavePath;

		sourcePath = inputSpec.replace(sep + "f" + sep, sep + "f" + sep + "student.out" + sep + "simulator" + sep + "Simulator_").replace(".f", "_x86_64.s");
		final File f = new File(sourcePath);
		f.getParentFile().mkdirs();

		System.out.println("waveName:       " + waveName);
		System.out.println("outputWaveName: " + outputWaveName);
		System.out.println("staffWavePath:  " + staffWavePath);
		System.out.println("sourcePath:     " + sourcePath);
	}
}
