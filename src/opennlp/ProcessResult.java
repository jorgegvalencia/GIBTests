package opennlp;

import java.util.ArrayList;
import java.util.List;

public class ProcessResult {
	private List<String> sentence;
	private List<List<String>> tokensList;
	private List<List<String>> posTagsList;
	private List<List<String>> chunkTagsList;

	public ProcessResult(){
		sentence = new ArrayList<String>();
		tokensList = new ArrayList<List<String>>();
		posTagsList = new ArrayList<List<String>>();
		chunkTagsList = new ArrayList<List<String>>();
	}

	public List<String> getSentence() {
		return sentence;
	}

	public List<List<String>> getTokensList() {
		return tokensList;
	}

	public List<List<String>> getPosTagsList() {
		return posTagsList;
	}

	public List<List<String>> getChunkTagsList() {
		return chunkTagsList;
	}

	public void setSentence(List<String> sentence) {
		this.sentence = sentence;
	}

	public void setTokensList(List<List<String>> tokensList) {
		this.tokensList = tokensList;
	}

	public void setPosTagsList(List<List<String>> posTagsList) {
		this.posTagsList = posTagsList;
	}

	public void setChunkTagsList(List<List<String>> chunkTagsList) {
		this.chunkTagsList = chunkTagsList;
	}


	
}
