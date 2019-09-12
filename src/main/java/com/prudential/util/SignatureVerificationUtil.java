package com.prudential.util;

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.prudential.prucv.controller.SignatureVerificationController;
import com.prudential.prucv.model.CONST;
import com.prudential.prucv.model.SignatureVerification;

import signaturesdk.beans.AcquisitionSignWord;
import signaturesdk.beans.SignWord;
import signaturesdk.beans.SignatureData;
import signaturesdk.features.Extract;
import signaturesdk.features.Normalize;
import signaturesdk.features.NormalizeSingleWord;
import signaturesdk.features.Sample;
import signaturesdk.features.utils.GMath;
import signaturesdk.storage.CSV;
import signaturesdk.verification.Verification;

public class SignatureVerificationUtil {

	final static Logger logger = Logger.getLogger(SignatureVerificationUtil.class);
	
	public static Vector<Double> compareSignature(SignatureVerification signatureVerification, int minDist) throws IOException, ParseException {
		Vector<Double> 	ret;
		boolean mergedToSingleList = signatureVerification.isMerged();
		SignatureData signatureData1 = new SignatureData(CSV.importRawSignatureFromJson(signatureVerification.getSign1(), mergedToSingleList));
		SignatureData signatureData2 = new SignatureData(CSV.importRawSignatureFromJson(signatureVerification.getSign2(), mergedToSingleList));
		LinkedList<AcquisitionSignWord> sign1 = signatureData1.getSignature();
		LinkedList<AcquisitionSignWord> sign2 = signatureData2.getSignature();
		
		//msgOutput.add("Normalize the size:");
		LinkedList<SignWord> f1;
		LinkedList<SignWord> f2;
		
		if (minDist == CONST.MULTIPLE_WORD) {
			f1 = (new Normalize(sign1, minDist)).size();
			f2 = (new Normalize(sign2, minDist)).size();
		} else {
			f1 = (new NormalizeSingleWord(sign1, minDist)).size();
			f2 = (new NormalizeSingleWord(sign2, minDist)).size();
		}
		
		sign1 = null;
		sign2 = null;
		
		int npS1 = Extract.pointsNumber(f1);
		int npS2 = Extract.pointsNumber(f2);
		
		logger.debug("Points number:" + " S1:" + npS1 + " - S2:" + npS2);
		logger.debug("Total time:" + " S1:" + Extract.totalTime(f1) + " - S2:" + Extract.totalTime(f2));
		
		
		
		int min = Math.min(f1.size(), f2.size());
		for(int i = 0; i < min; i++) {
			logger.debug("S1 Len:"+ GMath.polyLen(f1.get(i).getX(), f1.get(i).getY()));
			logger.debug("S2 Len:"+ GMath.polyLen(f2.get(i).getX(), f2.get(i).getY()));
			
		}
		ret = Verification.coordsDTW(f1, f2, mergedToSingleList);
		int i = 0;
		for(double v : ret) {
			logger.debug("DTW coords Word " + i + ": " + v);
			i++;
		}
		
		try {
			int sign1CP = Extract.calculateVelocities(f1);
			int sign2CP = Extract.calculateVelocities(f2);
			ret = Verification.velDTW(f1, f2);
			i = 0;
			for(double v : ret) {
				logger.debug("Dtw velocities Word " + i + ": " + v);
				i++;
			}
			logger.debug(" Numero s1:"+ sign1CP+" s2:"+sign2CP);
			ret = Verification.criticalPointsDTW(f1, f2, mergedToSingleList);
			i = 0;
			for(double v : ret) {
				logger.debug("Dtw on critical points Word " + i + ": " + v);
				i++;
			}
		} catch(Exception e) {
			logger.error("Exception", e);
		}
		Extract.angles(f1);
		Extract.angles(f2);
		ret = Verification.internalAnglesDTW(f1, f2);
		i = 0;
		for(double v : ret) {
			logger.debug("Dtw on internal angles Word " + i + ": " + v);
			i++;
		}
		
		logger.debug("");
		ret = Verification.externalAnglesDTW(f1, f2);
		i = 0;
		for(double v : ret) {
			logger.debug("Dtw on external angles Word " + i + ": " + v);
			i++;
		}
		Sample s = new Sample();
		try {
			s.sample(f1, f2, mergedToSingleList);
			ret = Verification.coordsER2(s.getSignature1(), s.getSignature2());
		} catch (Exception e) {
			logger.error("sample Exception", e);
			Vector<Double> retValues = new Vector<Double>();
			retValues.add((double) 0);
			ret = retValues;
		}
		
				
		return ret;
	}

}
