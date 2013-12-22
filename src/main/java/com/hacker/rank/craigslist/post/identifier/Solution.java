package com.hacker.rank.craigslist.post.identifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

//import org.json.simple.JSONObject;
//import org.json.simple.JSONValue;

public class Solution {
	static BufferedReader br;
	static PrintWriter out;
	static String INPUT = "";
	
	static String NG_HEAD = "\',";
	static String SEPARATOR = " ?=/\n()[]{}\"";
	static Set<String> STOP_WORDS = new HashSet<String>();
	static {
		String[] sts = {
			"a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also","although","always","am","among", "amongst", "amoungst", "amount",  "an", "and", "another", "any","anyhow","anyone","anything","anyway", "anywhere", "are", "around", "as",  "at", "back","be","became", "because","become","becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "bill", "both", "bottom","but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven","else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own","part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the"
		};
		for(String st : sts)STOP_WORDS.add(st);
	}
	
	static File base = null;
	
	static char[] ch = new char[10000];
	
	static String preprocess(String word)
	{
		word = word.trim().toLowerCase();
		int p = 0;
		for(int i = 0;i < word.length();i++){
			char c = word.charAt(i);
			if(p == 0 && NG_HEAD.indexOf(c) >= 0)continue; // ng head
			ch[p++] = c;
			if(c == '\\')ch[p++] = c;
		}
		for(int i = p-1;i >= 0;i--){
			char last = ch[i];
			if(last >= '0' && last <= '9')break;
			if(last >= 'a' && last <= 'z')break;
			p--;
		}
		if(p == 0)return null;
		return new String(ch, 0, p);
	}
	
	static class Datum
	{
		int id;
		double val;
		
		public Datum(int id, double val) {
			this.id = id;
			this.val = val;
		}
		
		public static Datum init(Datum d, int id, double val)
		{
			if(d == null){
				return new Datum(id, val);
			}else{
				d.id = id;
				d.val = val;
				return d;
			}
		}
	}
	
	static Map<String, Integer> indmap;
	static int ndocs;
	
	static class Doc {
		public City city;
		public Category category;
		public Section section;
		public List<Datum> vec;
	}
	
	static final double LOG2 = Math.log(2);
	static double normalize(double tf, double df)
	{
//		return (1 + Math.log(tf)/LOG2) * -(Math.log(df)/LOG2); // ltc 88.9%
//		return (LOG2 + Math.log(tf)) * -Math.log(df);
		
		return tf * -Math.log(df);
	}
	
	public static void solve() throws Exception
	{
		// prepare
		prepare();
		
		train();
		
		test();
	}
	
	static PassiveAggressive[][][] pas;
	static int nword;
	static List<Doc> docs;
	static int[] df;
	
	static void prepare() throws Exception
	{
		long S = System.nanoTime();
		
		BufferedReader brTrain = new BufferedReader(new InputStreamReader(new FileInputStream(new File(base, "training.json")), "UTF-8"));
//		BufferedReader brTrain = new BufferedReader(new InputStreamReader(new FileInputStream(new File("training.json")), "UTF-8"));
		ndocs = Integer.parseInt(brTrain.readLine());
		
		indmap = new HashMap<String, Integer>(); // <word, id>
		int[][] terms = new int[ndocs][]; // each doc's word freq
		df = new int[50000]; // each word's #appearing document.
		docs = new ArrayList<Doc>();
		nword = 0;
		
		{
			int p = 0;
			String line;
			while((line = brTrain.readLine()) != null){/*
				JSONObject obj = (JSONObject)JSONValue.parse(line);
				String city = ((String)obj.get("city")).replace('.', '_');
				String category = ((String)obj.get("category")).replace('-', '_');
				String section = ((String)obj.get("section")).replace('-', '_');
				String heading = (String)obj.get("heading");
				
				int[] lterms = new int[100];
				int tp = 0;
				Doc doc = new Doc();
				doc.city = City.valueOf(city);
				doc.category = Category.valueOf(category);
				doc.section = Section.valueOf(section);
				docs.add(doc);
				
				Set<String> set = new HashSet<String>();
				{
					if(!indmap.containsKey(city))indmap.put(city, nword++);
					if(set.add(city))df[indmap.get(city)]++;
					lterms[tp++] = indmap.get(city);
				}
				for(String corpus : new String[]{heading}){
					StringTokenizer toker = new StringTokenizer(corpus, SEPARATOR);
					while(toker.hasMoreTokens()){
						// preprocess
						String word = toker.nextToken();
						word = preprocess(word);
						if(word == null || word.length() <= 1)continue;
						if(STOP_WORDS.contains(word))continue;
						
						if(!indmap.containsKey(word))indmap.put(word, nword++);
						if(set.add(word))df[indmap.get(word)]++;
						lterms[tp++] = indmap.get(word);
					}
				}
				terms[p] = Arrays.copyOf(lterms, tp);
				p++;
			*/}
			if(p != ndocs){
				throw new RuntimeException();
			}
		}
		brTrain.close();
		
//		for(Map.Entry<String, Integer> e : indmap.entrySet()){
//			if(df[e.getValue()] >= 1)tr(e.getKey(), df[e.getValue()]);
//		}
		
		tr("#word : " + nword);
		
		for(int i = 0;i < ndocs;i++){
			Arrays.sort(terms[i]);
			List<Datum> vec = new ArrayList<Datum>();
			int ct = 0;
			for(int j = 0;j < terms[i].length;j++){
				ct++;
				if(j == terms[i].length-1 || terms[i][j] != terms[i][j+1]){
					vec.add(new Datum(terms[i][j], normalize((double)ct/terms[i].length, (double)df[terms[i][j]]/ndocs)));
//					(double)ct/terms[i].length*Math.log((double)ndocs/df[terms[i][j]]));
//					tr(terms[i][j], ct, ndocs, df[terms[i][j]]);
					ct = 0;
				}
			}
			docs.get(i).vec = vec;
		}
		
		long G = System.nanoTime();
		tr("prepare : " + (G-S)/1000000 + "ms");
	}
	
	public static void train()
	{
		// train
		long S = System.nanoTime();
		pas = new PassiveAggressive[4][4][4];
		for(int i = 0;i < 4;i++){
			for(int j = 0;j < 4;j++){
				for(int k = j+1;k < 4;k++){
					pas[i][j][k] = new PassiveAggressive(nword);
				}
			}
		}
		
		for(int rep = 0;rep < 3;rep++){
			Collections.shuffle(docs);
			for(Doc doc : docs){
				int sec = doc.section.ordinal();
				int cat = doc.category.ordinal() % 4;
				for(int op = 0;op < 4;op++){
					if(op == cat)continue;
					pas[sec][Math.min(op, cat)][Math.max(op, cat)].train(doc.vec, cat < op ? 1 : -1);
				}
			}
		}
		
		long G = System.nanoTime();
		tr("train : " + (G-S)/1000000 + "ms");
	}
	
	public static void test() throws Exception
	{
		Random gen = new Random(2);
		
		// test
		long S = System.nanoTime();
		int n = Integer.parseInt(br.readLine());
		int[] tterms = new int[1000];
		for(int i = 0;i < n;i++){/*
			String line = br.readLine();
			JSONObject obj = (JSONObject)JSONValue.parse(line);
			String city = ((String)obj.get("city")).replace('.', '_');
			String section = ((String)obj.get("section")).replace('-', '_');
			String heading = (String)obj.get("heading");
			
			int tp = 0;
			{
				if(!indmap.containsKey(city))indmap.put(city, nword++);
				tterms[tp++] = indmap.get(city);
			}
			for(String corpus : new String[]{heading}){
				StringTokenizer toker = new StringTokenizer(corpus, SEPARATOR);
				while(toker.hasMoreTokens()){
					// preprocess
					String word = toker.nextToken();
					word = preprocess(word);
					if(word == null || word.length() <= 1)continue;
					
					if(indmap.containsKey(word)){
						tterms[tp++] = indmap.get(word);
					}
				}
			}
			
			Arrays.sort(tterms, 0, tp);
			
			List<Datum> vec = new ArrayList<Datum>();
			int ct = 0;
			for(int j = 0;j < tp;j++){
				ct++;
				if(j == tp-1 || tterms[j] != tterms[j+1]){
					int tid = tterms[j];
					vec.add(new Datum(tid, normalize((double)ct/tterms.length, (double)df[tid]/ndocs)));
					ct = 0;
				}
			}
			int clus = elect(vec, City.valueOf(city), Section.valueOf(section), pas, gen);
			out.println(Category.values()[clus].name().replace('_', '-'));
		*/}
		long G = System.nanoTime();
		tr("test : " + (G-S)/1000000 + "ms");
	}
	
	static int elect(List<Datum> vec, City city, Section section, PassiveAggressive[][][] pas, Random gen)
	{
		int n = 4;
		int cans = (1<<n)-1;
		PassiveAggressive[][] spas = pas[section.ordinal()];
		
		while(true){
			int[] vote = new int[n];
			for(int j = 0;j < n;j++){
				if(cans<<31-j<0){
					for(int k = j+1;k < n;k++){
						if(cans<<31-k<0){
							if(spas[j][k].test(vec) == 1){
								vote[j]++;
							}else{
								vote[k]++;
							}
						}
					}
				}
			}
			
			int maxv = 0;
			int maxptn = 0;;
			for(int j = 0;j < n;j++){
				if(vote[j] > maxv){
					maxv = vote[j];
					maxptn = 0;
				}
				if(vote[j] == maxv){
					maxptn |= 1<<j;
				}
			}
			
			if(Integer.bitCount(maxptn) == 1)return Integer.numberOfTrailingZeros(maxptn) + section.ordinal() * 4;
			if(maxptn == cans){
				for(int i = gen.nextInt(Integer.bitCount(maxptn));i > 0;i--){
					cans &= cans-1;
				}
				return Integer.numberOfTrailingZeros(cans) + section.ordinal() * 4;
			}
			cans = maxptn;
		}
	}
	
	static class PassiveAggressive
	{
		double[] w;
		double C = 1; // PA-II aggressive.
		
		public PassiveAggressive(int n) {
			w = new double[n];
		}
		
		int test(List<Datum> x)
		{
			return dotWith(x) >= 0 ? 1 : -1;
		}
	
		// w dot x
		double dotWith(List<Datum> x)
		{
			double dot = 0;
			for(Datum e : x)dot += w[e.id] * e.val;
			return dot;
		}
		
		void train(List<Datum> x, int y)
		{
			double dot = dotWith(x);
			// w*x
			double loss = Math.max(0, 1-y*dot);
			if(loss == 0)return;
			
			double xnorm = 0;
			for(Datum e : x)xnorm += e.val * e.val;
			
			double tau = loss / (xnorm + C);
			for(Datum e : x){
				w[e.id] += tau * y * e.val;
			}
		}
	}
	
	static enum City {
		bangalore,
		chicago,
		delhi,
		dubai_en,
		frankfurt_en,
		geneva_en,
		hyderabad,
		kolkata_en,
		london,
		manchester,
		mumbai,
		newyork,
		paris_en,
		seattle,
		singapore,
		zurich_en
	};
	
	static enum Category {
		// community
		activities,
		artists,
		childcare,
		general,
		
		// for-sale
		appliances,
		cell_phones,
		photography,
		video_games,
		
		// housing
		housing,
		shared,
		temporary,
		wanted_housing,
		
		// services
		automotive,
		household_services,
		real_estate,
		therapeutic
	};
	
	static enum Section {
		community,
		for_sale, // for-sale
		housing,
		services
	};
	
	public static void main(String[] args) throws Exception
	{
		long S = System.currentTimeMillis();
		br = INPUT.isEmpty() ? 
				new BufferedReader(new InputStreamReader(System.in)) :
				new BufferedReader(new InputStreamReader(new FileInputStream(new File(base, "training.json"))));
		out = new PrintWriter(System.out);
		
		solve();
		out.flush();
		long G = System.currentTimeMillis();
		tr(G-S+"ms");
	}
	
	private static void tr(Object... o) { if(INPUT.length() != 0)System.out.println(Arrays.deepToString(o)); }
}
