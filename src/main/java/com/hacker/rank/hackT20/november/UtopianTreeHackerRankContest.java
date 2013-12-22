package com.hacker.rank.hackT20.november;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * The Utopian tree goes through 2 cycles of growth every year. The first growth cycle of the tree is during the monsoon season when 
 * it doubles in height. The second growth cycle is during the summer when it increases in height by 1 meter. If a new Utopian tree 
 * sampling of height 1 meter is planted just before the onset of the monsoon season, can you find the height of the tree after N cycles?
 * 
 * <br/> Input Format
 * <li/>The first line of the input contains an integer T, the number of testcases. 
 * <li/> T lines follow each line containing the integer N, indicating the number of cycles.
 *
 * <br/> Constraints
 * <br/> 1 <= T <= 10
 * <br/> 0 <= N <= 60
 *
 * <br/> Output Format
 * <br/> Print the height of the Utopian tree after N cycles.
 * 
 * @author Vivek
 */
public class UtopianTreeHackerRankContest {

	//~ static variable declaration // ==========================================================================
	private static BufferedReader bufferedReader;
	
	private static PrintWriter out;
	//~ static declaration ends // ==============================================================================
	
	//~ static block to initialize the variables // =============================================================
	static {
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		out 		   = new PrintWriter(System.out);
	}
	
	//~ static main method starts // ============================================================================
	public static void main (String...strings) throws IOException {
		
		// get the number of test cases
		int testCases = Integer.parseInt(bufferedReader.readLine());
		
		// get the cycles and solves for those cycles
		for (int i = 0; i < testCases; i++) {
			int cycles = Integer.parseInt(bufferedReader.readLine());
			
			int result = solve(cycles);
			
			out.println(result);
		}
		
		out.flush();
		bufferedReader.close();
	}
	
	// method that solves for the given cycles
	private static int solve(int cycles) throws IOException {
		if (cycles == 0) {
			return 1;
		}
		
		int result = 1;
		
		for (int i = 1; i <= cycles; i++) {
			if (i % 2 == 0) {
				result += 1;
			} else {
				result *= 2;
			}
		}
		return result;
	}
}