package com.zodiackillerciphers.old;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zodiackillerciphers.ciphers.Ciphers;

public class Quadrants {
	/** transform a cipher grid into quadrants that intersect at the position to the left and above the character
	 * at (i,j)
	 * 
	 * if skip is true, then row i and col j are both excluded from the returned result
	 * 
	 * xforms array specifies rotation/flip operation to be performed on each quadrant.
	 * 		0: R0F0
	 * 		1: R90F0
	 * 		2: R0F1
	 * 		3: R90F1
	 * 
	 * permutation determines a specific permutation of quadrant ordering in the returned result.
	 * 
	 * operations determine how the quadrants are pieced together.
	 *   concatenation:  two quadrants are output in sequence
	 *   interleave: two quadrants are mixed together by outputting alternating lines from each
	 *   	interleave top: align the quadrants at their tops before outputting alternating lines
	 *   	interleave bottom: align the quadrants at their bottoms before outputting alternating lines
	 * 
	 */
	public static String quadrants(String[] cipher, int i, int j, boolean skip) {
		
		String result = "";
		
		// bounds checking
		if (i>cipher.length) return null;
		if (j>cipher[0].length()) return null;
		
		int r,c;
		
		// q1 (upper-left)
		for (int row=0; row<i; row++) {
			for (int col=0; col<j; col++) {
				result += cipher[row].charAt(col);
			}
		}
		
		// q2 (upper-right)
		for (int row=0; row<i; row++) {
			for (int col=j+(skip?1:0); col<cipher[0].length(); col++) {
				result += cipher[row].charAt(col);
			}
		}
		
		// q3 (lower-left) 
		for (int row=i+(skip?1:0); row<cipher.length; row++) {
			for (int col=0; col<j; col++) {
				result += cipher[row].charAt(col);
			}
		}
		
		// q4 (lower-right)
		for (int row=i+(skip?1:0); row<cipher.length; row++) {
			for (int col=j+(skip?1:0); col<cipher[0].length(); col++) {
				result += cipher[row].charAt(col);
			}
		}
		
		return result;
		
	}

	/** extract the 4 quadrants as array of lists of strings */
	public static List<StringBuffer>[] quadrantsAsArray(String[] cipher, int i, int j, boolean skip) {

		List<StringBuffer>[] quads = new ArrayList[4];
		for (int k=0; k<quads.length; k++) quads[k] = new ArrayList<StringBuffer>();
		
		// bounds checking
		if (i>cipher.length) return null;
		if (j>cipher[0].length()) return null;
		
		int r,c;
		
		// q1 (upper-left)
		if (j>0)
			for (int row=0; row<i; row++) {
				quads[0].add(new StringBuffer());
				for (int col=0; col<j; col++) {
					quads[0].get(row).append(cipher[row].charAt(col));
				}
			}
		
		// q2 (upper-right)
		if (cipher[0].length() - (j+(skip?1:0)) > 0)
			for (int row=0; row<i; row++) {
				quads[1].add(new StringBuffer());
				for (int col=j+(skip?1:0); col<cipher[0].length(); col++) {
					quads[1].get(row).append(cipher[row].charAt(col));
				}
			}
		
		// q3 (lower-left)
		if (j>0)
			for (int row=i+(skip?1:0); row<cipher.length; row++) {
				int k = row-(i+(skip?1:0));
				quads[2].add(new StringBuffer());
				for (int col=0; col<j; col++) {
					quads[2].get(k).append(cipher[row].charAt(col));
				}
			}
		
		// q4 (lower-right)
		if (cipher[0].length() - (j+(skip?1:0)) > 0)
			for (int row=i+(skip?1:0); row<cipher.length; row++) {
				int k = row-(i+(skip?1:0));
				quads[3].add(new StringBuffer());
				for (int col=j+(skip?1:0); col<cipher[0].length(); col++) {
					quads[3].get(k).append(cipher[row].charAt(col));
				}
			}
		
		return quads;
	}
	
	/** return max width of given list */
	static int width(List<StringBuffer> list) {
		if (list == null) return 0;
		int max = 0;
		for (StringBuffer sb : list) {
			if (sb == null) continue;
			max = Math.max(sb.length(), max);
		}
		return max;
	}
	/** return height of given list */
	static int height(List<StringBuffer> list) {
		if (list == null) return 0;
		return list.size();
	}
	
	static List<StringBuffer> copyOf(List<StringBuffer> list) {
		if (list == null) return null;
		List<StringBuffer> newList = new ArrayList<StringBuffer>(list.size());
		for (StringBuffer sb : list) newList.add(new StringBuffer(sb));
		return newList;
	}
	
	/** clockwise rotation. */
	static List<StringBuffer> rotate(List<StringBuffer> list, int deg) {
		if (deg == 0) return copyOf(list);
		if (list == null) return null;
		if (list.size() == 0) return copyOf(list);
		List<StringBuffer> result;
		
		int h = height(list);
		int w = width(list);
		
		if (deg == 90) {
			result = new ArrayList<StringBuffer>(width(list));
			for (int i=0; i<w; i++) {
				StringBuffer sb = new StringBuffer();
				for (int j=h-1; j>=0; j--) {
					sb.append(list.get(j).charAt(i));
				}
				result.add(sb);
			}
			return result;
		}

		if (deg == 180) {
			result = new ArrayList<StringBuffer>(width(list));
			for (int i=h-1; i>=0; i--) {
				StringBuffer sb = new StringBuffer();
				for (int j=w-1; j>=0; j--) {
					sb.append(list.get(i).charAt(j));
				}
				result.add(sb);
			}
			return result;
		}
		
		if (deg == 270) {
			result = new ArrayList<StringBuffer>(width(list));
			for (int i=w-1; i>=0; i--) {
				StringBuffer sb = new StringBuffer();
				for (int j=0; j<h; j++) {
					sb.append(list.get(j).charAt(i));
				}
				result.add(sb);
			}
			return result;
		}
		
		return null;
		
	}

	/** flip horizontally. */
	static List<StringBuffer> flip(List<StringBuffer> list, int type) {
		if (type == 0) return copyOf(list);
		if (list == null) return null;
		if (list.size() == 0) return copyOf(list);
		int h = height(list);
		List<StringBuffer> result = new ArrayList<StringBuffer>(h);
		for (int i=0; i<h; i++) {
			StringBuffer sb = new StringBuffer(list.get(i)).reverse();
			result.add(sb);
		}
			
		return result;
	}

	/** concatenate */
	static List<StringBuffer> concat(List<StringBuffer> list1, List<StringBuffer> list2, int type) {
		// 0: concatenate right-top
		// 1: concatenate right-bottom
		// 2: concatenate bottom
		
		int newHeight = type == 2 ? height(list1) + height(list2) : Math.max(height(list1), height(list2));
		
		List<StringBuffer> result = new ArrayList<StringBuffer>(newHeight);
		if (type == 2) {
			result.addAll(list1);
			result.addAll(list2);
			return result;
		}
		
		if (type == 0) {
			for (int i=0; i<height(list1); i++) result.add(new StringBuffer(list1.get(i)));
			for (int i=0; i<list2.size(); i++) {
				if (i<list1.size()) {
					result.get(i).append(new StringBuffer(list2.get(i)));
				} else
					result.add(new StringBuffer(list2.get(i)));
			}
			return result;
		}
		
		if (type == 1) {
			List<StringBuffer> rev1 = reverse(list1);
			List<StringBuffer> rev2 = reverse(list2);
			List<StringBuffer> rev = concat(rev1, rev2, 0);
			return reverse(rev);
		}
		
		return null;
	}

	static List<StringBuffer> reverse(List<StringBuffer> list) {
		List<StringBuffer> result = new ArrayList<StringBuffer>(height(list));
		for (int i=height(list)-1; i>=0; i--) result.add(new StringBuffer(list.get(i)));
		return result;
	}
	

	/** return quadrant concatenations, using the given concatType.
	 * 
     * Let's try just these combinations.  Operators: c2 (concat down),  c0 (concat-right-top), c1 (concat-right-bottom)
     * for quadrants A,B,C,D, combinations are:  
     * 
     * ((A c2 B) c2 C) c2 D
     * (A c0 B) c2 (C c0 D)
     * (A c1 B) c2 (C c1 D)
     * ((A c0 B) c0 C) c0 D
     * ((A c1 B) c1 C) c1 D
	 **/
	static List<StringBuffer> concats(List<StringBuffer>[] quads, int concatType) {
		List<StringBuffer> A = quads[0];
		List<StringBuffer> B = quads[1];
		List<StringBuffer> C = quads[2];
		List<StringBuffer> D = quads[3];
		
		if (concatType == 0)
			return concat(concat(concat(A, B, 2), C, 2), D, 2);
		if (concatType == 1)
			return concat(concat(A, B, 0), concat(C, D, 0), 2);
		if (concatType == 2)
			return concat(concat(A, B, 1), concat(C, D, 1), 2);
		if (concatType == 3)
			return concat(concat(concat(A, B, 0), C, 0), D, 0);
		if (concatType == 4)
			return concat(concat(concat(A, B, 1), C, 1), D, 1);
		throw new IllegalArgumentException("Bad concatType: " + concatType);
	}
	
	/** return quadrant concatenations, using the given treetype, with the given concatenation operators */
	static List<StringBuffer> concats(List<StringBuffer>[] quads, int treeType, int[] types) {

		List<StringBuffer> A = quads[0];
		List<StringBuffer> B = quads[1];
		List<StringBuffer> C = quads[2];
		List<StringBuffer> D = quads[3];
		
		// ((AB)C)D
		if (treeType == 0) 
			return concat(concat(concat(A,B,types[0]),C,types[1]),D,types[2]);
		
		// (A(BC))D
		if (treeType == 1) 
			return concat(concat(A,concat(B,C,types[0]),types[1]),D,types[2]);

		// A((BC)D)
		if (treeType == 2) 
			return concat(A,concat(concat(B,C,types[0]),D,types[1]),types[2]);

		// A(B(CD))
		if (treeType == 3) 
			return concat(A,concat(B,concat(C,D,types[0]),types[1]),types[2]);

		// (AB)(CD)
		if (treeType == 4) 
			return concat(concat(A,B,types[0]),concat(C,D,types[1]),types[2]);

		throw new IllegalArgumentException("Bad treeType: " + treeType);
		
	}
	
	static String toLine(List<StringBuffer> list) {
		StringBuffer sb = new StringBuffer();
		for (StringBuffer s : list) sb.append(s);
		return sb.toString();
	}
	
	
	public static void go() {
		
		// xform = {rot0 noflip, rot90 noflip, rot0 flip, rot90 flip} 
		// quad(i) = {0 ... height}
		// quad(j) = {0 .. width}
		// ignoreBorder = {false, true}		
		// compute counts of repeated ngrams for n = {2 .. 5}
		// 
		// select combinations of (xform, quad(i), quad(j), ignoreBorder) that produce maximum ngram counts
		
		int which = 0;
		int h = which == 0 ? 20 : 24;
		int w = 17;
		String[] ciphers = which == 0 ? Zodiac.cipher340 : Zodiac.cipher408; 

		// array: xform, quad(i), quad(j), ignoreBorder, n, array of results: (count, countunique)
		int[][][][][][] data = new int[4][25][25][2][4][2];
		float[][][][][][] dataNorm = new float[4][25][25][2][4][2];
/*		for (int i=0; i<4; i++) 
			for (int j=0; j<=height; j++)
				for (int k=0; k<=width; k++)
					for (int l=0; l<2; l++)
						data[i][j][k][l] = -1;*/


		for (int x=0; x<ciphers.length; x++) {
			
			// account for rotation
			int width = x % 2 == 0 ? w : h; 
			int height = x % 2 == 0 ? h : w;
			
			String[] cipher = Ciphers.grid(ciphers[x], width);
			
			for (int i=0; i<=height; i++) {
				for (int j=0; j<=width; j++) {
					for (int k=0; k<2; k++) {
						String quad = quadrants(cipher, i, j, k>0);
						//System.out.println("cipher " + x + " i "+ i + " j " + j + " k " + k + " quad " + quad + " same? " + (quad.equals(ciphers[x])));
						if (quad == null) continue;
						for (int n=2; n<6; n++) {
							Map<String, Integer> map = NGrams.countNgrams(quad, n);
							int count = 0;
							int countUnique = 0;
							for (String s : map.keySet()) {
								if (map.get(s) > 1) {
									count += map.get(s);
									countUnique++;
								}
							}
							boolean same = false;
							//if (quad.equals(ciphers[x]) && (i!=0 && j!= 0)) same = true;
							//if (!same) {
								//System.out.println(countUnique + "    " + i+","+j+","+n+","+count+","+countUnique);
								data[x][i][j][k][n-2][0] = count;
								data[x][i][j][k][n-2][1] = countUnique;
							//} else data[x][i][j][k][n-2][0] = -1; // a way to mark dupes
						}
					}
				}
			}
		}
		
		// compute max counts for each n.
		int[][] max = new int[4][2];
		for (int x=0; x<data.length; x++) {
			for (int i=0; i<data[x].length; i++) {
				for (int j=0; j<data[x][i].length; j++) {
					for (int k=0; k<data[x][i][j].length; k++) {
						for (int n=0; n<data[x][i][j][k].length; n++) {
							int val = data[x][i][j][k][n][0];
							if (val > max[n][0]) max[n][0] = val;
							val = data[x][i][j][k][n][1];
							if (val > max[n][1]) max[n][1] = val;
						}
					}
				}
			}
		}
		
		// normalize the counts
		for (int x=0; x<data.length; x++) {
			for (int i=0; i<data[x].length; i++) {
				for (int j=0; j<data[x][i].length; j++) {
					for (int k=0; k<data[x][i][j].length; k++) {
						for (int n=0; n<data[x][i][j][k].length; n++) {
								dataNorm[x][i][j][k][n][0] = max[n][0] == 0 ? 1 : (float) data[x][i][j][k][n][0] / max[n][0];
								dataNorm[x][i][j][k][n][1] = max[n][1] == 1 ? 1 : (float) data[x][i][j][k][n][1] / max[n][1];
						}
					}
				}
			}
		}
		
		// output
		for (int i=0; i<data[0].length; i++) {
			for (int j=0; j<data[0][i].length; j++) {
				for (int x=0; x<data.length; x++) {
					// account for rotation
					int width = x % 2 == 0 ? w : h; 
					int height = x % 2 == 0 ? h : w;
					
					for (int k=0; k<data[x][i][j].length; k++) {
						String[] cipher = Ciphers.grid(ciphers[x], width);
						String quad = quadrants(cipher, i, j, k>0);
						String csv = "";
						float score1 = 0;
						float score2 = 0;
						String ng = ""; String ng2 = "";
						boolean ignore = true;
						for (int n=0; n<data[x][i][j][k].length; n++) {
							ng += "| style=\"text-align: right; border-style: solid; border-width: 1px\"|" + data[x][i][j][k][n][0] + "\n";
							ng += "| style=\"text-align: right; border-style: solid; border-width: 1px\"|" + data[x][i][j][k][n][1] + "\n";
							
							ng2 += data[x][i][j][k][n][0] + "," + data[x][i][j][k][n][1] + ",";
							if (!Float.isNaN(dataNorm[x][i][j][k][n][0])) score1 += dataNorm[x][i][j][k][n][0];
							if (!Float.isNaN(dataNorm[x][i][j][k][n][1])) score2 += dataNorm[x][i][j][k][n][1];
							ng2 += "m"+max[n][0]+",m"+max[n][1]+",d"+dataNorm[x][i][j][k][n][0]+",d"+dataNorm[x][i][j][k][n][1]+",";
							if (dataNorm[x][i][j][k][n][0] > 0.75) ignore = false;
						}
						score1 /= data[x][i][j][k].length;
						score2 /= data[x][i][j][k].length;
						ignore = score2<0.5;
						if (!ignore) { // ignore low scores
							System.out.println("|-valign=\"top\"");
							System.out.println("| style=\"text-align: right; border-style: solid; border-width: 1px\"|" + i);
							System.out.println("| style=\"text-align: right; border-style: solid; border-width: 1px\"|" + j);
							System.out.println("| style=\"text-align: right; border-style: solid; border-width: 1px\"|" + (k>0));
							System.out.println("| style=\"text-align: right; border-style: solid; border-width: 1px\"|" + name(x));
							System.out.println(ng);
							System.out.println("| style=\"text-align: right; border-style: solid; border-width: 1px\"|" + score1);
							System.out.println("| style=\"text-align: right; border-style: solid; border-width: 1px\"|" + score2); 
							//System.out.println("| style=\"text-align: right; border-style: solid; border-width: 1px\"| <pre>" + quad + "</pre>");
						}
						csv += i+","+j+","+(k>0)+","+name(x)+","+ng2 + score1+","+score2;
						String same = quad == null ? "" : quad.equals(Ciphers.cipher[which]) ? "same" : ""; 
						System.out.println(csv + " " + same);
					}
				}
			}
		}
		
		
		
	}
	
	/** rank quadrant permutations using remove_homophones */
	public static void go2() {
		
		Date dateStart = new Date();
		// xform = {rot0 noflip, rot90 noflip, rot0 flip, rot90 flip} 
		// quad(i) = {0 ... height}
		// quad(j) = {0 .. width}
		// ignoreBorder = {false, true}		
		// compute counts of repeated ngrams for n = {2 .. 5}
		// 
		// select combinations of (xform, quad(i), quad(j), ignoreBorder) that produce maximum ngram counts
		
		int which = 0;
		int h = which == 0 ? 20 : 24;
		int w = 17;
		String[] ciphers = which == 0 ? Zodiac.cipher340 : Zodiac.cipher408; 

		// array: xform, quad(i), quad(j), ignoreBorder, remove_homophone scores for L=2..9
		float[][][][][] data = new float[4][25][25][2][8];
		float[][][][][] dataNorm = new float[4][25][25][2][8];

		int tested = 0;
		
		for (int x=0; x<ciphers.length; x++) {
			
			System.out.println(name(x) + "...");
			// account for rotation
			int width = x % 2 == 0 ? w : h; 
			int height = x % 2 == 0 ? h : w;
			
			String[] cipher = Ciphers.grid(ciphers[x], width);
			for (int i=0; i<=height; i++) {
				System.out.println(" - Row "+i);
				for (int j=0; j<=width; j++) {
					for (int k=0; k<2; k++) {
						tested++;
						String quad = quadrants(cipher, i, j, k>0);
						//System.out.println("cipher " + x + " i "+ i + " j " + j + " k " + k + " quad " + quad + " same? " + (quad.equals(ciphers[x])));
						if (quad == null) continue;
						
						
						HomophonesKingBahler hom = new HomophonesKingBahler(quad);
						hom.translateCipher();
						hom.findStrings();
						hom.reduceStrings();
						hom.scoreStrings(HomophonesKingBahler.WHICH_SPECIAL_COUNT);

						for (int len=2; len<10; len++) {
							hom.topDisjoint(HomophonesKingBahler.NUM_DISJOINT, len, false);
							hom.scoreTop();
							data[x][i][j][k][len-2]=hom.topScores[0];
						}
					}
				}
			}
		}
		
		// compute max counts for each n.
		/*int[][] max = new int[4][2];
		for (int x=0; x<data.length; x++) {
			for (int i=0; i<data[x].length; i++) {
				for (int j=0; j<data[x][i].length; j++) {
					for (int k=0; k<data[x][i][j].length; k++) {
						for (int n=0; n<data[x][i][j][k].length; n++) {
							int val = data[x][i][j][k][n][0];
							if (val > max[n][0]) max[n][0] = val;
							val = data[x][i][j][k][n][1];
							if (val > max[n][1]) max[n][1] = val;
						}
					}
				}
			}
		}*/
		
		// normalize the counts
		/*for (int x=0; x<data.length; x++) {
			for (int i=0; i<data[x].length; i++) {
				for (int j=0; j<data[x][i].length; j++) {
					for (int k=0; k<data[x][i][j].length; k++) {
						for (int n=0; n<data[x][i][j][k].length; n++) {
								dataNorm[x][i][j][k][n][0] = max[n][0] == 0 ? 1 : (float) data[x][i][j][k][n][0] / max[n][0];
								dataNorm[x][i][j][k][n][1] = max[n][1] == 1 ? 1 : (float) data[x][i][j][k][n][1] / max[n][1];
						}
					}
				}
			}
		}*/
		
		// output
		for (int i=0; i<data[0].length; i++) {
			for (int j=0; j<data[0][i].length; j++) {
				for (int x=0; x<data.length; x++) {
					// account for rotation
					int width = x % 2 == 0 ? w : h; 
					int height = x % 2 == 0 ? h : w;
					
					for (int k=0; k<data[x][i][j].length; k++) {
						String[] cipher = Ciphers.grid(ciphers[x], width);
						String quad = quadrants(cipher, i, j, k>0);
						String csv = i+","+j+","+(k>0)+","+name(x)+",";
						for (int n=0; n<data[x][i][j][k].length; n++) {
							csv += data[x][i][j][k][n];
							if (n<data[x][i][j][k].length-1) csv +=",";
						}
						System.out.println(csv+","+quad);
					}
				}
			}
		}
		
		Date dateEnd = new Date();
		long e = dateEnd.getTime() - dateStart.getTime();
		float rate = (float) 1000 * tested / e;
		System.out.println("Tested " + tested + " combinations.  Elapsed: " + e + ".  Rate: " + rate + " combinations per second.");

		
	}

	static int[][] permutations = new int[][] {{0, 1, 2, 3}, {0, 1, 3, 2}, {0, 2, 1, 3}, {0, 2, 3, 1}, {0, 3, 1, 2}, {0, 3, 2, 1}, {1, 0, 2, 3}, {1, 0, 3, 2}, {1, 2, 0, 3}, {1, 2, 3, 0}, {1, 3, 0, 2}, {1, 3, 2, 0}, {2, 0, 1, 3}, {2, 0, 3, 1}, {2, 1, 0, 3}, {2, 1, 3, 0}, {2, 3, 0, 1}, {2, 3, 1, 0}, {3, 0, 1, 2}, {3, 0, 2, 1}, {3, 1, 0, 2}, {3, 1, 2, 0}, {3, 2, 0, 1}, {3, 2, 1, 0}};		
	
	
	/** compute h score for lengths 2 through 9.  then compute sum and average. */
	public static float[] measure(String cipher, int limit) {
		float[] data = new float[10];
		HomophonesKingBahler hom = new HomophonesKingBahler(cipher, limit);
		hom.translateCipher();
		hom.findStrings();
		hom.reduceStrings();
		hom.scoreStrings(HomophonesKingBahler.WHICH_SPECIAL_COUNT);

		float total = 0;
		for (int len=2; len<10; len++) {
			hom.topDisjoint(HomophonesKingBahler.NUM_DISJOINT, len, false);
			hom.scoreTop();
			data[len-2]=hom.topScores[0];
			total += data[len-2];
		}
		data[8] = total;
		data[9] = total/8;
		return data;
	}

	/** measure using bestSum  */
	public static int measure2(String cipher, int limit) {
		HomophonesKingBahler hom = new HomophonesKingBahler(cipher, limit);
		hom.translateCipher();
		hom.findStrings();
		hom.reduceStrings();
		hom.scoreStrings(HomophonesKingBahler.WHICH_SPECIAL_COUNT);
		//hom.scoreStringsBestBySymbol();
		//return hom.bestSum;
		return hom.adjustedScore;
	}
	
	/** append data to given string buffer */
	public static void csv(StringBuffer sb, float[] data) {
		for (int i=0; i<data.length; i++) { 
			sb.append(data[i]);
			sb.append(",");
		}
	}
	/** rank quadrant permutations using remove_homophones, expanded version, for a specific row/col (for easier multithreading) */
	public static void go3(int i, int j) {
		
		Date dateStart = new Date();
		
		int which = HomophonesProblem.which;
		int h = which == 0 ? 20 : 24;
		int w = 17;
		String[] cipher = Ciphers.grid(Ciphers.cipher[which], w);
		
		int tested = 0;
		
		int NUM = 85;
		float[] max = new float[NUM];
		String[] maxStrings = new String[NUM];
		for (int m=0; m<max.length; m++) max[m] = -Float.MIN_VALUE;
		
		int maxScore = 0;
		String maxScoreString = null;
		
		for (int k=0; k<2; k++) { // border?
			List<StringBuffer>[] quads = quadrantsAsArray(cipher, i, j, k>0);
			for (int p=0; p<permutations.length; p++) { // permutation
				List<StringBuffer>[] perm = new List[] {
					quads[permutations[p][0]],	
					quads[permutations[p][1]],	
					quads[permutations[p][2]],	
					quads[permutations[p][3]]	
				};
				for (int r1=0; r1<360; r1+=90) { // rotation
					for (int r2=0; r2<360; r2+=90) { // rotation
						for (int r3=0; r3<360; r3+=90) { // rotation
							for (int r4=0; r4<360; r4+=90) { // rotation
								for (int f1=0; f1<2; f1++) { // flip
									for (int f2=0; f2<2; f2++) { // flip
										for (int f3=0; f3<2; f3++) { // flip
											for (int f4=0; f4<2; f4++) { // flip
												List<StringBuffer>[] xformed = new List[] {
													flip(rotate(perm[0], r1), f1),
													flip(rotate(perm[1], r2), f2),
													flip(rotate(perm[2], r3), f3),
													flip(rotate(perm[3], r4), f4),
												};
												for (int c=0; c<5; c++) { // concat types
																
													StringBuffer sb = new StringBuffer();
													sb.append(i);
													sb.append(",");
													sb.append(j);
													sb.append(",");
													sb.append(k);
													sb.append(",");
													sb.append(p);
													sb.append(",");
													sb.append(r1);
													sb.append(",");
													sb.append(r2);
													sb.append(",");
													sb.append(r3);
													sb.append(",");
													sb.append(r4);
													sb.append(",");
													sb.append(f1);
													sb.append(",");
													sb.append(f2);
													sb.append(",");
													sb.append(f3);
													sb.append(",");
													sb.append(f4);
													sb.append(",");
													sb.append(c);
													sb.append(","); // 13																
													
													/*
													String q1 = toLine(xformed[0]);
													String q2 = toLine(xformed[1]);
													String q3 = toLine(xformed[2]);
													String q4 = toLine(xformed[3]);
													
													csv(sb, measure(q1));
													csv(sb, measure(q2));
													csv(sb, measure(q3));
													csv(sb, measure(q4));
													*/

													
													// measure homophone scores for entire new cipher text
													String newCipher = toLine(concats(xformed, c));
													
													//System.out.println(newCipher);
													//float[] dataAll = measure(newCipher, 1000); 
													//csv(sb, dataAll);
													
													int score = measure2(newCipher, 1000); 
													sb.append(score); sb.append(",");
													
													// measure homophone scores for each of the 4 quadrants
													/*
													String q1 = toLine(xformed[0]);
													String q2 = toLine(xformed[1]);
													String q3 = toLine(xformed[2]);
													String q4 = toLine(xformed[3]);
													
													float[] data1, data2, data3, data4;
													float[] datasums = new float[10];
													
													data1 = measure(q1, 30);
													for (int d=0; d<data1.length; d++) datasums[d] += data1[d];
													csv(sb, data1);
													
													data2 = measure(q2, 30);
													for (int d=0; d<data2.length; d++) datasums[d] += data2[d];
													csv(sb, data2);
													
													data3 = measure(q3, 30);
													for (int d=0; d<data3.length; d++) datasums[d] += data3[d];
													csv(sb, data3);
													
													data4 = measure(q4, 30);
													for (int d=0; d<data4.length; d++) datasums[d] += data4[d];
													csv(sb, data4); // 63
													
													csv(sb, datasums); // 73
													// compute mean
													float[] datamean = new float[datasums.length];
													for (int d=0; d<datasums.length; d++) datamean[d] = datasums[d] / 4;
													csv(sb, datamean); // 83
													*/

													// compute ngram counts
													int[] totals = new int[3];
													int[] sums = new int[3];
													
													float[] totalsAll = new float[12]; int ti = 0;
													for (int n=2; n<6; n++) { 
														NGrams.countNgrams(newCipher, n, totals);
														sb.append(totals[0]); totalsAll[ti++] = totals[0];
														sb.append(",");
														sb.append(totals[1]); totalsAll[ti++] = totals[1];
														sb.append(",");
														sb.append(totals[2]); totalsAll[ti++] = totals[2];
														sb.append(",");
														
														sums[0]+=totals[0];
														sums[1]+=totals[1];
														sums[2]+=totals[2];
													} // 95
													sb.append(sums[0]);
													sb.append(",");
													sb.append(sums[1]);
													sb.append(",");
													sb.append(sums[2]); // 98?

													
													System.out.println(sb);
													
													
													// look for new maximums; if found, store the string for display at the end.
													/*int m = 0;
													for (int n=0; n<dataAll.length; n++) { if (dataAll[n] > max[m]) { max[m] = dataAll[n]; maxStrings[m] = sb.toString(); } m++; }
													for (int n=0; n<data1.length; n++) { if (data1[n] > max[m]) { max[m] = data1[n]; maxStrings[m] = sb.toString(); } m++; }
													for (int n=0; n<data2.length; n++) { if (data2[n] > max[m]) { max[m] = data2[n]; maxStrings[m] = sb.toString(); } m++; }
													for (int n=0; n<data3.length; n++) { if (data3[n] > max[m]) { max[m] = data3[n]; maxStrings[m] = sb.toString(); } m++; }
													for (int n=0; n<data4.length; n++) { if (data4[n] > max[m]) { max[m] = data4[n]; maxStrings[m] = sb.toString(); } m++; }
													for (int n=0; n<datasums.length; n++) { if (datasums[n] > max[m]) { max[m] = datasums[n]; maxStrings[m] = sb.toString(); } m++; }
													for (int n=0; n<datamean.length; n++) { if (datamean[n] > max[m]) { max[m] = datamean[n]; maxStrings[m] = sb.toString(); } m++; }
													for (int n=0; n<totalsAll.length; n++) { if (totalsAll[n] > max[m]) { max[m] = totalsAll[n]; maxStrings[m] = sb.toString(); } m++; }
													for (int n=0; n<sums.length; n++) { if (sums[n] > max[m]) { max[m] = sums[n]; maxStrings[m] = sb.toString(); } m++; }
													*/
													if (score > maxScore) {
														maxScore = score;
														maxScoreString = sb.toString();
													}
													
													tested++;
													
													//long diff = (new Date().getTime() - dateStart.getTime());
													//System.out.println("Rate: " + 1000*tested/diff);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} 
		
		/*
		for (int m=0; m<maxStrings.length; m++) {
			System.out.println("MAX FOR FIELD [" + m + "]: " + maxStrings[m]);
		}*/
		
		/*
		StringBuffer sb = new StringBuffer();
		csv(sb, max);
		System.out.println("MAXIMUMS: " + sb);*/
		
		System.out.println("MAXIMUM bestSum: " + maxScoreString);
		
		Date dateEnd = new Date();
		long e = dateEnd.getTime() - dateStart.getTime();
		float rate = (float) 1000 * tested / e;
		System.out.println("Tested " + tested + " combinations.  Elapsed: " + e + ".  Rate: " + rate + " combinations per second.");

		
	}
	
	static String name(int x) {
		if (x==0) return "R0F0";
		if (x==1) return "R90F1";
		if (x==2) return "R0F1";
		if (x==3) return "R90F1";
		return "Huh?";
	}
	
	/** field names for go3 test */
	static String nameFrom(int field) {
		if (field==0) return "i";
		if (field==1) return "j";
		if (field==2) return "k";
		if (field==3) return "p";
		if (field==4) return "r1";
		if (field==5) return "r2";
		if (field==6) return "r3";
		if (field==7) return "r4";
		if (field==8) return "f1";
		if (field==9) return "f2";
		if (field==10) return "f3";
		if (field==11) return "f4";
		if (field==12) return "c";
		throw new RuntimeException("Bad field " + field);
	}
	
	static void testQuad() {
		String[] cipher = Ciphers.grid(Zodiac.cipher340[0], 17);
		for (int i=0; i<20; i++) {
			for (int j=0; j<17; j++) {
				for (int k=0; k<2; k++) {
					String quad = quadrants(cipher, i, j, k>0);
					System.out.println(quad);
				}
			}
		}
	}
	static void dump(List<StringBuffer>[] quad) {
		for (int i=0; i<quad.length; i++) {
			System.out.println("Quadrant " + i + ": ");
			dump(quad[i]);
		}
	}
	static void dump(List<StringBuffer> list) {
		for (int r=0; r<list.size(); r++) System.out.println(list.get(r));
	}
	
	static void testQuadArray() {
		String[] cipher = Ciphers.grid(Zodiac.cipher340[0], 17);
		for (int i=0; i<20; i++) {
			for (int j=0; j<17; j++) {
				for (int k=0; k<2; k++) {
					List<StringBuffer>[] quad = quadrantsAsArray(cipher, i, j, k>0);
					System.out.println(i+","+j+","+k);
					dump(quad);
				}
			}
		}
		List<StringBuffer>[] quad = quadrantsAsArray(cipher, 4, 5, false);
		System.out.println("\ntesting quad ");
		dump(quad[0]);
		System.out.println("\n");
		dump(quad[1]);
		System.out.println("\n");
		dump(quad[2]);
		System.out.println("\n");
		dump(quad[3]);
		System.out.println("\n");
		System.out.println("\nrot 0");
		dump(rotate(quad[0], 0));
		System.out.println("\nrot 90");
		dump(rotate(quad[0], 90));
		System.out.println("\nrot 180");
		dump(rotate(quad[0], 180));
		System.out.println("\nrot 270");
		dump(rotate(quad[0], 270));
		System.out.println("\nflip");
		dump(flip(quad[0], 1));
		
		System.out.println("\ntesting concats for ");
		dump(quad[0]);
		System.out.println("\n");
		dump(quad[3]);
		
		System.out.println("\nconcat 0");
		dump(concat(quad[0], quad[3], 0));
		System.out.println("\nconcat 1");
		dump(concat(quad[0], quad[3], 1));
		System.out.println("\nconcat 2");
		dump(concat(quad[0], quad[3], 2));
		

		System.out.println("\nOther order.  concat 0");
		dump(concat(quad[3], quad[0], 0));
		System.out.println("\nconcat 1");
		dump(concat(quad[3], quad[0], 1));
		System.out.println("\nconcat 2");
		dump(concat(quad[3], quad[0], 2));
		
		
		Set<String> set = new HashSet<String>();
		int count = 0;
		System.out.println("\n");
		for (int a = 0; a<5; a++) {
			for (int b=0; b<3; b++) {
				for (int c=0; c<3; c++) {
					for (int d=0; d<3; d++) {
						System.out.println("\n"+a+","+b+","+c+","+d);
						String result = toLine(concats(quad, a, new int[] {b,c,d}));
						System.out.println(result);
						if (set.contains(result)) System.out.println("Already seen");
						set.add(result);
						count++;
					}
				}
			}
		}
		System.out.println("counted " + count + ", unique " + set.size());

		
		dump(concats(quad, 0, new int[] {0,0,0}));
		dump(concats(quad, 0, new int[] {0,0,1}));
		
	}
	
	public static String makeCipher(int whichCipher, int i, int j, int k, int p, int r1, int r2, int r3, int r4, int f1, int f2, int f3, int f4, int c) {
		
		String[] cipher = Ciphers.grid(Ciphers.cipher[whichCipher], 17);
		List<StringBuffer>[] quads = quadrantsAsArray(cipher, i, j, k>0);

		//for (int x=0; x<quads.length; x++) { dump(quads[x]); System.out.println(" "); }
		
		List<StringBuffer>[] perm = new List[] {
				quads[permutations[p][0]],	
				quads[permutations[p][1]],	
				quads[permutations[p][2]],	
				quads[permutations[p][3]]	
			};

		//for (int x=0; x<perm.length; x++) { dump(perm[x]); System.out.println(" "); }

		List<StringBuffer>[] xformed = new List[] {
			flip(rotate(perm[0], r1), f1),
			flip(rotate(perm[1], r2), f2),
			flip(rotate(perm[2], r3), f3),
			flip(rotate(perm[3], r4), f4),
		};
		
		//System.out.println("After rots/flips");
		//for (int x=0; x<xformed.length; x++) { dump(xformed[x]); System.out.println(" "); }
		
		List<StringBuffer> cats = concats(xformed, c);
		//System.out.println("cats");
		//dump(cats);
		
		String newCipher = toLine(cats);
		//System.out.println("buh " + newCipher);
		return newCipher;
	}
	
	
	
	public static void testResult(int whichCipher, int i, int j, int k, int p, int r1, int r2, int r3, int r4, int f1, int f2, int f3, int f4, int c) {
		String newCipher = Quadrants.makeCipher(whichCipher, i, j, k, p, r1, r2, r3, r4, f1, f2, f3, f4, c);
		System.out.println("cipher " + newCipher);
		//HomophonesProblem.testCipher(newCipher);
		HomophonesKingBahler.testFindStrings(newCipher);
	}

	
	public static void main(String[] args) {
		//String quad = quadrants(Zodiac.grid(1, 17), 24, 17);
		//System.out.println(quad);
		//go();
		//go2();
		//testQuad();
		//testQuadArray();
		/*float[] data = measure(Zodiac.cipher[0]);
		for (int i=0; i<data.length; i++) 
			System.out.print(data[i]+",");
			*/
		//go3(0, 0);
		go3(Integer.valueOf(args[0]), Integer.valueOf(args[1]));
		//testResult(20, 3, 0, 1, 0, 0, 0, 90, 0, 0, 0, 0, 4); // best 408
		//testResult(17,9,1,15,0,0,180,0,0,1,0,0,4); // best 340
		
		
		/*
		Map<String, Integer> map = NGrams.countNgrams(Zodiac.cipher[1], 2);
		for (String s : map.keySet()) {
			if (map.get(s) > 1)
			System.out.println(s+":" + map.get(s));
		}*/
	}
	
	
}
