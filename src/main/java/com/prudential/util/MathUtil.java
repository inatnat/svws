package com.prudential.util;

import java.util.List;

public class MathUtil {
	@SuppressWarnings("unused")
	public static float calculateAverage(List<Float> marks) {
		float sum = 0;
		if (!marks.isEmpty()) {
			for (float mark : marks) {
				sum += mark;
			}
			return sum / marks.size();
		}
		return sum;
	}
	
	
	public static double roundToNDigits(double value, int precision) {
		return value;
//		
//		int scale = (int) Math.pow(10, precision);
//	    return (double) Math.round(value * scale) / scale;
 	}
	
	
	public static double round (double value, int precision) {
	    int scale = (int) Math.pow(10, precision);
	    return (double) Math.round(value * scale) / scale;
	}
}
