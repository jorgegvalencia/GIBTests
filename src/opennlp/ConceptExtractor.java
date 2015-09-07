package opennlp;

public class ConceptExtractor {

	public static void main(String[] args) {
		CriteriaProcessor cp = new CriteriaProcessor();
		String criteria = cp.downloadCT("https://clinicaltrials.gov/show/NCT00875901?displayxml=true");
		if(criteria != null){
			cp.getEntities(cp.processText(criteria));
			//cp.printData();
		}
	}

}
