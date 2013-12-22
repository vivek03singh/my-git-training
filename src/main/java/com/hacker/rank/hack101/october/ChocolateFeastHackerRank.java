package com.hacker.rank.hack101.october;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Given the total amount and the price of one chocolate this class will find out the number of chocolates one can buy. Also this class will take an
 * input of the number of wrappers that is required to avail a free chocolates
 * 
 * <br> Input Condition </br>
 * <br> Total amount N: 2 <= N <= 100000 </br>
 * <br> Price of each chocolate C: 1 <= C <= N </br>
 * <br> The number of wrappers M: 2 <= M <= N </br>
 * 
 * @author Vivek
 */
public class ChocolateFeastHackerRank {

	//~ static declaration // ============================================================================
	private static BufferedReader bufferedReader;
	
	private static List<Integer> answers = new ArrayList<Integer>();
	
	//~ static block // ==================================================================================
	static {
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	//~ main method starts here
	public static void main(String...strings) throws IOException {
		
		int testCases = Integer.parseInt(bufferedReader.readLine());
		
		for (int i = 0; i < testCases; i++) {
			String input = bufferedReader.readLine();
			
			int totalAmount = Integer.parseInt(input.split(" ")[0]); int price = Integer.parseInt(input.split(" ")[1]); int wrappers = Integer.parseInt(input.split(" ")[2]);
			
			solve(totalAmount, price, wrappers);
		}
		
		// close the used resources
		close();
		
		for (int i = 0; i < answers.size(); i++) {
			System.out.println(answers.get(i));
		}
	}
	
	private static void solve(int totalAmount, int price, int wrappers) {
		
	}
	
	//~ static method // =================================================================================
	private static void close() throws IOException {
		if (bufferedReader != null) 
			bufferedReader.close();
	}
}