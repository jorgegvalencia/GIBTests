package mainapp.Model;

import java.util.*;

/**
 * @author jorgegvalencia
 */
public final class Preprocessor {
    public Preprocessor() {
    }
    private final List<String> stopWords = new ArrayList<String>(Arrays.asList("and","by","for","in","of","or","the","to","with","no"));
    private final List<String> exclusionWords = new ArrayList<String>(Arrays.asList("about","alongside","an","anything","around","as","at",
    		"because","before","being","both","cannot","chronically","consists","covered","does",
    		"during","every","find","from","instead","into","more","must","no","not","only","or",
    		"properly","side","sided","some","something","specific","than","that","things","this",
    		"throughout","up","using","usually","when","while"));
    
    public String preProcessText(String text){
		String refinedText;
		// Elimina los guiones de puntos de contenido
		refinedText = text.replaceAll("-\\s+(?=[A-Z])", "");
		// Elimina los puntos de contenido numéricos
		refinedText = refinedText.replaceAll("([0-9]+\\.)+\\s", "");
		// A las oraciones sin punto final y con salto de línea se le añade un punto final
		refinedText = refinedText.replaceAll("(?<=.)\n\\s+(?=[A-Z]{1}[a-z])", ". ");
		refinedText = refinedText.replaceAll("\\.{2}", ". ");
		//refinedText = refinedText.replaceAll("-", " ");
		refinedText = refinedText.replaceAll("\\s+", " ");
		//refinedText = refinedText.replaceAll("(?<=\\p{Punct})\n\\s+(?=[A-Z])", "\n");
		//refinedText = refinedText.replaceAll(" [a-z]. ","");
		return refinedText;
	}
    
    public String removeSW(String text){
    	String refinedText = preProcessText(text);
    	for(String sw: stopWords){
    		refinedText = refinedText.replaceAll("\\s"+sw+"\\s", " ");
    	}
    	return refinedText;
    }
    
    public String removeEW(String text){
    	String refinedText = preProcessText(text);
    	for(String ew: exclusionWords){
    		refinedText = refinedText.replaceAll("\\s"+ew+"\\s", " ");
    	}
    	return refinedText;
    }
}