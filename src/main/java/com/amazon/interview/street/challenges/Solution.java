package com.amazon.interview.street.challenges;

import java.io.*;
import java.util.*;

public class Solution {

	BufferedReader br;
	PrintWriter out;
	StringTokenizer st;
	boolean eof;

	void solve() throws IOException {
		System.out.println(Integer.toBinaryString(4));
		System.out.println(Integer.toBinaryString(4 >> 1));
		System.out.println(Integer.toBinaryString(2));
		int n = nextInt();
		int[] a = new int[n];
		for (int i = 0; i < n; i++) {
			a[i] = nextInt() - i;
		}
		
		int[] d = new int[n + 1];
		Arrays.fill(d, Integer.MAX_VALUE);
		d[0] = Integer.MIN_VALUE;
		
		int ans = 0;
		for (int i = 0; i < n; i++) {
			int x = a[i];
			if (x <= 0)
				continue;
			int l = 0;
			int r = ans + 1;
			while (r - l > 1) {
				int mid = (l + r) >>> 1;
				if (d[mid] > x) {
					r = mid;
				} else {
					l = mid;
				}
			}
			if (r == ans + 1) {
				ans++;
			}
			d[r] = x;
		}
		
		out.println(n - ans);
	}

	Solution() throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		out = new PrintWriter(System.out);
		solve();
		out.close();
	}

	public static void main(String[] args) throws IOException {
		new Solution();
	}

	String nextToken() {
		while (st == null || !st.hasMoreTokens()) {
			try {
				st = new StringTokenizer(br.readLine());
			} catch (Exception e) {
				eof = true;
				return null;
			}
		}
		return st.nextToken();
	}

	String nextString() {
		try {
			return br.readLine();
		} catch (IOException e) {
			eof = true;
			return null;
		}
	}

	int nextInt() throws IOException {
		return Integer.parseInt(nextToken());
	}

	long nextLong() throws IOException {
		return Long.parseLong(nextToken());
	}

	double nextDouble() throws IOException {
		return Double.parseDouble(nextToken());
	}
}
