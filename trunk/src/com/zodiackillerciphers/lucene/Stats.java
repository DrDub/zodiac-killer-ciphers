package com.zodiackillerciphers.lucene;

import java.util.HashMap;
import java.util.Map;

public class Stats {
	public static float ENGLISH_IOC = 0.0667f; 
	public static float ENGLISH_ENTROPY = 4.1f; 
	public static float ENGLISH_CHI2 = 0.55f; 
	
	public static Float ioc(StringBuffer sb) {
		float sum = 0;
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		for (int i=0; i<sb.length(); i++) {
			Character key = sb.charAt(i);
			Integer val = map.get(key);
			if (val == null) val = 0;
			val++;
			map.put(key, val);
		}
		
		Integer val;
		for (Character key : map.keySet()) {
			val = map.get(key);
			sum += val*(val-1);
		}
		return sum/(sb.length()*(sb.length()-1));
	}
	
	public static Float iocDiff(StringBuffer sb) {
		return Math.abs(ENGLISH_IOC - ioc(sb));
	}
	
	public static Float entropy(StringBuffer sb) {
		float sum = 0; float pm; // probability mass
		int L = sb.length();
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		for (int i=0; i<L; i++) {
			Character key = sb.charAt(i);
			Integer val = map.get(key);
			if (val == null) val = 0;
			val++;
			map.put(key, val);
		}
		for (Character key : map.keySet()) {
			pm = ((float)map.get(key))/L;
			sum += pm*Math.log(pm)/Math.log(2);
		}
		return -1.0f*sum;
		
	}
	
	public static Float entropyDiff(StringBuffer sb) {
		return Math.abs(ENGLISH_ENTROPY - entropy(sb));
	}

	public static Float chi2(StringBuffer sb) {
		
		int uniq = 0; float chi2 = 0; float curr;
		int L = sb.length();
		float pm; // probability mass
		
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		for (int i=0; i<L; i++) {
			Character key = sb.charAt(i);
			Integer val = map.get(key);
			if (val == null) {
				uniq++;
				val = 0;
			}
			val++;
			map.put(key, val);
		}
		pm = ((float)L)/uniq;
		for (Character key : map.keySet()) {
			curr = map.get(key)-pm;
			curr *= curr;
			curr /= pm;
			chi2 += curr;
		}
		return chi2/L;
	}
	public static Float chi2Diff(StringBuffer sb) {
		return Math.abs(ENGLISH_CHI2 - chi2(sb));
	}

}
