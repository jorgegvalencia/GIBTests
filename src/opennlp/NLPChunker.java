package opennlp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;

public class NLPChunker {
	String model_route;

	public NLPChunker(String model){
		model_route = model;
	}

	public String[] chunk(String[] tokenList, String[] tagList){
		InputStream modelIn = null;
		ChunkerModel model = null;
		String[] chunkTags = null;
		try {
			modelIn = new FileInputStream(model_route);
			model = new ChunkerModel(modelIn);
			ChunkerME chunker = new ChunkerME(model);
			chunkTags = chunker.chunk(tokenList, tagList);
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
