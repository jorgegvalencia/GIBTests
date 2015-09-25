package mainapp.Model;


import gov.nih.nlm.nls.metamap.AcronymsAbbrevs;
import gov.nih.nlm.nls.metamap.ConceptPair;
import gov.nih.nlm.nls.metamap.Ev;
import gov.nih.nlm.nls.metamap.Mapping;
import gov.nih.nlm.nls.metamap.MetaMapApi;
import gov.nih.nlm.nls.metamap.MetaMapApiImpl;
import gov.nih.nlm.nls.metamap.Negation;
import gov.nih.nlm.nls.metamap.PCM;
import gov.nih.nlm.nls.metamap.Position;
import gov.nih.nlm.nls.metamap.Result;
import gov.nih.nlm.nls.metamap.Utterance;

import java.util.*;

/**
 * @author jorgegvalencia
 */
public class ConceptExtractor {

	private MetaMapApi mmapi;
	private String options;

	public ConceptExtractor() {
		this.mmapi = new MetaMapApiImpl();
		this.options = "-R SNOMEDCT_US";
		mmapi.setOptions(options);
	}

	public List<Result> queryFromString(String text) {
		List<Result> resultList = mmapi.processCitationsFromString(text);
		return resultList;
	}

	public void printAcronymsAbbrevs(Result result) {
		List<AcronymsAbbrevs> aaList;
		try {
			aaList = result.getAcronymsAbbrevs();
			if (aaList.size() > 0) {
				System.out.println("Acronyms and Abbreviations:");
				for (AcronymsAbbrevs e: aaList) {
					System.out.println("Acronym: " + e.getAcronym());
					System.out.println("Expansion: " + e.getExpansion());
					System.out.println("Count list: " + e.getCountList());
					System.out.println("CUI list: " + e.getCUIList());
				}
			} else {
				System.out.println(" None.");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void printNegations(Result result) {
		List<Negation> negList;
		try {
			negList = result.getNegations();
			if (negList.size() > 0) {
				System.out.println("Negations:");
				for (Negation e: negList) {
					System.out.println("type: " + e.getType());
					System.out.print("Trigger: " + e.getTrigger() + ": [");
					for (Position pos: e.getTriggerPositionList()) {
						System.out.print(pos  + ",");
					}
					System.out.println("]");
					System.out.print("ConceptPairs: [");
					for (ConceptPair pair: e.getConceptPairList()) {
						System.out.print(pair + ",");
					}
					System.out.println("]");
					System.out.print("ConceptPositionList: [");
					for (Position pos: e.getConceptPositionList()) {
						System.out.print(pos + ",");
					}
					System.out.println("]");
				}
			} else {
				System.out.println(" None.");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void printUtterances(Result result) {
		try {
			for (Utterance utterance: result.getUtteranceList()) {
				System.out.println("Utterance:");
				System.out.println(" Id: " + utterance.getId());
				System.out.println(" Utterance text: " + utterance.getString());
				System.out.println(" Position: " + utterance.getPosition());
				for (PCM pcm: utterance.getPCMList()) {
					System.out.println("Phrase:");
					System.out.println(" text: " + pcm.getPhrase().getPhraseText());
					System.out.println(" Minimal Commitment Parse: " + pcm.getPhrase().getMincoManAsString());
					System.out.println("Candidates:");
					for (Ev ev: pcm.getCandidateList()) {
						System.out.println(" Candidate:");
						System.out.println("  Score: " + ev.getScore());
						System.out.println("  Concept Id: " + ev.getConceptId());
						System.out.println("  Concept Name: " + ev.getConceptName());
						System.out.println("  Preferred Name: " + ev.getPreferredName());
						System.out.println("  Matched Words: " + ev.getMatchedWords());
						System.out.println("  Semantic Types: " + ev.getSemanticTypes());
						System.out.println("  MatchMap: " + ev.getMatchMap());
						System.out.println("  MatchMap alt. repr.: " + ev.getMatchMapList());
						System.out.println("  is Head?: " + ev.isHead());
						System.out.println("  is Overmatch?: " + ev.isOvermatch());
						System.out.println("  Sources: " + ev.getSources());
						System.out.println("  Positional Info: " + ev.getPositionalInfo());
					}
					System.out.println("Mappings:");
					for (Mapping map: pcm.getMappingList()) {
						System.out.println(" Map Score: " + map.getScore());
						for (Ev mapEv: map.getEvList()) {
							System.out.println("   Score: " + mapEv.getScore());
							System.out.println("   Concept Id: " + mapEv.getConceptId());
							System.out.println("   Concept Name: " + mapEv.getConceptName());
							System.out.println("   Preferred Name: " + mapEv.getPreferredName());
							System.out.println("   Matched Words: " + mapEv.getMatchedWords());
							System.out.println("   Semantic Types: " + mapEv.getSemanticTypes());
							System.out.println("   MatchMap: " + mapEv.getMatchMap());
							System.out.println("   MatchMap alt. repr.: " + mapEv.getMatchMapList());
							System.out.println("   is Head?: " + mapEv.isHead());
							System.out.println("   is Overmatch?: " + mapEv.isOvermatch());
							System.out.println("   Sources: " + mapEv.getSources());
							System.out.println("   Positional Info: " + mapEv.getPositionalInfo());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String> getUtterancesFromText(String text) {
		List<String> utterances = new ArrayList<String>();
		try{

			List<Result> result = queryFromString(text);
			for(Result res: result){
				for(Utterance uttr: res.getUtteranceList()){
					utterances.add(uttr.getString());
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return utterances;
	}

	public List<EligibilityCriteria> createECFromText(String options,String text){
		mmapi.setOptions(options);
		List<EligibilityCriteria> list = new ArrayList<EligibilityCriteria>();
		try{
			List<Result> resultList = mmapi.processCitationsFromString(text);
			for(Result result: resultList){
				for (Utterance utterance: result.getUtteranceList()) {
					EligibilityCriteria ec = new EligibilityCriteria();
					ec.setUtterance(utterance.getString());
					List<Concept> conceptList = new ArrayList<Concept>();
					for (PCM pcm: utterance.getPCMList()){
						for (Mapping map: pcm.getMappingList()) {
							for(Ev mapEv: map.getEvList()){
								Concept concept = new Concept(mapEv.getConceptName(),mapEv.getPreferredName(),mapEv.getConceptId(),mapEv.getScore(),mapEv.getSemanticTypes(),mapEv.getMatchedWords());
								conceptList.add(concept);
							}
							ec.setScore(map.getScore());
						}
					}
					ec.setConcepts(conceptList);
					list.add(ec);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return list;

	}

	public void setOptions(String options) {
		this.options = options;
		mmapi.setOptions(options);
	}

}