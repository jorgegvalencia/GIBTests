package opennlp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;

public class NLPChunker {
	String model_route;

	public NLPChunker(String model){
		model_route = model;
	}

	public String[] chunk(String[] tokens, String[] posTags){
		InputStream modelIn = null;
		ChunkerModel model = null;
		String chunkTags[] = null;
		try {
			modelIn = new FileInputStream(model_route);
			model = new ChunkerModel(modelIn);
			ChunkerME chunker = new ChunkerME(model);
			chunkTags = chunker.chunk(tokens, posTags);
		}
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		catch (IOException e) {
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
		return chunkTags;

	}
}
