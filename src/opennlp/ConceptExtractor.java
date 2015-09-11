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
		CriteriaPreprocessor cp = new CriteriaPreprocessor();
		// NCT01358877
		// NCT00148876
		ClinicalTrial ct = downloadCT("NCT01358877");
		String criteria = ct.getExc_criteria();

		//with chunks
		if(criteria != null){
			List<ProcessResult> results = cp.processText(criteria);
			CriteriaPreprocessor.printData(results);
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

	public static class CriteriaPreprocessor{
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
			createEntities(result);
			return result;
		}

		public List<ProcessResult> processText(String text){
			String refinedText;
			// Elimina los guiones de puntos de contenido
			refinedText = text.replaceAll("-\\s+(?=[A-Z])", "");
			// Elimina los puntos de contenido numéricos
			refinedText = refinedText.replaceAll("([0-9]+\\.)+\\s", "");
			// A las oraciones sin punto final y con salto de línea se le añade un punto final
			refinedText = refinedText.replaceAll("(?<=.)\n\\s+(?=[A-Z]{1}[a-z])", ". ");
			refinedText = refinedText.replaceAll("\\.{2}", ". ");
			//refinedText = refinedText.replaceAll("(?<=\\p{Punct})\n\\s+(?=[A-Z])", "\n");
			//refinedText = refinedText.replaceAll(" [a-z]. ","");
			List<ProcessResult> resultList = new ArrayList<ProcessResult>();
			for(String sentence: sd.sentenceDetect(refinedText)){
				resultList.add(processSentence(sentence));
			}
			return resultList;
		}


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

		private void createEntities(ProcessResult result){
			List<String> entities = new ArrayList<String>();
			StringBuilder sb = new StringBuilder();
			boolean nounphrase = false;
			for(int i=0; i < result.getTokensList().size();i++){
				// new
				if(result.getChunkTagsList().get(i).equals("B-NP")){
					sb.setLength(0);
					if(!result.getTokensList().get(i).equals("the"))
					sb.append(result.getTokensList().get(i)+" ");
					//entities.add(sb.toString());
					nounphrase = true;
				}
				// new
				else if(result.getChunkTagsList().get(i).equals("I-NP")){
					if(!result.getTokensList().get(i).equals("the"))
					sb.append(result.getTokensList().get(i)+" ");
				}
				else{
					if(nounphrase){
						entities.add(sb.toString());
						sb.setLength(0);
					}
					nounphrase = false;
				}
			}
			result.setNounPhrases(entities);
		}

		public static void printData(List<ProcessResult> data){
			System.out.format("%7s %10s\t %7s\t %7s\n","Sentence","Token","POSTag","ChunkTag");
			for(int i2 = 0; i2 < data.size();i2++){
				System.out.println(data.get(i2).getSentence());
				for(int i1 = 0; i1 < data.get(i2).getTokensList().size(); i1++){
					if(data.get(i2).getChunkTagsList().get(i1).contains("NP")){
						System.out.format("%7d: %10s\t %7s\t %7s<<<<\n",
								i2,
								data.get(i2).getTokensList().get(i1),
								data.get(i2).getPosTagsList().get(i1), 
								data.get(i2).getChunkTagsList().get(i1));
					}
					else
						System.out.format("%7d: %10s\t %7s\t %7s\n",
								i2,
								data.get(i2).getTokensList().get(i1),
								data.get(i2).getPosTagsList().get(i1), 
								data.get(i2).getChunkTagsList().get(i1));
				}
				System.out.println("==========");
				System.out.println("Noun Phrases: "+data.get(i2).getNounPhrases().toString());
				System.out.println("==========");
				System.out.println("==========");
			}
		}
	}
}
