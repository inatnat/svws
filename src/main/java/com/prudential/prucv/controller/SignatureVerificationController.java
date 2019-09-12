package com.prudential.prucv.controller;


import org.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.prudential.prucv.model.CONST;
import com.prudential.prucv.model.Signature;
import com.prudential.prucv.model.SignatureVerification;
import com.prudential.prucv.model.Similarity;
import com.prudential.util.MathUtil;
import com.prudential.util.SignatureVerificationUtil;




import signaturesdk.beans.AcquisitionSignWord;
import signaturesdk.beans.SignWord;
import signaturesdk.beans.SignatureData;
import signaturesdk.features.Extract;
import signaturesdk.features.Normalize;
import signaturesdk.features.Sample;
import signaturesdk.features.utils.GMath;
import signaturesdk.hash.Directional;
import signaturesdk.storage.CSV;
import signaturesdk.verification.Verification;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

@Configuration
@RestController
@PropertySource("classpath:config.properties")
public class SignatureVerificationController {

	final static Logger logger = Logger.getLogger(SignatureVerificationController.class);

	@RequestMapping(value = "/verify", method = RequestMethod.POST)
	public ResponseEntity<Similarity> verifySignature(@RequestBody SignatureVerification signatureVerification) {
		logger.info(signatureVerification.toString());
		List<Float> m = new ArrayList<Float>();
		List<Float> s = new ArrayList<Float>();
		
		Similarity similarity = new Similarity();
		try {
			Vector<Double> 	ret = SignatureVerificationUtil.compareSignature(signatureVerification, CONST.MULTIPLE_WORD);
			//signature, merged to single list
			int i = 1;
			for(double v: ret) {
				
				logger.info("Multiple Word " + i + ": " + MathUtil.round((v*100), 1) + "%");
				m.add((float)MathUtil.round((v*100), 1));
				i++;
			}
			ret = SignatureVerificationUtil.compareSignature(signatureVerification, CONST.SINGLE_WORD);
			for(double v: ret) {
				logger.info("Single Word " + i + ": " + MathUtil.round((v*100), 1) + "%");
				s.add((float)MathUtil.round((v*100), 1));
				i++;
			}
			
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
		similarity.setMultipleWordScore(m);
		similarity.setSingleWordScore(s);
		return new ResponseEntity<Similarity>(similarity, HttpStatus.OK);
	}
	
	
	
}
