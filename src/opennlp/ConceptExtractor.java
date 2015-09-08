package opennlp;

public class ConceptExtractor {

	public static void main(String[] args) {
		CriteriaProcessor cp = new CriteriaProcessor();
		ClinicalTrial ct = cp.downloadCT("https://clinicaltrials.gov/show/NCT01334021?displayxml=true");
		String criteria = ct.getExc_criteria();
		
		//with sentences
		for(String sentence: cp.processSentences(criteria)){
			System.out.println(">>>"+sentence+"<<<"+"\n");
		}
		
		//with chunks
		if(criteria != null){
			cp.getEntities(cp.processText(criteria));
			cp.printData();
		}
	}

}
