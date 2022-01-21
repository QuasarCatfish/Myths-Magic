package com.quas.mythsmagic.util;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

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
	
	@SafeVarargs
	public static <T> T[] reverse(T...arr) {
		for (int q = 0; q < arr.length / 2; q++) {
			T t = arr[q];
			arr[q] = arr[arr.length - q - 1];
			arr[arr.length - q - 1] = t;
		}
		
		return arr;
	}
	
	public static String formatTimeRelative(long time) {
		Duration delta = Duration.ofMillis(time - System.currentTimeMillis());
		if (delta.isZero() || delta.isNegative()) return "0 minutes";
		
		ChronoUnit[] times = reverse(ChronoUnit.values());
		for (ChronoUnit cu : times) {
			if (cu.compareTo(ChronoUnit.MINUTES) < 0) break;
			if (cu.compareTo(ChronoUnit.YEARS) > 0) continue;
			if (cu == ChronoUnit.HALF_DAYS) continue;
			
			long div = delta.dividedBy(cu.getDuration());
			if (div > 0) {
				String name = cu.toString().toLowerCase();
				name = name.substring(0, name.length() - 1);
				return quantity(div, name, "s");
			}
		}
		
		return "1 minute";
	}
}
