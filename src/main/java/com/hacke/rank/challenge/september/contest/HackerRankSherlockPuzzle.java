package com.hacke.rank.challenge.september.contest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/** 
 * Sherlock Holmes is bored to death as there aren’t any interesting cases to solve. Dr Watson finding it impossible to be around Sherlock 
 * goes out to get some fresh air. Just as he lays his feet out of the door, he is thrown right back into his house by the explosion in front 
 * of his apartment. The metropolitan police arrive at the scene a few minutes later. They search the place thoroughly for any evidence that 
 * could throw some light on the event that unfolded. Unable to get any clues, they call Sherlock for help. After repeated persuasion by
 * Mycroft Holmes and Dr Watson, Sherlock agrees to look at the crime scene.
 *
 * Sherlock walks into the apartment, the walls are all black and dripping wet as the fire was recently extinguished. With his skills, he 
 * observes, deduces and eliminates the impossible. What’s left after it is a simple puzzle left by an evil computer genius and the future 
 * Arch Nemesis of Sherlock, Jim Moriarty.
 *
 * Given a binary string (S) which contains ‘0’s and ‘1’s and an integer K, find the length (L) of the longest continuous subsequence of 
 * (S * K) such that twice the number of zeroes is <= thrice the number of ones (2 * #0s <= 3 * #1s) in that sequence.
 *
 * S * K is defined as follows: S * 1 = S
 * S * K = S + S * (K - 1)
 *
 * Input Format 
 * The first (and only) line contains an integer K and the binary string S separated by a single space.
 *
 * Constraints
 * 1 <= |S| <= 1,000,000
 * 1 <= K <= 1,000,000
 *
 * Output Format
 * A single integer L - the answer to the test case
 *
 * Sample Input
 *
 * 2 00010
 * Sample Output
 * 2
 * 
 * @author Vivek
 * 
 **/
public class HackerRankSherlockPuzzle {

	//~ Static initializer // =============================================================================
	private static BufferedReader bufferedReader;
	
	
	//~ Methods // ========================================================================================
	public static void main(String[] args) throws IOException {
		init();
		
		String input = bufferedReader.readLine();
		
		long K = Long.parseLong(input.split(" ")[0]); String binaryString = input.split(" ")[1];
		
		destroy();
		
		long count0 = 0;
		long count1 = 0;
		
		char[] binaryArray = binaryString.toCharArray();
		
		for (int i = 0; i < binaryString.length(); i++) {
			if (binaryArray[i] == '0') {
				count0++;
			}
			
			else if (binaryArray[i] == '1') {
				count1++;
			}
		}
		
		solve(binaryString, count0 * K, count1 * K);
	}
	
	//~ Method init() // ==================================================================================
	private static void init() throws IOException {
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	//~ Method destroy() // ===============================================================================
	private static void destroy() throws IOException {
		if (bufferedReader != null) {
			bufferedReader.close();
		}
	}
	
	//~ Method solve() // =================================================================================
	private static void solve(String binaryString, long count0, long count1) {
		if (count0 * 2 <= count1 * 3) {
			System.out.println(binaryString.length());
		}
		
		else {
			int difference = (int) ((count0 * 2 - count1 * 3) - 2);
			System.out.println(binaryString.substring(difference, binaryString.length()).length());
		}
	}
}