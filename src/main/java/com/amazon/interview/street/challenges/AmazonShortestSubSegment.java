package com.amazon.interview.street.challenges;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Given a String and a number of test string this class will find the shortest distane in the given String where the test string appears.
 *  
 * @author a501097
 */
public class AmazonShortestSubSegment {

	//~ static variable declarations // ==========================================================
	/**
	 * {@link BufferedReader} data
	 */
	private static BufferedReader bufferedReader;
	
	/**
	 * holder data. Holds the position of the test data in the input string
	 */
	private static int[] holder;
	
	/**
	 * PATTERN for the input String
	 */
	private static final String PATTERN = "([^a-zA-Z])";
	
	/**
	 * static init method
	 */
	private static void init() throws IOException {
		AmazonShortestSubSegment.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	/**
	 * static destroy method
	 */
	private static void destroy() throws IOException {
		if (AmazonShortestSubSegment.bufferedReader != null) 
			AmazonShortestSubSegment.bufferedReader.close();
	}
	
	//~ static main method starts here // =========================================================
	/**
	 * main method
	 */
	public static void main (String[] strings) throws IOException {
		// initialize the resources
		init();
		
		String inputString = AmazonShortestSubSegment.bufferedReader.readLine();
		
		// convert the charSequence to an array of String
		String[] words = inputString.split(" ");
		
		// get the number of words to search for
		int nbOfWords = Integer.parseInt(AmazonShortestSubSegment.bufferedReader.readLine());
		
		List<String> search = new ArrayList<String>();
		
		// fill the words to search for
		for (int i = 0; i < nbOfWords; i++) {
			search.add(AmazonShortestSubSegment.bufferedReader.readLine().toLowerCase());
		}
		
		holder = new int[words.length];
		
		Arrays.fill(holder, -1);
		
		// destroy the used resources
		destroy();
		
		solve(words, search);
		
		boolean done = false;
		
		List<String> answer = new ArrayList<String>();
		
		int j = 0;
		while (!done) {
			for (int i = 0; i < search.size(); i++) {
				if (holder[j] != -1) {
					answer.add(words[holder[j]]);
				} else {
					i = -1;
					answer.clear();
				}
				
				j++;
				
				if (answer.size() == search.size()) {
					done = true;
					break;
				} else if (j == holder.length) {
					System.out.println("NO SUBSEGMENT FOUND");
					done = true;
					j = -1;
					break;
				}
			}
		}
		
		if (done == true && j != -1) {
			for (String word : answer) {
				System.out.print(word + " ");
			}
		}
	}
	
	/**
	 * static solve method.
	 * 
	 * This method will read through the words array and locate the position of the words to search for in search list and fill it in holder array
	 */
	private static void solve(String[] words, List<String> search) {
		// iterate words
		for (int i = 0; i < words.length; i++) {
			words[i] = words[i].replaceAll(PATTERN, "");
			
			if (words[i] != null) {
				// check if this word is in the search list
				if (search.contains(words[i].toLowerCase())) {
					holder[i] = i;
				}
			}
		}
		// end iteration
	}
}