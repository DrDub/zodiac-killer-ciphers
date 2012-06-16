package com.zodiackillerciphers.anagram;

import java.util.Arrays;

public class Anagrams {
	/** return true if the given word can be found in the given string */
	public static boolean anagram(String word, String str) {
		char[] c1 = word.toCharArray();
		char[] c2 = str.toCharArray();
		Arrays.sort(c1); Arrays.sort(c2);
		//System.out.println(c1);
		//System.out.println(c2);
		int i1 = 0; int i2 = 0;
		int total = 0;
		
		while (i1 < word.length() && i2 < str.length()) {
			while (c1[i1] != c2[i2]) {
				i2++;
				if (i2 > str.length()-1) break;
			}
			if (i2 > str.length()-1) break;
			
			total++; i1++; i2++;
			//System.out.println("total " + total);
		}
		if (total == word.length()) return true;
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println(anagram("george bush", "he bugs gore"));
	}
}
