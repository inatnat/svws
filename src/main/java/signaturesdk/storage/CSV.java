package signaturesdk.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.prudential.prucv.controller.SignatureVerificationController;

import signaturesdk.beans.AcquisitionSignWord;

/**
 * Format:
 * x,y;pressure;time; <--segment 
 * x,y;pressure;time;
 * 
 * x,y;pressure;time; <--segment
 * x,y;pressure;time;
 */
public class CSV {

	final static Logger logger = Logger.getLogger(CSV.class);
	
	/**
	 * Export a raw signature (x,y,pressure and time) to CSV
	 * 
	 * @param FileName
	 *            output filename
	 * @param asw
	 *            the AcquisitionSignWord to export
	 * @throws IOException
	 *             if there's some writing error
	 */
	public static void exportRawSignature(LinkedList<AcquisitionSignWord> listAsw, String FileName)
			throws IOException {

		// prepare di output buffer
		FileWriter fileWriter = new FileWriter(FileName, false);
		BufferedWriter buffWriter = new BufferedWriter(fileWriter);

		
		for(AcquisitionSignWord asw : listAsw) {
			// get points info
			LinkedList<Double> Lx = asw.getX();
			LinkedList<Double> Ly = asw.getY();
			LinkedList<Double> Lp = asw.getPressure();
			LinkedList<Long> Lt = asw.getTime();
	
	
			for (int i = 0; i < asw.getPointsNum(); i++) {
				double x = Lx.get(i);
				double y = Ly.get(i);
				double p = Lp.get(i);
				long t = Lt.get(i);
	
				String point = String
						.format(Locale.US,
								"%.3f;%.3f;%.3f;%d;\n",
								x, y, p, t);
				buffWriter.append(point);
			}
			buffWriter.append("\n");
		}

		buffWriter.close();// from javadoc: flush and close
		fileWriter.close();

		return;
	}
	
	/**
	 * Import a raw signature (x,y,pressure and time) froma a CSV file
	 * @param FileName CSV to read
	 * @return
	 * @throws IOException file reading error
	 * @throws ParseException CSV is bad formatted
	 */
	public static LinkedList<AcquisitionSignWord> importRawSignature(String FileName)
			throws IOException, ParseException {

		// prepare di output buffer
		FileReader fileReader = new FileReader(FileName);
		BufferedReader buffReader = new BufferedReader(fileReader);
		
		LinkedList<AcquisitionSignWord> listAsw = new LinkedList<AcquisitionSignWord>();
		
		
		AcquisitionSignWord asw = new AcquisitionSignWord();
		// get points
		LinkedList<Double> Lx = asw.getX();
		LinkedList<Double> Ly = asw.getY();
		LinkedList<Double> Lp = asw.getPressure();
		LinkedList<Long> Lt = asw.getTime();

		NumberFormat nf = NumberFormat.getInstance(Locale.US);

		//for more newline beetween signature word
		boolean prevLineEmpty = false;
		
		//read all the file
		for(String line = buffReader.readLine(); line != null; line = buffReader.readLine()) {
			//a new signature segment
			if(line.compareTo("") == 0) {
				if(prevLineEmpty == false) { // ended to read a full line

					//add the readed segment
					listAsw.add(asw);
					
					//start a new segment to read
					asw = new AcquisitionSignWord();
					// get points
					Lx = asw.getX();
					Ly = asw.getY();
					Lp = asw.getPressure();
					Lt = asw.getTime();
					
					prevLineEmpty = true;
				}
				
			} else {
				//split the line
				String arrStr[] = line.split(";");
				
				//parse the values
				Lx.add(nf.parse(arrStr[0]).doubleValue());
				Ly.add(nf.parse(arrStr[1]).doubleValue());
				Lp.add(nf.parse(arrStr[2]).doubleValue());
				Lt.add(nf.parse(arrStr[3]).longValue());
				
				prevLineEmpty = false;//say to read a full line
			}
			
		}

		buffReader.close();
		fileReader.close();

		return listAsw;
	}

	/**
	 * Import a raw signature (x,y,pressure and time) froma a CSV file
	 * @param FileName CSV to read
	 * @return
	 * @throws IOException file reading error
	 * @throws ParseException CSV is bad formatted
	 */
	public static LinkedList<AcquisitionSignWord> importRawSignatureFromJson(JSONArray jsons, boolean mergedToSingleList)
			throws IOException, ParseException {

		LinkedList<AcquisitionSignWord> listAsw = new LinkedList<AcquisitionSignWord>();
		LinkedList<AcquisitionSignWord> processedAsw = new LinkedList<AcquisitionSignWord>();
		
		//read all the file
		for (int i=0; i<jsons.length(); i++) {
			JSONObject json = (JSONObject)jsons.get(i);
			JSONArray points = json.getJSONArray("points");
			AcquisitionSignWord asw = new AcquisitionSignWord();
			LinkedList<Double> Lx = asw.getX();
			LinkedList<Double> Ly = asw.getY();
			LinkedList<Double> Lp = asw.getPressure();
			LinkedList<Long> Lt = asw.getTime();
			Lx = asw.getX();
			Ly = asw.getY();
			Lp = asw.getPressure();
			Lt = asw.getTime();
			
			for (int j=0; j<points.length(); j++) {
				
				JSONObject point = points.getJSONObject(j);
				Lx.add(Double.valueOf(point.getFloat("x")));
				Ly.add(Double.valueOf(point.getFloat("y")));
				Lp.add(0.5);
				Lt.add(point.getLong("time"));
				
			}
			listAsw.add(asw);
		}
		/*
		if (mergedToSingleList) {
			AcquisitionSignWord am = new AcquisitionSignWord();
			LinkedList<Double> Lx = am.getX(); 
			LinkedList<Double> Ly = am.getY(); 
			LinkedList<Double> Lp = am.getPressure(); 
			LinkedList<Long> Lt = am.getTime(); 
			for (int i=0; i< listAsw.size(); i++) {
				AcquisitionSignWord a = listAsw.get(0);
				Lx.addAll(a.getX());
				Ly.addAll(a.getY());
				Lp.addAll(a.getPressure());
				Lt.addAll(a.getTime());
			}
			processedAsw.add(am);
		} else {
			processedAsw = listAsw;
		}*/
		
		processedAsw = listAsw;
		for (int i=0;i<processedAsw.size();i++) {
			logger.debug("size X " + processedAsw.get(i).getX().size() + " Y " + processedAsw.get(i).getY().size() + " Time " + processedAsw.get(i).getTime().size()); 			
		}
		return processedAsw;
	}

	
}
