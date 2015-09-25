package mainapp.Model;

import java.util.*;

/**
 * @author jorgegvalencia
 */
public class Concept {
    public Concept() {
    }
    private String name;
    private String prefered;
    private String cui;
    private int score;
    private List<String> semTypes;
    private List<String> tokens;
    /**
     * @param name 
     * @param prefered 
     * @param cui 
     * @param score 
     * @param semType 
     * @param tokens
     */
	public Concept(String name, String prefered, String cui, int score, List<String> semType, List<String> tokens) {
		super();
		this.name = name;
		this.prefered = prefered;
		this.cui = cui;
		this.score = score;
		this.semTypes = semType;
		this.tokens = tokens;
	}
	public String getName() {
		return name;
	}
	public String getPrefered() {
		return prefered;
	}
	public String getCui() {
		return cui;
	}
	public int getScore() {
		return score;
	}
	public List<String> getSemTypes() {
		return semTypes;
	}
	public List<String> getTokens() {
		return tokens;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPrefered(String prefered) {
		this.prefered = prefered;
	}
	public void setCui(String cui) {
		this.cui = cui;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public void setSemTypes(List<String> semTypes) {
		this.semTypes = semTypes;
	}
	public void setTokens(List<String> tokens) {
		this.tokens = tokens;
	}
	
	public void printConcept(){
		System.out.format("CUI: %s SCORE: %5d SEMTYPES: %s NAME: %"+name.length()+"s PREF: %"+prefered.length()+"s TOKENS: %s \n",cui,score,semTypes,name,prefered,tokens);
	}
	

}