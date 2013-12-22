package com.hacker.rank.hack101.october;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Minimize the unfairness
 * 
 * @author Vivek
 */
public class HackerRankAngryChildren {

	//~ static initializer // ====================================================================================
	private static BufferedReader bufferedReader;
	
	private static List<Integer> candies = new ArrayList<Integer>();
	
	private static List<Integer> availableUnfairness = new ArrayList<Integer>();
	
	//~ static block // ==========================================================================================
	static {
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	//~ static main method // ====================================================================================
	public static void main(String...strings) throws IOException {
		// get the number of packets
		int packets = Integer.parseInt(bufferedReader.readLine());
		
		// get the number of children
		int children = Integer.parseInt(bufferedReader.readLine());
		
		// get the number of candies available in the packet
		for (int i = 0; i < packets; i++) {
			candies.add(Integer.parseInt(bufferedReader.readLine()));
		}
		
		Collections.sort(candies);
		
		for (int i = 0; i < candies.size(); ) {
			int unfairness = candies.get(children - 1 + i) - candies.get(i);
			availableUnfairness.add(unfairness);
			i++;
			
			if (children - 1 + i == candies.size())
				break;
		}
		
		Collections.sort(availableUnfairness);
		
		System.out.println(availableUnfairness.get(0));
		
		// close the resources
		close();
	}
	
	private static void close() throws IOException {
		if (bufferedReader != null) 
			bufferedReader.close();
	}
}