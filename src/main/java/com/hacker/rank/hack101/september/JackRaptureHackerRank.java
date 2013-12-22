package com.hacker.rank.hack101.september;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Jack has just moved to a new city called Rapture. However, he is confused by Rapture’s public transport system. The rules of the public transport are as follows:
 *
 * Every pair of connected stations has a fare assigned to it.
 *
 * If a passenger travels from station A to station B, he only has to pay the difference between the fare from A to B and the cumulative fare that he has paid to 
 * reach station A [fare(A,B) - total fare to reach station A]. If the difference is negative, he can travel free of cost from A to B.
 *
 * Since Jack is new to the city, he is unemployed and low on cash. He needs your help to figure out the most cost efficient way to go from the first station to 
 * the last station. You are given the number of stations N, and the fare between the E pair of stations that are connected.
 *
 * Input Format
 * The first line contains two integers, N and E, followed by E lines containing three integers each: the two stations that are connected to each other and the 
 * fare between them (C).
 *
 * Output Format
 * The minimum fare to be paid to reach station N from station 1. If the station N cannot be reached from station 1, print “NO PATH EXISTS” (without quotes).
 * 
 *  <br> Constraints
 * 
 *  <br> 1<=N<=50000
 *  <br> 1<=E<=500000
 *  <br> 1<=C<=10^7
 *
 *  <br> Sample Input
 *  <br> 5 5
 *  <br> 1 2 60
 *  <br> 3 5 70 
 *  <br> 1 4 120
 *  <br> 4 5 150
 *  <br> 2 3 80
 *
 * <br> Sample Output
 * <br> 80
 * 
 * @author Vivek
 */
public class JackRaptureHackerRank {

	//~ Static variable // ==============================================================================
	private static BufferedReader bufferedReader;
	
	private static Map<Integer, List<int[]>> stations = new HashMap<Integer, List<int[]>>();
	
	private static Node root;
	
	//~ Static initializer method // ====================================================================
	private static void init() throws IOException {
		JackRaptureHackerRank.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	//~ Static destroyer method // ======================================================================
	private static void destroy() throws IOException {
		JackRaptureHackerRank.bufferedReader.close();
	}
	
	//~ Static main method // ===========================================================================
	public static void main(String...strings) throws IOException {
		init(); // initialize the resources
		
		String inputString = JackRaptureHackerRank.bufferedReader.readLine(); // contains the number of points and the route
		
		int N = Integer.parseInt(inputString.split(" ")[0]); int E = Integer.parseInt(inputString.split(" ")[1]);
		
		int initialPoint = 1; int finalPoint = N;
		
		// get the route
		for (int i = 0; i < E; i++) {
			String routeString = JackRaptureHackerRank.bufferedReader.readLine();
			
			int point1 = Integer.parseInt(routeString.split(" ")[0]); int point2 = Integer.parseInt(routeString.split(" ")[1]);
			int fare   = Integer.parseInt(routeString.split(" ")[2]);
			
			if (stations.containsKey(new Integer(point1))) {
				List<int[]> pointsAndFare = stations.get(new Integer(point1));
				
				int[] newPointAndFare = new int[2];
				newPointAndFare[0] = point2;
				newPointAndFare[1] = fare;
				
				pointsAndFare.add(newPointAndFare);
				stations.put(new Integer(point1), pointsAndFare);
			}
			
			else {
				List<int[]> connectedPoints = new ArrayList<int[]>();
				int[] pointAndFare = new int[2];
				pointAndFare[0] = point2;
				pointAndFare[1] = fare;
				
				connectedPoints.add(pointAndFare);
				stations.put(point1, connectedPoints);
			}
		}
		
		buildRoute(stations, initialPoint, finalPoint);
		/*int amount = 0;
		
		amount = findMinimumFare(stations, initialPoint, finalPoint, amount);
		
		System.out.println(amount);*/
		
		destroy(); // destroy the resources
	}
	//~ Static main method ends // ======================================================================
	
	/**
	 * method that finds the minimum fare
	 */
	private static void buildRoute(Map<Integer, List<int[]>> stations, int initialPoint, int finalPoint) {
		List<int[]> connectingPoint = stations.get(new Integer(initialPoint));
		
		System.out.println("There are " + connectingPoint.size() + "paths from " +initialPoint);
		
		for (int[] points : connectingPoint) {
			initialPoint = points[0];
			
			if (initialPoint == finalPoint) {
				
			}
			
			else {
				
			}
		}
	}
}

/**
 * class that holds the station and its connected routes
 */
class Node {
	
	/**
	 * point data
	 */
	private int point;
	
	/**
	 * connected routes
	 */
	private List<Node> nodes;
	
	/**
	 * default constructor
	 */
	public Node(int point) {
		this.point = point;
		this.nodes = new ArrayList<Node>();  
	}
	
	/**
	 * get the point data
	 */
	public int getPoint() {
		return this.point;
	}
	
	/**
	 * get the connected routes
	 */
	public List<Node> getNodes() {
		return this.nodes;
	}
}