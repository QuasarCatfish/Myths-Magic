package com.quas.mythsmagic.util;

public class Util {

	public static String quantity(long quantity, String object, String pluralize) {
		return String.format("%,d %s%s", quantity, object, quantity == 1 ? "" : pluralize);
	}
}
