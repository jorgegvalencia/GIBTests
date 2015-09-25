package mainapp.Model;


import gov.nih.nlm.nls.metamap.Result;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import mainapp.Model.ClinicalTrial;

/**
 * @author jorgegvalencia
 */
public final class App {

	public static void main(String[] args) {
		//Test1();
		//Test2();
		Test7();
	}

	public static void Test1(){
		// NCT01358877
		// NCT00148876
		ClinicalTrial ct = downloadCT("NCT01358877");
		ConceptExtractor ce = new ConceptExtractor();

		/* Preprocess criteria text */
		String criteria = ct.getCriteria();

		// check for inclusion and exclusion

		// remove stop words

		/* Extract concepts */
		List<String> uttr = ce.getUtterancesFromText(criteria);

		// for each utterance, query to MetaMap
		for(String utterance: uttr){
			Result result = ce.queryFromString(utterance).get(0);
			// create EC for each utterance
			// check the flag

			// create Concepts

			// check matching type: simple, complex, partial
		}
	}

	public static void Test2(){
		ClinicalTrial ct = downloadCT("NCT01358877");
		ConceptExtractor ce = new ConceptExtractor();
		Preprocessor pp = new Preprocessor();
		String criteria = ct.getCriteria();
		List<String> uttr = ce.getUtterancesFromText(pp.preProcessText(criteria));
		for(String ut: uttr){
			System.out.println(ut);
		}
	}

	public static void Test3(){
		ClinicalTrial ct = downloadCT("NCT01358877");
		ConceptExtractor ce = new ConceptExtractor();
		Preprocessor pp = new Preprocessor();
		String criteria = ct.getCriteria();
		
		List<Concept> conceptList1 = new ArrayList<Concept>();
		List<EligibilityCriteria> list = ce.createECFromText("-R SNOMEDCT_US",pp.preProcessText(criteria));
		for(EligibilityCriteria ec: list){
			ec.printEC();
			for(Concept concept: ec.getConcepts()){
				concept.printConcept();
				conceptList1.add(concept);
			}
		}
		createConceptsCSV("csvtest"+System.currentTimeMillis()+".csv",conceptList1);
	}
	
	public static void Test4(){
		ClinicalTrial ct = downloadCT("NCT01358877");
		ConceptExtractor ce = new ConceptExtractor();
		Preprocessor pp = new Preprocessor();
		String criteria = ct.getCriteria();
		
		List<Concept> conceptList1 = new ArrayList<Concept>();
		List<EligibilityCriteria> list = ce.createECFromText("-R SNOMEDCT_US",pp.removeSW(criteria));
		for(EligibilityCriteria ec: list){
			ec.printEC();
			for(Concept concept: ec.getConcepts()){
				concept.printConcept();
				conceptList1.add(concept);
			}
		}
		createConceptsCSV("csvtest"+System.currentTimeMillis()+".csv",conceptList1);
	}
	
	public static void Test5(){
		ClinicalTrial ct = downloadCT("NCT01358877");
		ConceptExtractor ce = new ConceptExtractor();
		Preprocessor pp = new Preprocessor();
		String criteria = ct.getCriteria();
		
		List<Concept> conceptList1 = new ArrayList<Concept>();
		List<EligibilityCriteria> list = ce.createECFromText("-R SNOMEDCT_US",pp.removeEW(criteria));
		for(EligibilityCriteria ec: list){
			ec.printEC();
			for(Concept concept: ec.getConcepts()){
				concept.printConcept();
				conceptList1.add(concept);
			}
		}
		createConceptsCSV("csvtest"+System.currentTimeMillis()+".csv",conceptList1);
	}
	
	public static void Test6(){
		ClinicalTrial ct = downloadCT("NCT01358877");
		ConceptExtractor ce = new ConceptExtractor();
		Preprocessor pp = new Preprocessor();
		String criteria = ct.getCriteria();
		
		List<Concept> conceptList = new ArrayList<Concept>();
		
		List<EligibilityCriteria> list1 = ce.createECFromText("-y -R SNOMEDCT_US",pp.preProcessText(criteria));
		for(EligibilityCriteria ec: list1){
			for(Concept concept: ec.getConcepts()){
				conceptList.add(concept);
			}
		}
		List<EligibilityCriteria> list2 = ce.createECFromText("-y -R SNOMEDCT_US",pp.removeSW(criteria));
		for(EligibilityCriteria ec: list2){
			for(Concept concept: ec.getConcepts()){
				conceptList.add(concept);
			}
		}
		List<EligibilityCriteria> list3 = ce.createECFromText("-y -R SNOMEDCT_US",pp.removeEW(criteria));
		for(EligibilityCriteria ec: list3){
			for(Concept concept: ec.getConcepts()){
				conceptList.add(concept);
			}
		}
		
		createConceptsCSV("csvtest"+System.currentTimeMillis()+".csv",conceptList);
	}
	
	public static void Test7(){
		ClinicalTrial ct = downloadCT("NCT01358877");
		ConceptExtractor ce = new ConceptExtractor();
		Preprocessor pp = new Preprocessor();
		String criteria = ct.getCriteria();
		String text = pp.removeSW(criteria);
		text = pp.removeEW(text);
		
		List<Concept> conceptList = new ArrayList<Concept>();
		
		List<EligibilityCriteria> list = ce.createECFromText("-y -R SNOMEDCT_US",text);
		for(EligibilityCriteria ec: list){
			for(Concept concept: ec.getConcepts()){
				conceptList.add(concept);
			}
		}
		
		createConceptsCSV("csvtest"+System.currentTimeMillis()+".csv",conceptList);
	}


	public static ClinicalTrial downloadCT(String nctId) {
		ClinicalTrial ct = new ClinicalTrial();
		String path = "https://clinicaltrials.gov/show/"+nctId+"?displayxml=true";
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "text/xml");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream()),"utf-8"));
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLStreamReader streamReader = factory.createXMLStreamReader(br);
			String currentElement = null;
			while(streamReader.hasNext()){
				int event = streamReader.next();
				switch(event){
				case XMLStreamConstants.START_ELEMENT:
					currentElement = streamReader.getLocalName();
					switch(currentElement){
					case "url":
						ct.setUrl(new URL(streamReader.getElementText()));
						break;
					case "nct_id":
						ct.setNctId(streamReader.getElementText());
						break;
					case "brief_title":
						ct.setTitle(streamReader.getElementText());
						break;
					case "overall_status":
						//
						//ct.setOverall_status(overall_status);
						break;
					case "start_date":
						ct.setStartDate(streamReader.getElementText());
						break;
					case "study_type":
						ct.setStudyType(streamReader.getElementText());
						break;
					case "sampling_method":
						ct.setSamplingMethod(streamReader.getElementText());
						break;
					default:
					}
					break;
				case XMLStreamConstants.CHARACTERS:
					if(currentElement.equals("brief_summary")){
						streamReader.next();
						if(streamReader.getEventType() == XMLStreamReader.START_ELEMENT){
							currentElement = streamReader.getLocalName();
							ct.setBriefSummary(streamReader.getElementText());
						}
					}
					else if(currentElement.equals("criteria")){
						// process criteria to inclusion and exclusion
						streamReader.next();
						if(streamReader.getEventType() == XMLStreamReader.START_ELEMENT){
							currentElement = streamReader.getLocalName();
							ct.setCriteria((streamReader.getElementText()));
						}
					}
					else if(currentElement.equals("study_pop")){
						streamReader.next();
						if(streamReader.getEventType() == XMLStreamReader.START_ELEMENT){
							currentElement = streamReader.getLocalName();
							ct.setStudyPop(((streamReader.getElementText())));
						}
					}
					break;
				}
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return ct;
	}

	public static void createConceptsCSV(String filename, List<Concept> list){
		try{
			FileWriter writer = new FileWriter(filename);
			writer.append("CUI");
			writer.append(',');
			writer.append("Score");
			writer.append(',');
			writer.append("SemTypes");
			writer.append(',');
			writer.append("Name");
			writer.append("\n");
			for(Concept c: list){
				writer.append(c.getCui());
				writer.append(',');
				writer.append(String.valueOf(c.getScore()).replace("-", ""));
				writer.append(',');
				String st="";
				for(String s: c.getSemTypes()){
					st = st.concat(s+" ");
				}
				writer.append(st);
				writer.append(',');
				writer.append(c.getName());
				writer.append('\n');
			}
			writer.flush();
			writer.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		} 
	}
}