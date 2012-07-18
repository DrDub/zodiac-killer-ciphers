package com.zodiackillerciphers.old;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** A Pivot is an n-gram (n > 1) that intersects at some symbol in a block of text. */ 
public class Pivot {
	
	/** row of intersection */
	public int row;
	/** col of intersection */
	public int col;
	
	/** number of "legs" of this pivot */
	public int legsCount;
	
	/** the legs */
	public List<String> legs;
	
	/** list of positions occupied by this pivot */
	public List<int[]> positions;
	
	/** list of directions of lengths having min length 3 */
	public Set<String> directions;

	/** list of leg pairs, so we can match orientations */
	public Set<String> pairs;
	
	public Pivot() {
		positions = new ArrayList<int[]>();
		legs = new ArrayList<String>();
		directions = new HashSet<String>();
		pairs = new HashSet<String>();
	}
	
	public int longestLeg() {
		int max = 0;
		if (legs == null) return 0;
		for (String leg : legs) {
			max = Math.max(max, leg.length());
		}
		return max;
	}
	
	public String toString() {
		String line = "row " + row + ", col " + col + ", legsCount " + legsCount + ", longestLeg " + longestLeg() + ", " + dumpLegs() + ", " + pairs();
		return line;
	}
	
	public String pairs() {
		if (pairs == null) return null;
		String line = "";
		for (String pair : pairs) line += pair + " ";
		return line;
		
	}
	
	public String dumpLegs() {
		String line = "";
		if (legs == null) return line;
		for (String leg : legs) if (leg != null) line += "["+leg + "] ";
		return line;
	}
	
	public void makePairs() {
		if (directions == null) return;
		if (directions.contains("N") && directions.contains("E")) pairs.add("NE");
		if (directions.contains("N") && directions.contains("S")) pairs.add("NS");
		if (directions.contains("N") && directions.contains("W")) pairs.add("NW");
		if (directions.contains("E") && directions.contains("S")) pairs.add("ES");
		if (directions.contains("E") && directions.contains("W")) pairs.add("EW");
		if (directions.contains("S") && directions.contains("W")) pairs.add("SW");
	}
	
	public String html(String sub) {
		String html = "<tt>";
		
		Set<String> set = new HashSet<String>();
		set.add(row+","+col);
		for (int[] rc : positions) set.add(rc[0]+","+rc[1]);
		
		for (int r=0; r<Pivots.H; r++) {
			for (int c=0; c<Pivots.W; c++) {
				String letter = Pivots.charAt(sub, new int[] {r, c});
				String key = r+","+c;
				if (set.contains(key)) html += "<b>" + letter + "</b>";
				else html += letter;
			}
			html += "<br>";
		}
		
		html += "</tt>";
		return html;
	}
	
	public static String html(String sub, List<Pivot> pivots) {
		String html = "<tt>";
		
		Set<String> set = new HashSet<String>();
		
		for (Pivot pivot : pivots) {
			if (pivot.legsCount > 1 && pivot.longestLeg() > 2) {
				set.add(pivot.row+","+pivot.col);
				for (int[] rc : pivot.positions) set.add(rc[0]+","+rc[1]);
			}
		}
		
		for (int r=0; r<Pivots.H; r++) {
			for (int c=0; c<Pivots.W; c++) {
				String letter = Pivots.charAt(sub, new int[] {r, c});
				String key = r+","+c;
				if (set.contains(key)) html += "<b>" + letter + "</b>";
				else html += letter;
			}
			html += "<br>";
		}
		
		html += "</tt>";
		return html;
		
	}
	
	public static void main(String[] args) {
		String s1 = "UV";
		String s2 = "WX";
		String s3 = "YZ";
		
		for (int a=0; a<2; a++) {
			for (int b=0; b<2; b++) {
				for (int c=0; c<2; c++) {
					for (int d=0; d<2; d++) {
						for (int e=0; e<2; e++) {
							for (int f=0; f<2; f++) {
								String c1 = ""+s1.charAt(a)+s2.charAt(b)+s3.charAt(c);
								String c2 = ""+s1.charAt(d)+s2.charAt(e)+s3.charAt(f);
								System.out.println(c1+" "+c2+" "+(c1.equals(c2)));
							}	
						}
					}
				}
			}
		}
		}
	
}
