package com.zodiackillerciphers.ciphers;

public class Cipher {
	public String cipher;
	public String solution;
	public String description;
	
	public Cipher(String description, String cipher, String solution) {
		super();
		this.cipher = cipher;
		this.description = description;
		this.solution = solution;
		check();
	}
	public void check() {
		if (this.cipher.contains("?"))
			throw new RuntimeException("This cipher uses special character '?' that will break the ngram counts. " + this.toString());
	}
	public String toString() {
		return "[" + description + "] [" + cipher + "] [" + solution + "]";
	}

}
