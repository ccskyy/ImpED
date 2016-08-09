package com.yorker.imped.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateUtil {

	private static List<String> datePatterns = null;
	static{
		datePatterns = new ArrayList<String>();
		datePatterns.add("dd-MMM-yyyy");
		datePatterns.add("dd-MMM-yy");
		datePatterns.add("dd/MM/yyyy");
		datePatterns.add("MMM dd, yyyy");
		datePatterns.add("E, MMM dd yyyy");
		datePatterns.add("EEEE, MMM dd, yyyy HH:mm:ss a");
		datePatterns.add("MM/dd/yy");
		datePatterns.add("M/dd/yy");
		datePatterns.add("yyyy.MM.dd G 'at' HH:mm:ss z");
		datePatterns.add("EEE, MMM d, ''yy");
		datePatterns.add("h:mm a");
		datePatterns.add("hh 'o''clock' a, zzzz");	
		datePatterns.add("K:mm a, z");
		datePatterns.add("yyyyy.MMMMM.dd GGG hh:mm aaa");	
		datePatterns.add("EEE, d MMM yyyy HH:mm:ss Z");
		datePatterns.add("yyMMddHHmmssZ");
		datePatterns.add("yyyy-MM-dd'T'HH:mm:ss.SSSZ");	
		datePatterns.add("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");	
		datePatterns.add("YYYY-'W'ww-u");
	}
	
	/**
	 * Check if the given string is date or not
	 * @param date in string format
	 * @return true if the given string is date
	 */
	public static boolean isDate(String dateString) {
		boolean isDt = false;
		{
			SimpleDateFormat formatter = null;
			for (String dp : datePatterns){
				formatter = new SimpleDateFormat(dp);
				try {
					formatter.parse(dateString);
					isDt = true;
					break;
				} catch (ParseException e) {
					;
				}
			}
		}
		return isDt;
	}
	
	/**
	 * Get date pattern for given date string
	 * @param date in string format
	 * @return date pattern
	 */
	public static String getDatePattern(String dateString) {
		String dtFormat = null;
		{
			SimpleDateFormat formatter = null;
			for (String dp : datePatterns){
				formatter = new SimpleDateFormat(dp);
				try {
					formatter.parse(dp);
					dtFormat = dp;
					break;
				} catch (ParseException e) {
					;
				}
			}
		}
		return dtFormat;
	}
	
	/**
	 * Get date for given date string
	 * @param date in string format
	 * @return date
	 */
	public static Date parseDate(String dateString) {
		Date dt = null;
		{
			SimpleDateFormat formatter = null;
			for (String dp : datePatterns){
				formatter = new SimpleDateFormat(dp);
				try {
					dt = formatter.parse(dp);
					break;
				} catch (ParseException e) {
					;
				}
			}
		}
		return dt;
	}
	
	/**
	 * Get Timestamp for given date string
	 * @param date in string format
	 * @return Timestamp 
	 */
	public static Timestamp parseTimestamp(String dateString) {
		Timestamp ts = null;
		{
			SimpleDateFormat formatter = null;
			for (String dp : datePatterns){
				formatter = new SimpleDateFormat(dp);
				try {
					Date dt = formatter.parse(dateString);
					ts = new Timestamp(dt.getTime());
					break;
				} catch (ParseException e) {
					;
				}
			}	
		}
		return ts;
	}
	
	/**
	 * Get Timestamp for given date string
	 * @param date in string format
	 * @return Timestamp 
	 */
	public static Timestamp parseTimestamp(String dateString,String pattern) {
		Timestamp ts = null;
		{
			SimpleDateFormat formatter = new SimpleDateFormat(pattern);
			try {
				Date dt = formatter.parse(dateString);
				ts = new Timestamp(dt.getTime());
			} catch (ParseException e) {
				;
			}
		}
		return ts;
	}
}
