package com.zodiackillerciphers.constraints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zodiackillerciphers.ciphers.Ciphers;
import com.zodiackillerciphers.tests.LetterFrequencies;

public class Info {
	/** the cipher substring */
	public String substring;
	/** where it appears in the original cipher */
	public int index;
	/** map used to track symbol frequency distribution */
	public Map<Character, Integer> counts; 
	/** list of pairs of repeating symbols, represented by indices of the substring */
	public List<int[]> pairs;
	/** estimated probability of constraint being met by random english plaintext */
	public float probability;
	
	public Info(String substring, int index) {
		this.substring = substring;
		this.index = index;
		computeCounts();
		computePairs();
		computeProbability();
	}
	public void computeCounts() {
		counts = Ciphers.countMap(substring);
	}
	
	public void computeProbability() {
		probability = 1f;
		for (Character key : counts.keySet()) {
			Integer val = counts.get(key);
			if (val < 2) continue;
			probability *= LetterFrequencies.computeIOC(val);
		}
	}
	
	public void computePairs() {
		pairs = new ArrayList<int[]>();
		Map<Character, Integer> firsts = new HashMap<Character, Integer>();
		for (int i=0; i<substring.length(); i++) {
			char key = substring.charAt(i);
			Integer val = firsts.get(key);
			if (val == null) {
				val = i;
			} else {
				pairs.add(new int[] {val, i});
			}
			firsts.put(key, val);
		}
	}
	
	public String toString() {
		return substring.length() + ", " + index + ", " + substring + ", " + probability + ", " + pairs();
	}
	
	public String pairs() {
		String s = "";
		if (pairs == null) return s;
		for (int[] i : pairs) {
			s += "{" + i[0] + " " + i[1] + "} ";
		}
		return s;
	}
	
	/** return true if the given text does not violate the constraints */
	public boolean fit(String text) {
		
		if (text == null) return false;
		if (text.length() != substring.length()) return false;
		if (pairs == null) return true;
		
		for (int[] pair : pairs) {
			if (text.charAt(pair[0]) != text.charAt(pair[1])) return false;
		}
		return true;
	}
	
	
}
