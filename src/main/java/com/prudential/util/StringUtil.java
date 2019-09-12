package com.prudential.util;

import java.util.ArrayList;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Logger;


public class StringUtil {

	final static Logger logger = Logger.getLogger(StringUtil.class);
	
	
	public String correctHkId(ArrayList<String> input) {
		
		String ret = input.get(1);
		try {
			String prefix = "";
			String number = "";
			String postfix = input.get(1).substring(input.get(1).indexOf("("),  input.get(1).length());
			if (input.get(1).substring(input.get(1).indexOf("(")).length() == 7 ) {
				prefix = input.get(1).substring(1);
				number  = input.get(1).substring(1, 7);
				
			} else {
				prefix = input.get(1).substring(2);
				number  = input.get(1).substring(2, 8);			
			}
			prefix = prefix.replace("0", "O");
			prefix = prefix.replace("1", "I");
			prefix = prefix.replace("2", "Z");
			number = number.replace("O", "0");
			number = number.replace("o", "0");
			number = number.replace("I", "1");
			number = number.replace("i", "1");
			number = number.replace("l", "1");
			number = number.replace("z", "2");
			number = number.replace("Z", "2");
			ret = prefix + number + postfix;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	
	
	public String replaceCharacter(ArrayList<String> input) {
		return input.get(1).replace(input.get(2), input.get(3));
	}
	
	public String replaceCharacterWithSubstring(ArrayList<String> input) {
		String ret = input.get(1).substring(0, Integer.valueOf(input.get(4))) +
				input.get(1).substring(Integer.valueOf(input.get(4)), Integer.valueOf(input.get(5))).replace(input.get(2), input.get(3)) +
				input.get(1).substring(Integer.valueOf(input.get(5)));
		
		return ret;
	}
	
	
	public String trimLastSpace(ArrayList<String> input) {
		return input.get(1).trim();
	}
	
	public String removeNonAlphaNumeric(ArrayList<String> input) {
		return input.get(1).replaceAll("[\\W]|_", "");
	}
	
	public String removeAlphaNumeric(ArrayList<String> input) {
		String[] alphaNumerics = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "y", "z", 
								  "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "Y", "Z",
								  "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
		for (int i=0; i< alphaNumerics.length; i++) {
			input.set(1, input.get(1).replaceAll(alphaNumerics[i], ""));
		}
		
		return input.get(1);
	}
	
	public String removeAlpha(ArrayList<String> input) {
		String[] alphaNumerics = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "y", "z", 
								  "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "Y", "Z"};
		for (int i=0; i< alphaNumerics.length; i++) {
			input.set(1, input.get(1).replaceAll(alphaNumerics[i], ""));
		}
		
		return input.get(1);
	}
	
	public String removeNumeric(ArrayList<String> input) {
		String[] alphaNumerics = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
		for (int i=0; i< alphaNumerics.length; i++) {
			input.set(1, input.get(1).replaceAll(alphaNumerics[i], ""));
		}
		
		return input.get(1);
	}
	
	public String camelCase(ArrayList<String> input) {
		return toCamelCase(input.get(1));

	}
	
	public String toUpperCase(ArrayList<String> input) {
		return input.get(1).toUpperCase();

	}
	
	public String toLowerCase(ArrayList<String> input) {
		return input.get(1).toLowerCase();

	}
	
	
	private String toCamelCase(String s){
	   String[] parts = s.split(" ");
	   String camelCaseString = "";
	   for (String part : parts){
	      camelCaseString = camelCaseString + " " + toProperCase(part);
	   }
	   return camelCaseString;
	}

	private String toProperCase(String s) {
	    return s.substring(0, 1).toUpperCase() +
	               s.substring(1).toLowerCase();
	}
	
	
}
