package com.hacker.rank.hack101.october;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Stack Exchange is an information powerhouse, built on the power of crowdsourcing. It has 105 different topics and each topic has a 
 * library of questions which have been asked and answered by knowledgeable members of the StackExchange community. The topics are as 
 * diverse as travel, cooking, programming, engineering and photography.
 * 
 * There are hand-picked ten different topics (such as Electronics, Mathematics, Photography etc.) from Stack Exchange, and we provide
 * you with a set of questions from these topics.
 * 
 * Given a question and an excerpt, this class's task is to identify which among the 10 topics it belongs to.
 * 
 * <br/> Input Format<br/>
 * The first line will be an integer N. N lines follow each line being a valid JSON object. The following fields of raw data are given in json
 *
 * <br/> question (string) : The text in the title of the question.
 * <br/> excerpt (string) : Excerpt of the question body.
 * <br/> topic (string) : The topic under which the question was posted.
 * <br/> The input for the program has all the fields but topic which you have to predict as the answer.

 * @author Vivek
 */
public class StackExchangeQuestIdentifier {

	private static final String fileName = "training_stack_exchange.json";
	
	public static void main(String...strings) throws IOException {
		// open the file
		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(fileName)));
		PrintWriter out 			  = new PrintWriter(System.out);
		
		int jsonLine = Integer.parseInt(bufferedReader.readLine());

		assert(jsonLine >= 1 && jsonLine <= 22000);
		
		for (int i = 0; i < jsonLine; i++) {
			// get the JSON object
			Map<Classifier, CharSequence> parsedLine = JSONParser.parserLine(bufferedReader.readLine());
			
			CharSequence topic    = parsedLine.get(Classifier.TOPIC);
			CharSequence question = parsedLine.get(Classifier.QUESTION);
			CharSequence excerpt  = parsedLine.get(Classifier.EXCERPT);
			
			ClassifyText.classify(topic, question, excerpt);
		}
		
		bufferedReader.close();
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		
		// get the number of queries
		int queryCount = Integer.parseInt(bufferedReader.readLine());
		
		for (int i = 0; i < queryCount; i++) {
			Map<Classifier, CharSequence> query = JSONParser.parserLine(bufferedReader.readLine());
			
			CharSequence topic    = query.get(Classifier.TOPIC);
			CharSequence question = query.get(Classifier.QUESTION);
			CharSequence excerpt  = query.get(Classifier.EXCERPT);
			
			String category = ClassifyText.findCategory(topic, question, excerpt);
			out.println(category);
		}
		
		out.flush();
		out.close();
	}
}

/**
 * this class will classify the text comming from the input line
 */
class ClassifyText {
	/**
	 * unwanted characters set
	 */
	private static final String[] TRASHCHAR = { "\\n", "\\r", "..." };
	
	private static final String REGEX 		= "[^\\w']+";
	
	private static final int MIN_LENGTH     = 2;
	
	private static final Map<Integer, String> ID_NAME_PAIR = new HashMap<Integer, String>();
	
	private static final Map<String, Integer> NAME_ID_PAIR = new HashMap<String, Integer>();
	
	private static final Map<Bigram, WordStatistics> bigramStatistics = new HashMap<Bigram, WordStatistics>();
	
	private static final Map<String, WordStatistics> wordStatistics = new HashMap<String, WordStatistics>();
	
	private static long[] documentsCountByCategory, wordsCountByCategory, bigramCountsByCategory;
	
	/**
	 * static block
	 */
	static {
		createTopicIdentifier();
		
		if (documentsCountByCategory == null && wordsCountByCategory == null && bigramCountsByCategory == null) {
			documentsCountByCategory = new long[ID_NAME_PAIR.size()];
			wordsCountByCategory = new long[ID_NAME_PAIR.size()];
			bigramCountsByCategory = new long[ID_NAME_PAIR.size()];
		}
	}
	
	/**
	 * build the classification
	 */
	public static void classify(CharSequence topic, CharSequence question, CharSequence excerpt) {
		// add the question and excerpt together to build the frequency of the words for this topic
		CharSequence text = question + " " + question + " " + excerpt;
		
		// remove the TRASHCHAR from the text
		for (String trash : TRASHCHAR) {
			text = text.toString().replace(trash, " ");
		}
		
		text = text.toString().trim();
		
		String[] tokens = text.toString().split(REGEX);
		
		List<String> uncommonWords = new ArrayList<String>();
		
		// iterate
		for (String token : tokens) {
			token = token.toLowerCase().endsWith("'s") ? token.substring(0, token.length() - "'s".length()) : token.toLowerCase().trim();
			
			if (token.length() >= MIN_LENGTH && !SkipWords.stopWords.contains(token)) {
				uncommonWords.add(token);
			}
		}
		
		int categoryID = NAME_ID_PAIR.get(topic);
		
		addByCategory(categoryID, uncommonWords.size());
		
		// create BIGRAM
		for (int i = 0; i < uncommonWords.size() - 1; i++) {
			Bigram bigram = new Bigram(uncommonWords.get(i), uncommonWords.get(i + 1));
			
			createBigram(categoryID, bigram);
			
			if (i == uncommonWords.size() - 2) {
				createWordStatistics(categoryID, uncommonWords.get(i + 1));
			} else {
				createWordStatistics(categoryID, uncommonWords.get(i));
			}
		}
	}
	
	/**
	 * method that finds the category based on the input value
	 * 
	 * @param topic
	 * @param question
	 * @param excerpt
	 * @return
	 */
	public static String findCategory(CharSequence topic, CharSequence question, CharSequence excerpt) {
		// content
		String content = question + " " + excerpt;
		
		double[] probs = new double[documentsCountByCategory.length];
		
		// remove the TRASHCHAR from the text
		for (String trash : TRASHCHAR) {
			content = content.toString().replace(trash, " ");
		}
		
		content = content.toString().trim();
		
		String[] tokens = content.toString().split(REGEX);
		
		List<String> uncommonWords = new ArrayList<String>();
		
		// iterate
		for (String token : tokens) {
			token = token.toLowerCase().endsWith("'s") ? token.substring(0, token.length() - "'s".length()) : token.toLowerCase().trim();
			
			if (token.length() >= MIN_LENGTH && !SkipWords.stopWords.contains(token)) {
				uncommonWords.add(token);
			}
		}
		
		for (int i = 0; i < uncommonWords.size() - 1; i++) {
			Bigram bigram = new Bigram(uncommonWords.get(i), uncommonWords.get(i + 1));
			
			WordStatistics wordStatistic = bigramStatistics.get(bigram);
			
			if (wordStatistic != null) {
				for (int j = 0; j < documentsCountByCategory.length; j++) {
					long top = wordStatistic.get(j) + 1;
					long bottom = bigramStatistics.size() + bigramCountsByCategory[j];
					probs[j] += Math.log((double) top / bottom);
				}
			}
		}
		
		// check words
		for (String word : tokens) {
			WordStatistics wordStatistic = wordStatistics.get(word);
			
			if (wordStatistic != null) {
				for (int j = 0; j < documentsCountByCategory.length; j++) {
					long top = wordStatistic.get(j) + 1;
					long bottom = wordStatistics.size() + wordsCountByCategory[j];
					probs[j] += Math.log((double) top / bottom);					
				}
			}
		}
		
		int categoryWithMaxProb = 0;

		for (int i = 1; i < documentsCountByCategory.length; i++) {
			if (probs[i] > probs[categoryWithMaxProb]) {
				categoryWithMaxProb = i;
			}
		}
		
		return ID_NAME_PAIR.get(categoryWithMaxProb);
	}
	/**
	 * create topic identifier
	 */
	private static void createTopicIdentifier() {
		// array of categories
		String[] CATEGORIES = {"gis", "security", "photo", "mathematica", "unix", "wordpress", "scifi", "electronics", "android", "apple"};
		
		for (int i = 0; i < CATEGORIES.length; i++) {
			ID_NAME_PAIR.put(i, CATEGORIES[i]);
			NAME_ID_PAIR.put(CATEGORIES[i], i);
		}
	}
	
	/**
	 * create the record by category name
	 */
	private static void addByCategory(int categoryId, long wordCount) {
		++documentsCountByCategory[categoryId];
		wordsCountByCategory[categoryId] += wordCount;
		bigramCountsByCategory[categoryId] += Math.max(0, wordCount - 1);
	}
	
	/**
	 * method that create Bigram holder
	 */
	private static void createBigram(int categoryID, Bigram bigram) {
		// check if this bigram already exists in the statistics. If exist increment the counter else add the new object
		WordStatistics wordStatistics = bigramStatistics.get(bigram);
		
		if (wordStatistics == null) {
			wordStatistics = new WordStatistics(documentsCountByCategory.length);
			bigramStatistics.put(bigram, wordStatistics);
		}
		wordStatistics.increament(categoryID);
	}
	
	/**
	 * create each words statistics
	 */
	private static void createWordStatistics(int categoryID, String word) {
		// check if this word exists in the wordStatics. If exists then increment the counter else add the new WordStatistics
		WordStatistics wordStatistic = wordStatistics.get(word);
		
		if (wordStatistic == null) {
			wordStatistic = new WordStatistics(documentsCountByCategory.length);
			wordStatistics.put(word, wordStatistic);
		}
		wordStatistic.increament(categoryID);
	}
}

/**
 * This class will parse the line coming from the input file
 * 
 * @author Vivek
 *
 * @param <String>
 */
class JSONParser {
	
	/**
	 * regex definition
	 */
	private static final String COMMA_REGEX = "[{}\",]";
	
	private static final String[] TRASHCHAR = { "\\n", "\\r", "..." };
	
	/**
	 * method that parse the line and store the information in a map
	 */
	public static Map<Classifier, CharSequence> parserLine(CharSequence line) {
		// store the formal argument to remain it constants
		CharSequence tempLine = line;
		
		// store the information from the line into the map
		Map<Classifier, CharSequence> parser = new HashMap<Classifier, CharSequence>();
		
		tempLine = tempLine.toString().trim().replaceAll(COMMA_REGEX, " ");

		tempLine = tempLine.toString().trim();
		
		// remove the TRASHCHAR from the text
		for (String trash : TRASHCHAR) {
			tempLine = tempLine.toString().replace(trash, "");
		}
				
		String[] tokens = tempLine.toString().split("   ");
		
		for (String token : tokens) {
			int index = token.indexOf(":");
			
			if (index != -1) {
				String key = token.substring(0, index);
				String value = token.substring(index + 1, token.length());
				
				Classifier classifier = Classifier.getClassifierEnum(key.trim());
				
				parser.put(classifier, value.trim());
		
			}
		}		
		return parser;
	}
}

/**
 * classifier enumerator
 * 
 * @author Vivek
 */
enum Classifier {
	/**
	 * trending topic
	 */
	TOPIC ("topic"),
	
	/**
	 * trending question
	 */
	QUESTION ("question"),
	
	/**
	 * trending excerpt
	 */
	EXCERPT ("excerpt");
	
	/**
	 * classifier string
	 */
	private String classifier;
	
	/**
	 * constructor
	 */
	private Classifier(String classifier) {
		this.classifier = classifier;
	}
	
	/**
	 * return classifier string
	 */
	public String getClassifier() {
		return this.classifier;
	}
	
	/**
	 * returns the classifier enum
	 */
	public static Classifier getClassifierEnum(String classifier) {
		
		// iterates
		for (Classifier classifier2 : Classifier.values()) {
			if (classifier2.getClassifier().equals(classifier)) {
				return classifier2;
			}
		}
		
		return null;
	}
}

/**
 * Bigram class
 */
class Bigram {
	
	/**
	 * firstWord data
	 */
	private String firstWord;
	
	/**
	 * secondWord data 
	 */
	private String secondWord;
	
	/**
	 * constructor with two parameter
	 */
	public Bigram (String firstWord, String secondWord) {
		this.firstWord = firstWord;
		this.secondWord = secondWord;
	}
	
	/**
	 * calculate hashCode
	 */
	@Override
	public int hashCode() {
		int hash = 31;
		
		hash = hash * (hash + (firstWord == null ? 0 : firstWord.hashCode())) * (hash + (secondWord == null ? 0 : secondWord.hashCode()));
		hash = StackExchangeQuestIdentifier.class.hashCode() * hash;
		
		return hash;
	}
	
	/**
	 * check the equality of the object
	 */
	@Override
	public boolean equals(Object obj) {
		boolean result = true;
		
		if (obj == null || (obj != null && !(obj instanceof Bigram))) 
			result = false;
		
		else {
			Bigram bigram = (Bigram) obj;
			
			if (this.firstWord.equals(bigram.firstWord) && this.secondWord.equals(bigram.secondWord))
				result = true;
		}
		return result;
	}
}

/**
 * class that records the frequency of the occurrence of the words
 * 
 * @author Vivek
 */
class WordStatistics {
	/**
	 * count the occurrences 
	 */
	private long count;
	
	/**
	 * count the frequencies of the occurrences for a category
	 */
	private long[] frequencies;
	
	/**
	 * default constructor with one parameter
	 */
	public WordStatistics (int categoryCount) {
		this.count = 0;
		frequencies = new long[categoryCount];
	}
	
	/**
	 * increment the counter
	 */
	public void increament(int categoryID) {
		this.count += 1;
		++frequencies[categoryID];
	}
	
	/**
	 * get count
	 * 
	 * @param categoryId
	 * @return
	 */
	public long get(int categoryId) {
		return frequencies[categoryId];
	}
	
	/**
	 * return count
	 */
	public long getCount() {
		return this.count;
	}
}

/**
 * class SkipWords contains the common words which needs to be skipped while building the {@link ClassifyText}
 * 
 * @author Vivek
 *
 */
class SkipWords {
	/**
	 * collection of words that needs to be skipped while reading the input from the file
	 */
	private static final String[] SKIPWORDS = {"a's", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "ain't",	"all", "allow", "allows", "almost", "alone",
		"along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate",
		"appropriate", "are", "aren't", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe",
		"below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "c'mon", "c's", "came", "can", "can't", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning",
		"consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldn't", "course", "currently", "definitely", "described", "despite", "did", "didn't", "different", "do", "does", "doesn't", "doing",
		"don't", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex",
		"exactly", "example", "except", "far", "few", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go",
		"goes", "going", "gone", "got", "gotten", "greetings", "had", "hadn't", "happens", "hardly", "has", "hasn't", "have", "haven't", "having", "he", "he's", "hello", "help", "hence", "her", "here", "here's", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi",
		"him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i'd", "i'll", "i'm", "i've", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar",
		"instead", "into", "inward", "is", "isn't", "it", "it'd", "it'll", "it's", "its", "itself", "just", "keep", "keeps", "kept", "know", "known", "knows", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "let's", "like", "liked", "likely", "little", "look", "looking", "looks",
		"ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new",
		"next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours",
		"ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively",
		"right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldn't", "since", "six",
		"so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "t's", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx",
		"that", "that's", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "there's", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "they'd", "they'll", "they're", "they've", "think", "third", "this", "thorough", "thoroughly", "those",
		"though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use",
		"used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasn't", "way", "we", "we'd", "we'll", "we're", "we've", "welcome", "well", "went", "were", "weren't", "what", "what's", "whatever", "when", "whence", "whenever", "where", "where's", "whereafter",
		"whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "who's", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "won't", "wonder", "would", "wouldn't", "yes", "yet", "you", "you'd",
		"you'll", "you're", "you've", "your", "yours", "yourself", "yourselves", "zero"};
	
	/**
	 * store the skip words array into the set
	 */
	public static Set<String> stopWords;
	
	/**
	 * static blocks that store all the skip words to a stopWords set
	 */
	static {
		stopWords = new HashSet<String>();
		
		for (String word : SKIPWORDS) {
			stopWords.add(word);
		}
	}
}