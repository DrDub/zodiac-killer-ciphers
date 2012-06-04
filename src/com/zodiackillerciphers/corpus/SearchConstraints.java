package com.zodiackillerciphers.corpus;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zodiackillerciphers.ciphers.Cipher;
import com.zodiackillerciphers.ciphers.Ciphers;
import com.zodiackillerciphers.constraints.Compute;
import com.zodiackillerciphers.constraints.Info;
import com.zodiackillerciphers.io.FileUtil;

public class SearchConstraints {
	/** search corpus using pairwise constraints */
	public static void search(String cipher, String solution, String dirCorpus, String dirTmp, int maxLength, float maxProbability) {
		System.out.println("dirCorpus " + dirCorpus);
		System.out.println("dirTmp " + dirTmp);
		System.out.println("cipher " + cipher);
		System.out.println("solution " + solution);
		System.out.println("maxLength " + maxLength);
		System.out.println("maxLength " + maxLength);
		Map<Integer, List<Info>> map = Compute.constraints(cipher, solution, maxLength, maxProbability);

		Search.makeUnzipDir(dirTmp+"/unzipped");
		
		List<File> files = Reader.list(dirCorpus);
		System.out.println("Number of files to process: " + files.size());
		long length = 0;
		String text;
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
			// convert text to uppercase alphabet stream
			StringBuffer converted = FileUtil.convert(text);
			for (int i=0; i<converted.length(); i++) {
				
				for (Integer len : map.keySet()) {
					if (i+len > converted.length()) continue;
					List<Info> list = map.get(len);
					String substring = converted.substring(i, i+len);
					if (spurious(substring)) continue; // ignore putative solutions that have too many repeated plaintext letters
					for (Info info : list) {
						if (info.fit(substring)) {
							System.out.println("Constraint match: " + file.getAbsolutePath() + ", " + substring + ", " + info + match(info, substring, solution));
						}
					}
				}
				
			}
		}
		
		System.out.println("Done processing files.  Total text length: " + length);

	}
	
	public static boolean spurious(String substring) {
		if (substring == null) return false;
		Set<Character> set = new HashSet<Character>();
		for (int i=0; i<substring.length(); i++) {
			set.add(substring.charAt(i));
			if (set.size() > 2) return false;
		}
		return set.size() < 3;
	}
	
	/** determine correctness of putative solution */
	public static String match(Info info, String putative, String solution) {
		if (solution == null) return "";
		
		int count = 0;
		for (int i=0; i<info.substring.length(); i++) {
			if (putative.charAt(i) == solution.charAt(i+info.index)) count++;
		}
		return ", " + solution.substring(info.index, info.index+putative.length()) + ", " + ((float)count)/info.substring.length();
	}
	
	
//	public static void search(String dirCorpus, String dirTmp, String cipher, String solution, int maxLength, float maxProbability) {
	public static void main(String[] args) {
		Cipher c = Ciphers.cipher[Integer.valueOf(args[0])];
		search(c.cipher, c.solution == null ? null : c.solution.toUpperCase(), args[1], args[2], Integer.valueOf(args[3]), Float.valueOf(args[4]));
	}
	
}
