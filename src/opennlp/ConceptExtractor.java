package opennlp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class ConceptExtractor {

	public static void main(String[] args) {
		CriteriaProcessor cp = new CriteriaProcessor();
		ClinicalTrial ct = downloadCT("NCT01334021");
		String criteria = ct.getExc_criteria();

		//with chunks
		if(criteria != null){
			List<ProcessResult> results = cp.processText(criteria);
			CriteriaProcessor.printData(results);
		}
	}
	public static ClinicalTrial downloadCT(String nct_id){
		ClinicalTrial ct = new ClinicalTrial();
		String path = "https://clinicaltrials.gov/show/"+nct_id+"?displayxml=true";
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "text/xml");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

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
						ct.setNct_id(streamReader.getElementText());
						break;
					case "brief_title":
						ct.setTitle(streamReader.getElementText());
						break;
					case "overall_status":
						//
						//ct.setOverall_status(overall_status);
						break;
					case "start_date":
						ct.setStart_date(streamReader.getElementText());
						break;
					case "study_type":
						ct.setStudy_type(streamReader.getElementText());
						break;
					case "sampling_method":
						ct.setSampling_method(streamReader.getElementText());
						break;
					default:
					}
					break;
				case XMLStreamConstants.CHARACTERS:
					if(currentElement.equals("brief_summary")){
						streamReader.next();
						if(streamReader.getEventType() == XMLStreamReader.START_ELEMENT){
							currentElement = streamReader.getLocalName();
							ct.setBrief_summary(streamReader.getElementText());
						}
					}
					else if(currentElement.equals("criteria")){
						// process criteria to inclusion and exclusion
						streamReader.next();
						if(streamReader.getEventType() == XMLStreamReader.START_ELEMENT){
							currentElement = streamReader.getLocalName();
							ct.setExc_criteria((streamReader.getElementText()));
						}
					}
					else if(currentElement.equals("study_pop")){
						streamReader.next();
						if(streamReader.getEventType() == XMLStreamReader.START_ELEMENT){
							currentElement = streamReader.getLocalName();
							ct.setStudy_pop(((streamReader.getElementText())));
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
		//ct.printCT();
		return ct;
	}
	
	public static class CriteriaProcessor{
		private NLPSentenceD sd = new NLPSentenceD("en-sent.bin");
		private NLPTokenizer tk = new NLPTokenizer("en-token.bin");
		private NLPTagger tg = new NLPTagger("en-pos-maxent.bin");
		private NLPChunker ck = new NLPChunker("en-chunker.bin");
		
		public ProcessResult processSentence(String sentence){
			ProcessResult result = new ProcessResult();
			result.setSentence(sentence);
			//result.setSentence(sd.sentenceDetect(text));
			
			List<String> sentenceTokens = tk.tokenize(sentence);
			List<String> sentencePosTags = tg.posTag(sentenceTokens);
			List<String> sentenceChunkTags = ck.chunk(sentenceTokens,sentencePosTags);

			result.setTokensList(sentenceTokens);
			result.setPosTagsList(sentencePosTags);
			result.setChunkTagsList(sentenceChunkTags);
			return result;
		}
		
		public List<ProcessResult> processText(String text){
			List<ProcessResult> resultList = new ArrayList<ProcessResult>();
			for(String sentence: sd.sentenceDetect(text)){
				resultList.add(processSentence(sentence));
			}
			return resultList;
		}
		
/*		public List<String> processSentences(String text){
			sentences = sd.sentenceDetect(text);

			for(String sentence: sentences){
				tokens.addAll(tk.tokenize(sentence));
			}
			return sentences;
		}*/

/*		public void getEntities(List<String> chunks){
			int i=0;
			while(i<tokens.size()){
				if(chunks.get(i).contains("O")){
					tokens.remove(i);
					posTags.remove(i);
					chunkTags.remove(i);
				}
				else{
					i++;
				}
			}
			
			for(String entity: createEntities()){
				System.out.println(entity.toString());
			}
		}*/

/*		private List<String> createEntities(){
			List<String> entities = new ArrayList<String>();
			StringBuilder sb = new StringBuilder();
			boolean prevB = false;
			for(int i=0; i < tokens.size();i++){
				sb.append(tokens.get(i)+" ");
				// new
				if(chunkTags.get(i).contains("B") && !prevB){
					entities.add(sb.toString());
					sb.setLength(0);
					prevB = true;
				}
				// new
				else if(chunkTags.get(i).contains("B") && prevB){
					entities.add(sb.toString());
					sb.setLength(0);
				}
				else if(chunkTags.get(i).contains("I")){
					prevB = false;
				}
			}
			return entities;
		}*/

		public static void printData(List<ProcessResult> data){
			System.out.format("%7s %10s\t %7s\t %7s\n","Sentence","Token","POSTag","ChunkTag");
			for(int i2 = 0; i2 < data.size();i2++){
				for(int i1 = 0; i1 < data.get(i2).getTokensList().size(); i1++){
					System.out.format("%7d: %10s\t %7s\t %7s\n",
							i2,
							data.get(i2).getTokensList().get(i1),
							data.get(i2).getPosTagsList().get(i1), 
							data.get(i2).getChunkTagsList().get(i1));
				}
				System.out.println("==========");
			}
		}
	}
}
