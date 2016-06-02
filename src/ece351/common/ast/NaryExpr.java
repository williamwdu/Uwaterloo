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

package ece351.common.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.parboiled.common.ImmutableList;

import ece351.util.Examinable;
import ece351.util.Examiner;

/**
 * An expression with multiple children. Must be commutative.
 */
public abstract class NaryExpr extends Expr {

	public final ImmutableList<Expr> children;

	public NaryExpr(final Expr... exprs) {
		Arrays.sort(exprs);
		ImmutableList<Expr> c = ImmutableList.of();
		for (final Expr e : exprs) {
			c = c.append(e);
		}
    	this.children = c;
	}
	
	public NaryExpr(final List<Expr> children) {
		final ArrayList<Expr> a = new ArrayList<Expr>(children);
		Collections.sort(a);
		this.children = ImmutableList.copyOf(a);
	}

	/**
	 * Each subclass must implement this factory method to return
	 * a new object of its own type. 
	 */
	public abstract NaryExpr newNaryExpr(final List<Expr> children);

	/**
	 * Construct a new NaryExpr (of the appropriate subtype) with 
	 * one extra child.
	 * @param e the child to append
	 * @return a new NaryExpr
	 */
	public NaryExpr append(final Expr e) {
		return newNaryExpr(children.append(e));
	}

	/**
	 * Construct a new NaryExpr (of the appropriate subtype) with 
	 * the extra children.
	 * @param list the children to append
	 * @return a new NaryExpr
	 */
	public NaryExpr appendAll(final List<Expr> list) {
		final List<Expr> a = new ArrayList<Expr>(children.size() + list.size());
		a.addAll(children);
		a.addAll(list);
		return newNaryExpr(a);
	}

	/**
	 * Check the representation invariants.
	 */
	public boolean repOk() {
		// programming sanity
		assert this.children != null;
		// should not have a single child: indicates a bug in simplification
		assert this.children.size() > 1 : "should have more than one child, probably a bug in simplification";
		// check that children is sorted
		int i = 0;
		for (int j = 1; j < this.children.size(); i++, j++) {
			final Expr x = this.children.get(i);
			assert x != null : "null children not allowed in NaryExpr";
			final Expr y = this.children.get(j);
			assert y != null : "null children not allowed in NaryExpr";
			assert x.compareTo(y) <= 0 : "NaryExpr.children must be sorted";
		}
		// no problems found
		return true;
	}

	/**
	 * The name of the operator represented by the subclass.
	 * To be implemented by each subclass.
	 */
	public abstract String operator();
	
	/**
	 * The complementary operation: NaryAnd returns NaryOr, and vice versa.
	 */
	abstract protected Class<? extends NaryExpr> getThatClass();
	

	/**
     * e op x = e for absorbing element e and operator op.
     * @return
     */
	public abstract ConstantExpr getAbsorbingElement();

    /**
     * e op x = x for identity element e and operator op.
     * @return
     */
	public abstract ConstantExpr getIdentityElement();


	@Override 
    public final String toString() {
    	final StringBuilder b = new StringBuilder();
    	b.append("(");
    	int count = 0;
    	for (final Expr c : children) {
    		b.append(c);
    		if (++count  < children.size()) {
    			b.append(" ");
    			b.append(operator());
    			b.append(" ");
    		}
    		
    	}
    	b.append(")");
    	return b.toString();
    }


	@Override
	public final int hashCode() {
		return 17 + children.hashCode();
	}

	@Override
	public final boolean equals(final Object obj) {
		if (!(obj instanceof Examinable)) return false;
		return examine(Examiner.Equals, (Examinable)obj);
	}
	
	@Override
	public final boolean isomorphic(final Examinable obj) {
		return examine(Examiner.Isomorphic, obj);
	}
	
	private boolean examine(final Examiner e, final Examinable obj) {
		// basics
		if (obj == null) return false;
		if (!this.getClass().equals(obj.getClass())) return false;
		final NaryExpr that = (NaryExpr) obj;
		// if the number of children are different, consider them not equivalent
		if ( that.children.size()!= this.children.size()) return false;
		// since the n-ary expressions have the same number of children and they 
		//are sorted, just iterate and 
		// supposed to be sorted, but might not be (because repOk might not pass)
		// if they aren't the same elements in the same order return false
		for (int i =0; i < this.children.size(); i++) {
    		if (!this.children.get(i).equals(that.children.get(i))){
    			return false;
    		}
    		}
		//e.examine(obj, this);
    		return true;
    	
		// no significant differences found, return true
		
// TODO: longer code snippet
	}

	
	@Override
	protected final Expr simplifyOnce() {
		assert repOk();
		final Expr result = 
				simplifyChildren().
				mergeGrandchildren().
				foldIdentityElements().
				foldAbsorbingElements().
				foldComplements().
				removeDuplicates().
				simpleAbsorption().
				subsetAbsorption().
				singletonify();
		assert result.repOk();
		return result;
	}
	
	/**
	 * Call simplify() on each of the children.
	 */
	private NaryExpr simplifyChildren() {
		// note: we do not assert repOk() here because the rep might not be ok
		// the result might contain duplicate children, and the children
		// might be out of order
		ArrayList<Expr> list = new ArrayList<>();
		for (int i=0; i< this.children.size(); i++){
		Expr simplified = this.children.get(i).simplify();
		list.add(simplified);		
		}
		return newNaryExpr(list); // TODO: replace this stub
	}

	
	private NaryExpr mergeGrandchildren() {
		// extract children to merge using filter (because they are the same type as us)
			// if no children to merge, then return this (i.e., no change)		
		if (this.children.isEmpty()){
			return this;
		}
		NaryExpr filtered = filter(this.getClass(), true);
		if (filtered.children.isEmpty()) return this;
		ArrayList<Expr> list = new ArrayList<Expr>(this.children);
		for (int i =0; i < filtered.children.size(); i++) {
			NaryExpr secondLayer = (NaryExpr) filtered.children.get(i);
				list.remove(secondLayer);
				list.addAll(secondLayer.children);
			
		}

		NaryExpr result = newNaryExpr(list);
			// use filter to get the other children, which will be kept in the result unchanged
		
			// merge in the grandchildren
		
			// assert result.repOk():  this operation should always leave the AST in a legal state
		assert result.repOk();
		return result; // TODO: replace this stub
	}


    private NaryExpr foldIdentityElements() {
    	// if we have only one child stop now and return self
    	
    	if (this.children.size() != 1){
    		ArrayList<Expr> list = new ArrayList<>();
    		list.add(this.getIdentityElement());
    		NaryExpr result = removeAll(list, Examiner.Equals);
    		if (result.children.isEmpty()){
    			return result.append(this.getIdentityElement());
    		}
    		else{
    		return result;
    		}
    	}
    	else{
    		return this;
    	}
    	// we have multiple children, remove the identity elements
    		// all children were identity elements, so now our working list is empty
    		// return a new list with a single identity element
    		// normal return
    	// do not assert repOk(): this fold might leave the AST in an illegal state (with only one child)
		 // TODO: replace this stub
    }

    private NaryExpr foldAbsorbingElements() {
		// absorbing element: 0.x=0 and 1+x=1
			// absorbing element is present: return it
			// not so fast! what is the return type of this method? why does it have to be that way?
			// no absorbing element present, do nothing
    	// do not assert repOk(): this fold might leave the AST in an illegal state (with only one child)
    	if (contains(this.getAbsorbingElement(),Examiner.Equals)){
    		ArrayList<Expr> list = new ArrayList<>();
    		list.add(this.getAbsorbingElement());
    		NaryExpr result = newNaryExpr(list);
    		return result;
    	}
    	return this; // TODO: replace this stub
	}

	private NaryExpr foldComplements() {
		// collapse complements
		// !x . x . ... = 0 and !x + x + ... = 1
		// x op !x = absorbing element
		// find all negations
		NotExpr not = new NotExpr();
		NaryExpr filtered = this.filter(not.getClass(), true);
		for(int i=0; i< filtered.children.size(); i++){
			NotExpr target = (NotExpr) filtered.children.get(i);
			Expr constant = target.expr;
			if(this.contains(constant,Examiner.Equals)){
				ArrayList<Expr> list = new ArrayList<>();
	    		list.add(this.getAbsorbingElement());
	    		NaryExpr result = newNaryExpr(list);
	    		return result;					
			}
		}
		// for each negation, see if we find its complement
				// found matching negation and its complement
				// return absorbing element
		// no complements to fold
    	// do not assert repOk(): this fold might leave the AST in an illegal state (with only one child)
		return this; // TODO: replace this stub
	}

	private NaryExpr removeDuplicates() {
		// remove duplicate children: x.x=x and x+x=x
		// since children are sorted this is fairly easy
			// no changes
			// removed some duplicates
		for(int j=0; j < this.children.size()-1; j++){
		Expr base = this.children.get(j);
		for(int i=j+1; i< this.children.size(); i++){
			if (base.equals(this.children.get(i))){
				ArrayList<Expr> list = new ArrayList<>();
	    		list.add(base);
				NaryExpr result = removeAll(list, Examiner.Equals);
				return result.append(base);
			}
		}
		}
    	// do not assert repOk(): this fold might leave the AST in an illegal state (with only one child)
		return this; // TODO: replace this stub
	}

	private NaryExpr simpleAbsorption() {
		// (x.y) + x ... = x ...
		// check if there are any conjunctions that can be removed
		
		NaryExpr filtered = this.filter(this.getThatClass(), true);
		ArrayList<NaryExpr> varlist = new ArrayList<>();
		for (int i =0; i < filtered.children.size(); i++) {
			NaryExpr secondLayer = (NaryExpr) filtered.children.get(i);
				varlist.add(secondLayer);			
		}
		NaryExpr filtered2 = this.filter(this.getThatClass(), false);
		for (NaryExpr temp : varlist){
			for (int j=0; j < temp.children.size();j++ ){
			for (int k =0; k < filtered2.children.size(); k++) {
				if (filtered2.children.get(k).equals(temp.children.get(j))){
					ArrayList<Expr> result = new ArrayList<>();
					result.add(temp);
					return this.removeAll(result, Examiner.Equals);
				}			
			}
			}
		}
		
		
    	// do not assert repOk(): this operation might leave the AST in an illegal state (with only one child)
		return this; // TODO: replace this stub
	}

	private NaryExpr subsetAbsorption() {
		ArrayList<Expr> list = new ArrayList<Expr>();
		NaryExpr filtered = this.filter(this.getThatClass(), true);
		ArrayList<NaryExpr> comparelist = new ArrayList<>();
		for (int i =0; i < filtered.children.size(); i++) {
			NaryExpr secondLayer = (NaryExpr) filtered.children.get(i);
			comparelist.add(secondLayer);
		}

		for (int j=0; j < comparelist.size(); j++){
			NaryExpr item =  comparelist.get(j);
			boolean kill = true;
			for (int k =0; k < comparelist.size(); k++) {
				if (k==j){
					
				}
				else{
				for(int itemcounter=0; itemcounter < item.children.size(); itemcounter++){
					if (!comparelist.get(k).contains(item.children.get(itemcounter), Examiner.Equals)){
						kill = false;
					}

				}
				if (kill == true){
					list.add(comparelist.get(k));
				}
				}
			}
		}
			
		return this.removeAll(list, Examiner.Equals); // TODO: replace this stub
	}

	/**
	 * If there is only one child, return it (the containing NaryExpr is unnecessary).
	 */
	private Expr singletonify() {
		// if we have only one child, return it
		// having only one child is an illegal state for an NaryExpr
		// multiple children; nothing to do; return self
		if (this.children.size() == 1){
			return (this.children.get(0));
		}
		else{
		return this;
		}// TODO: replace this stub
	}

	/**
	 * Return a new NaryExpr with only the children of a certain type, 
	 * or excluding children of a certain type.
	 * @param filter
	 * @param shouldMatchFilter
	 * @return
	 */
	public final NaryExpr filter(final Class<? extends Expr> filter, final boolean shouldMatchFilter) {
		ImmutableList<Expr> l = ImmutableList.of();
		for (final Expr child : children) {
			if (child.getClass().equals(filter)) {
				if (shouldMatchFilter) {
					l = l.append(child);
				}
			} else {
				if (!shouldMatchFilter) {
					l = l.append(child);
				}
			}
		}
		return newNaryExpr(l);
	}

	public final NaryExpr filter(final Expr filter, final Examiner examiner, final boolean shouldMatchFilter) {
		ImmutableList<Expr> l = ImmutableList.of();
		for (final Expr child : children) {
			if (examiner.examine(child, filter)) {
				if (shouldMatchFilter) {
					l = l.append(child);
				}
			} else {
				if (!shouldMatchFilter) {
					l = l.append(child);
				}
			}
		}
		return newNaryExpr(l);
	}

	public final NaryExpr removeAll(final List<Expr> toRemove, final Examiner examiner) {
		NaryExpr result = this;
		for (final Expr e : toRemove) {
			result = result.filter(e, examiner, false);
		}
		return result;
	}

	public final boolean contains(final Expr expr, final Examiner examiner) {
		for (final Expr child : children) {
			if (examiner.examine(child, expr)) {
				return true;
			}
		}
		return false;
	}

}
