package com.quas.mythsmagic.util;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Rand {

	public static int nextInt(int bound) {
		return ThreadLocalRandom.current().nextInt(bound);
	}
	
	private static char[] digits = "0123456789".toCharArray();
	public static String nextDigitString() {
		StringBuilder sb = new StringBuilder();
		for (int q = 0; q < 30; q++) sb.append(nextElement(digits));
		return sb.toString();
	}
	
	public static int nextElement(int[] arr) {
		return arr[nextInt(arr.length)];
	}
	
	public static char nextElement(char[] arr) {
		return arr[nextInt(arr.length)];
	}
	
	public static <T> T nextElement(T[] arr) {
		return arr[nextInt(arr.length)];
	}
	
	public static <T> T nextElement(List<T> list) {
		return list.get(nextInt(list.size()));
	}
}
