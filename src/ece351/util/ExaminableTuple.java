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

import java.util.Collection;

/**
 * Generic pair of objects. Used by methods that need to return two values.
 */
public final class ExaminableTuple<X, Y> implements Examinable {
	public final X x;
	public final Y y;

	public ExaminableTuple(final X x, final Y y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean isomorphic(final Examinable obj) {
		return examine(Examiner.Isomorphic, obj);
	}

	@Override
	public boolean equivalent(final Examinable obj) {
		return examine(Examiner.Equivalent, obj);
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Examinable) {
			return examine(Examiner.Equals, (Examinable)obj);
		} else {
			return false;
		}
	}
	
	private boolean examine(final Examiner examiner, final Examinable obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (!getClass().equals(obj.getClass())) return false;
		@SuppressWarnings("rawtypes")
		final ExaminableTuple that = (ExaminableTuple) obj;
		// x
		// two cases: objects and collections
		if (this.x instanceof Collection) {
//			final Collection<Examinable> c1 = (Collection<Examinable>) this.x;
//			final Collection<Examinable> c2 = (Collection<Examinable>) that.x;
			throw new UnsupportedOperationException("not yet implemented");
		} else {
			final Examinable e1 = (Examinable) this.x;
			final Examinable e2 = (Examinable) that.x;
			if (!examiner.examine(e1, e2)) return false;
		}
		// y
		if (this.y instanceof Collection) {
			throw new UnsupportedOperationException("not yet implemented");
		} else {
			final Examinable e1 = (Examinable) this.y;
			final Examinable e2 = (Examinable) that.y;
			if (!examiner.examine(e1, e2)) return false;
		}
		// no significant differences found, return true
		return true;
	}
}
