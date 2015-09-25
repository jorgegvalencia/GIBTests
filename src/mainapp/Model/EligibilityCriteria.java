package mainapp.Model;

import java.util.*;

/**
 * @author jorgegvalencia
 */
public class EligibilityCriteria {

    private int type;
    private String utterance;
    private boolean negation;
    private boolean temporal;
    private List<Concept> concepts;
    private int score;
    
    public EligibilityCriteria() {
    	this.type = 0;
    	this.utterance = "";
    	this.negation = false;
    	this.temporal = false;
    	this.score = 0;
    	this.concepts = new ArrayList<Concept>();
    }
    
	public EligibilityCriteria(String utterance, boolean negation, boolean temporal) {
		this.utterance = utterance;
		this.negation = negation;
		this.temporal = temporal;
	}

	public String getUtterance() {
		return utterance;
	}

	public boolean isNegation() {
		return negation;
	}

	public boolean isTemporal() {
		return temporal;
	}

	public List<Concept> getConcepts() {
		return concepts;
	}

	public int getScore() {
		return score;
	}

	public void setUtterance(String utterance) {
		this.utterance = utterance;
	}

	public void setNegation(boolean negation) {
		this.negation = negation;
	}

	public void setTemporal(boolean temporal) {
		this.temporal = temporal;
	}

	public void setConcepts(List<Concept> concepts) {
		this.concepts = concepts;
	}

	public void setScore(int score) {
		this.score = score;
	}
    
	public void printEC(){
		System.out.println("EC");
		System.out.println("Type: "+type);
		System.out.println("Utterance: "+utterance);
		System.out.println("Score: "+score);
		System.out.println("Has negation: "+negation);
		System.out.println("Has temporal: "+temporal);
		String conceptCUIs = "";
		for(Concept concept: concepts){
			conceptCUIs = conceptCUIs.concat(concept.getCui() + "|" + concept.getName() + "|");
		}
		System.out.println(conceptCUIs);
		System.out.println("-------------");
	}
    

}