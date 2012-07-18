package com.zodiackillerciphers.old;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Search for repeated fragments.  A fragment is a snippet of cipher text, that repeats in at least its first or last symbol, and possibly
 * symbols in between.
 */
public class Fragments {
	public static void search(String ciphertext) {
		
		FragmentInfo.countsMap = HomophonesProblem.countsMapFor(ciphertext);
		FragmentInfo.L = ciphertext.length();
		
		Map<String, FragmentInfo> map = new HashMap<String, FragmentInfo>(); // track fragment info for each fragment template
		for (int n=2; n<ciphertext.length()/2; n++) {
			//System.out.println("Searching for fragments of length " + n);
			
			for (int i=0; i<=ciphertext.length()-n; i++) {
				String sub1 = ciphertext.substring(i, i+n);
				for (int j=i+n; j<=ciphertext.length()-n; j++) {
					String sub2 = ciphertext.substring(j,j+n);
					if (sub2.charAt(0) != sub1.charAt(0)) continue;
					if (sub2.charAt(sub2.length()-1) != sub1.charAt(sub1.length()-1)) continue;
					
					//System.out.println(sub1 + ","+sub2 + ","+i+","+j);
					
					// we match at the first and last symbols.  derive the fragment template from the two substrings.
					String frag = fragmentFrom(sub1, sub2);
					
					// skip ahead if we already counted this fragment before.
					if (map.get(frag) != null) {
						if (map.get(frag).positions.contains(j)) continue;
					}
					
					// otherwise, create a new instance of FragmentInfo, or update the existing one
					FragmentInfo fragInfo = map.get(frag);
					if (fragInfo == null) {
						fragInfo = new FragmentInfo();
						fragInfo.fragment = frag;
					}
					if (!fragInfo.positions.contains(i)) {
						fragInfo.positions.add(i);
						fragInfo.fragments.add(sub1);
					}
					if (!fragInfo.positions.contains(j)) {
						fragInfo.positions.add(j);
						fragInfo.fragments.add(sub2);
					}
					map.put(frag, fragInfo);
				}
			}
		}
		
		dump(map);
	}
	
	/** dump map in increasing order of wildcard length */
	static void dump(Map<String, FragmentInfo> map) {
		int max = 0;
		for (String key : map.keySet()) max = Math.max(max, map.get(key).wildcardLength());
		
		for (int i=0; i<=max; i++) {
			
			if (FragmentInfo.forWiki) {
				wikiStart(i);
			}
			else System.out.println("======= WILDCARD LENGTH [" + i + "] =======");
			
			List<FragmentInfo> list = new ArrayList<FragmentInfo>();
			for (String key : map.keySet()) if (map.get(key).wildcardLength() == i) {
				list.add(map.get(key));
			}
			Collections.sort(list);
			for (FragmentInfo info : list) System.out.println(info.toString());
			
			if (FragmentInfo.forWiki) wikiEnd();
		}
	}
	
	static void wikiStart(int i) {
		System.out.println("=== Wildcard length: " + i + " ===");
		System.out.println("{|class=\"wikitable sortable\" style=\"border-collapse: separate; border-spacing: 0; border-width: 1px; border-style: solid; border-color: #000; padding: 0\"");
		System.out.println("|-valign=\"top\"");
		System.out.println("! style=\"border-style: solid; border-width: 1px\"|  Pattern");
		System.out.println("! style=\"border-style: solid; border-width: 1px\"|  Positions");
		System.out.println("! style=\"border-style: solid; border-width: 1px\"|  Fragments");
		System.out.println("! style=\"border-style: solid; border-width: 1px\"|  Order");
		System.out.println("! style=\"border-style: solid; border-width: 1px\"|  Reps");
		System.out.println("! style=\"border-style: solid; border-width: 1px\"|  Probability");
	}
	
	static void wikiEnd() {
		System.out.println("|}");
	}
	
	
	/** generate fragment template from given strings.  replace non-matching symbols with wildcards */
	public static String fragmentFrom(String sub1, String sub2) {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<sub1.length(); i++) {
			if (sub1.charAt(i) == sub2.charAt(i)) sb.append(sub1.charAt(i));
			else sb.append("?");
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		//search(Ciphers.cipher[5].cipher);
		//search("HNBS_p#(d-U2zly|R+|>Epyp985G<z+<6GBcc+FMR+:pMR+2MlRc9FX.T)kD>Bc7+^KJ+U/lSN13+WdHp(M^zFqfbV5Ry^*zLCWNl#+ltl%j++tJ#f:B1z<p^OU8jO;#Z^E|+54K6W7kV%Z*d-2ORJ|*N29(CctSPDGV|*U+2+D5|4CO<PBzkWW35dc_FOYT5bEp+O_Z|Y(pFCXNBpB4F.>^FSYO1.)OPkGYc7pMBcV.lHO8L<L++FVzy<b.cVUfWTBAT*#+&>.+AFT+(4ZMB/*|GKzR42z@6BM&;t5q|(-K2fHKkDLL4yKB8+-G))C;d)J2/(|9K-OFR++2Lpc+");
		//search("d2GTL1|kPV^lp>REH)fK*<.YWD%O#(B+pNJHz#L)(WGZU+Mc:yB2KR++Op3V*8l^7ppS/k4&+PF5|djtz+M9_(D2>FkCd*-OlF^R8p|Lz.VGXcU2;%qK+5#9L@+zYN_+O#jfJ2G(K46AycBF2RZ+b+M<d-yBF<7pO+J^+VUlz-OKMTbpBYD|Et5/R+UFB&+.M4T5*|JRlc<2R8;(cBF5|N+#yS96z++t4Vc.b425f^NFGl+-5ZUV>EC94:*1XBy2GqMf.^pO(KBz3.c|L)|BWlF+<C61L+TcRp)(/THSOPcWzCW)++cC-*BOY_Bt7<WdkF|+;K|A8OZzSkpNHDM>");
		
		// http://www.ciphermysteries.com/2011/11/09/the-lady-magdalene-de-lancey-ciphers-2
		//search("shtesirreshteltdtyetoogdterldcofcshtsrglateshedeshdgtrshitdhlyskbtwisterhdgthisthatlydsrsofseedltilingsorgtrdbghiliglingsolersafftchiygfetdjsitlotltertelrleslitypdgrebrditlosefdrelrsethligksofillttleselrgridthjdbgdtehtetsisovdlgansdtofdrhtghldsdbtdilijchesoerdreisgdrdtatderihjsthitsibitbdersjchrtdthsdpsprotisshojosbtjeexptrtiyleitltsbijte");
		// http://en.wikipedia.org/wiki/D'Agapeyeff_cipher
		search("75628285916291648164917485846474748284838163818174748262647583828491757465837575759363656581638175857575646282928574638275748381658184856485648585638272628362818172816463758281648363828581636363047481919184638584656485656294626285918591749172756465757165836264748182846282649181936562648484918385749181657274838385828364627262656283759272638282727283828584758281837284628283758164757485816292000");
		
	}
}
