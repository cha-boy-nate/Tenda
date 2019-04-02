package test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ExampleAPICall {
    
	public static void main (String[] args) throws IOException 
	{
		try {
			//Format the URL to retrieve.
			URL url = new URL("http://34.217.162.221/user/lookup/1");
			
			//Open connection.
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			
			//In case the connection fails i.e not a 200 reposense code.
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			//Variable for assignment to retrieved data.
			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())
			));
			
			//Printing the return from call.
			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}
			conn.disconnect();
			} 
		
		//In case of errors
		catch (MalformedURLException e) {
			e.printStackTrace();
		}	
		catch (IOException e) {
			e.printStackTrace();
		}
	}
    }
