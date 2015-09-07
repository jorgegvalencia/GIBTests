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

	public static void main(String[] args){
		String criteria = downloadCT("https://clinicaltrials.gov/show/NCT00875901?displayxml=true");
		if(criteria != null){
			processText(criteria);
		}
	}
	public static String downloadCT(String ctpath){
		String output = null;
		String line;
		try {
			URL url = new URL(ctpath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "text/xml");
			System.out.println(conn.getContentType());
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

	public static List<String[]> processText(String text){
		NLPSentenceD sd = new NLPSentenceD("C:/Users/Jorge/nlp/en-sent.bin");
		NLPTokenizer tk = new NLPTokenizer("C:/Users/Jorge/nlp/en-token.bin");
		NLPTagger tg = new NLPTagger("C:/Users/Jorge/nlp/en-pos-maxent.bin");
		NLPChunker ck = new NLPChunker("C:/Users/Jorge/nlp/en-chunker.bin");

		String sentences[];
		List<String[]> tokens = new ArrayList<String[]>();
		List<String[]> posTags = new ArrayList<String[]>();
		List<String[]> chunkTags = new ArrayList<String[]>();

		sentences = sd.sentenceDetect(text);

		for(String sentence: sentences){
			tokens.add(tk.tokenize(sentence));
		}
		int i = 0;
		while(i < tokens.size()){
			posTags.add(tg.posTag(tokens.get(i)));
			i++;
		}
		i = 0;
		while(i < tokens.size()){
			chunkTags.add(ck.chunk(tokens.get(i), posTags.get(i)));
			i++;
		}
		//
		for(int i1 = 0; i1 < tokens.size(); i1++){
			System.out.println(">================ Sentence "+ i1 );
			for(int i2 = 0; i2 < tokens.get(i1).length;i2++){
				System.out.format("%10s\t %10s\t %10s\n",tokens.get(i1)[i2],posTags.get(i1)[i2],chunkTags.get(i1)[i2]);
			}		
		}
		//
		return chunkTags;
	}
	
	public List<String> getEntities(List<String[]> chunks){
		return null;
	}
}
