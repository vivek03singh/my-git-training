package com.hacke.rank.challenge.september.contest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * <blockquote>Problem Statement</blockquote>
 * Sherlock Holmes is getting paranoid about Professor Moriairty, his archenemy. All his efforts to subdue Moriairty have been in vain. These days
 * Sherlock is working on the conversation he had with Dr. Watson. Watson mentioned that the CIA have been having weird problems with there
 * supercomputer 'The Beast' recently.
 * 
 * This afternoon, Sherlock recieved a note from Moriairty, saying he has infected 'The Beast' with a virus. In addition the note had the number 'k'
 * printed on it. After doing some calculation, Sherlock found out the key to remove the virus is the largest 'Decent' number having 'k' digits.
 * 
 * <br>A 'Decent' Number has-</br>
 * <p>1. Only 3 and 5 as its digit</p>
 * <p>2. Number of times 3 appears is divisible by 5.</p>
 * <p>3. Number of times 5 appears is divisible by 3.</p>
 * 
 * <p>Meanwhile, the counter to destruction of ‘The Beast’ is running very fast. Can you save ‘The Beast’ and find the key before Sherlock?</p>
 * 
 * <br>Input Format</br>
 * <p>1st line will contain an integer T, the number of test cases, followed by T lines, each line containing an integer N i.e. the number of digits 
 * in the number</p>
 * 
 * <br>Output Format</br>
 * <p>Largest Decent number having N digits. If no such number exists, you tell Sherlock that he is wrong and print ‘-1’ </p>
 * 
 * <br>Constraints</br>
 * <p>1<=T<=20</p>
 * <p>1<=N<=100000</p>
 * 
 * <br>Sample Input</br>
 * <p>4</p>
 * <p>1</p>
 * <p>3</p>
 * <p>5</p>
 * <p>11</p>	
 * 
 * <br>Sample Output</br>
 * <p>-1</p>
 * <p>555</p>
 * <p>33333</p>
 * <p>55555533333</p>
 * 
 * @author Vivek
 */
public class SherlockAndTheBeast {

	//~ Static initializer // ===========================================================
	
	private static BufferedReader bufferedReader;
	private static final int FAILURE_CODE = -1;
	private static List<String> decentNumbers = new ArrayList<String>();
	
	// Methods // =======================================================================
	
	private static void init() throws IOException {
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	private static void destroy() throws IOException {
		if (bufferedReader != null) {
			bufferedReader.close();
		}
	}
	
	// main method
	public static void main(String[] args) throws IOException {
		init();
		
		int testCases = Integer.parseInt(bufferedReader.readLine());
		
		if (testCases < 1 || testCases > 30) {
			System.exit(FAILURE_CODE);
		}
		
		for (int i = 0; i < testCases; i++) {
			long N = Long.parseLong(bufferedReader.readLine());
			
			solveForN(N);
		}
		
		for (String decentNumber : decentNumbers) {
			System.out.println(decentNumber);
		}
		
		destroy();
	}
	
	private static void solveForN(long N) {
		int constant3 = 3; int constant5 = 5;
		StringBuilder decentNumber = new StringBuilder();
		
		if (N % 15 == 0) {
			for (long i = 0; i < N; i++) {
				decentNumber.append(5);
			}
			decentNumbers.add(decentNumber.toString());
			return;
		} else if (N == 3) {
			decentNumber.append(555);
			decentNumbers.add(decentNumber.toString());
			return;
		} else if (N == 5) {
			decentNumber.append(33333);
			decentNumbers.add(decentNumber.toString());
			return;
		}

		long remainder3 = N % constant3;
		long X = (N - remainder3) / constant3; long Y;
		
		while ((N - 3*X) % constant5 != 0) {
			X--;
			if (X < 0) {
				decentNumber.append(-1);
				decentNumbers.add(decentNumber.toString());
				return;
			}
		}
		
		Y = (N - 3*X) / constant5;
		
		for (long i = 0; i < 3*X; i++) {
			decentNumber.append(5);
		}
		
		for (long j = 0; j < 5*Y; j++) {
			decentNumber.append(3);
		}
		
		decentNumbers.add(decentNumber.toString());
	} 
}