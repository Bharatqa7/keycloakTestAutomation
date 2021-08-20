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

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.json.*;

import static org.junit.Assert.*;

public class Common_Steps {
	
	private StepData stepData;
	
	private String loginURL = "http://localhost:8080/auth/realms/#realm#/protocol/openid-connect/token";
	
	private String userName= "admin";
	private String password  = "Pa55w0rd";
	private String grant_type = "password";
	private String client_cli = "admin-cli";
	
	public Common_Steps(StepData stepData) {
		this.stepData = stepData;
	}
	
	@Given("I am an authenticated keycloak as {string} in {string} Realm")
	public void i_am_an_authenticated_keycloak_as_in_realm(String userAccessType, String realmType) {
		
		this.stepData.stringHashMap.put("realm",realmType);
		
		String rawData = "username="+userName+"&password="+password+"&grant_type="+grant_type+"&client_id="+client_cli;
		String type = "application/x-www-form-urlencoded";
		loginURL = loginURL.replaceAll("#realm#", realmType);
		try {
			byte[] postData = rawData.getBytes( StandardCharsets.UTF_8 );
			URL u = new URL(loginURL);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty( "Content-Type", type );
			conn.setRequestProperty("charset", "utf-8");
			conn.setRequestProperty( "Content-Length", String.valueOf(rawData.length()));
			conn.setUseCaches(false);
			OutputStream os = conn.getOutputStream();
			os.write(postData);
			
			// For POST only - END

			int responseCode = conn.getResponseCode();
			System.out.println("POST Response Code :: " + responseCode);

			if (responseCode == HttpURLConnection.HTTP_OK) { //success
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
	
	@Then("I get response code of {string}")
	public void i_get_response_code_of(String responseCode) {
	    assertEquals(responseCode, this.stepData.stringHashMap.get("responseCode"));
	}

	@Then("I get get response body of type {string}")
	public void i_get_get_response_body_of_type(String bodyType) {
		assertEquals(bodyType, this.stepData.stringHashMap.get("bodyType"));
	}
	
}
