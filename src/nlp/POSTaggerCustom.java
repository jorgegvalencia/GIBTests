package nlp;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;


public class POSTaggerCustom {
	public static void main(String[] args) {
		InputStream modelIn = null;

		try {
			modelIn = new FileInputStream("C:/Users/Jorge/nlp/en-pos-maxent.bin");
			POSModel model = new POSModel(modelIn);
			POSTaggerME tagger = new POSTaggerME(model);
			String sent[] = new String[]{"Most", "large", "cities", "in", "the", "US", "had",
					"morning", "and", "afternoon", "newspapers", "."};		  
			String tags[] = tagger.tag(sent);
			double probs[] = tagger.probs();
			for(String tag : tags){
				System.out.println(tag);
			}
		}
		catch (IOException e) {
			// Model loading failed, handle the error
			e.printStackTrace();
		}
		finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				}
				catch (IOException e) {
				}
			}
		}

	}

}
