package com.eighti.onebkk.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("rawtypes")
public class CommonUtil {

	public static boolean validList(List<?> values) {
		return values != null && !values.isEmpty();
	}

	public static boolean validCollection(Collection col) {
		return col != null && !col.isEmpty();
	}

	public static String formatNumber(Integer value) {
		if (!validNumber(value)) {
			return "0";
		}

		return String.format("%,d", value);
	}

	public static double calculatePercentage(double obtained, double total) {

		return obtained * 100 / total;
	}

	public static boolean validNumber(Number value) {
		return value != null && value.floatValue() > 0;
	}

	public static boolean validString(String value) {
		return value != null && !value.trim().isEmpty();
	}

	public static boolean validLong(Long val) {
		return val != null && val.compareTo(0L) > 0;
	}

	public static boolean validBigDecimal(BigDecimal val) {
		return val != null && val.setScale(0, RoundingMode.HALF_UP).compareTo(BigDecimal.ZERO) > 0;
	}

	public static boolean validInteger(Integer val) {
		return val != null && val.compareTo(0) > 0;
	}

	public static boolean validDate(Date value) {
		return value != null;
	}

	public static boolean validDate(String value) {
		return value != null && !value.trim().isEmpty();
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.trim().equals("") || str.isEmpty()) {
			return true;
		}
		return false;
	}

	// Method to check if a Base64 string exceeds a specified size limit
	public static boolean exceedsSizeLimit(String base64String, int sizeLimitInBytes) {
		// Calculate the estimated size of the decoded byte array
		int estimatedSize = (int) (base64String.length() / 1.333);
		// Check if the estimated size exceeds the specified limit
		return estimatedSize > sizeLimitInBytes;
	}
}
