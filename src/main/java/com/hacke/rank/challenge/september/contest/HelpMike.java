package com.hacke.rank.challenge.september.contest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HelpMike {

	private static BufferedReader bufferedReader;
	private static final int FAILURE_CODE = -1;
	private static List<Long> answers = new ArrayList<Long>();
	
	public static void main(String[] args) throws IOException {
		init();
		
		int testCases = Integer.parseInt(bufferedReader.readLine());
		
		if (testCases < 1 || testCases > 100) {
			System.exit(FAILURE_CODE);
		}
		
		for (int i = 0; i < testCases; i++) {
			String pair = bufferedReader.readLine();
			
			long N = Long.parseLong(pair.split(" ")[0]); long K = Long.parseLong(pair.split(" ")[1]);
			
			if (N < K || N > (long) Math.pow(10, 9)) {
				break;
			} else {
				solve(N, K);
			}
		}
		
		for (Long answer : answers) {
			System.out.println(answer);
		}
		
		close();
	}
	
	private static void solve(Long N, long K) {
		long count = 0;
		
		for (long i = 1; i <= N; i++) {

			long resedue = K - i % K;
			resedue = K >>> resedue;
		
			while (resedue <= N) {
				if (i < resedue) {
					count++;
				}
				resedue = resedue + K;
			}
		}
		answers.add(count);
	}
	
	private static void init() throws IOException {
		bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	private static void close() throws IOException {
		if (bufferedReader != null) {
			bufferedReader.close();
		}
	}
}