package com.hacker.rank.hackT20.november;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Mr. Road Runner bought a piece of land in the middle of a desert for a nominal amount. It turns out that the piece of land is now 
 * worth millions of dollars as it has an oil reserve under it. Mr. Road Runner contacts the ACME corp to set up the oil wells on his 
 * land. Setting up oil wells is a costly affair and the charges of setting up oil wells are as follows.
 *
 * The rectangular plot bought by Mr. Road Runner is divided into r * c blocks. Only some blocks are suitable for setting up the oil 
 * well and these blocks have been marked. ACME charges nothing for building the first oil well. For every subsequent oil well built, 
 * the cost would be the maximum ACME distance between the new oil well and the existing oil wells.
 *
 * If (x,y) is the position of the block where a new oil well is setup and (x1, y1) is the position of the block of an existing oil well, 
 * the ACME distance is given by
 * <br/><br/>
 * max(|x-x1|, |y-y1|)
 * the maximum ACME distance is the maximum among all the ACME distance between existing oil wells and new wells.
 *
 * If the distance of any two adjacent blocks (horizontal or vertical) is considered 1 unit, what is the minimum cost (E) in units it takes 
 * to set up oil wells across all the marked blocks?
 * <br/><br/>
 * Input format 
 * The first line of the input contains two space separated integers r c.
 * r lines follow each containing c space separated integers. 
 * 1 indicates that the block is suitable for setting up an oil well, whereas 0 isn’t.
 * <br/><br/>
 * r c  
 * M11 M12 ... M1c  
 * M21 M22 ... M2c  
 * ...  
 * Mr1 Mr2 ... Mrc  
 * Output format
 * Print the minimum value E as the answer.
 * 
 * @author Vivek
 */
public class OilWellHackerRankContest {

	//~ static variable declaration // ============================================================================================
	private static BufferedReader bufferedReader;
	
	private static PrintWriter writer;
	//~ static declaration ends // ================================================================================================
	
	
	//~ static block
	static {
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		writer 		   = new PrintWriter(System.out);
	}
	
	//~ main method starts here // ================================================================================================
	public static void main (String...strings) throws IOException {
		
		// get the input from the console
		String inputs = bufferedReader.readLine();
		
		int R = Integer.parseInt(inputs.split(" ")[0]); int C = Integer.parseInt(inputs.split(" ")[1]);
		
		assert(1 <= R && 50 >= R); assert (1 <= C && 50 >= C); // check the constraints

		List<Coordinates> position = new ArrayList<Coordinates>();
		
		// get the points
		for (int i = 1; i <= R; i++) {
			String points = bufferedReader.readLine();
			
			String[] positions = points.split(" "); assert (positions.length == C);
			
			for (int j = 1; j <= positions.length; j++) {
				int point = Integer.parseInt(positions[j - 1]);
				
				if (point == 1) {
					Coordinates coordinates = new Coordinates(i, j);
					position.add(coordinates);
				}
			}
		}
		
		Collections.sort(position);
		
		int result = solveForMaxDistanceBetweenPoints(position);
		
		writer.println(result);
		writer.flush();
		
		writer.close();
		bufferedReader.close();
	}
	
	private static int solveForMaxDistanceBetweenPoints (List<Coordinates> position) {
		int minE = 0;
		
		for (int i = 0; i < position.size() - 1;) {
			Coordinates point1 = position.get(i); // initial point
			Coordinates point2 = position.get(i + 1);
			
			i = i + 1;
			int xValue = (point2.getXPosition() - point1.getXPosition()) > 0 ? (point2.getXPosition() - point1.getXPosition()) : -1 * (point2.getXPosition() - point1.getXPosition());
			int yValue = (point2.getYPosition() - point1.getYPosition()) > 0 ? (point2.getYPosition() - point1.getYPosition()) : -1 * (point2.getYPosition() - point1.getYPosition());
			
			minE += Math.max(xValue, yValue);
		}
		
		return minE;
	}
}

/**
 * class that holds the coordinates of the points where a well can be bored
 * 
 * @author Vivek
 *
 */
class Coordinates implements Comparable<Coordinates> {
	
	//~ variable // ===============================================================================================================
	private int xPosition;
	
	private int yPosition;
	//~ variable declaration ends here // =========================================================================================
	
	public Coordinates (int xPosition, int yPosition) {
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}
	
	/**
	 * return xPosition
	 */
	public int getXPosition () {
		return this.xPosition;
	}
	
	/**
	 * return yPosition
	 */
	public int getYPosition () {
		return this.yPosition;
	}

	public int compareTo(Coordinates ordinates) {
		return this.yPosition == ordinates.getYPosition() ? 0 : (this.yPosition > ordinates.getYPosition() ? 1 : -1);
	}

}