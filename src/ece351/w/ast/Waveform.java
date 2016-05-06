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

package ece351.w.ast;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import org.parboiled.common.ImmutableList;

import ece351.util.Examinable;

public final class Waveform implements Examinable {
	public final String name;
	public final ImmutableList<String> bits;
	
	/**
	 * Constructs a Waveform object with a name give by parameter name and a
	 * list of bits given by parameter bits
	 * 
	 * @param bits
	 * @param name
	 */
	public Waveform(final ImmutableList<String> bits, final String name) {
		this.name = name;
		this.bits = bits;
	}

	/**
	 * Constructs a new Waveform with an empty list of bits and with the name
	 * "anon"
	 * */
	public Waveform() {
		this.name = "<anon>";
		this.bits = ImmutableList.of();
	}

	/**
	 * Constructs a Waveform object with the name given by parameter name and a
	 * empty list of bits
	 * 
	 * @param name
	 */
	public Waveform(final String name) {
		this.name = name;
		this.bits = ImmutableList.of();
	}

	public boolean repOk() {
		assert name != null;
		assert !name.isEmpty();
		assert bits != null;
		assert !bits.isEmpty();
		for (final String b : bits) {
			assert b != null;
			assert !b.isEmpty();
			assert LEGAL_BITS.contains(b);
		}
		return true;
	}

	public final static SortedSet<String> LEGAL_BITS;
	static {
		final SortedSet<String> s = new TreeSet<String>();
		s.add("0");
		s.add("1");		
		s.add("U");
		LEGAL_BITS = Collections.unmodifiableSortedSet(s);
	}
	
	/**
	 * Construct a new Waveform with all the bits this Waveform has plus the parameter.
	 * Usage: waveform = waveform.append(bit);
	 *
	 * @param bit
	 *            the bit you want to append to the list
	 * @return Returns a new Waveform with parameter bit attached to the
	 *         original Waveform
	 */
	public Waveform append(final String bit) {
		return new Waveform(bits.append(bit), name);
	}
	
	public Waveform rename(final String n) {
		return new Waveform(bits, n);
	}
	
	@Override
	public String toString() {
		// STUB: return "TODO"; // TODO: Waveform.toString()
// TODO: short code snippet
throw new ece351.util.Todo351Exception();
	}

	/**
	 * If we override equals() then we must override hashCode().
	 */
	@Override
	public int hashCode() {
		int hash = 17;
		hash = hash * 13 + name.hashCode();
		hash = hash * 13 + bits.hashCode();
		return hash;
	}

	/**
	 * Waveform objects are immutable, so compare state.
	 */
	@Override
	public boolean equals(final Object obj) {
		// basics
		if (obj == null) return false;
		if (obj == this) return true;
		if (!obj.getClass().equals(this.getClass())) return false;		
		final Waveform other = (Waveform) obj;
		assert repOk();
		assert other.repOk();
		// compare fields
		if (!name.equals(other.name)) return false;
		if (!bits.equals(other.bits)) return false;
		// no differences found, so must be same
		return true;
	}

	/**
	 * The order of the bits always matters, so we simply
	 * define isomorphic to be equals.
	 */
	@Override
	public boolean isomorphic(final Examinable obj) {
		return equals(obj);
	}

	/**
	 * Defined to be equals().
	 */
	@Override
	public boolean equivalent(final Examinable obj) {
		return equals(obj);
	}

}
