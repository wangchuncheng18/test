package com.tarotdt.pas.web.util;

import java.util.regex.Pattern;

public class IntegerUtils {
	public static int toInt(String v) {
		if (v.isEmpty()) {
			return 0;
		}
		if (v.startsWith("+")) {
			return toInt(v.substring(1));
		}
		if (v.startsWith("-")) {
			return -toInt(v.substring(1));
		}

		int base = 10;
		if ((v.startsWith("0x")) || (v.startsWith("0X"))) {
			base = 16;
			v = v.substring(2);
		} else if (v.startsWith("0")) {
			base = 8;
			v = v.substring(1);
		} else {
			base = 10;
		}
		v = v.replaceAll("_|,", "");

		return Integer.parseInt(v, base);
	}

	public static boolean isNumeric(String val) {
		val = val.toLowerCase();
		String baseRatExp;
		if (val.startsWith("0x")) {
			baseRatExp = "^[-+]*0x[0-9a-f_,]$";
		} else {
			if (val.startsWith("0")) {
				baseRatExp = "^[-+]*0[01_,]$";
			} else {
				baseRatExp = "^[-+]*[0-9_,]*$";
			}
		}
		return Pattern.compile(baseRatExp).matcher(val).find();
	}
}
