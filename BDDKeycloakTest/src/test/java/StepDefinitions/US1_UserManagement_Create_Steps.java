package StepDefinitions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

import io.cucumber.java.en.When;

public class US1_UserManagement_Create_Steps {
	
	private StepData stepData;
	private String createUserURL = "http://localhost:8080/auth/admin/realms/#realm#/users";
	
	public US1_UserManagement_Create_Steps(StepData stepData) {
		this.stepData = stepData;
	}
	
	@When("I create a new user with Username : {string} and FirstName : {string} and Email : {string}")
	public void i_create_a_new_user_with_username_and_first_name_and_email(String userName, String firstName, String email) {
		String rawData = "{\"username\": \""+userName+"\",\"firstName\": \""+firstName+"\",\"email\": \""+email+"\"}";
		String type = "application/json; utf-8";
		createUserURL = createUserURL.replaceAll("#realm#", this.stepData.stringHashMap.get("realm"));
		try {
			byte[] postData = rawData.getBytes( StandardCharsets.UTF_8 );
			URL u = new URL(createUserURL);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization","Bearer "+this.stepData.stringHashMap.get("bearerToken"));
			conn.setRequestProperty( "Content-Type", type );
			conn.setRequestProperty("charset", "utf-8");
			conn.setRequestProperty( "Content-Length", String.valueOf(rawData.length()));
			conn.setUseCaches(false);
			OutputStream os = conn.getOutputStream();
			os.write(postData);
			
			// For POST only - END

			int responseCode = conn.getResponseCode();
			System.out.println("POST Response Code :: " + responseCode);
			this.stepData.stringHashMap.put("responseCode",String.valueOf(responseCode));

			if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) { //success
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

//				response.toString().split("\"access_token\":\"")[1].split("\"")[0];
				JSONObject responseJSONObj = new JSONObject(response.toString());
				this.stepData.stringHashMap.put("bearerToken", responseJSONObj.getString("access_token"));
			} else {
				System.out.println("POST request not worked");
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
