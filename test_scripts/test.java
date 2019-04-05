import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class test {
    
	public static void main (String[] args) throws IOException 
	{
		
		try {
			//Format the URL to retrieve. Our app input and current page needs to define whats after the server address.
			URL url = new URL("http://127.0.0.1:5000/login");
			String email = "nathanj@spu.edu";
			String password = "password";
			String data = "{\"email\":\""+email+"\",\"password\":\""+password+"\"}";
			//String data = "{\"email\":\"nathanj@spu.edu\",\"password\":\"password\"}";
			System.out.println(data);
			//String data = "\"{"\"user"\": "\"Nathan"\","\"password"\":"\"test"\"}";
			//Open connection.
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);
    		conn.getOutputStream().write(data.getBytes("UTF-8"));
			
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
