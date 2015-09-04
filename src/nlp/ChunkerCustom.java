package nlp;

import java.io.*;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.util.Sequence;

public class ChunkerCustom {
	public static void main (String[] args){
		InputStream modelIn = null;
		ChunkerModel model = null;

		try {
			modelIn = new FileInputStream("C:/Users/Jorge/nlp/en-chunker.bin");
			model = new ChunkerModel(modelIn);
			ChunkerME chunker = new ChunkerME(model);
			String sent[] = new String[] { "Rockwell", "International", "Corp.", "'s",
					"Tulsa", "unit", "said", "it", "signed", "a", "tentative", "agreement",
					"extending", "its", "contract", "with", "Boeing", "Co.", "to",
					"provide", "structural", "parts", "for", "Boeing", "'s", "747",
					"jetliners", "." };
			String pos[] = new String[] { "NNP", "NNP", "NNP", "POS", "NNP", "NN",
					"VBD", "PRP", "VBD", "DT", "JJ", "NN", "VBG", "PRP$", "NN", "IN",
					"NNP", "NNP", "TO", "VB", "JJ", "NNS", "IN", "NNP", "POS", "CD", "NNS",
			"." };
			String tag[] = chunker.chunk(sent, pos);
			for(int i = 0; i < tag.length;i++){
				System.out.println(sent[i] + "\t\t\t" + pos[i] + "\t" + tag[i]);
			}
		} catch (IOException e) {
			// Model loading failed, handle the error
			e.printStackTrace();
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
