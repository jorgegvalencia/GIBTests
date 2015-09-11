package opennlp;

import java.util.ArrayList;
import java.util.List;

public class ProcessResult {
	private String sentence;
	private List<String> tokensList;
	private List<String> posTagsList;
	private List<String> chunkTagsList;
	private List<String> nounPhrases;

	public ProcessResult(){
		sentence = new String();	
		tokensList = new ArrayList<String>();
		posTagsList = new ArrayList<String>();
		chunkTagsList = new ArrayList<String>();	
	}

	public String getSentence() {
		return sentence;
	}

	public List<String> getTokensList() {
		return tokensList;
	}

	public List<String> getPosTagsList() {
		return posTagsList;
	}

	public List<String> getChunkTagsList() {
		return chunkTagsList;
	}

	public List<String> getNounPhrases() {
		return nounPhrases;
	}

	public void setNounPhrases(List<String> nounPhrases) {
		this.nounPhrases = nounPhrases;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public void setTokensList(List<String> tokensList) {
		this.tokensList = tokensList;
	}

	public void setPosTagsList(List<String> posTagsList) {
		this.posTagsList = posTagsList;
	}

	public void setChunkTagsList(List<String> chunkTagsList) {
		this.chunkTagsList = chunkTagsList;
	}
}
