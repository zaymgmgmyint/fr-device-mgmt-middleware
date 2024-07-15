package com.eighti.onebkk.utils;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class CommonUtil {

	private static final Logger LOG = LoggerFactory.getLogger(CommonUtil.class);

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

	/**
	 * Convert the given millisecond into the local date.
	 * 
	 * @param milliseconds
	 * @return the converted local date
	 */
	public static LocalDate convertMilliSecondToLocalDate(String milliseconds) {
		LocalDate localDate = null;
		try {
			// Convert milliseconds to Instant
			Instant instant = Instant.ofEpochMilli(Long.valueOf(milliseconds));

			// Convert Instant to LocalDate in a specific time zone
			localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("convertMilliSecondToLocalDate() >>> Error converting milliseconds to local date: "
					+ e.getMessage(), e);
			return null;
		}

		return localDate;
	}

	/**
	 * Convert the given millisecond into the local date time.
	 * 
	 * @param milliseconds
	 * @return the converted local date time
	 */
	public static LocalDateTime convertMilliSecondToLocalDateTime(String milliseconds) {
		LocalDateTime localDateTime = null;
		try {
			// Convert milliseconds to Instant
			Instant instant = Instant.ofEpochMilli(Long.valueOf(milliseconds));

			// Convert Instant to LocalDate in a specific time zone
			localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("convertMilliSecondToLocalDateTime() >>> Error converting milliseconds to local date time: "
					+ e.getMessage(), e);
			return null;
		}

		return localDateTime;
	}

	/**
	 * To check if a Base64 string exceeds a specified size limit.
	 * 
	 * @param base64String
	 * @param sizeLimitInBytes
	 * @return true when the size exceeds the specified size limit, otherwise false
	 */
	public static boolean exceedsSizeLimit(String base64String, int sizeLimitInBytes) {
		// Calculate the estimated size of the decoded byte array
		int estimatedSize = (int) (base64String.length() / 1.333);
		// Check if the estimated size exceeds the specified limit
		return estimatedSize > sizeLimitInBytes;
	}

	/**
	 * Convert the given string into the local date format.
	 * 
	 * @param dateStr date string
	 * @return the date formatted by yyyy-MM-dd
	 */
	public static LocalDate changeStringToDate(String dateStr) {
		// Define the expected date format (can be adjusted based on your input format)
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateConstant.DATE_FORMAT_yyyymmdd);
		try {
			// Attempt to parse the string using the formatter
			LocalDate dateTime = LocalDate.parse(dateStr, formatter);
			return dateTime;
		} catch (Exception e) {
			e.printStackTrace();
			// Handle parsing exceptions (e.g., invalid format, invalid date)
			LOG.error("changeStringToDate() >>> Invalid date format. Please use YYYY-MM-DD format.");
			return null;
		}
	}

	/**
	 * Convert the given string into the local date time format.
	 * 
	 * @param dateStr date string
	 * @return the date formatted by yyyy-MM-dd HH:mm:ss
	 */
	public static LocalDateTime changeStringToDateTime(String dateStr) {
		// Define the expected date format (can be adjusted based on your input format)
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateConstant.DATE_FORMAT_yyyy_mm_dd_HHmmss);
		try {
			// Attempt to parse the string using the formatter
			LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
			return dateTime;
		} catch (Exception e) {
			e.printStackTrace();
			// Handle parsing exceptions (e.g., invalid format, invalid date)
			LOG.error("changeStringToDate() >>> Invalid date time format. Please use YYYY-MM-DD HH:mm:ss format.");
			return null;
		}
	}

	public static String convertImageToBase64(String imagePath) throws Exception {
		String base64String = "";
		File file = new File(imagePath);
		byte[] imagesBytes = Files.readAllBytes(file.toPath());
		base64String = Base64.getEncoder().encodeToString(imagesBytes);

		return base64String;
	}
}
