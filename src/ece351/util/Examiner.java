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

import java.util.BitSet;
import java.util.List;

import org.parboiled.common.ImmutableList;

public enum Examiner {

	Equals {
		public boolean examine(final Examinable a, final Examinable b) {
			if (a == null && b == null) return true;
			if (a == null) return false;
			if (b == null) return false;
			return a.equals(b);
		}

	},
	
	Equivalent {
		public boolean examine(final Examinable a, final Examinable b) {
			if (a == null && b == null) return true;
			if (a == null) return false;
			if (b == null) return false;
			return a.equivalent(b);
		}
	},

	Isomorphic {
		public boolean examine(final Examinable a, final Examinable b) {
			if (a == null && b == null) return true;
			if (a == null) return false;
			if (b == null) return false;
			return a.isomorphic(b);
		}
	};
	
	public abstract boolean examine(final Examinable a, final Examinable b);

	/**
	 * Returns true if the two lists have the same elements 
	 * in the same order.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T extends Examinable> boolean orderedExamination(final Examiner e, final List<T> a, final List<T> b) {
		if (a == null && b == null) return true;
		if (a == null) return false;
		if (b == null) return false;
		// now we know that both are not null
		final int size = b.size();
		if (a.size() != size) return false;
		for (int i = 0; i < size; i++) {
			if (!e.examine(a.get(i), b.get(i))) {
				return false;
			}
		}
		// no significant differences found, return true
		return true;
	}

	/**
	 * Returns true if the two lists have the same elements, 
	 * possibly in a different order.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T extends Examinable, L extends List<T>> boolean unorderedExamination(final Examiner e, final L a, final L b) {
		final Tuple<? extends List<T>, ? extends List<T>> d = symmetricDifference(e, a, b, true);
		return d.x.isEmpty() && d.y.isEmpty();
	}
	
	/**
	 * Return the elements in A and B that are not in the other list, as determined
	 * by the examiner. If failfast is used then the results will be incomplete,
	 * and are only reliable as an indication of whether the difference is non-empty.
	 * This code assumes that individual examination calls are expensive, and makes
	 * an effort to reduce their number.
	 * @param e
	 * @param a
	 * @param b
	 * @param failfast
	 * @return
	 */
	public static <T extends Examinable, L extends List<T>> Tuple<? extends List<T>,? extends List<T>> symmetricDifference(final Examiner e, final L a, final L b, final boolean failfast) {
		final ImmutableList<T> empty = ImmutableList.of();
		if (a == null && b == null) return new Tuple<List<T>,List<T>>(empty,empty);
		if (a == null) return new Tuple<List<T>,List<T>>(empty,b);
		if (b == null) return new Tuple<List<T>,List<T>>(a,empty);
		if (failfast) {
			if (a.size() != b.size()) return new Tuple<List<T>,List<T>>(a,b);
		}
		// now we know that both are not null
		ImmutableList<T> aResult = empty;
		final int sizeB = b.size();
		final BitSet bitsB = new BitSet(sizeB);
		// check that everything in A is also in B
		for (final T x : a) {
			boolean matched = false;
			for (int j = 0; j < sizeB; j++) {
				if (!bitsB.get(j)) {
					// we haven't matched this index yet, try to do so now
					final T y = b.get(j);
					if (e.examine(x, y)) {
						// matched!
						matched = true;
						bitsB.set(j);
						break;
					}
				}
			}
			if (!matched) {
				aResult = aResult.append(x);
				if (failfast) return new Tuple<List<T>,List<T>>(aResult,empty);
			}
		}
		// what didn't match from B?
		ImmutableList<T> bResult = empty;
		for (int i = 0; i < sizeB; i++) {
			if (!bitsB.get(i)) {
				bResult = bResult.append(b.get(i));
			}
		}
		return new Tuple<List<T>,List<T>>(aResult,bResult);
	}
	
	/**
	 * Wrapper for types that do not extend Examinable.
	 * For example, could be used to compare two lists of strings.
	 * If an ordered equality of lists is needed, just call list.equals(otherlist).
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T> boolean unorderedEquals(final List<T> a, final List<T> b) {
		ImmutableList<EqualsExaminer<T>> la = ImmutableList.of();
		ImmutableList<EqualsExaminer<T>> lb = ImmutableList.of();
		for (final T item : a) { la = la.append(new EqualsExaminer<T>(item)); }
		for (final T item : b) { lb = lb.append(new EqualsExaminer<T>(item)); }
		return unorderedExamination(Examiner.Equals, la, lb);
	}

	/**
	 * You could use this method, or just call list.equals(otherList)
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T> boolean orderedEquals(final List<T> a, final List<T> b) {
		ImmutableList<EqualsExaminer<T>> la = ImmutableList.of();
		ImmutableList<EqualsExaminer<T>> lb = ImmutableList.of();
		for (final T item : a) { la = la.append(new EqualsExaminer<T>(item)); }
		for (final T item : b) { lb = lb.append(new EqualsExaminer<T>(item)); }
		return orderedExamination(Examiner.Equals, la, lb);
	}
	
	private static class EqualsExaminer<T> implements Examinable {
		private final T item;
		
		public EqualsExaminer (final T item) { this.item = item; }
		
		@Override
		public boolean equals(final Object obj) {
			if (obj == null) return false;
			if (!obj.getClass().equals(this.getClass())) return false;
			final EqualsExaminer<?> that = (EqualsExaminer<?>) obj;
			return this.item.equals(that.item);
		}
		@Override
		public boolean isomorphic(Examinable obj) { return equals(obj); }
		@Override
		public boolean equivalent(Examinable obj) { return equals(obj);	}
	}

}
