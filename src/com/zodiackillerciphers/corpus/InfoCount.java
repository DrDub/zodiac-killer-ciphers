package com.zodiackillerciphers.corpus;


public class InfoCount implements Comparable {
	public int count = 0;
	public String substring;
	@Override
	public int compareTo(Object o) {
		InfoCount a = this;
		InfoCount b = (InfoCount) o;
		
		if (a.substring.length() < b.substring.length()) return 1;
		if (a.substring.length() > b.substring.length()) return -1;
		
		if (a.count < b.count) return 1;
		if (a.count > b.count) return -1;
		return 0;
	}
	
	public String toString() {
		return "Length: '''" + substring.length() + "'''  Substring: '''" + substring + "'''  Matches: '''" + count + "'''";
	}
}
