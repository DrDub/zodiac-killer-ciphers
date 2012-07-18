package com.zodiackillerciphers.old;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zodiackillerciphers.ciphers.Ciphers;

public class NGrams {
	
	static String wild = "??????"; // quick way to avoid looping in ngrams(int n, String text, int i, boolean gaps) 
	/** return list of ngrams for the given text at the given position.
	 * if gaps is true, include gapped variations
	 */
	public static List<String> ngrams(int n, String text, int i, boolean gaps) {
		List<String> list = new ArrayList<String>();
		if (i>=text.length()-n+1) return list;
		
		if (gaps) {
			StringBuffer ngram;
			if (n==2) { // A?B, A??B, A???B
				int pos1; int pos2; // locations of A and B
				for (int a=0; a<4; a++) {
					ngram = new StringBuffer();
					pos1 = i;
					pos2 = i+a+1;
					if (pos2 >= text.length()) break;
					ngram.append(text.charAt(pos1)).append(wild.substring(0, a)).append(text.charAt(pos2));
					list.add(ngram.toString());
				}
			} else if (n==3) { // AB?C, AB??C, AB???C etc
				int pos1; int pos2; int pos3; // locations of A, B, and C
				for (int a=0; a<4; a++) {
					for (int b=0; b<4; b++) {
						ngram = new StringBuffer();
						pos1 = i;
						pos2 = i+a+1;
						pos3 = pos2+b+1;
						if (pos3 >= text.length()) break;
						ngram.append(text.charAt(pos1)).append(wild.substring(0, a)).append(text.charAt(pos2)).append(wild.substring(0, b)).append(text.charAt(pos3));
						list.add(ngram.toString());
					}
				}
				
			} else if (n==4) { // ABC?D, A?B?C?D, etc
				int pos1; int pos2; int pos3; int pos4; // locations of A, B, C, and D
				for (int a=0; a<4; a++) {
					for (int b=0; b<4; b++) {
						for (int c=0; c<4; c++) {
							ngram = new StringBuffer();
							pos1 = i;
							pos2 = i+a+1;
							pos3 = pos2+b+1;
							pos4 = pos3+c+1;
							if (pos4 >= text.length()) break;
							ngram.append(text.charAt(pos1)).append(wild.substring(0, a)).append(text.charAt(pos2)).append(wild.substring(0, b)).append(text.charAt(pos3)).append(wild.substring(0, c)).append(text.charAt(pos4));
							//System.out.println(ngram);
							list.add(ngram.toString());
						}
					}
				}
				
			} else 
				list.add(text.substring(i,i+n)); // standard ngram
		} 
		return list;
		
	}
	public static Map<String, Integer> countNgrams(String text, int n) {
		return countNgrams(text, n, new int[2]);
	}
	public static Map<String, Integer> countNgrams(String text, int n, int[] totals) {
		return countNgrams(text, n, totals, false);
	}
	public static Map<String, Integer> countNgrams(String text, int n, int[] totals, boolean gaps) {
		totals[0] = 0; totals[1] = 0; totals[2] = 0; Set<String> repeats = new HashSet<String>();
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		Set<Character> uniqueLetters = new HashSet<Character>();
		for (int i=0; i<text.length()-n+1; i++) {
			List<String> ngrams = ngrams(n, text, i, gaps);
			for (String ngram : ngrams) {
				Integer count = map.get(ngram);
				if (count == null) count = 1;
				else count++;
				map.put(ngram, count);
				
				if (count > 1) {
					repeats.add(ngram);
					for (int j=0; j<ngram.length(); j++) uniqueLetters.add(ngram.charAt(j));
					if (count == 2) totals[0] += 2; 
					else totals[0]++;
					//System.out.println(ngram + ":" + map.get(ngram));
				}
				
			}
		}
		totals[1] = repeats.size();
		totals[2] = uniqueLetters.size();
		return map;
	}
	
	public static void testSolved() {
		int[] totals = new int[3];
		
		for (int n=2; n<6; n++) { 
			Map<String, Integer> map = countNgrams("ilikekillingpeoplebecauseitissomuchfunitismorefunthankillingwildgameintheforrestbecausemanisthemostdangertueanamalofalltokillsomethinggivesmethemostthrillingexperenceitisevenbetterthangettingyourrocksoffwithagirlthebestpartofitisthaewheninparadicesndalltheihavekilledwillbecomemyslavesiwillnotgiveyoumynamebecauseyouwilltrytosloidownorstopmycollectingofslavesfoemyafterlifeebeorietemethhpit", n, totals);
			for (String key : map.keySet()) System.out.println("key " + key + " val " + map.get(key));
			System.out.println(n+","+totals[0] + ","+ totals[1] + "," + totals[2]);
		}
	}
	
	public static void testNgrams() {
		String text = "abcdefghijklmnop";
		for (int n=2; n<5; n++) {
			System.out.println("n " + n);
			List<String> ngrams = ngrams(n, text, 0, true);
			for (String ngram : ngrams) System.out.println(" - " + ngram);
		}
	}

	public static void testBeale() {
		String text = Ciphers.cipher[33].cipher;
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i=0; i<text.length()-4; i++) { 
			List<String> ngrams = ngrams(4, text, i, true);
			for (String ngram : ngrams) {
				Integer val = map.get(ngram);
				if (val == null) val = 0;
				val++;
				map.put(ngram, val);
				//if (ngram.startsWith("G")) System.out.println(ngram);
				
			}
		}
		for (String key : map.keySet()) if (map.get(key) > 1) System.out.println(map.get(key) + "	" + key + "	" + text.indexOf(key));
	}
	
	public static void testDora() {
		String text = Ciphers.cipher[30].cipher;
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i=0; i<text.length()-4; i++) { 
			List<String> ngrams = ngrams(4, text, i, true);
			for (String ngram : ngrams) {
				Integer val = map.get(ngram);
				if (val == null) val = 0;
				val++;
				map.put(ngram, val);
				//if (ngram.startsWith("G")) System.out.println(ngram);
				
			}
		}
		for (String key : map.keySet()) if (map.get(key) > 1) System.out.println(map.get(key) + "	" + key + "	" + text.indexOf(key));
	}
	
	public static void test340() {
		int n=3;
		String text = Ciphers.cipher[0].cipher;
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i=0; i<text.length()-n; i++) { 
			List<String> ngrams = ngrams(n, text, i, true);
			for (String ngram : ngrams) {
				Integer val = map.get(ngram);
				if (val == null) val = 0;
				val++;
				map.put(ngram, val);
				//if (ngram.startsWith("G")) System.out.println(ngram);
				
			}
		}
		for (String key : map.keySet()) if (map.get(key) > 1) System.out.println(map.get(key) + "	" + key + "	" + text.indexOf(key));
	}
	public static float[] measure(String ciphertext) {
		return measure(ciphertext, Ciphers.alphabet(ciphertext).length()); // ignore whitespace during ngram search
	}
	/** measure the internal structure of the given cipher text by counting n-gram patterns, including those with gaps, from 
	 * n=2 to n=4. 
	 * 
	 * @param ciphertext cipher text to measure
	 * @param alpha number of symbols in the cipher alphabet
	 * @return score array (see below) 
	 */
	public static float[] measure(String ciphertext, int alpha) {
		int[] totals = new int[3];
		
		/* scores array.  elements:
		 * 
		 * # of 2-grams (no gaps)
		 * # of 3-grams (no gaps)
		 * # of 4-grams (no gaps)
		 * combined score (no gaps) - weighted sum of n-gram unique repetition counts.  the formula is: (n2 / 4) + (n3 / 2) + n4
		 * # of 2-grams (gaps)
		 * # of 3-grams (gaps)
		 * # of 4-grams (gaps)
		 * combined score (gaps) - weighted sum of n-gram unique repetition counts.  the formula is: (n2 / 4) + (n3 / 2) + n4
		 * combined score (all) - sum of the two combined scores
		 * combined score (all) divided by cipher length
		 * 
		 */
		float[] scores = new float[10]; 
		
		for (int n=2; n<5; n++) { 
			Map<String, Integer> map = countNgrams(ciphertext, n, totals, true);
			//System.out.println(n+","+totals[0] + ","+ totals[1] + "," + totals[2]);
			
			for (String ngram : map.keySet()) {
				if (map.get(ngram) < 2) continue;
				if (ngram.contains("?")) { // gaps
					//System.out.println("counting " + ngram + " with gaps");
					scores[n+2]++;
				} else { 
					//System.out.println("counting " + ngram + " without gaps");
					scores[n-2]++;
				}

			}
			//String line = "  - ";
			//for (String ngram : map.keySet()) if (map.get(ngram) > 1) line += ngram + ": " + map.get(ngram) + " ";
			//System.out.println(line);
		}
		
		for (int i=0; i<3; i++) scores[3] += scores[i] / Math.pow(2, 2-i); // combined score (no gaps)
		for (int i=0; i<3; i++) scores[7] += scores[i+4] / Math.pow(2, 2-i); // combined score (gaps) 
		scores[8] = scores[3] + scores[7];
		//scores[9] = scores[8] / alpha;
		scores[9] = scores[8] / ciphertext.length();
		//System.out.println("Uniques score: " + score);
		return scores;
	}
	
	public static void dump(float[] scores) {
		String line = "";
		for (int i=0; i<scores.length; i++) line += scores[i] + "	";
		System.out.println(line);
	}

	public static void main(String[] args) {
		//testSolved();
		//dump(measure(Ciphers.cipher[0].cipher));
		//dump(measure(Ciphers.cipher[1].cipher));
		//testNgrams();
		test340();
		//testBeale();
		//testDora();
		//testSolved();
	}
}
