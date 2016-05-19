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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.parboiled.common.ImmutableList;

import ece351.w.ast.WProgram;
import ece351.w.ast.Waveform;


public final class TransformSVG2W {
	
	/**
	 * Transforms an instance of WSVG to an instance of WProgram.
	 */
	public static final WProgram transform(final WSVG wsvg) {
		final List<Line> lines = new ArrayList<Line>(wsvg.segments);
		final List<Pin> pins = new ArrayList<Pin>(wsvg.pins);

		// sort the line segments by their Y position, then X position
		// think about a small example waveform and the order of the 
		// line segments after this sort is performed.
		Collections.sort(lines, COMPARE_Y_X);

		// Place holder for the list of waveforms in the final WProgram result.
		ImmutableList<Waveform> waveforms = ImmutableList.of();

		// the set of Y values in use for the current waveform
		final Set<Integer> setY = new LinkedHashSet<Integer>();

		// the set of line segments for the current waveform
		final List<Line> extract = new ArrayList<Line>();
		
		// lines are taken off the global list and added to the extract list
		// then the extract list is used to construct a new waveform object 
		// finally, the extract list is cleared and the process repeats
		setY.add(lines.get(0).y1);
		while(!lines.isEmpty()) {
			final Line line = lines.get(0);
			if (setY.contains(line.y1)){
				if(setY.contains(line.y2)){
					extract.add(line);
					lines.remove(0);
					//System.out.println("add x1="+line.x1);
				}
				else{
					setY.add(line.y2);
					extract.add(line);
					lines.remove(0);
					//System.out.println("add x1="+line.x1);
				}
			}
			else{
				if(setY.contains(line.y2)){
					setY.add(line.y1);
					extract.add(line);
					lines.remove(0);
					//System.out.println("add x1="+line.x1);
				}
				else{
					waveforms = waveforms.append(transformLinesToWaveform(extract, pins));
					extract.clear();
					setY.clear();
					setY.add(line.y1);
					pins.remove(0);
					//System.out.println("transformLinesToWaveform");
				}
			}
// TODO: longer code snippet
		}

		// the last waveform
		if(!extract.isEmpty()) {
			waveforms = waveforms.append(transformLinesToWaveform(extract, pins));
			//System.out.println("last waveformm transformLinesToWaveform");
			
		}
		//System.out.println("push + " + waveforms);
		return new WProgram(waveforms);
	}

	/**
	 * Transform a list of Line to an instance of Waveform
	 */
	private static Waveform transformLinesToWaveform(final List<Line> lines, final List<Pin> pins) {
		if(lines.isEmpty()) return null;

		// Sort by the middle of two x-coordinates.
		Collections.sort(lines, COMPARE_X);

		// Place holder for the list of bits.
		ImmutableList<String> bits = ImmutableList.of();
		ArrayList<String> list = new ArrayList<>();
		// The first line of the waveform.
		final Line first = lines.get(0);

		for(int i = 1; i < lines.size(); i++) {
			Line current = lines.get(i);
			if (current.x1 == current.x2){
			}
			else{
				if (current.y2 > first.y1){
				list.add("0");
				//System.out.print("0");
				}
				else{
					list.add("1");
					//System.out.print("1");

				}
			}
			// If a dot, skip it.
// TODO: longer code snippet
		}

		// Get the corresponding id for this waveform.
		String id = pins.get(0).id;
		bits = ImmutableList.copyOf(list);
		//System.out.println(id);
		return new Waveform(bits, id); // construct a new waveform object
// TODO: longer code snippet

	}

	public final static Comparator<Line> COMPARE_X = new Comparator<Line>() {
		@Override
		public int compare(final Line l1, final Line l2) {
			if(l1.x1 < l2.x1) return -1;
			if(l1.x1 > l2.x1) return 1;
			if(l1.x2 < l2.x2) return -1;
			if(l1.x2 > l2.x2) return 1;
			return 0;
		}
	};

	/**
	 * Sort lines according to their y position first, and then x position second.
	 */
	public final static Comparator<Line> COMPARE_Y_X = new Comparator<Line>() {
		@Override
		public int compare(final Line l1, final Line l2) {
			final double y_mid1 = (double) (l1.y1 + l1.y2) / 2.0f;
			final double y_mid2 = (double) (l2.y1 + l2.y2) / 2.0f;
			final double x_mid1 = (double) (l1.x1 + l1.x2) / 2.0f;
			final double x_mid2 = (double) (l2.x1 + l2.x2) / 2.0f;
			if (y_mid1 < y_mid2) return -1;
			if (y_mid1 > y_mid2) return 1;
			if (x_mid1 < x_mid2) return -1;
			if (x_mid1 > x_mid2) return 1;
			return 0;
		}
	};

}
