package com.prudential.util;

import java.util.ArrayList;
import java.util.List;

public class CommonUtil {
	public static ArrayList<String> parameters(String methodConfiguration, String value) {
		String method = methodConfiguration.substring(0, methodConfiguration.indexOf("("));
		List<String> a = new ArrayList<String>();
		a.add(method);
		a.add(value);
		String parameters = methodConfiguration.substring(methodConfiguration.indexOf("(") + 1, methodConfiguration.indexOf(")"));
		while (parameters.contains("|")) {
			String parameter = parameters.substring(0, parameters.indexOf("|"));
			parameters = parameters.substring(parameters.indexOf("|") + 1);
			a.add(parameter);
		}
		a.add(parameters);
		return (ArrayList<String>) a;
	}

}
