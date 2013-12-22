package com.amazon.interview.street.challenges;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 
 * @author Vivek
 */
public class AmazonFindShortestSubSegment {

	//~ static variable // =========================================================================================
	
	/**
	 * {@link BufferedReader} data object
	 */
	private static BufferedReader bufferedReader;
	
	/**
	 * {@link LinkedHashMap} data object
	 * 
	 * holds the string to search for as a key and its position in the input string
	 */
	private static Map<String, Integer> holder = new LinkedHashMap<String, Integer>();
	
	/**
	 * regex to restrict or remove the unwanted characters from the string
	 */
	private static final Pattern PATTERN = Pattern.compile("[^a-zA-Z]+");
	
	/**
	 * default constructor
	 */
	public AmazonFindShortestSubSegment() {
		AmazonFindShortestSubSegment.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	//~ static methods // ==========================================================================================
	
	/**
	 * main method starts here
	 */
	public static void main(String...strings) throws IOException {
		// initialize the resources
		new AmazonFindShortestSubSegment();
		
		// get the input
		CharSequence inputString = AmazonFindShortestSubSegment.bufferedReader.readLine();
		
		// convert the whole text into an array and remove the unwanted characters and numbers
		String[] texts = PATTERN.split(inputString);
		
		// get the number of words to search for
		int nbOfText = Integer.parseInt(AmazonFindShortestSubSegment.bufferedReader.readLine());
		
		List<String> words = new ArrayList<String>();
		
		// get the actual text to search for and put them in words list
		for (int i = 0; i < nbOfText; i++) {
			words.add(AmazonFindShortestSubSegment.bufferedReader.readLine());
		}
		
		solve(inputString, texts, words);
	}
	
	/**
	 * method that will find the closest appearance of words in an inputstring
	 */
	private static void solve(CharSequence inputString, String[] texts, List<String> words) {
		
	}
}