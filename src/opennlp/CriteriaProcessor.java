package opennlp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CriteriaProcessor {
	private NLPSentenceD sd;
	private NLPTokenizer tk;
	private NLPTagger tg;
	private NLPChunker ck;
	private List<String> sentences;
	private List<String> tokens;
	private List<String> posTags;
	private List<String> chunkTags;
	
	public CriteriaProcessor(){
		sd = new NLPSentenceD("C:/Users/Jorge/nlp/en-sent.bin");
		tk = new NLPTokenizer("C:/Users/Jorge/nlp/en-token.bin");
		tg = new NLPTagger("C:/Users/Jorge/nlp/en-pos-maxent.bin");
		ck = new NLPChunker("C:/Users/Jorge/nlp/en-chunker.bin");

		sentences = new ArrayList<String>();
		tokens = new ArrayList<String>();
		posTags = new ArrayList<String>();
		chunkTags = new ArrayList<String>();
	}

	/*public static void main(String[] args){
		String criteria = downloadCT("https://clinicaltrials.gov/show/NCT00875901?displayxml=true");
		if(criteria != null){
			processText(criteria);
		}
	}*/
	public String downloadCT(String ctpath){
		String output = null;
		String line;
		try {
			URL url = new URL(ctpath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "text/xml");
			//System.out.println(conn.getContentType());
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream()),"utf-8"));
			boolean criteria = false;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				if(line.contains("<eligibility>")){
					criteria = true;
				}
				else if(line.contains("</eligibility>")){
					criteria = false;
				}
				if(criteria){
					sb.append(line);
					sb.append("\n");
				}
			}
			output = sb.toString();
			output = output.replaceAll("\\<.*?\\>", "");
			//System.out.println(output);
			conn.disconnect();
		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		}
		return output;
	}

	public List<String> processText(String text){
		sentences = sd.sentenceDetect(text);

		for(String sentence: sentences){
			tokens.addAll(tk.tokenize(sentence));
		}
		posTags.addAll(tg.posTag(tokens));
		chunkTags.addAll(ck.chunk(tokens, posTags));
		//
		//printData();
		//
		return chunkTags;
	}

	public void getEntities(List<String> chunks){
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
	}
	
	private List<String> createEntities(){
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
	}
	
	public void printData(){
		for(int i1 = 0; i1 < tokens.size(); i1++){
			System.out.format("%10s\t %7s\t %7s\n",tokens.get(i1),posTags.get(i1),chunkTags.get(i1));	
		}
	}
}
