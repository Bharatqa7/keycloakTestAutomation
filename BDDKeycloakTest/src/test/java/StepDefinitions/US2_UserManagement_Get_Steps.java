package StepDefinitions;
import org.json.*;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class US2_UserManagement_Get_Steps {
	
	private StepData stepData;
	private String getUserURL = "http://localhost:8080/auth/admin/realms/#realm#/users/";

	public US2_UserManagement_Get_Steps(StepData stepData) {
		this.stepData = stepData;
	}

	@When("I want to get deatils of user with ID : {string}")
	public void i_want_to_get_deatils_of_user_with_id(String userID) {
		getUserURL = getUserURL.replaceAll("#realm#", this.stepData.stringHashMap.get("realm"))+userID;
		System.out.println(getUserURL);
		try {
			URL u = new URL(getUserURL);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization","Bearer "+this.stepData.stringHashMap.get("bearerToken"));
			conn.setRequestProperty( "Content-Type", "*/*" );
			conn.setUseCaches(false);
			
			// For POST only - END

			int responseCode = conn.getResponseCode();
			System.out.println("GET Response Code :: " + responseCode);
			this.stepData.stringHashMap.put("responseCode",String.valueOf(responseCode));

			if (responseCode == HttpURLConnection.HTTP_OK) { //success
				this.stepData.stringHashMap.put("bodyType","JSON");
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

//				response.toString().split("\"access_token\":\"")[1].split("\"")[0];
				this.stepData.stringHashMap.put("responseBody", response.toString());
				System.out.println(response.toString());
			} else {
				System.out.println("GET request not worked");
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

	@Then("I get key {string} with value {string} in response body")
	public void i_get_key_with_value_in_response_body(String bodyKey, String bodyValue) {
		JSONObject responseJSONObj = new JSONObject(this.stepData.stringHashMap.get("responseBody"));
		assertEquals(responseJSONObj.get(bodyKey), bodyValue);
	}

}
