package ws;

import java.io.*;
import java.net.*;

public class Rest {

	public static void main(String[] args) throws IOException {
		try {

			URL url = new URL("https://clinicaltrials.gov/show/NCT02087852?displayxml=true");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/xml");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			PrintWriter pw = new PrintWriter("C:/Users/Jorge/nlp/input.txt","UTF-8");

			String output;
			boolean criteria = false;
			while ((output = br.readLine()) != null) {
				if(output.contains("<eligibility>")){
					criteria = true;
				}
				else if(output.contains("</eligibility>")){
					criteria = false;
				}
				if(criteria){
					System.out.println(output);
					pw.println(output);
				}
				pw.close();
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
	}
}
