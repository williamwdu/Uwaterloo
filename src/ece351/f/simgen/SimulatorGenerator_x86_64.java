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

import java.io.PrintWriter;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import ece351.common.ast.AndExpr;
import ece351.common.ast.AssignmentStatement;
import ece351.common.ast.ConstantExpr;
import ece351.common.ast.EqualExpr;
import ece351.common.ast.Expr;
import ece351.common.ast.NAndExpr;
import ece351.common.ast.NOrExpr;
import ece351.common.ast.NaryAndExpr;
import ece351.common.ast.NaryOrExpr;
import ece351.common.ast.NotExpr;
import ece351.common.ast.OrExpr;
import ece351.common.ast.VarExpr;
import ece351.common.ast.XNOrExpr;
import ece351.common.ast.XOrExpr;
import ece351.common.visitor.PostOrderExprVisitor;
import ece351.f.analysis.DepthCounter;
import ece351.f.analysis.DetermineInputVars;
import ece351.f.ast.FProgram;
import ece351.w.ast.WProgram;

public class SimulatorGenerator_x86_64 extends PostOrderExprVisitor {

	/** Where the generated code gets written to. */
	private PrintWriter out = new PrintWriter(System.out);
	
	/**
	 * Each AssignmentStatement uses a different subset of the input variables.
	 * So each AssignmentStatement needs its own mapping of input variables to registers.
	 */
	private SortedMap<AssignmentStatement, SortedMap<VarExpr, String>> registerAllocation;
	

	/** The current level of indenting. */
	private String indent = "";
	
	/** The current AssignmentStatement. */
	private AssignmentStatement currentAStmt;
	
	/** 
	 * A comparator to sort AssignmentStatements by their output variable,
	 * so that we can store them in a TreeMap (which must be able to sort its elements).
	 */
	private static final Comparator<AssignmentStatement> ASTMT_COMPARATOR = new Comparator<AssignmentStatement>(){

		@Override
		public int compare(AssignmentStatement s1, AssignmentStatement s2) {
			return s1.outputVar.compareTo(s2.outputVar);
		}};


	/** 
	 * Generate x64 assembly to evaluate fprog on wprog.
	 * @param wprog W input to the fprog
	 * @param fprog the F program to be evaluated
	 * @param out where to write the generated x64 assembly code
	 */
	public void generate(final WProgram wprog, final FProgram fprog, final PrintWriter out)
	{
		this.out = (out == null) ? this.out : out;

		// overall header
		genHeader();
		
		// allocate storage to remember how registers are allocated
		registerAllocation = new TreeMap<AssignmentStatement, SortedMap<VarExpr, String>>(ASTMT_COMPARATOR);

		// allocate registers + generate assembly procedure for each formula
		for (final AssignmentStatement stmt : fprog.formulas)
		{
// TODO: short code snippet
throw new ece351.util.Todo351Exception();
		}
		
		// header for main
		genFuncHeader("main", 0);
		
		// evaluate each formula at each time step
		for (final AssignmentStatement stmt : fprog.formulas)
		{
			printIdentifier(stmt.outputVar.identifier);
			for (int t = 0 ; t < wprog.timeCount() ; t++)
			{
				genx86PutChar(' ');
				// call the procedure to evaluate this formula at this time
				// first load the input variables into the registers
				// then output the resulting value
// TODO: short code snippet
throw new ece351.util.Todo351Exception();
			}
			genx86PutChar(';');
			genx86PutChar('\n');
		}
		println("");

		// footer for main
		genFuncFooter();
	}

	/**
	 * Generate x64 assembly to evaluate a formula.
	 * @param stmt the formula to be evaluated
	 */
	public void generate(final AssignmentStatement stmt)
	{
		currentAStmt = stmt;
		
		final String funcName = "out_" + currentAStmt.outputVar;
		// use DepthCounter to measure the stack depth needed; might need to add a constant
// TODO: short code snippet
throw new ece351.util.Todo351Exception();

		// generate header, traverse expr, generate footer
// TODO: short code snippet
throw new ece351.util.Todo351Exception();
	}
	
	/** Movq constant value to %rax and pushq it on the stack. */
	@Override
	public Expr visitConstant(final ConstantExpr e) {
// TODO: short code snippet
throw new ece351.util.Todo351Exception();
	}

	/** Pushq a variable onto the stack from its allocated register. */
	@Override
	public Expr visitVar(final VarExpr e) {
// TODO: short code snippet
throw new ece351.util.Todo351Exception();
	}

	/** Expect operand in %rax. What operator to use? Think bitwise. */
	@Override
	public Expr visitNot(final NotExpr e) {
// TODO: short code snippet
throw new ece351.util.Todo351Exception();
	}

	/** Expect operands in %rax and %rbx. */
	@Override
	public Expr visitAnd(final AndExpr e) {
// TODO: short code snippet
throw new ece351.util.Todo351Exception();
	}

	/** Expect operands in %rax and %rbx. */
	@Override
	public Expr visitOr(final OrExpr e) {
// TODO: short code snippet
throw new ece351.util.Todo351Exception();
	}

	@Override public Expr visitNaryAnd(final NaryAndExpr e) { throw new UnsupportedOperationException(); }
	@Override public Expr visitNaryOr(final NaryOrExpr e) { throw new UnsupportedOperationException(); }
	@Override public Expr visitNOr(final NOrExpr e) { throw new UnsupportedOperationException(); }
	@Override public Expr visitXOr(final XOrExpr e) { throw new UnsupportedOperationException(); }
	@Override public Expr visitXNOr(final XNOrExpr e) { throw new UnsupportedOperationException(); }
	@Override public Expr visitNAnd(final NAndExpr e) { throw new UnsupportedOperationException(); }
	@Override public Expr visitEqual(final EqualExpr e) { throw new UnsupportedOperationException(); }

	private void loadRegisters(final WProgram wprog, final int t, final AssignmentStatement stmt) {
		final Map<VarExpr, String> mvars_regs = registerAllocation.get(stmt);
// TODO: longer code snippet
throw new ece351.util.Todo351Exception();
	}

	private void allocateRegisters(final AssignmentStatement stmt)
	{
		final Set<String> inputs = DetermineInputVars.inputVars(stmt);
		if (inputs.size() > 8)
		{
			throw new UnsupportedOperationException("can't process formula with more than 8 input variables");
		}
		
// TODO: longer code snippet
throw new ece351.util.Todo351Exception();
	}
	
	private void genHeader()
	{
		indent();
		indent();
		println(".text");
	}
	
	private void genFuncHeader(final String name, final int depth)
	{
		println(".globl\t" + name);
		println(".type\t" + name + ", @function");
		outdent();
		outdent();
		println(name + ":");
		indent();
		indent();
		println("pushq", "%rbp");
		println("movq", "%rsp", "%rbp");
		int depthNeeded = 8*depth;
		
		if (depthNeeded != 0) 
			println("subq", "$"+depthNeeded, "%rsp");
	}
	
	private void genFuncFooter()
	{
		println("popq", "%rax");
		println("leave");
		println("ret");
	}
	
	private void outputValue() {
		// movq value to output into %rdi
// TODO: short code snippet
throw new ece351.util.Todo351Exception();
		println("add", "$48", "%rdi"); // char for 0 is ASCII 48
		println("call", "putchar");
	}

	private void functionCall(final AssignmentStatement stmt) {
// TODO: short code snippet
throw new ece351.util.Todo351Exception();
	}

	private void printIdentifier(final String identifier) {
		for (int i = 0 ; i < identifier.length() ; i++)
		{
			char ch = identifier.charAt(i);
			genx86PutChar(ch);
		}	
		genx86PutChar(':');
	}

	private void genx86PutChar(final char ch) {
		println("movq", "$" + (int)ch, "%rdi");
		println("call", "putchar");
	}
	
	private void println(final String s) {
		out.print(indent);
		out.println(s);
	}
	
	public void println(final String command, final String arg1)
	{
		println(command + "\t" + arg1);
	}
	
	public void println(final String command, final String arg1, final String arg2)
	{
		println(command + "\t" + arg1 + ", " + arg2);
	}
	
	private void indent() {
		indent = indent + "    ";
	}
	
	private void outdent() {
		indent = indent.substring(0, indent.length() - 4);
	}

}
