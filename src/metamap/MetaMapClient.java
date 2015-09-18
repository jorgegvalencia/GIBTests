package metamap;

import java.util.ArrayList;
import java.util.List;

import gov.nih.nlm.nls.metamap.*;

public class MetaMapClient {
	private MetaMapApi api;
	private String options;

	public MetaMapClient(String options){
		this.api = new MetaMapApiImpl();
		this.options = options;
		api.setOptions(options);
	}

	public List<Result> queryFromString(String text){
		List<Result> resultList = api.processCitationsFromString(text);
		return resultList;
	}

	public void printAcronymsAbbrevs(Result result){
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

	public void printNegations(Result result){
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

	public void printUtterances(Result result){
		try {
			for (Utterance utterance: result.getUtteranceList()) {
				System.out.println("=================================================================================================================================");
				System.out.println("Utterance:");
				System.out.println(" Id: " + utterance.getId());
				System.out.println(" Utterance text: " + utterance.getString());
				//System.out.println(" Position: " + utterance.getPosition());
				for (PCM pcm: utterance.getPCMList()) {
					System.out.println("Phrase:");
					System.out.println(" text: " + pcm.getPhrase().getPhraseText());
					System.out.println("Candidates:");
					//System.out.format("%20s\t %20s\t %20s\t %20s\n","SCTID","Name","Prefered","Matched Words","Semantic Types");
					//System.out.println("-----------------------------------------------------------------------------------------");
					for (Ev ev: pcm.getCandidateList()) {
						//System.out.println(" Candidate:");
						//System.out.println("  Score: " + ev.getScore());
						//System.out.format("%20s\t %20s\t %20s\t %20s\n",ev.getConceptId(),ev.getConceptName(),ev.getPreferredName(),ev.getMatchedWords(),ev.getSemanticTypes());
						/*System.out.println("  Concept Id: " + ev.getConceptId());
						System.out.println("  Concept Name: " + ev.getConceptName());
						System.out.println("  Preferred Name: " + ev.getPreferredName());
						System.out.println("  Matched Words: " + ev.getMatchedWords());
						System.out.println("  Semantic Types: " + ev.getSemanticTypes());
						System.out.println("  MatchMap: " + ev.getMatchMap());
						System.out.println("  MatchMap alt. repr.: " + ev.getMatchMapList());
						System.out.println("  is Head?: " + ev.isHead());
						System.out.println("  is Overmatch?: " + ev.isOvermatch());
						System.out.println("  Sources: " + ev.getSources());
						System.out.println("  Positional Info: " + ev.getPositionalInfo());*/
					}
					if(!pcm.getMappingList().isEmpty()){
						System.out.println("Mappings:");
						System.out.println("------------------------------------------------------------------------------------------------------------------------------------------");
						System.out.format("%10s | %40s | %40s | %40s | %5s \n","CUI","Name","Prefered","Matched Words","Semantic Types");
						System.out.println("------------------------------------------------------------------------------------------------------------------------------------------");
						for (Mapping map: pcm.getMappingList()) {
							//System.out.println(" Map Score: " + map.getScore());
							for (Ev mapEv: map.getEvList()) {
								System.out.format("%10s | %40s | %40s | %40s | %5s \n",mapEv.getConceptId(),mapEv.getConceptName(),mapEv.getPreferredName(),mapEv.getMatchedWords(),mapEv.getSemanticTypes());
								/*System.out.println("   Score: " + mapEv.getScore());
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
							System.out.println("   Positional Info: " + mapEv.getPositionalInfo());*/
							}
						}
						System.out.println("------------------------------------------------------------------------------------------------------------------------------------------");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setOptions(String options){
		this.options = options;
		api.setOptions(options);
	}

	public List<String> getConceptsList(Result result){
		ArrayList<String> list = new ArrayList<String>();
		try{
			for (Utterance utterance: result.getUtteranceList()) {
				for (PCM pcm: utterance.getPCMList()) {
					for (Mapping map: pcm.getMappingList()) {
						for (Ev mapEv: map.getEvList()) {
							list.add(mapEv.getPreferredName());
						}
					}
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		for(int i=0; i<list.size();i++){
			System.out.println(list.get(i));
		}
		return list;
	}

	public void addOption(String option){
		this.options = options.concat(" "+option);
		api.setOptions(option);
	}
	public void showOptions(){
		System.out.println(this.options);
	}
}
