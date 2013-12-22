package com.hacke.rank.challenge.september.contest;

import java.io.*;
import java.util.*;
import java.math.*;

public class StackExchangeHackerRank implements Runnable {
	
	static BufferedReader in;
	static PrintWriter out;
	static StringTokenizer st;
	static Random rnd;

	static class CategoriesHolder {
		private static final String[] categories = { "gis", "security",
				"photo", "mathematica", "unix", "wordpress", "scifi",
				"electronics", "android", "apple" };
		private Map<String, Integer> nameToId;
		private Map<Integer, String> idToName;

		public CategoriesHolder() {
			nameToId = new HashMap<String, Integer>();
			idToName = new HashMap<Integer, String>();
			for (int i = 0; i < categories.length; i++) {
				nameToId.put(categories[i], i);
				idToName.put(i, categories[i]);
			}
			if (nameToId.size() != idToName.size())
				throw new AssertionError();
		}

		public int size() {
			return categories.length;
		}

		public String getName(int id) {
			return idToName.get(id);
		}

		public int getId(String name) {
			return nameToId.get(name);
		}
	}

	class JsonParser {
		public Map<String, String> parse(String text) {
			// Remove brackets
			if (text.charAt(0) != '{' || text.charAt(text.length() - 1) != '}')
				throw new AssertionError();
			text = text.substring(1, text.length() - 1);

			// Parse
			Map<String, String> result = new HashMap<String, String>();

			while (text.length() > 0) {
				String key = getToken(text);
				// Remove token
				text = text.substring(key.length() + 2);
				// Remove ":"
				text = text.substring(1);
				String value = getToken(text);
				// Remove token
				text = text.substring(value.length() + 2);
				// Remove ","
				if (text.length() > 0) {
					if (text.charAt(0) != ',')
						throw new AssertionError();
					text = text.substring(1);
				}
				// Add to map
				result.put(key, value);
			}

			// Check keys
			final String[] defaultKeys = { "question", "excerpt" };
			for (String defaultKey : defaultKeys)
				if (!result.containsKey(defaultKey))
					throw new AssertionError();

			return result;
		}

		private String getToken(String text) {
			if (text.charAt(0) != '"')
				throw new AssertionError();

			int state = 0;

			for (int i = 1; i < text.length(); i++) {
				char c = text.charAt(i);
				if (state == 0) {
					if (c == '"') {
						return text.substring(1, i);
					} else if (c == '\\') {
						state = 1;
					}
				} else if (state == 1) {
					state = 0;
				} else {
					throw new AssertionError();
				}
			}

			throw new AssertionError();
		}
	}

	static class Tokenizer {
		// http://dev.mysql.com/doc/refman/5.7/en/fulltext-stopwords.html
		private static final String[] stopWordsList = { "a's", "able", "about",
				"above", "according", "accordingly", "across", "actually",
				"after", "afterwards", "again", "against", "ain't", "all",
				"allow", "allows", "almost", "alone", "along", "already",
				"also", "although", "always", "am", "among", "amongst", "an",
				"and", "another", "any", "anybody", "anyhow", "anyone",
				"anything", "anyway", "anyways", "anywhere", "apart", "appear",
				"appreciate", "appropriate", "are", "aren't", "around", "as",
				"aside", "ask", "asking", "associated", "at", "available",
				"away", "awfully", "be", "became", "because", "become",
				"becomes", "becoming", "been", "before", "beforehand",
				"behind", "being", "believe", "below", "beside", "besides",
				"best", "better", "between", "beyond", "both", "brief", "but",
				"by", "c'mon", "c's", "came", "can", "can't", "cannot", "cant",
				"cause", "causes", "certain", "certainly", "changes",
				"clearly", "co", "com", "come", "comes", "concerning",
				"consequently", "consider", "considering", "contain",
				"containing", "contains", "corresponding", "could", "couldn't",
				"course", "currently", "definitely", "described", "despite",
				"did", "didn't", "different", "do", "does", "doesn't", "doing",
				"don't", "done", "down", "downwards", "during", "each", "edu",
				"eg", "eight", "either", "else", "elsewhere", "enough",
				"entirely", "especially", "et", "etc", "even", "ever", "every",
				"everybody", "everyone", "everything", "everywhere", "ex",
				"exactly", "example", "except", "far", "few", "fifth", "first",
				"five", "followed", "following", "follows", "for", "former",
				"formerly", "forth", "four", "from", "further", "furthermore",
				"get", "gets", "getting", "given", "gives", "go", "goes",
				"going", "gone", "got", "gotten", "greetings", "had", "hadn't",
				"happens", "hardly", "has", "hasn't", "have", "haven't",
				"having", "he", "he's", "hello", "help", "hence", "her",
				"here", "here's", "hereafter", "hereby", "herein", "hereupon",
				"hers", "herself", "hi", "him", "himself", "his", "hither",
				"hopefully", "how", "howbeit", "however", "i'd", "i'll", "i'm",
				"i've", "ie", "if", "ignored", "immediate", "in", "inasmuch",
				"inc", "indeed", "indicate", "indicated", "indicates", "inner",
				"insofar", "instead", "into", "inward", "is", "isn't", "it",
				"it'd", "it'll", "it's", "its", "itself", "just", "keep",
				"keeps", "kept", "know", "known", "knows", "last", "lately",
				"later", "latter", "latterly", "least", "less", "lest", "let",
				"let's", "like", "liked", "likely", "little", "look",
				"looking", "looks", "ltd", "mainly", "many", "may", "maybe",
				"me", "mean", "meanwhile", "merely", "might", "more",
				"moreover", "most", "mostly", "much", "must", "my", "myself",
				"name", "namely", "nd", "near", "nearly", "necessary", "need",
				"needs", "neither", "never", "nevertheless", "new", "next",
				"nine", "no", "nobody", "non", "none", "noone", "nor",
				"normally", "not", "nothing", "novel", "now", "nowhere",
				"obviously", "of", "off", "often", "oh", "ok", "okay", "old",
				"on", "once", "one", "ones", "only", "onto", "or", "other",
				"others", "otherwise", "ought", "our", "ours", "ourselves",
				"out", "outside", "over", "overall", "own", "particular",
				"particularly", "per", "perhaps", "placed", "please", "plus",
				"possible", "presumably", "probably", "provides", "que",
				"quite", "qv", "rather", "rd", "re", "really", "reasonably",
				"regarding", "regardless", "regards", "relatively",
				"respectively", "right", "said", "same", "saw", "say",
				"saying", "says", "second", "secondly", "see", "seeing",
				"seem", "seemed", "seeming", "seems", "seen", "self", "selves",
				"sensible", "sent", "serious", "seriously", "seven", "several",
				"shall", "she", "should", "shouldn't", "since", "six", "so",
				"some", "somebody", "somehow", "someone", "something",
				"sometime", "sometimes", "somewhat", "somewhere", "soon",
				"sorry", "specified", "specify", "specifying", "still", "sub",
				"such", "sup", "sure", "t's", "take", "taken", "tell", "tends",
				"th", "than", "thank", "thanks", "thanx", "that", "that's",
				"thats", "the", "their", "theirs", "them", "themselves",
				"then", "thence", "there", "there's", "thereafter", "thereby",
				"therefore", "therein", "theres", "thereupon", "these", "they",
				"they'd", "they'll", "they're", "they've", "think", "third",
				"this", "thorough", "thoroughly", "those", "though", "three",
				"through", "throughout", "thru", "thus", "to", "together",
				"too", "took", "toward", "towards", "tried", "tries", "truly",
				"try", "trying", "twice", "two", "un", "under",
				"unfortunately", "unless", "unlikely", "until", "unto", "up",
				"upon", "us", "use", "used", "useful", "uses", "using",
				"usually", "value", "various", "very", "via", "viz", "vs",
				"want", "wants", "was", "wasn't", "way", "we", "we'd", "we'll",
				"we're", "we've", "welcome", "well", "went", "were", "weren't",
				"what", "what's", "whatever", "when", "whence", "whenever",
				"where", "where's", "whereafter", "whereas", "whereby",
				"wherein", "whereupon", "wherever", "whether", "which",
				"while", "whither", "who", "who's", "whoever", "whole", "whom",
				"whose", "why", "will", "willing", "wish", "with", "within",
				"without", "won't", "wonder", "would", "wouldn't", "yes",
				"yet", "you", "you'd", "you'll", "you're", "you've", "your",
				"yours", "yourself", "yourselves", "zero" };

		private static final String[] suffixes = { "'s" };
		private static final String[] trashSubstrings = { "\\n", "\\r", "..." };
		private static final int minTokenLength = 2;
		private static final String delimiters = "[^\\w']+";

		private Set<String> stopWords;

		public Tokenizer() {
			stopWords = new HashSet<String>();
			for (String stopWord : stopWordsList) {
				stopWords.add(stopWord);
			}
		}

		public List<String> tokenize(String text) {
			text = cleanUpQuery(text);

			String[] tokens = text.split(delimiters);
			List<String> result = new ArrayList<String>();

			for (String token : tokens) {
				token = cleanUpToken(token);
				if (token.length() >= minTokenLength
						&& !stopWords.contains(token)) {
					result.add(token);
				}
			}

			return result;
		}

		private String cleanUpToken(String token) {
			token = token.toLowerCase();
			for (String suffix : suffixes) {
				if (token.endsWith(suffix)) {
					token = token
							.substring(0, token.length() - suffix.length());
				}
			}
			return token;
		}

		private String cleanUpQuery(String text) {
			for (String trashSubstring : trashSubstrings) {
				text = text.replace(trashSubstring, " ");
			}
			text = text.trim();
			return text;
		}
	}

	class WordStatistic {
		private long sum;
		private long[] frequencies;

		public WordStatistic(int categoriesCount) {
			sum = 0;
			frequencies = new long[categoriesCount];
		}

		public void increment(int categoryId) {
			++sum;
			++frequencies[categoryId];
		}

		public long get(int categoryId) {
			return frequencies[categoryId];
		}

		public long getSum() {
			return sum;
		}

		public int isUnique() {
			int result = -1;
			for (int i = 0; i < frequencies.length; i++) {
				if (frequencies[i] > 0) {
					if (result == -1) {
						result = i;
					} else {
						return -1;
					}
				}
			}
			if (result == -1) {
				throw new AssertionError();
			}
			return result;
		}
	}

	class Bigram {
		String firstWord, secondWord;

		public Bigram(String firstWord, String secondWord) {
			this.firstWord = firstWord;
			this.secondWord = secondWord;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((firstWord == null) ? 0 : firstWord.hashCode());
			result = prime * result
					+ ((secondWord == null) ? 0 : secondWord.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Bigram other = (Bigram) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (firstWord == null) {
				if (other.firstWord != null)
					return false;
			} else if (!firstWord.equals(other.firstWord))
				return false;
			if (secondWord == null) {
				if (other.secondWord != null)
					return false;
			} else if (!secondWord.equals(other.secondWord))
				return false;
			return true;
		}

		private StackExchangeHackerRank getOuterType() {
			return StackExchangeHackerRank.this;
		}
	}

	class ObviousSolution {
		private Map<String, String> words;
		private Map<Bigram, String> bigrams;

		final String[][] cachedWords = { { "bootcamp", "apple" },
				{ "arcsde", "gis" }, { "rooting", "android" },
				{ "potter", "scifi" }, { "erdas", "gis" },
				{ "tamron", "photo" }, { "stochastic", "mathematica" },
				{ "picard", "scifi" }, { "thrones", "scifi" },
				{ "7d", "photo" }, { "6v", "electronics" },
				{ "geodatabase", "gis" }, { "csrf", "security" },
				{ "frodo", "scifi" }, { "crew", "scifi" },
				{ "ic", "electronics" }, { "protagonist", "scifi" },
				{ "novels", "scifi" }, { "stark", "scifi" },
				{ "vampire", "scifi" }, { "cm10", "android" },
				{ "tardis", "scifi" }, { "symbology", "gis" },
				{ "gods", "scifi" }, { "rectifier", "electronics" },
				{ "divider", "electronics" }, { "1980s", "scifi" },
				{ "buddypress", "wordpress" }, { "vhdl", "electronics" },
				{ "palpatine", "scifi" }, { "tinymce", "wordpress" },
				{ "futurama", "scifi" }, { "verilog", "electronics" },
				{ "thirds", "photo" }, { "d7000", "photo" },
				{ "systemd", "unix" }, { "spaceship", "scifi" },
				{ "geojson", "gis" }, { "gdal", "gis" }, { "marvel", "scifi" },
				{ "soldering", "electronics" }, { "spoilers", "scifi" },
				{ "msp430", "electronics" }, { "weapons", "scifi" },
				{ "epsg", "gis" }, { "amps", "electronics" },
				{ "adamantium", "scifi" }, { "tolkien", "scifi" },
				{ "ight", "mathematica" }, { "superman", "scifi" },
				{ "angels", "scifi" }, { "microcontrollers", "electronics" },
				{ "thor", "scifi" }, { "sauron", "scifi" },
				{ "permalinks", "wordpress" }, { "ical", "apple" },
				{ "volt", "electronics" }, { "ddos", "security" },
				{ "hobbit", "scifi" }, { "saruman", "scifi" },
				{ "weapon", "scifi" }, { "dumbledore", "scifi" },
				{ "arcgis", "gis" }, { "inductor", "electronics" },
				{ "55mm", "photo" }, { "yoda", "scifi" },
				{ "cyanogenmod", "android" }, { "dslrs", "photo" },
				{ "batman", "scifi" }, { "rhel", "unix" }, { "550d", "photo" },
				{ "copper", "electronics" }, { "opamp", "electronics" },
				{ "woocommerce", "wordpress" }, { "shapefile", "gis" },
				{ "tony", "scifi" }, { "autofocus", "photo" },
				{ "solenoid", "electronics" }, { "bjt", "electronics" },
				{ "mosfet", "electronics" }, { "sextante", "gis" },
				{ "d90", "photo" }, { "shortcode", "wordpress" },
				{ "microcontroller", "electronics" },
				{ "lightsaber", "scifi" }, { "capacitors", "electronics" },
				{ "adc", "electronics" }, { "dragons", "scifi" },
				{ "micromax", "android" }, { "arcobjects", "gis" },
				{ "anakin", "scifi" }, { "olympus", "photo" },
				{ "pyqgis", "gis" }, { "postgis", "gis" },
				{ "knight", "scifi" }, { "leaflet", "gis" },
				{ "captain", "scifi" }, { "hulk", "scifi" },
				{ "geometries", "gis" }, { "kies", "android" },
				{ "wizards", "scifi" }, { "arcmap", "gis" },
				{ "oscilloscope", "electronics" }, { "starfleet", "scifi" },
				{ "vader", "scifi" }, { "geoserver", "gis" },
				{ "datasheet", "electronics" }, { "ndsolve", "mathematica" },
				{ "diode", "electronics" }, { "hermione", "scifi" },
				{ "borg", "scifi" }, { "nikkor", "photo" },
				{ "voyager", "scifi" }, { "openlayer", "gis" },
				{ "photographing", "photo" }, { "taxonomies", "wordpress" },
				{ "rasters", "gis" }, { "hmac", "security" },
				{ "mapinfo", "gis" }, { "vampires", "scifi" },
				{ "fastboot", "android" }, { "pentax", "photo" },
				{ "aliens", "scifi" }, { "gml", "gis" },
				{ "i2c", "electronics" }, { "fujifilm", "photo" },
				{ "geotools", "gis" }, { "clockworkmod", "android" },
				{ "lord", "scifi" }, { "loki", "scifi" },
				{ "tasker", "android" }, { "opengeo", "gis" },
				{ "trilogy", "scifi" }, { "magical", "scifi" },
				{ "eos", "photo" }, { "transistors", "electronics" },
				{ "gis", "gis" }, { "duos", "android" },
				{ "spatialite", "gis" }, { "photographers", "photo" },
				{ "fme", "gis" }, { "luke", "scifi" }, { "cwm", "android" },
				{ "oscillator", "electronics" }, { "shapefiles", "gis" },
				{ "pgrouting", "gis" }, { "t2i", "photo" },
				{ "jellybean", "android" }, { "t3i", "photo" },
				{ "polyline", "gis" }, { "sci", "scifi" },
				{ "spi", "electronics" }, { "heroes", "scifi" },
				{ "hogwarts", "scifi" }, { "pwm", "electronics" },
				{ "asimov", "scifi" }, { "rs232", "electronics" },
				{ "supernatural", "scifi" }, { "wolfram", "mathematica" },
				{ "inverter", "electronics" }, { "mxd", "gis" },
				{ "metabox", "wordpress" }, { "solder", "electronics" },
				{ "35mm", "photo" }, { "resistors", "electronics" },
				{ "npn", "electronics" }, { "ogr", "gis" },
				{ "linestring", "gis" }, { "ohm", "electronics" },
				{ "12v", "electronics" }, { "obi", "scifi" },
				{ "wgs84", "gis" }, { "eaters", "scifi" },
				{ "wolverine", "scifi" }, { "osm", "gis" },
				{ "multisite", "wordpress" }, { "empire", "scifi" },
				{ "lvm", "unix" }, { "arcpy", "gis" }, { "harry", "scifi" },
				{ "automator", "apple" }, { "telephoto", "photo" },
				{ "chest", "scifi" }, { "portraits", "photo" },
				{ "schematic", "electronics" }, { "volts", "electronics" },
				{ "d5000", "photo" }, { "voltages", "electronics" },
				{ "qgis", "gis" }, { "geotiff", "gis" }, { "blood", "scifi" },
				{ "strap", "photo" }, { "jedi", "scifi" },
				{ "mutant", "scifi" }, { "destruction", "scifi" },
				{ "wp_query", "wordpress" }, { "24v", "electronics" },
				{ "wiring", "electronics" }, { "gingerbread", "android" },
				{ "spoiler", "scifi" }, { "wkt", "gis" },
				{ "resistor", "electronics" }, { "schematics", "electronics" },
				{ "horcrux", "scifi" }, { "80s", "scifi" }, { "wfs", "gis" },
				{ "muggle", "scifi" }, { "regulator", "electronics" },
				{ "avengers", "scifi" }, { "metasploit", "security" },
				{ "gandalf", "scifi" }, { "osm2po", "gis" },
				{ "add_action", "wordpress" }, { "70s", "scifi" },
				{ "modelbuilder", "gis" }, { "85mm", "photo" },
				{ "wpdb", "wordpress" }, { "census", "gis" },
				{ "200mm", "photo" }, { "usm", "photo" }, { "elves", "scifi" },
				{ "60d", "photo" }, { "tng", "scifi" },
				{ "arccatalog", "gis" }, { "voldemort", "scifi" },
				{ "diodes", "electronics" }, { "600d", "photo" },
				{ "geoprocessing", "gis" }, { "d3100", "photo" },
				{ "optimus", "android" }, { "polylines", "gis" },
				{ "intersect", "gis" }, { "altium", "electronics" },
				{ "grub", "unix" }, { "ogr2ogr", "gis" }, { "bokeh", "photo" },
				{ "metering", "photo" }, { "fantasy", "scifi" },
				{ "landsat", "gis" }, { "darth", "scifi" },
				{ "customizer", "wordpress" }, { "transistor", "electronics" },
				{ "openlayers", "gis" }, { "geoext", "gis" },
				{ "mosfets", "electronics" }, { "snape", "scifi" },
				{ "xbee", "electronics" }, { "federation", "scifi" },
				{ "penetration", "security" }, { "starship", "scifi" },
				{ "stargate", "scifi" }, { "aragorn", "scifi" },
				{ "coolpix", "photo" }, { "permalink", "wordpress" },
				{ "wordpress", "wordpress" }, { "macos", "apple" },
				{ "android", "android" }, { "macbook", "apple" },
				{ "debian", "unix" }, { "5v", "electronics" },
				{ "eagle", "electronics" }, { "s4", "android" },
				{ "icloud", "apple" }, { "exposures", "photo" },
				{ "risks", "security" }, { "alien", "scifi" },
				{ "xperia", "android" }, { "adb", "android" },
				{ "jelly", "android" }, { "pagination", "wordpress" },
				{ "dem", "gis" }, { "sed", "unix" }, { "bean", "android" },
				{ "rooted", "android" }, { "capacitor", "electronics" },
				{ "ota", "android" }, { "lon", "gis" },
				{ "mcu", "electronics" }, { "mathematica", "mathematica" },
				{ "xss", "security" }, { "uno", "electronics" },
				{ "fiction", "scifi" }, { "dbi", "electronics" },
				{ "arduino", "electronics" }, { "appstore", "apple" } };

		final String[][] cachedBigrams = { { "google", "play", "android" } };

		final String[][] cachedRegExps = { { "[\\d]+db[i]?", "electronics" } };

		public ObviousSolution() {
			words = new HashMap<String, String>();
			for (String[] pair : cachedWords) {
				String key = pair[0], category = pair[1];
				words.put(key, category);
			}
			bigrams = new HashMap<Bigram, String>();
			for (String[] pair : cachedBigrams) {
				String firstWord = pair[0], secondWord = pair[1], category = pair[2];
				bigrams.put(new Bigram(firstWord, secondWord), category);
			}
		}

		public String getSolution(Bigram bigram) {
			return bigrams.get(bigram);
		}

		public String getSolution(String key) {
			String result = words.get(key);
			if (result != null)
				return result;

			for (String[] pair : cachedRegExps) {
				String regExp = pair[0], category = pair[1];
				if (key.matches(regExp)) {
					return category;
				}
			}

			return null;
		}
	}

	class ContentClassifier {
		private Tokenizer tokenizer;
		private CategoriesHolder categoriesHolder;
		private Map<String, WordStatistic> wordsStatistics;
		private Map<Bigram, WordStatistic> bigramStatistics;
		private ObviousSolution obviousSolution;
		private long[] documentsCountByCategory, wordsCountByCategory,
				bigramsCountByCategory;
		private long documentsCount;

		public ContentClassifier() {
			tokenizer = new Tokenizer();
			categoriesHolder = new CategoriesHolder();
			wordsStatistics = new HashMap<String, WordStatistic>();
			bigramStatistics = new HashMap<Bigram, WordStatistic>();
			obviousSolution = new ObviousSolution();
			documentsCountByCategory = new long[categoriesHolder.size()];
			wordsCountByCategory = new long[categoriesHolder.size()];
			bigramsCountByCategory = new long[categoriesHolder.size()];
			documentsCount = 0;
		}

		public void learn(String category, String title, String text) {
			// Simulate 4x weight for title
			text = title + " " + title + " " + title + " " + title + " " + text;

			List<String> tokens = tokenizer.tokenize(text);

			int categoryId = categoriesHolder.getId(category);
			addCategory(categoryId, tokens.size());

			for (int j = 0; j < tokens.size() - 1; j++) {
				Bigram bigram = new Bigram(tokens.get(j), tokens.get(j + 1));
				addBigram(categoryId, bigram);
			}

			for (String token : tokens) {
				addToken(categoryId, token);
			}

			++documentsCount;
		}

		private void addToken(int categoryId, String token) {
			WordStatistic wordsStatistic = wordsStatistics.get(token);
			if (wordsStatistic == null) {
				wordsStatistic = new WordStatistic(categoriesHolder.size());
				wordsStatistics.put(token, wordsStatistic);
			}
			wordsStatistic.increment(categoryId);
		}

		private void addBigram(int categoryId, Bigram bigram) {
			WordStatistic wordsStatistic = bigramStatistics.get(bigram);
			if (wordsStatistic == null) {
				wordsStatistic = new WordStatistic(categoriesHolder.size());
				bigramStatistics.put(bigram, wordsStatistic);
			}
			wordsStatistic.increment(categoryId);
		}

		private void addCategory(int categoryId, long newWords) {
			++documentsCountByCategory[categoryId];
			wordsCountByCategory[categoryId] += newWords;
			bigramsCountByCategory[categoryId] += Math.max(0, newWords - 1);
		}

		public String classify(String title, String text) {
			// Simulate 2x weight for title
			text = title + " " + title + " " + text;

			List<String> tokens = tokenizer.tokenize(text);

			double[] probs = new double[categoriesHolder.size()];

			// Better result without this :/
			// for (int i = 0; i < categoriesHolder.size(); i++) {
			// probs[i] = Math.log((double) documentsCountByCategory[i]
			// / documentsCount);
			// }

			// Check bigrams
			for (int j = 0; j < tokens.size() - 1; j++) {
				Bigram bigram = new Bigram(tokens.get(j), tokens.get(j + 1));

				String obvious = obviousSolution.getSolution(bigram);

				if (obvious != null) {
					return obvious;
				}

				WordStatistic bigramStatistic = bigramStatistics.get(bigram);

				if (bigramStatistic != null) {
					for (int i = 0; i < categoriesHolder.size(); i++) {
						long top = bigramStatistic.get(i) + 1;
						long bottom = bigramStatistics.size()
								+ bigramsCountByCategory[i];
						probs[i] += Math.log((double) top / bottom);
					}
				}
			}

			// Check words
			for (String token : tokens) {
				String obvious = obviousSolution.getSolution(token);

				if (obvious != null) {
					return obvious;
				}

				WordStatistic wordsStatistic = wordsStatistics.get(token);

				if (wordsStatistic != null) {
					for (int i = 0; i < categoriesHolder.size(); i++) {
						long top = wordsStatistic.get(i) + 1;
						long bottom = wordsStatistics.size()
								+ wordsCountByCategory[i];
						probs[i] += Math.log((double) top / bottom);
					}
				}
			}

			int categoryWithMaxProb = 0;

			for (int i = 1; i < categoriesHolder.size(); i++) {
				if (probs[i] > probs[categoryWithMaxProb]) {
					categoryWithMaxProb = i;
				}
			}

			return categoriesHolder.getName(categoryWithMaxProb);
		}
	}

	private void solve() throws IOException {
		ContentClassifier contentClassifier = train();
		answer(contentClassifier);
		// check(contentClassifier);
	}

	private ContentClassifier train() throws IOException {
		final String fileName = "training.json";

		in = new BufferedReader(new FileReader(fileName));
		JsonParser jsonParser = new JsonParser();
		ContentClassifier contentClassifier = new ContentClassifier();

		int questionsCount = nextInt();
		for (int i = 0; i < questionsCount; i++) {
			Map<String, String> json = jsonParser.parse(in.readLine());
			String category = json.get("topic");
			String title = json.get("question");
			String text = json.get("excerpt");
			contentClassifier.learn(category, title, text);
		}

		return contentClassifier;
	}

	private void check(ContentClassifier contentClassifier) throws IOException {
		in = new BufferedReader(new InputStreamReader(System.in));
		JsonParser jsonParser = new JsonParser();

		int questionsCount = nextInt(), right = 0, total = 0;

		for (int i = 0; i < questionsCount; i++) {
			Map<String, String> json = jsonParser.parse(in.readLine());
			String category = json.get("topic");
			String title = json.get("question");
			String text = json.get("excerpt");
			String result = contentClassifier.classify(title, text);

			if (result.equals(category)) {
				++right;
			} else {
				out.println("Wrong:");
				out.println(category + " " + result);
				out.println(text);
				out.println(new Tokenizer().tokenize(text));
			}
			++total;
		}

		double accuracy = (double) right / total;
		System.err.println("Accuracy: " + accuracy);
	}

	private void answer(ContentClassifier contentClassifier) throws IOException {
		in = new BufferedReader(new InputStreamReader(System.in));

		JsonParser jsonParser = new JsonParser();
		CategoriesHolder categories = new CategoriesHolder();

		int questionsCount = nextInt();

		for (int i = 0; i < questionsCount; i++) {
			String result = categories.getName(rnd.nextInt(categories.size()));

			try {
				Map<String, String> json = jsonParser.parse(in.readLine());
				String title = json.get("question");
				String text = json.get("excerpt");
				result = contentClassifier.classify(title, text);
			} catch (Exception e) {

			}

			out.println(result);
		}
	}

	public static void main(String[] args) {
		new StackExchangeHackerRank().run();
	}

	public void run() {
		try {
			out = new PrintWriter(System.out);

			rnd = new Random();

			solve();

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private String nextToken() throws IOException {
		while (st == null || !st.hasMoreTokens()) {
			String line = in.readLine();

			if (line == null)
				return null;

			st = new StringTokenizer(line);
		}

		return st.nextToken();
	}

	private int nextInt() throws IOException {
		return Integer.parseInt(nextToken());
	}

	private long nextLong() throws IOException {
		return Long.parseLong(nextToken());
	}

	private double nextDouble() throws IOException {
		return Double.parseDouble(nextToken());
	}
}