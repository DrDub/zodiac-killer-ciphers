package com.zodiackillerciphers.cosine;

public class CosineSimilarityResult implements Comparable {
	public String key;
	public float val;

	/** sort by descending val */
	public int compareTo(Object o) {
		CosineSimilarityResult c = (CosineSimilarityResult) o;
		return Float.compare(c.val, this.val); 
	}
	
	public String toString() {
		return key + ": " + val;
	}
}
