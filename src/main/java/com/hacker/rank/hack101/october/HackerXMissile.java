package com.hacker.rank.hack101.october;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * save the city
 *  
 * @author Vivek
 */
public class HackerXMissile {

	//~ static initializer // =====================================================================
	private static BufferedReader bufferedReader;
	
	//~ static block // ===========================================================================
	static {
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	//~ static main method // =====================================================================
	public static void main(String...strings) throws IOException {
		
		// number of missiles
		int numberOfMissiles = Integer.parseInt(bufferedReader.readLine());
		
		List<Integer> time = new ArrayList<Integer>();
		List<Integer> freq = new ArrayList<Integer>();
		
		for (int i = 0; i < numberOfMissiles; i++) {
			String input = bufferedReader.readLine();
			
			int moment = Integer.parseInt(input.split(" ")[0]); // time at which the bomb was released
			time.add(moment);
			
			int frequency = Integer.parseInt(input.split(" ")[1]); // frequency of the missile
			freq.add(frequency);
		}
		
		int j = 0;
		int count = 1;
		
		for(int i = 1; i < freq.size(); i++) {
			int timeElapsed = time.get(i) - time.get(j);
			
			if (freq.get(j) + timeElapsed < freq.get(i)) {
				count++;
			}
		}
		
		System.out.println(count);
	}
}