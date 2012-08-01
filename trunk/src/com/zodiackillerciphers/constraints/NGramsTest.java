package com.zodiackillerciphers.constraints;

import java.util.List;

import com.zodiackillerciphers.ciphers.Ciphers;
import com.zodiackillerciphers.corpus.SearchConstraints;
import com.zodiackillerciphers.io.FileUtil;

public class NGramsTest {
	
	public static void test1() {
		String cipher = Ciphers.cipher[1].cipher;

		List<String> list = FileUtil.loadFrom("docs/test3.txt");
		
		for (String line : list) {
			String[] split = line.split(",");
			int pos = Integer.valueOf(split[1].substring(1));
			String sub = split[2].substring(1);
			String plain = split[3].substring(1);
			Info info1 = new Info(sub, pos);
			info1.plaintext = plain;
			float score1 = SearchConstraints.score(info1, cipher, false, false);
			float score2 = SearchConstraints.score(info1, cipher, false, true);
			float score3 = SearchConstraints.score(info1, cipher, true, false);
			float score4 = SearchConstraints.score(info1, cipher, true, true);
			//System.out.println(score1 + ", " + score2 + ", " + score3 + ", " + score4 + ", " + info1);
		}
	}
	
	public static void test2() {
		String cipher = Ciphers.cipher[1].cipher;

		List<String> list = FileUtil.loadFrom("docs/constraint-search-408-new-3-results.txt");
		
		for (String line : list) {
			if (!line.contains("- Info:")) continue;
			String[] split = line.split(",");
			float prob = Float.valueOf(split[5]);
			float score = Float.valueOf(split[8]);
			float val = score/prob;
			System.out.println(val + ", " + line);
		}
	}
	public static void main(String[] args) {
		test2();
	}
}
