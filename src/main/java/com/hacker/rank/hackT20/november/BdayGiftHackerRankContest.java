package com.hacker.rank.hackT20.november;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;

/**
 * Isaac has to buy a new HackerPhone for his girlfriend Amy. He is exploring the shops in the town to compare the prices whereupon 
 * he finds a shop located on the first floor of a building, that has a unique pricing policy. There are N stairs leading to the shop. 
 * A numbered ball is placed on each of the steps.
 * 
 * The shopkeeper gives Isaac a fair coin and asks him to toss the coin before climbing each step. If the result of the toss is a ‘Heads’, 
 * Isaac should pick up the ball, else leave it and proceed to the next step.
 *
 * The shopkeeper then asks Isaac to find the sum of all the numbers he has picked up (let’s say S). The price of the HackerPhone is then the 
 * expected value of S. Help Isaac find the price of the HackerPhone.
 *
 * <br/> Input Format
 * The first line of the input contains an integer N, the number of steps. N lines follow, which are the numbers written on the ball on each step.
 *
 * <br/> Output Format
 * A single line containing expected value.
 *
 * Note : Expected value must be printed as a decimal number having exactly one digit after decimal. It is guaranteed that the correct answer 
 * will have at most one digit after decimal.
 *
 * <br/> Constraints
 * <br/> 1 <= N <= 40
 * <br/> 1 <= number on any ball <=109
 * 
 * @author Vivek
 */
public class BdayGiftHackerRankContest {

	/**
	 * {@link BufferedReader} data
	 */
	private static BufferedReader bufferedReader;
	
	/**
	 * {@link PrintWriter} data
	 */
	private static PrintWriter out;
	
	/**
	 * initialize resources
	 */
	static {
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		out = new PrintWriter(System.out);
	}
	
	// starts main method
	public static void main (String...strings) throws IOException {
		int stepCount = Integer.parseInt(bufferedReader.readLine());
		
		long result = 0;
		
		for (int i = 0; i < stepCount; i++) {
			long number = Long.parseLong(bufferedReader.readLine());
			
			result += number;
		}
		
		out.println(new DecimalFormat(".#").format((double)result / 2));
		
		out.flush();
		
		out.close();
		
		bufferedReader.close();
	}
}