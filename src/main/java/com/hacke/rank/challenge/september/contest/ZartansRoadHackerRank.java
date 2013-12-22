package com.hacke.rank.challenge.september.contest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class will find if there are any route using which a person comes back to the point where that person starts from.
 * 
 * @author Vivek
 */
public class ZartansRoadHackerRank {

	//~ static variable // ====================================================================
	private static BufferedReader bufferedReader;
	
	//~ default constructor // ================================================================
	public ZartansRoadHackerRank() {
		ZartansRoadHackerRank.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	//~ main method starts here // ============================================================
	public static void main(String...strings) throws IOException {
		new ZartansRoadHackerRank();
		
		int N = Integer.parseInt(ZartansRoadHackerRank.bufferedReader.readLine()); // number of junction
		int R = Integer.parseInt(ZartansRoadHackerRank.bufferedReader.readLine()); // number of routes
		
		for (int i = 0; i < R; i++) {
			String points = ZartansRoadHackerRank.bufferedReader.readLine();
			
			int point1 = Integer.parseInt(points.split(" ")[0]); int point2 = Integer.parseInt(points.split(" ")[1]);
		}
	}
	//~ main method ends here // ==============================================================
}