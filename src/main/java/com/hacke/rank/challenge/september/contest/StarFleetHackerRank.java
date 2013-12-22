package com.hacke.rank.challenge.september.contest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author Vivek
 */
public class StarFleetHackerRank {

	//~ Static variable // =================================================================
	private static BufferedReader bufferedReader;
	
	private static List<StarFleetHackerRank.PositionAndFreq> fighters = new ArrayList<StarFleetHackerRank.PositionAndFreq>();
	
	//~ Static method initializer ==========================================================
	private static void init() throws IOException 
	{
		StarFleetHackerRank.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	//~ Static method destroyer ============================================================
	private static void destroy() throws IOException 
	{
		
		if (StarFleetHackerRank.bufferedReader != null) 
			StarFleetHackerRank.bufferedReader.close();
	}
	
	//~ Static main method begins // =======================================================
	public static void main(String...strings) throws IOException {
		init(); // initialize the resources
		
		String inputString = StarFleetHackerRank.bufferedReader.readLine();
		
		int N = Integer.parseInt(inputString.split(" ")[0]); int Q = Integer.parseInt(inputString.split(" ")[1]); int V = Integer.parseInt(inputString.split(" ")[2]);
		
		fightersCoordinatesAndFreq(N);
		
		destroy(); // destroy the resources
	}
	//~ Static main method ends // =========================================================
	
	//~ Fighters coordinates method // =====================================================
	/**
	 * method that gets the fighters coordinates and frequency
	 */
	private static void fightersCoordinatesAndFreq(int N) throws IOException {
		
		for (int i = 0; i < N; i++) {
			String fighersInput = StarFleetHackerRank.bufferedReader.readLine();
			
			int X = Integer.parseInt(fighersInput.split(" ")[0]); int Y = Integer.parseInt(fighersInput.split(" ")[1]); int F = Integer.parseInt(fighersInput.split(" ")[2]);
			
			PositionAndFreq positionAndFreq = new PositionAndFreq(X, Y, F);
			
			fighters.add(positionAndFreq);
		}
		
		Collections.sort(fighters); // sort the fighters list based on their Y-axis
	}
	
	/**
	 * static inner class that holds the coordinates and frequency of a fighter
	 */
	@SuppressWarnings( {"unused"} )
	private static class PositionAndFreq implements Comparable<PositionAndFreq> {
		
		// X - Coordinates
		private final int X;
		
		// Y - Coordinates
		private final int Y;
		
		// F - Frequency
		private final int F;
		
		public PositionAndFreq(int X, int Y, int F) {
			this.X = X;
			this.Y = Y;
			this.F = F;
		}

		public int compareTo(PositionAndFreq obj) {
			return obj.Y > this.Y ? 1 : (obj.Y < this.Y ? -1 : 0);
		}
	}
}