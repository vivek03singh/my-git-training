package com.hacker.rank.hack101.october;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Two strings A and B, consisting of small English alphabet letters are called pseudo-isomorphic if
 *
 * Their lengths are equal
 * </br>For every pair (i,j), where 1 <= i < j <= |A|, B[i] = B[j], iff A[i] = A[j]
 * </br>For every pair (i,j), where 1 <= i < j <= |A|, B[i] != B[j] iff A[i] != A[j]
 * </br>Naturally, we use 1-indexation in these definitions and |A| denotes the length of the string A.
 * </br>
 * You are given a string S, consisting of no more than 105 lowercase alphabetical characters. For every prefix of S denoted by S’, you are expected to 
 * find the size of the largest possible set of strings , such that all elements of the set are substrings of S’ and no two strings inside the set are 
 * pseudo-isomorphic to each other.
 * </br>
 * </br>if S = abcde 
 * </br>then, 1st prefix of S is ‘a’
 * </br>then, 2nd prefix of S is ‘ab’
 * </br>then, 3rd prefix of S is ‘abc’
 * </br>then, 4th prefix of S is ‘abcd’ and so on..
 * </br>
 * </br>Input Format
 * </br>The first and only line of input will consist of a single string S. The length of S will not exceed 10^5.
 * </br>
 * </br>Output Format
 * </br>Output N lines. On the ith line, output the size of the largest possible set for the first i alphabetical characters of S such that no two strings 
 * in the set are pseudo-isomorphic to each other.
 * </br>
 * Constraints
 * </br>1 <= |S| <= 105
 * </br>S contains only lower-case english alphabets (‘a’ - ‘z’).
 * </br>
 * Sample Input #00
 * </br>
 * </br>abbabab  
 * </br>Sample Output #00
 * </br>
 * </br>1   
 * </br>2   
 * </br>4   
 * </br>6   
 * </br>9   
 * </br>12   
 * </br>15

 * @author Vivek
 */

public class PseudoIsomorphicSubstrings {

	//~ static variable declaration // =======================================================================
	private static BufferedReader bufferedReader;
	
	//~ static block // ======================================================================================
	static {
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	//~ static main method starts here // ====================================================================
	public static void main(String...strings) throws IOException {
		// get the input string
		String inputString = bufferedReader.readLine();
		
		// close all open stream
		close();
		
		char[] characters = inputString.toCharArray();
		
		List<String> subStrings = new ArrayList<String>();
		
		// find the substring which are not PseudoIsomorphic 
		for (int i = 0; i < characters.length; i++) {
			int k = 1;
			for (int j = 0; j <= i; j++) {
				String subS = inputString.substring(j, k);
				
				if (!subStrings.contains(subS)) {
					subStrings.add(subS);
				}
				k++;
			}
		}
	}
	//~ static main method ends here // ======================================================================
	
	//~ static close method // ===============================================================================
	private static void close() throws IOException {
		if (bufferedReader != null) {
			bufferedReader.close();
		}
	}
	
}