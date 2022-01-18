package com.quas.mythsmagic.util;

import org.apache.commons.lang3.StringUtils;

public class Util {

	public static String quantity(long quantity, String object, String pluralize) {
		return String.format("%,d %s%s", quantity, object, quantity == 1 ? "" : pluralize);
	}
	
	public static int levenshtein(String stringA, String stringB) {
		char[] a = process(stringA);
		char[] b = process(stringB);
		int[][] memo = new int[a.length + 1][b.length + 1];
		
		for (int q = 0; q <= a.length; q++) memo[q][0] = q;
		for (int w = 0; w <= b.length; w++) memo[0][w] = w;
		
		for (int q = 1; q <= a.length; q++) {
			for (int w = 1; w <= b.length; w++) {
				int cost = a[q - 1] == b[w - 1] ? 0 : 1;
				memo[q][w] = min(memo[q - 1][w - 1] + cost, memo[q - 1][w] + 1, memo[q][w - 1] + 1);
				
				if (q > 1 && w > 1 && a[q - 1] == b[w - 2] && a[q - 2] == b[w - 1]) {
					memo[q][w] = min(memo[q][w], memo[q - 2][w - 2] + cost);
				}
			}
		}
		
		return memo[a.length][b.length];
	}
	
	private static char[] process(String s) {
		return StringUtils.stripAccents(s).toLowerCase().replaceAll("\\W", "").toCharArray();
	}
	
	public static int min(int...arr) {
		if (arr.length == 0) return 0;
		int min = arr[0];
		for (int x : arr) if (x < min) min = x;
		return min;
	}
}
