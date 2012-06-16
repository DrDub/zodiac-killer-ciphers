package com.zodiackillerciphers.names;

public class Name {
	public String name;
	public float frequency;
	public float frequencyCumulative;
	public int rank;
	
	public String toString() {
		return name + ", " + frequency + ", " + frequencyCumulative + ", " + rank;
	}
}
