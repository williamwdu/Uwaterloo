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

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

final class Pin {
	
	final static String TextX = "x";
	final static String TextY = "y";

	final String id;
	final int x, y;

	Pin(final String id, final int x, final int y) {
		this.id = id;
		this.x = x;
		this.y = y;
		assert repOk();
	}
	
	public boolean repOk() {
		assert id != null : "id is null";
		return true;
	}

	@Override
	public String toString() {
		return id + " (" + x + "," + y + ")";
	}
	
	public String toSVG() {
		return "<text x=\"" + x + "\" y=\"" + y + "\">" + id + "</text>";
	}
	
	public static String toSVG(final String id, final int x, final int y) {
		final Pin p = new Pin(id, x, y);
		return p.toSVG();
	}
	
	public static Pin fromSVG(final Node node) {
		final String nodeName = node.getNodeName();
		if (nodeName.equals("text") && node.hasAttributes() && node.getChildNodes().getLength() == 1) {
			final NamedNodeMap nnMap = node.getAttributes();
			if (nnMap.getNamedItem(TextX) != null && nnMap.getNamedItem(TextY) != null && node.getFirstChild().getNodeValue() != null) {
				final int x = Integer.parseInt(nnMap.getNamedItem(TextX).getNodeValue());
				final int y = Integer.parseInt(nnMap.getNamedItem(TextY).getNodeValue());
				return new Pin(node.getFirstChild().getNodeValue(), x, y);
			}
		}
		return null;
	}

}
