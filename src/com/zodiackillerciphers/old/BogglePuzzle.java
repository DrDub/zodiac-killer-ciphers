package com.zodiackillerciphers.old;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** searches a grid of characters for all possible Boggle-style formations of words */
public class BogglePuzzle {

	static String wordsFile = "/Users/doranchak/projects/work/java/zodiac/letters/wordlist.txt";

	static Map<String, Integer> prefixCounts = new HashMap<String, Integer>();
	static Set<String> words = new HashSet<String>();

	static Set<String> wordsZodiac = Words.lettersWords();
	
	static String[] puzzle = {
		"ilikekillingpeopl",
		"ebecauseitissomuc",
		"hfunitismorefunth",
		"ankillingwildgame",
		"intheforrestbecau",
		"semanisthemoatdan",
		"gertueanamalofall",
		"tokillsomethinggi",
		"vesmethemoatthril",
		"lingexperenceitis",
		"evenbetterthanget",
		"tingyourrocksoffw",
		"ithagirlthebestpa",
		"rtofitiathaewheni",
		"dieiwillbereborni",
		"nparadicesndallth",
		"eihavekilledwillb",
		"ecomemyslavesiwil",
		"lnotgiveyoumyname",
		"becauseyouwilltry",
		"tosloidownorstopm",
		"ycollectingofslav",
		"esformyafterlifee",
		"beorietemethhpiti"		
	};
	
	static String[] puzzle4 = {
		"beensolvedbypolic",
		"emilitaryoramateu",
		"rcodebreakersifit",
		"isaconventionalco",
		"dewithaconvention",
		"alsolutionitveryl",
		"ikleywouldhavebee",
		"nsolvedbynowwhati",
		"fthefirststagesol",
		"utionisnotacodebu",
		"tawordpuzzlewould",
		"atauntingserialki",
		"llermakeawordpuzz",
		"lethebtkdidseehtt",
		"pwwwtabloidcolumn",
		"combtkpuzzlehtmlt",
		"herewerealsopossi",
		"blewordpuzzleelem",
		"entsintheblackdah",
		"liacaseandtheyapp"

	};
	static String[] puzzle3 = {
		"herceanbigivethem",
		"helltoobtsalteseh",
		"lse_iluehstheolhs",
		"seeanamebweollrke",
		"seilllfmiapillsga",
		"emrnpaodemagpcett",
		"oalstbneu_shbllei",
		"thesefoolshallsee",
		"mtilklerepl_saask",
		"dlaublnsloeatplsd",
		"ulraaleitalektiso",
		"et_arsieataillllp",
		"laessolhiapl_tnmr",
		"ahphneaeakl_balll",
		"slsveeseaecbueadl",
		"i_lwllstoenleithe",
		"r_tleaeatlpaslihe",
		"llhsals_ioshtathe",
		"ipgmstallsaoleda_",
		"cithhegsleomaisnl"		
	};
	
	static String[] puzzle1 = {
		"tegkeanbiiaytoaro",
		"eeelnlnlneurhrsdh",
		"lljvheieaenhtldtf",
		"aeeanasrbneneegsr",
		"yihedrxoatsiepoik",
		"esgnsanfrociskrnn",
		"ltesml rivoabrdta",
		"narfdxlneyeudesti",
		"ohhebeegrslvlweos",
		"fdaibenfeneahsllf",
		"iegktreanulebohsn",
		"rhvagfartoohrepls",
		"deialleeatslvn ag",
		"aasendtrobrvboree",
		"lloyrjoicekbietfe",
		"avrndlsnnenrdhmar",
		"gvoetyechesaelaht",
		"eehecdevinatoknhe",
		"asioeharlyunlrfcv",
		"khnteeiadenswas e"
	};
	static String[] puzzle2 = {
		"lksdjflksdjf",
		"iueoqioopqop",
		"aenmaneameee",
		"oiqwpeipoqpq",
		"nmznemneoqia",
		"aeeaibneudof"
	};
	
	static char[][] grid;
	
	static void add(String prefix) {
		Integer val = prefixCounts.get(prefix);
		if (val == null)
			val = 1;
		else
			val++;
		prefixCounts.put(prefix, val);
	}

	static void addWord(String word) {
		words.add(word);
		for (int i = 1; i < word.length(); i++) {
			add(word.substring(0, i));
		}
	}

	static public String read() {
		// ...checks on aFile are elided
		StringBuffer contents = new StringBuffer();

		// declared here only to make visible to finally clause
		BufferedReader input = null;
		try {
			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			input = new BufferedReader(new FileReader(new File(wordsFile)));
			String line = null; // not declared within while loop
			/*
			 * readLine is a bit quirky : it returns the content of a line MINUS
			 * the newline. it returns null only for the END of the stream. it
			 * returns an empty String if two newlines appear in a row.
			 */
			while ((line = input.readLine()) != null) {
				addWord(line);
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (input != null) {
					// flush and close both "input" and its underlying
					// FileReader
					input.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return contents.toString();
	}

	public static void makePuzzle() {
		grid = new char[puzzle.length][puzzle[0].length()];
		for (int row=0; row<grid.length; row++) {
			for (int col=0; col<grid[0].length; col++) {
				grid[row][col] = puzzle[row].charAt(col);
			}
		}
	}
	
	public static void solve(boolean zodiacOnly) {
		
		boolean straightOnly = false;
		
		int sum = 0;
		for (int len=12; len>3; len--) {
			//System.out.println("- Searching for words of length [" + len + "]...");
			Integer count = 0;
			for (int row=0; row<grid.length; row++) {
				for (int col=0; col<grid[0].length; col++) {
					Set<String> path = new HashSet<String>();
					
					if (straightOnly) {
						count += solve(row, col, row, col, new StringBuffer(), len, path, 0, zodiacOnly);
						count += solve(row, col, row, col, new StringBuffer(), len, path, 1, zodiacOnly);
						count += solve(row, col, row, col, new StringBuffer(), len, path, 2, zodiacOnly);
						count += solve(row, col, row, col, new StringBuffer(), len, path, 3, zodiacOnly);
						//count += solve(row, col, row, col, new StringBuffer(), len, path, 4, zodiacOnly);  //left to right
						count += solve(row, col, row, col, new StringBuffer(), len, path, 5, zodiacOnly);
						count += solve(row, col, row, col, new StringBuffer(), len, path, 6, zodiacOnly);
						count += solve(row, col, row, col, new StringBuffer(), len, path, 7, zodiacOnly);
					} else {
						count += solve(row, col, row, col, new StringBuffer(), len, path, null, zodiacOnly);
					}
				}
			}
			sum += count;
			//System.out.println("Found [" + count + "] words of length [" + len + "].");
		}
		System.out.println("total words: " + sum);
	}

	public static boolean isZodiacWord(String word) {
		return wordsZodiac.contains(word);
	}
	// if direction is null, allow any direction.  otherwise, only search in that direction.
	public static int solve(int row, int col, int startRow, int startCol, StringBuffer prefix, int length, Set<String> path, Integer direction, boolean zodiacOnly) {
		if (row < 0 || row > grid.length-1) return 0;
		if (col < 0 || col > grid[0].length-1) return 0;
		prefix.append(grid[row][col]);
		//System.out.println(row+","+col+","+startRow+","+startCol+","+prefix+","+length);
		if (prefixCounts.get(prefix.toString()) == null) return 0; // stop search because nothing matches this prefix.
		int count = 0;
		path.add(row+","+col);
		if (prefix.length() == length)
			if (words.contains(prefix.toString())) {
				boolean isZ = isZodiacWord(prefix.toString());
				if (!zodiacOnly || isZ) {
					System.out.println("found [" + prefix + "] at row " + (startRow+1) + ", " + (isZodiacWord(prefix.toString()) ? "ZODIAC WORD, " : "") + "column " + (startCol+1) + ", path " + dumpPath(path));
					count++;
				}
			}
		
		if (direction == null || direction == 0) count += solve(row-1, col-1, startRow, startCol, new StringBuffer(prefix), length, path, direction, zodiacOnly);
		if (direction == null || direction == 1) count += solve(row-1, col, startRow, startCol, new StringBuffer(prefix), length, path, direction, zodiacOnly);
		if (direction == null || direction == 2) count += solve(row-1, col+1, startRow, startCol, new StringBuffer(prefix), length, path, direction, zodiacOnly);
		if (direction == null || direction == 3) count += solve(row, col-1, startRow, startCol, new StringBuffer(prefix), length, path, direction, zodiacOnly);
		if (direction == null || direction == 4) count += solve(row, col+1, startRow, startCol, new StringBuffer(prefix), length, path, direction, zodiacOnly);
		if (direction == null || direction == 5) count += solve(row+1, col-1, startRow, startCol, new StringBuffer(prefix), length, path, direction, zodiacOnly);
		if (direction == null || direction == 6) count += solve(row+1, col, startRow, startCol, new StringBuffer(prefix), length, path, direction, zodiacOnly);
		if (direction == null || direction == 7) count += solve(row+1, col+1, startRow, startCol, new StringBuffer(prefix), length, path, direction, zodiacOnly);
		return count;
	}
	
	public static String dumpPath(Set<String> path) {
		if (path == null) return "";
		StringBuffer sb = new StringBuffer();
		for (String p : path) {
			sb.append(p).append(" ");
		}
		return sb.toString();
	}
	
	public static void testAK() {
		String excerpt = "btkwordpuzzleunmarkedbtkwordpuzzlepartiallysolvedproposedzodiacfirststagesolutionvirginunmarkedzodiacproposedfirststagesolutionasawordpuzzle" + 
		"markeddoranchakandthebigzandeveryonethebestfbisupercomputershavenotsolvedthezodiacandithasnotbeensolvedbypolicemilitaryoramateurcodebreakers" + 
		"ifitisaconventionalcodewithaconventionalsolutionitverylikleywouldhavebeensolvedbynowwhatifthefirststagesolutionisnotacodebutawordpuzzlewould" + 
		"atauntingserialkillermakeawordpuzzlethebtkdidseehttpwwwtabloidcolumncombtkpuzzlehtmltherewerealsopossiblewordpuzzleelementsintheblackdahliac" + 
		"aseandtheyappearincrimemoviestvshowspulpnovelsandcomicbooksandsimilarpuzzlesappearinthenewspaperandseekandfindbooksatthedrugstorezodiacsuspe" + 
		"cttedkaczynskialsomadesuchwordpuzzlestellingafriendhewashisvictimandhehadtofindthelettersofmynameinamusicalcompositionhewrotetheywerehiddeni" + 
		"nthelastletterofeachlineyourpostsmademethinkwhydoiclaimthatthewordpuzzleaspectoftheproposedsolutionislikelyvalidandintendedbythezodiacyoupro" + 
		"vedwhatweallkneworsuspectedanywaythatifyoutakeanycombinationofwordsandputtheminaxgridyouwillhaverandomlycreatednewwordsthatappearverticallyd" + 
		"iagonallyandbackwardsinyourexampleyoushoweddozensofwordsliketsaranalacremallmalttheoxfordenglishdictionaryhasoverwordsinitwhatisayissignific" + 
		"antabouttheproposedpartialsolutionisthetypeofwordscreatedthereisalsosomeinterestinggeographyduelleadstolistleadstobombsleadstotheobutunlessd" + 
		"oranchakorthebigzcanthinkofawaytoincludegeographywecanleavethatoutoftheexperimentwhenyoutrytosolveacodeonemethodistowritedownwordsyouexpectm" + 
		"aybeencodedsoinamilitarycontextyoumightwritedowntroopsattackmovetanksdawnretreatetcinakidnappingcasewithacodedmessageyoumightwritedownasword" + 
		"stolookforransommoneycashbagchildkidexchangemeetpoliceetcintryingtodecipherthewhatarethetowordswewouldthinkzodiacmightusemaybewordslikefunki" + 
		"llkillinghuntknifestabgunhoodsymbolcodeshepolicedeadslavescollectnamelistbombsblastgameballtiesropeetcletssetasidenamewhichappearsinnormalre" + 
		"adandblastwhichappearstwicebutanagrammedjustlookingfornonanagramwordsappearingverticaldiagonalorbackwardsintheproposedpartialsolutionwehavel" + 
		"istbombsgameballtiesthosearewordszodiacusedandthatithinkwouldbeonanyoneslisttolookforareyousayingthosewordsjustappearbychancewealsohaveother" + 
		"wordsthatzodiacmightuseandorwereusedbyazodiacsuspectandknownsfareaserialkillertedkaczynskiduelbarsleashstallstheobutletskeepthissimpleandsti" + 
		"cktothewordszodiachimselfusedgametieslistbombsballyousaythesewordsonlyappearbyrandomchanceokmychallengeisthisletscomeupwithalistofzodiacword" + 
		"sletsincludetheandotherslikeslaveskilletcithinkitwouldbefairlysimpletorunxgridsofenglishlangaugesentencesmobydicknytimesandthenhavethecomput" + 
		"erseekandfindthezodiacwordsappearingverticallydiagonallyandbackwardscorrectspellingandnoanagramsithinkandabovewouldbeaclearfailuretomebutiwi" + 
		"llacceptandaboveasafailuremeaningifyouruntestsandtimesormoreyougetormorezodiacwordsfromthelistofyouwillhaveprovenitcouldberandomchanceandthe" + 
		"reforiamwrongandiwillposttheresultshereandonwwwunazodcomandwebsleuthsbutifyouruntheandyougetormorezodiacwordszerotimesornotmorethantimesthat" + 
		"willbedeemedasuccessfulshowingthatsucharesultisveryunlikelytohappenbyrandomchanceandyouwillpostitatzkfdoesthatsoundfairdoranchakdoyoutakethe" + 
		"challengeorwanttomodifythetermswhataboutthebigzonboardsiranyoneelseiwouldloveitifdoranchakthebigzandaplayertobenamedlatertookthechallengeitc" + 
		"ouldbeinterestingtoseeifresultsareesentiallythesamewhydoesitmatterifiamrightandthisisanintendedwordpuzzlethenwehaveafirststagesolutiontothea" + 
		"ndastaringpointtogettotheendsolutionhighlydebatedgotopageoneofthisthreadforthehistorythiswasdoneorsoyearsagotakingtherawgraysmithsolutionasd" + 
		"oneandcorrectedbybullittkiteobiwanedandothersonoldzkboardthreeyearsagoinoticedwordslikelistbombsgametiesleashtheoetcappearingisaythisisveryu" + 
		"nlikelytohappenbychanceifiamrightthenthisisproofthisisavalisolutionoratleastpartiallyvalidbutifthesewordswouldappearinoutofwordgridsthenitco" + 
		"uldberandomsosolutionprobablynotvalidornomorevalidthananyotherhencethechallengeforatestexperimentchallengenotacceptedbydoranchakyouneedtorea" + 
		"daboutconfirmationbiasifiamwrongacceptthechallengeandprovemewrongwordsusedbythezodiaclikelistbombsgametieswouldnotallappearbychanceandyoukno" + 
		"witthatiswhyyoudeclinedthechallengeperhapsthebigztravorsomeoneelsewilltakeitonthanksgoodpointsandwhatiproposehereissimilartowhatbtklaterdidw" + 
		"hatblackdahliakillermayhavedoneearlierandwhathasbeendoneincrimemoviesandnovelszmayhavereadandiamveryconfidentinmyresearchskillsandanalytical" + 
		"abilitiesbutthereasoniaskdoranchakthebigzandotherstolookatthisisidonotownacomputerthatcandoitanddonotownabrainwithexcellentcomputerprogrammi" + 
		"ngskillslikedoranchakandbigzsoallicandoissayhereissomethingextraordinaryifiamrightitcouldbeafirststeponthepathtosolvingthedoranchakkaczynski" + 
		"wrotealotaboutrestraintsincludingleashbarsandloseingthemtherearerestraintsmentionedwealsohaveotherwordsrelevanttozortklikedueltheoetcplusall" + 
		"therandomwordsyouhaveshownweegtinanygridifiamwrongdowordgridsandseeifanycreatekwordszodiacusedandinproximityimeanwordszodiacusedandinproximi" + 
		"tyoranywheretravwhatbullittkiteobiwanandothersdidistrytofigureoutwhatwasarawcorrectandlogicalsolutionbeforegraysmithmuckeditupaboutyearsagot" + 
		"heycameupwiththissolutionwithlineslikeigivethemhelltoseeanamethesefoolshallseethenaboutyearsagoinoticedsomethingneverclaimedornotedbygraysmi" + 
		"thbullittoranyoneandthatisthatwordszodiacusedlikelistbombsgametiesappearedverticallydiagonallyorbackwardscorrectlyspellednoanagramsandwordst" + 
		"hattkusedlikeleashbarsstallsandalsowordslikeduelandtiesandoterstomeandsomeothersthatwasoddandinterestingthenifyoulookatseeanamerightbeforeit" + 
		"istheoandthelettersaroundthatcaesarshiftwithshiftstokaczynskithatissomethingdoranchakandbigzsayhappenstolessthanoflettersequencesithinkthere" + 
		"stofthisfirststagesolutionbecomesafinalreadablesolutionwithcaesarshiftsandsomeotherstepidontknowyet";
		System.out.println("Reading words...");
		read();
		for (int i=0; i<excerpt.length()-340; i++) {
			String s340 = excerpt.substring(i,i+340);

			System.out.println("excerpt: " + s340);
			puzzle = new String[20];
			for (int j=0; j<20; j++) {
				puzzle[j] = s340.substring(j*17,j*17+17);
			}
			makePuzzle();
			solve(false);
			
		}
	}
	
	public static void testSolve() {
		System.out.println("Reading words...");
		read();
		System.out.println(prefixCounts.keySet().size());
		System.out.println(words.size());
		System.out.println("Solving...");
		
		
		makePuzzle();
		solve(false);
	}

	public static void main(String[] args) {
		testSolve();
		//testAK();
	}

}
