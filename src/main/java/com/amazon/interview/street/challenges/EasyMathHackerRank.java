package com.amazon.interview.street.challenges;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

public class EasyMathHackerRank {

	
	public static class IOFast {
		private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		private PrintWriter out = new PrintWriter(System.out);
		
		void setFileIO(String ins, String outs) throws IOException {
			in = new BufferedReader(new FileReader(ins));
			out = new PrintWriter(new FileWriter(outs));
		}
		
		private static int pos, readLen;
		private static final char[] buffer = new char[1024 * 8];
		private static final char[] str    = new char[500000 * 8 * 2];
		private static boolean[] isDigit   = new boolean[256];
		private static boolean[] isSpace   = new boolean[256];
		private static boolean[] isLineSep = new boolean[256];
		
		static {
			for (int i = 0; i < 10; i++) {
				isDigit['0' + i] = true;
			}
			isDigit['-'] = true;
			isSpace[' '] = isSpace['\r'] = isSpace['\n'] = isSpace['\t'] = true;
			isLineSep['\n'] = isLineSep['\r'] = true;
		}
		
		public int read() throws IOException {
			if (pos >= readLen) {
				pos = 0;
				readLen = in.read(buffer);
				if (readLen <= 0) {
					throw new EndOfFileRuntimeException();
				}
			}
			
			return buffer[pos++];
		}
		
		public int nextInt() throws IOException {
			return Integer.parseInt(nextString());
		}

		public long nextLong() throws IOException {
			return Long.parseLong(nextString());
		}

		public char nextChar() throws IOException {
			while(true) {
				final int c = read();
				if(!isSpace[c]) { return (char)c; }
			}
		}
		
		int reads(char[] cs, int len, boolean[] accept) throws IOException {
			try {
				while(true) {
					final int c = read();
					if(c < accept.length && accept[c]) { break; }
					cs[len++] = (char)c;
				}
			}
			catch(EndOfFileRuntimeException e) { ; }
			
			return len;
		}

		public char[] nextLine() throws IOException {
			int len = 0;
			str[len++] = nextChar();
			len = reads(str, len, isLineSep);
			
			try {
				if(str[len-1] == '\r') { len--; read(); }
			}
			catch(EndOfFileRuntimeException e) { ; }
			
			return Arrays.copyOf(str, len);
		}

		public String nextString() throws IOException {
			return new String(next());
		}

		public char[] next() throws IOException {
			int len = 0;
			str[len++] = nextChar();
			len = reads(str, len, isSpace);
			return Arrays.copyOf(str, len);
		}

		public double nextDouble() throws IOException {
			return Double.parseDouble(nextString());
		}

		public long[] nextLongArray(final int n) throws IOException {
			final long[] res = new long[n];
			for(int i = 0; i < n; i++) {
				res[i] = nextLong();
			}
			return res;
		}

		public int[] nextIntArray(final int n) throws IOException {
			final int[] res = new int[n];
			for(int i = 0; i < n; i++) {
				res[i] = nextInt();
			}
			return res;
		}

		public int[][] nextIntArray2D(final int n, final int k) throws IOException {
			final int[][] res = new int[n][];
			for(int i = 0; i < n; i++) {
				res[i] = nextIntArray(k);
			}
			return res;
		}

		public double[] nextDubleArray(final int n) throws IOException {
			final double[] res = new double[n];
			for(int i = 0; i < n; i++) {
				res[i] = nextDouble();
			}
			return res;
		}
	}
	
	public static class EndOfFileRuntimeException extends RuntimeException {
		private static final long serialVersionUID = 75961661285039687L;
	}
}