package com.prudential.prucv.model;

import org.json.JSONArray;

public class SignatureVerification {
	JSONArray sign1;
	
	JSONArray sign2;
	
	boolean merged;
	
	

	public boolean isMerged() {
		return merged;
	}

	public void setMerged(boolean merged) {
		this.merged = false;
	}

	public JSONArray getSign1() {
		return sign1;
	}

	public void setSign1(JSONArray sign1) {
		this.sign1 = sign1;
	}

	public JSONArray getSign2() {
		return sign2;
	}

	public void setSign2(JSONArray sign2) {
		this.sign2 = sign2;
	}

	@Override
	public String toString() {
		return "SignatureVerification [sign1=" + sign1 + ", sign2=" + sign2 + "]";
	}
	
	
}
