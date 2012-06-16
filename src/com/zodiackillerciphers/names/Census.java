package com.zodiackillerciphers.names;

import java.util.ArrayList;
import java.util.List;

import com.zodiackillerciphers.io.FileUtil;

public class Census {
	
	public static String PREFIX = "docs/census-names";
	
	/** read all the Census names files into memory */
	public static List<Name> readFirstMale() {
		return read("dist.male.first.txt");
	}
	
	public static List<Name> readFirstFemale() {
		return read("dist.female.first.txt");
	}
	
	public static List<Name> readLast() {
		return read("dist.all.last.txt");
	}
	
	public static List<Name> read(String fileName) {
		List<Name> names = new ArrayList<Name>();
		List<String> lines = FileUtil.loadFrom(PREFIX + "/" + fileName);
		for (String line : lines) {
			Name name = nameFrom(line);
			names.add(name);
			//System.out.println(name);
		}
		
		return names;
	}
	
	public static Name nameFrom(String line) {
		Name name = new Name();
		String[] vals = valsFrom(line);
		name.name = vals[0];
		name.frequency = Float.valueOf(vals[1]);
		name.frequencyCumulative = Float.valueOf(vals[2]);
		name.rank = Integer.valueOf(vals[3]);
		return name;
	}
	
	public static String[] valsFrom(String line) {
		String[] split = line.split(" ");
		String[] vals = new String[4];
		int i=0;
		for (String s : split) {
			if ("".equals(s)) continue;
			vals[i++] = s;
		}
		return vals;
	}
	
	public static void main(String[] args) {
		readLast();
	}
}
