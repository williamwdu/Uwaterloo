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

package ece351.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


public final class TestInputs351 {

	public static Collection<Object[]> javaFiles() {
		return oneD2twoD(Utils351.filesR("src", "^.*\\.java$"));
	}
	
	public static Collection<Object[]> formulaFiles() {
		return formulaFiles(".*");
	}
	
	public static Collection<Object[]> formulaFiles(String filter) {
		filter = "^" + filter + "\\.f$";
		return oneD2twoD(Utils351.files("tests/f", filter));
	}
	
	public static Collection<Object[]> formulaFiles(String filter1, String filter2) {
		filter1 = "^" + filter1 + "\\.f$";
		filter2 = "^" + filter2 + "\\.f$";
		return oneD2twoD(Utils351.files("tests/f", filter1), Utils351.files("tests/f", filter2));
	}
	
	public static Collection<Object[]> badFormulaFiles() {
		return oneD2twoD(Utils351.files("tests/f/ungrammatical", "^.*\\.f$"));
	}
	
	public static Collection<Object[]> waveFiles() {
		return oneD2twoD(Utils351.filesR("tests/wave", "^.*\\.wave$"));
	}
	
	public static Collection<Object[]> badWaveFiles() {
		return oneD2twoD(Utils351.filesR("tests/wave", "^.*\\.badwave$"));
	}

	public static Collection<Object[]> studentSVGfiles() {
		Object[] staffFiles = Utils351.files("tests/wave/student.out", "^.*\\.svg$");
		Object[] contribFiles = Utils351.files("tests/wave/contributed/student.out", "^.*\\.svg$");
		return oneD2twoD(staffFiles, contribFiles);
	}
	
	public static Collection<Object[]> vhdlFiles() {
		return vhdlFiles(".*");
	}
	
	public static Collection<Object[]> vhdlFiles(String filter) {
		filter = "^" + filter + "\\.vhd$";
		return oneD2twoD(Utils351.files("tests/vhdl", filter));
	}
	
	public static Collection<Object[]> badVhdlFiles() {
		return oneD2twoD(Utils351.files("tests/vhdl/ungrammatical", "^.*\\.vhd$"));
	}
	
	public static Collection<Object[]> badDefVhdlFiles() {
		return oneD2twoD(Utils351.files("tests/vhdl/undefined-var", "^.*\\.vhd$"));
	}
	
	public static Collection<Object[]> desugaredVhdlFiles() {
		return oneD2twoD(Utils351.files("tests/vhdl/staff.out/desugared", "^.*\\.vhd$"));
	}
	
	public static Collection<Object[]> elaboratedVhdlFiles() {
		return oneD2twoD(Utils351.files("tests/vhdl/staff.out/elaborated", "^.*\\.vhd$"));
	}
	
	public static Collection<Object[]> processSplitVhdlFiles() {
		return oneD2twoD(Utils351.files("tests/vhdl/staff.out/split", "^.*\\.vhd$"));
	}
	
	public static Collection<Object[]> synthesizedFFiles() {
		return oneD2twoD(Utils351.files("tests/vhdl/staff.out/synthesize", "^.*\\.f$"));
	}
	

	private final static Object[] EMPTY = new Object[0];
	public static Collection<Object[]> oneD2twoD(final Object[] in) {
		return oneD2twoD(in, EMPTY);
	}

	public static Collection<Object[]> oneD2twoD(final Object[] in1, final Object[] in2) {
		final Collection<Object[]> out = new ArrayList<Object[]>(in1.length + in2.length);
		for (final Object obj : in1) {
			out.add(new Object[] {obj});
		}
		for (final Object obj : in2) {
			out.add(new Object[] {obj});
		}
		return out;
	}

	
	public final static boolean knownIsomorphicFormulas(String f1, String f2) {
		f1 = f1.replace(".f", "");
		f2 = f2.replace(".f", "");
		final Set<String> s = ISOMORPHIC_FORMULAS.get(f1);
		if (s == null) {
			return false;
		} else {
			return s.contains(f2);
		}
	}
	
	public final static boolean knownEquivalentFormulas(String f1, String f2) {
		f1 = f1.replace(".f", "");
		f2 = f2.replace(".f", "");
		final Set<String> s = EQUIVALENT_FORMULAS.get(f1);
		if (s == null) {
			return false;
		} else {
			return s.contains(f2);
		}
	}
	
	public final static Map<String,Set<String>> ISOMORPHIC_FORMULAS;
	public final static Map<String,Set<String>> EQUIVALENT_FORMULAS;

	static {
		final Map<String,Set<String>> m = new TreeMap<String,Set<String>>();

		// isomorphic
		// GENERATED by GenerateKnownEquivalent
		m.put("cse2", s("cse4"));
		m.put("cse3", s("cse5"));
		m.put("cse4", s("cse2"));
		m.put("cse5", s("cse3"));
		m.put("ex02", s("ex04"));
		m.put("ex04", s("ex02"));
		m.put("ex05", s("ex11"));
		m.put("ex11", s("ex05"));
		m.put("opt0_left_parens", s("opt0_no_parens"));
		m.put("opt0_no_parens", s("opt0_left_parens"));
		m.put("opt1_and_true1", s("opt1_and_true2"));
		m.put("opt1_and_true2", s("opt1_and_true1"));
		m.put("opt1_or_false1", s("opt1_or_false2"));
		m.put("opt1_or_false2", s("opt1_or_false1"));
		m.put("opt2_and_false1", s("opt2_and_false2"));
		m.put("opt2_and_false2", s("opt2_and_false1"));
		m.put("opt2_or_true1", s("opt2_or_true2"));
		m.put("opt2_or_true2", s("opt2_or_true1"));
		m.put("opt4_and_complement1", s("opt4_and_complement2"));
		m.put("opt4_and_complement2", s("opt4_and_complement1"));
		m.put("opt4_or_complement1", s("opt4_or_complement2"));
		m.put("opt4_or_complement2", s("opt4_or_complement1"));
		m.put("opt6_and_or", s("opt6_and_or2"));
		m.put("opt6_and_or2", s("opt6_and_or"));
		m.put("opt6_or_and", s("opt6_or_no_paren"));
		m.put("opt6_or_no_paren", s("opt6_or_and"));
		m.put("z02", s("z06", "z07", "z08"));
		m.put("z06", s("z02", "z07", "z08"));
		m.put("z07", s("z02", "z06", "z08"));
		m.put("z08", s("z02", "z06", "z07"));
		m.put("z10", s("z11", "z12"));
		m.put("z11", s("z10", "z12"));
		m.put("z12", s("z10", "z11"));
		invert(m);
		ISOMORPHIC_FORMULAS = freeze(m);

		// equivalent
		// GENERATED by GenerateKnownEquivalent
		m.put("cse2", s("cse4"));
		m.put("cse3", s("cse5"));
		m.put("cse4", s("cse2"));
		m.put("cse5", s("cse3"));
		m.put("ex00", s("opt2_and_false1", "opt2_and_false2", "opt3_not_true", "opt4_and_complement1", "opt4_and_complement2"));
		m.put("ex01", s("opt2_or_true1", "opt2_or_true2", "opt3_not_false", "opt4_or_complement1", "opt4_or_complement2", "opt4_or_complement3"));
		m.put("ex02", s("ex04", "nary_or", "opt1_and_true1", "opt1_and_true2", "opt1_or_false1", "opt1_or_false2", "opt3_not_not", "opt5_and_dup", "opt5_or_dup", "opt6_and_or", "opt6_and_or2", "opt6_or_and", "opt6_or_no_paren"));
		m.put("ex04", s("ex02", "nary_or", "opt1_and_true1", "opt1_and_true2", "opt1_or_false1", "opt1_or_false2", "opt3_not_not", "opt5_and_dup", "opt5_or_dup", "opt6_and_or", "opt6_and_or2", "opt6_or_and", "opt6_or_no_paren"));
		m.put("ex05", s("ex11", "opt7_big1"));
		m.put("ex11", s("ex05", "opt7_big1"));
		m.put("nary_or", s("ex02", "ex04", "opt1_and_true1", "opt1_and_true2", "opt1_or_false1", "opt1_or_false2", "opt3_not_not", "opt5_and_dup", "opt5_or_dup", "opt6_and_or", "opt6_and_or2", "opt6_or_and", "opt6_or_no_paren"));
		m.put("opt0_left_parens", s("opt0_no_parens", "opt0_right_parens"));
		m.put("opt0_no_parens", s("opt0_left_parens", "opt0_right_parens"));
		m.put("opt0_right_parens", s("opt0_left_parens", "opt0_no_parens"));
		m.put("opt1_and_true1", s("ex02", "ex04", "nary_or", "opt1_and_true2", "opt1_or_false1", "opt1_or_false2", "opt3_not_not", "opt5_and_dup", "opt5_or_dup", "opt6_and_or", "opt6_and_or2", "opt6_or_and", "opt6_or_no_paren"));
		m.put("opt1_and_true2", s("ex02", "ex04", "nary_or", "opt1_and_true1", "opt1_or_false1", "opt1_or_false2", "opt3_not_not", "opt5_and_dup", "opt5_or_dup", "opt6_and_or", "opt6_and_or2", "opt6_or_and", "opt6_or_no_paren"));
		m.put("opt1_or_false1", s("ex02", "ex04", "nary_or", "opt1_and_true1", "opt1_and_true2", "opt1_or_false2", "opt3_not_not", "opt5_and_dup", "opt5_or_dup", "opt6_and_or", "opt6_and_or2", "opt6_or_and", "opt6_or_no_paren"));
		m.put("opt1_or_false2", s("ex02", "ex04", "nary_or", "opt1_and_true1", "opt1_and_true2", "opt1_or_false1", "opt3_not_not", "opt5_and_dup", "opt5_or_dup", "opt6_and_or", "opt6_and_or2", "opt6_or_and", "opt6_or_no_paren"));
		m.put("opt2_and_false1", s("ex00", "opt2_and_false2", "opt3_not_true", "opt4_and_complement1", "opt4_and_complement2"));
		m.put("opt2_and_false2", s("ex00", "opt2_and_false1", "opt3_not_true", "opt4_and_complement1", "opt4_and_complement2"));
		m.put("opt2_or_true1", s("ex01", "opt2_or_true2", "opt3_not_false", "opt4_or_complement1", "opt4_or_complement2", "opt4_or_complement3"));
		m.put("opt2_or_true2", s("ex01", "opt2_or_true1", "opt3_not_false", "opt4_or_complement1", "opt4_or_complement2", "opt4_or_complement3"));
		m.put("opt3_not_false", s("ex01", "opt2_or_true1", "opt2_or_true2", "opt4_or_complement1", "opt4_or_complement2", "opt4_or_complement3"));
		m.put("opt3_not_not", s("ex02", "ex04", "nary_or", "opt1_and_true1", "opt1_and_true2", "opt1_or_false1", "opt1_or_false2", "opt5_and_dup", "opt5_or_dup", "opt6_and_or", "opt6_and_or2", "opt6_or_and", "opt6_or_no_paren"));
		m.put("opt3_not_true", s("ex00", "opt2_and_false1", "opt2_and_false2", "opt4_and_complement1", "opt4_and_complement2"));
		m.put("opt4_and_complement1", s("ex00", "opt2_and_false1", "opt2_and_false2", "opt3_not_true", "opt4_and_complement2"));
		m.put("opt4_and_complement2", s("ex00", "opt2_and_false1", "opt2_and_false2", "opt3_not_true", "opt4_and_complement1"));
		m.put("opt4_or_complement1", s("ex01", "opt2_or_true1", "opt2_or_true2", "opt3_not_false", "opt4_or_complement2", "opt4_or_complement3"));
		m.put("opt4_or_complement2", s("ex01", "opt2_or_true1", "opt2_or_true2", "opt3_not_false", "opt4_or_complement1", "opt4_or_complement3"));
		m.put("opt4_or_complement3", s("ex01", "opt2_or_true1", "opt2_or_true2", "opt3_not_false", "opt4_or_complement1", "opt4_or_complement2"));
		m.put("opt5_and_dup", s("ex02", "ex04", "nary_or", "opt1_and_true1", "opt1_and_true2", "opt1_or_false1", "opt1_or_false2", "opt3_not_not", "opt5_or_dup", "opt6_and_or", "opt6_and_or2", "opt6_or_and", "opt6_or_no_paren"));
		m.put("opt5_or_dup", s("ex02", "ex04", "nary_or", "opt1_and_true1", "opt1_and_true2", "opt1_or_false1", "opt1_or_false2", "opt3_not_not", "opt5_and_dup", "opt6_and_or", "opt6_and_or2", "opt6_or_and", "opt6_or_no_paren"));
		m.put("opt6_and_or", s("ex02", "ex04", "nary_or", "opt1_and_true1", "opt1_and_true2", "opt1_or_false1", "opt1_or_false2", "opt3_not_not", "opt5_and_dup", "opt5_or_dup", "opt6_and_or2", "opt6_or_and", "opt6_or_no_paren"));
		m.put("opt6_and_or2", s("ex02", "ex04", "nary_or", "opt1_and_true1", "opt1_and_true2", "opt1_or_false1", "opt1_or_false2", "opt3_not_not", "opt5_and_dup", "opt5_or_dup", "opt6_and_or", "opt6_or_and", "opt6_or_no_paren"));
		m.put("opt6_or_and", s("ex02", "ex04", "nary_or", "opt1_and_true1", "opt1_and_true2", "opt1_or_false1", "opt1_or_false2", "opt3_not_not", "opt5_and_dup", "opt5_or_dup", "opt6_and_or", "opt6_and_or2", "opt6_or_no_paren"));
		m.put("opt6_or_no_paren", s("ex02", "ex04", "nary_or", "opt1_and_true1", "opt1_and_true2", "opt1_or_false1", "opt1_or_false2", "opt3_not_not", "opt5_and_dup", "opt5_or_dup", "opt6_and_or", "opt6_and_or2", "opt6_or_and"));
		m.put("opt7_big1", s("ex05", "ex11"));
		m.put("z02", s("z06", "z07", "z08"));
		m.put("z06", s("z02", "z07", "z08"));
		m.put("z07", s("z02", "z06", "z08"));
		m.put("z08", s("z02", "z06", "z07"));
		m.put("z10", s("z11", "z12"));
		m.put("z11", s("z10", "z12"));
		m.put("z12", s("z10", "z11"));
		invert(m);
		EQUIVALENT_FORMULAS = freeze(m);
	}

	private static Map<String,Set<String>> freeze(final Map<String,Set<String>> m) {
		final Map<String,Set<String>> n = new TreeMap<String,Set<String>>();
		for (final Map.Entry<String, Set<String>> e : m.entrySet()) {
			final Set<String> s = new TreeSet<String>(e.getValue());
			n.put(e.getKey(), s);
		}
		return Collections.unmodifiableMap(n);
	}
	
	private static void madd(final Map<String, Set<String>> m, final String s, final Set<String> ss) {
		final Set<String> existingSet = m.get(s);
		if (existingSet == null) {
			m.put(s, ss);
		} else {
			existingSet.addAll(ss);
		}
	}
	
	private static void invert(final Map<String, Set<String>> m) {
		final Map<String,Set<String>> toAdd = new TreeMap<String,Set<String>>();

		// make sure the inverse keys exist
		for (final Map.Entry<String, Set<String>> e : m.entrySet()) {
			final String k = e.getKey();
			final Set<String> s = e.getValue();
			for (final String k2 : s) {
				if (!m.containsKey(k2)) {
					toAdd.put(k2, s(k));
				}
			}
		}
		m.putAll(toAdd);

		// make sure the inverse sets are complete
		for (final Map.Entry<String, Set<String>> e : m.entrySet()) {
			final String k = e.getKey();
			final Set<String> s = e.getValue();
			for (final String k2 : s) {
				final Set<String> s2 = m.get(k2);
				s2.add(k);
				s2.addAll(s);
			}
		}
	}

	private static Set<String> s(final String...args) {
		final Set<String> s = new TreeSet<String>();
		for (final String a : args) {
			s.add(a);
		}
		return s;
	}

}
