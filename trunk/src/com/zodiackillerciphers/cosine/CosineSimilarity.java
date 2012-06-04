package com.zodiackillerciphers.cosine;

import java.util.ArrayList;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zodiackillerciphers.ciphers.Ciphers;

public class CosineSimilarity {
	public ContactVector contactvector;
	public Map<Character, Float> best; // track best cosine similarity per distinct symbol
	public float score;
	public float min = Float.MAX_VALUE;
	public float max = -Float.MAX_VALUE;
	public float mean = 0;
	private void updateContactVector(String ciphertext) {
		contactvector = new ContactVector();
		for (int i=0; i<ciphertext.length(); i++) {
			Character x = null, xp = null, xf = null;
			x = ciphertext.charAt(i);
			if (i>0) xp = ciphertext.charAt(i-1);
			if (i<ciphertext.length()-1) xf = ciphertext.charAt(i+1);

			contactvector.incBefore(x, xp);
			contactvector.incAfter(x, xf);
		}
	}
	
	public void dumpContactVector() {
		for (Character ch : contactvector.mapBefore.keySet()) {
			for (Character ch2 : contactvector.mapBefore.get(ch).keySet()) {
				System.out.println("before " + ch + " " + ch2 + ": " + contactvector.mapBefore.get(ch).get(ch2));
			}
		}
		for (Character ch : contactvector.mapAfter.keySet()) {
			for (Character ch2 : contactvector.mapAfter.get(ch).keySet()) {
				System.out.println("after " + ch + " " + ch2 + ": " + contactvector.mapAfter.get(ch).get(ch2));
			}
		}
	}
	
	/**
	 * @param ciphertext
	 * @param sort
	 * @return
	 */
	public List<CosineSimilarityResult> compute(String ciphertext, boolean sort) {
		//System.out.println(ciphertext);
		best = new HashMap<Character, Float>();
		updateContactVector(ciphertext);
		List<CosineSimilarityResult> results = new ArrayList<CosineSimilarityResult>();
		List<Character> a = Ciphers.alphabetAsList(ciphertext);
		//System.out.println(ciphertext);
		//for (Character ch : a) System.out.println(ch);
		for (int i=0; i<a.size(); i++) {
			char ch1 = a.get(i); 
			for (int j=i+1; j<a.size(); j++) {
				char ch2 = a.get(j);
				CosineSimilarityResult r = new CosineSimilarityResult();
				r.key = ""+ch1+ch2;
				r.val = cosineSimilarity(ch1, ch2);
				results.add(r);
				
				// track the best-seen similarity values per symbol
				Float b = best.get(ch1);
				if (b == null) b = 0f;
				b = Math.max(b, r.val);
				best.put(ch1, b);
				
				b = best.get(ch2);
				if (b == null) b = 0f;
				b = Math.max(b, r.val);
				best.put(ch2, b);
				
				
			}
		}
		if (sort) Collections.sort(results);
		
		score = 0; 
		for (Character ch : best.keySet()) {
			score += best.get(ch);
			//System.out.println("Best for " + ch + " is " + best.get(ch) +", score now " + score);
			min = Math.min(min, best.get(ch));
			max = Math.max(max, best.get(ch));
		}
		mean = score / best.size();
		
		return results;
	}
	
	public float cosineSimilarity(char ch1, char ch2) {
		//System.out.println("buh ch1 " + ch1 + " ch2 " + ch2);
		//try {
			Set<Character> a = new HashSet<Character>();
			if (contactvector.mapBefore.get(ch1) != null) for (Character ch : contactvector.mapBefore.get(ch1).keySet()) a.add(ch);
			if (contactvector.mapAfter.get(ch1) != null) for (Character ch : contactvector.mapAfter.get(ch1).keySet()) a.add(ch);
			//System.out.println("feh " + contactvector.mapBefore.get(ch2));
			if (contactvector.mapBefore.get(ch2) != null) for (Character ch : contactvector.mapBefore.get(ch2).keySet()) a.add(ch);
			if (contactvector.mapAfter.get(ch2) != null) for (Character ch : contactvector.mapAfter.get(ch2).keySet()) a.add(ch);
			List<Integer> x = new ArrayList<Integer>();
			List<Integer> y = new ArrayList<Integer>();
			for (Character ch : a) {
				x.add(contactvector.before(ch1, ch));
				y.add(contactvector.before(ch2, ch));
			}
			
			for (Character ch : a) {
				x.add(contactvector.after(ch1, ch));
				y.add(contactvector.after(ch2, ch));
			}
			
			float val = cosineDistance(x, y);
			//System.out.println("ch1 " + ch1 + " ch2 " + ch2 + " val " + val);
			return val;
		//} catch (Exception e) {
		//	System.out.println("[" + ch1 + "][" + ch2 + "] " + e);
		//}
		//return 0;
	}
	
	public float cosineDistance(List<Integer> x, List<Integer> y) {
		float dot = 0; // dot product
		float magX = 0; // magnitude of vector x
		float magY = 0; // magnitude of vector y
		for (int i=0; i<x.size(); i++) {
			int xi = x.get(i);
			int yi = y.get(i);
			dot += xi*yi;
			magX += xi*xi;
			magY += yi*yi;
			//System.out.println(" - i" + i + " xi " + xi + " yi " + yi + " dot " + dot + " magX " + magX + " magY " + magY);
		}
		magX = (float) Math.sqrt(magX);
		magY = (float) Math.sqrt(magY);
		return dot/(magX*magY);
	}
	/**
	 * returns array of stats on the top 25 cosine similarities in the given list
	 * @param list the sorted list of cosine similarities
	 * @return array of [max cosine similarity, sum of top 25, mean of top 25
	 */
	public float[] top25(List<CosineSimilarityResult> list) {
		float[] results = new float[3]; // max, sum, mean
		
		for (int i=0; i<25 && i<list.size(); i++) {
			results[1] += list.get(i).val;
			//System.out.println(list.get(i).val);
		}
		
		results[0] = list.get(0).val;
		results[2] = results[1] / 25;
		return results;
		
	}

	public static float[] measure(String ciphertext) {
		CosineSimilarity c = new CosineSimilarity();
		List<CosineSimilarityResult> results = c.compute(ciphertext, false);
		//int count=0;
		//float[] f = c.top25(results);
		//return f[1];
		return new float[] {c.score, c.min, c.max, c.mean};
	}
	
	
	public static void main(String[] args) {
		CosineSimilarity c = new CosineSimilarity();
		//List<CosineSimilarityResult> results = c.compute(org.oranchak.lucene.Ciphers.ciphers[3][0].toString(), false);
		List<CosineSimilarityResult> results = c.compute(Ciphers.cipher[9].cipher, false);
		int count=0;
		for (CosineSimilarityResult r : results) {
			System.out.println(r.key + "," + r.val);
			count++;
			if (count == 25) break;
		}
		float[] f = c.top25(results);
		for (int i=0; i<f.length; i++) System.out.println("f"+i+": " + f[i]);
		System.out.println("Best per symbol:");
		int symbols = 0;
		for (Character ch : c.best.keySet()) {
			System.out.println(" - " + ch + ": " + c.best.get(ch));
			symbols++;
		}
		System.out.println("Best score: " + c.score);
		System.out.println("Average per symbol: " + (c.score/symbols));
		//c.dumpContactVector();
	}
}
