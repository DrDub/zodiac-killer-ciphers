package com.zodiackillerciphers.corpus;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zodiackillerciphers.ciphers.Cipher;
import com.zodiackillerciphers.ciphers.Ciphers;
import com.zodiackillerciphers.constraints.Compute;
import com.zodiackillerciphers.constraints.Info;
import com.zodiackillerciphers.constraints.TopHeap;
import com.zodiackillerciphers.io.FileUtil;

/** derive ngram statistics from a large corpus */
public class NgramStats {
	public static void compute(String dirCorpus, String dirTmp, int minLength, int maxLength) {
		Search.makeUnzipDir(dirTmp+"/unzipped");
		List<File> files = Reader.list(dirCorpus);
		System.out.println("Number of files to process: " + files.size());
		long length = 0;
		String text;
		
		Map<Integer, Map<String, Integer>> ngrams = new HashMap<Integer, Map<String, Integer>>(); 
		
		for (int f=0; f<files.size(); f++) {
			float p = 100*((float)f)/files.size();
			System.out.println(f + " of " + files.size() + " (" + (int) p + "%)");
			File file = files.get(f);
			if (file.getName().toLowerCase().endsWith("_h.zip")) {
				continue; // ignoring HTML zips 
			}
			
			try {
				text = Search.read(file, dirTmp);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			if (text == null) continue;
			length += text.length();
			
			StringBuffer converted = FileUtil.convert(text);
			
			for (int i=0; i<converted.length(); i++) {
				for (int L=minLength; L<=maxLength; L++) {
					int j=i+L;
					if (j>converted.length()) break;
					String ngram = converted.substring(i,j);
					//System.out.println(L + ": " + ngram);
					
					Map<String, Integer> map = ngrams.get(L);
					if (map == null) {
						map = new HashMap<String, Integer>();
						ngrams.put(L, map);
					}
					Integer val = map.get(ngram);
					if (val == null) val = 0;
					val++;
					map.put(ngram, val);
				}
			}
			/*if (f > 0 && f%1000 == 0) {
				dump(ngrams);
				ngrams.clear();
			}*/
			
		}
		System.out.println("Ngram stats: ");
		dump(ngrams);
		System.out.println("Done processing files.  Total text length: " + length);

	}
	
	public static void dump(Map<Integer, Map<String, Integer>> ngrams) {
		System.out.println("Dumping ngrams");
		for (Integer L : ngrams.keySet()) {
			Map<String, Integer> map = ngrams.get(L);
			for (String ngram : map.keySet()) {
				Integer val = map.get(ngram);
				System.out.println("ngram," + L + "," + ngram + "," + val);
			}
		}
	}
	
	public static void main(String[] args) {
		compute(args[0], args[1], Integer.valueOf(args[2]), Integer.valueOf(args[3]));
	}
}
