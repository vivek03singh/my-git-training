package com.amazon.interview.street.challenges;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * The Euclidean Technique for Computing the GCD of two integers
 * 
 * Given two integers ‘x’ and ‘y’ a recursive technique to find their GCD is the Euclidean Algorithm.
 * This Algorithm tells us that, for computing the GCD of two positive integers x and y, if x and y are equal, GCD(x,y) = x, 
 * otherwise GCD(x,y) = GCD(x-y,y) if x > y. There are a few optimizations that can be made to the above logic, to arrive at a more efficient implementation.
 * 
 * @author Vivek
 */
public class EuclidianGCDHackerRankContest {

	//~ static variable declaration // ===========================================================================
	private static BufferedReader bufferedReader;
	
	private static PrintWriter printWriter;
	//~ static variable declaration ends here // =================================================================
	
	//~ static block starts here // ==============================================================================
	static {
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		printWriter    = new PrintWriter(System.out);
	}
	//~ static block ends here // ================================================================================
	
	//~ main method starts here 
	public static void main(String...strings) throws IOException {
		// get the input from the console
		String input = bufferedReader.readLine();
		
		int M = Integer.parseInt(input.split(" ")[0]); int K = Integer.parseInt(input.split(" ")[1]);
		
		findGCD(M, K);
		
		printWriter.flush();
		bufferedReader.close();
		printWriter.close();
	}
	
	//~ static method that finds GCD of two numbers // ===========================================================
	private static void findGCD(int M, int K) {
		int m = M, k = K; // in order to keep formal arguments constants
		
		while (k != 0 && m != 0) {
			if (m > k) { m = m % k; }
			else { k = k % m; } 
		}
		
		printWriter.print(Math.max(m,  k));
	}
}