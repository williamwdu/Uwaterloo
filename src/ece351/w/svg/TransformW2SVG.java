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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import ece351.util.CommandLine;
import ece351.util.Debug;
import ece351.w.ast.WProgram;
import ece351.w.ast.Waveform;
//import ece351.w.parboiled.WParboiledParser;
import ece351.w.rdescent.WRecursiveDescentParser;

public final class TransformW2SVG {

	/**
	 * Try changing the value of this flag and see how it changes the performance
	 * of the test harnesses.
	 */
	public static final boolean USE_DOM_XML_PARSER = true;
// TODO: short code snippet

	
	public static void main(final CommandLine c) {
		final WProgram wp;
		if (c.parbparser) {
		//wp = WParboiledParser.parse(c.readInputSpec());
			wp = null;
		} else if (c.handparser) {
			wp = WRecursiveDescentParser.parse(c.readInputSpec());
		} else {
			wp = WRecursiveDescentParser.parse(c.readInputSpec());
		}
		
		final PrintWriter pw = c.resolveOutputSpec();
		transform(wp, pw);
		pw.flush();
	}
	
	public static void main(final String... args) {
		final CommandLine c = new CommandLine(args);
		main(c);
	}
	
	public static void main(final String inputSpec) {
		main(new String[]{inputSpec});
	}
	
	public static String transform(final WProgram wp) {
		final StringWriter sw = new StringWriter();
		final PrintWriter out = new PrintWriter(sw);
		transform(wp, out);
		out.close();
		return sw.toString();
	}
	
	public static void transform(final WProgram wp, final PrintWriter out) {
		
		// header
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">");
		out.println("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100%\" height=\"100%\" version=\"1.1\">");
		out.println("<style type=\"text/css\"><![CDATA[line{stroke:#006600;fill:#00 cc00;} text{font-size:\"large\";font-family:\"sans-serif\"}]]></style>");
		out.println();

		final int WIDTH = 100;
		
		int y_mid = 150;
		int y_prev = 150;
		int y_pos =150;
		final int y_off =50;

		// loop on waveforms
		// this line implicitly uses an iterator
		for (final Waveform w : wp.waveforms) {
			// reset the initial x position
			int x = 50;

			// write out the waveform name
			out.println(Pin.toSVG(w.name, x, y_mid));

			// advance the x position to start drawing
			x=100;
			

			// loop on bits
			for (final String bit : w.bits) {
				// set the y position according to the value of the bit
				// draw the vertical line
				// draw the horizontal line
				// get ready for the next bit
				// TODO: longer code snippet
				if (bit == "1"){
					int x1 = x;
					int x2 = x1;
					int y1 = y_prev;
					int y2 = y_mid + y_off;
					y_prev = y2;
					out.println(Line.toSVG(x1, y1, x2, y2));
				}
				else{
					int x1 = x;
					int x2 = x1;
					int y1 = y_prev;
					int y2 = y_mid - y_off;
					y_prev = y2;
					out.println(Line.toSVG(x1, y1, x2, y2));
				}
				int x1 = x;
				int x2 = x1 + WIDTH;
				x = x2;
				int y1 = y_prev;
				int y2 = y1;
				out.println(Line.toSVG(x1, y1, x2, y2));
			}
			
			// advance the y position for the next pin
			// TODO: short code snippet
			y_pos = y_pos + 3*y_off;
			y_prev = y_pos;
			y_mid = y_pos;

		}

		// footer
		out.println("</svg>");
		
	}

}
