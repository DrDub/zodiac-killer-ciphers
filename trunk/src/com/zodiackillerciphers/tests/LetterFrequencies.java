package com.zodiackillerciphers.tests;

public class LetterFrequencies {
	public static float[] frequencies = new float[] {
		.08167f, .01492f, .02782f, .04253f, .12702f, .02228f, .02015f, .06094f, .06966f, .00153f, .00772f, .04025f, .02406f, .06749f, .07507f, .01929f, .00095f, .05987f, .06327f, .09056f, .02758f, .00978f, .02360f, .00150f, .01974f, .00074f		
	};
	
	public static float computeIOC(int n) {
		float sum = 0;
		for (int i=0; i<frequencies.length; i++) {
			sum += Math.pow(frequencies[i], n);
		}
		//System.out.println(sum);
		return sum;
	}
	
	public static void main(String[] args) {
		System.out.println(computeIOC(Integer.valueOf(args[0])));
	}
}
