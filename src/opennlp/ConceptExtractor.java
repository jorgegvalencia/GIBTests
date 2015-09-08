package opennlp;

public class ConceptExtractor {

	public static void main(String[] args) {
		CriteriaProcessor cp = new CriteriaProcessor();
		ClinicalTrial ct = cp.downloadCT("https://clinicaltrials.gov/show/NCT01334021?displayxml=true");
		String criteria = ct.getExc_criteria();
		for(String sentence: cp.processText(criteria)){
			System.out.println(">>>"+sentence+"<<<"+"\n");
		}
/*		if(criteria != null){
			cp.getEntities(cp.processText(criteria));
			//cp.printData();
		}*/
	}

}
