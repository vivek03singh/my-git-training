package com.amazon.interview.street.challenges;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class HalloweenPartyContest {

	//~ static variable declaration // ===================================================================================
	private static BufferedReader bufferedReader;
	
	private static PrintWriter out;
	// ~ static declaration ends // ======================================================================================
	
	//~ static block // ==================================================================================================
	static {
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		out = new PrintWriter(System.out);
	}
	//~ static block ends // =============================================================================================
	
	//~ static main method // ============================================================================================
	public static void main(String...strings) throws IOException {
		
		// get the number of test cases
		int testCases = Integer.parseInt(bufferedReader.readLine());
		
		for (int i = 0; i < testCases; i++) {
			
			// get the number
			int K = Integer.parseInt(bufferedReader.readLine());
			
			out.println((long) (K / 2) * ((K + 1) / 2));
		}
		
		out.flush();
	}
}