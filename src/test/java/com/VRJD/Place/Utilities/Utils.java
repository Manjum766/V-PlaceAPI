package com.VRJD.Place.Utilities;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ResourceBundle;

import org.json.JSONObject;
import org.json.JSONTokener;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Utils {

	private static final String accessUri = "https://oauth2/default/v1/token ";
	private static final String clientId = " ";
	private static final String clientSecret = " ";
	private static final String clientGrantType = "client_credentials";
	private static final String clientScope = "Custom_Scope";

	private static final String BASE_URL = "https://bookstore.toolsqa.com";
	private static final String USERNAME = "TOOLSQA-Test";
	private static final String PASSWORD = "Test@@123";

	public static RequestSpecification requestSpecification;
	public static Response response;
	public static ResourceBundle resourceBundle;

	public static String generateOKTAToken() throws InterruptedException {
		String token = "";
		requestSpecification = null;
		requestSpecification = RestAssured.given().auth().preemptive().basic(clientId, clientSecret).baseUri(accessUri)
				.header("accept", "application/json").header("cache-control", "no-cache")
				.header("content-type", "application/x-www-form-urlencoded").formParam("grant-type", clientGrantType)
				.formParam("scope", clientScope);
		System.out.println(requestSpecification.toString());
		Thread.sleep(10000);
		Response response = requestSpecification.post();
		Thread.sleep(10000);
		System.out.println("Response Recieved: " + response.toString());
		JsonPath jsonPath = response.jsonPath();
		token = jsonPath != null ? (jsonPath.getString("token_type")) + " " + jsonPath.getString("access_token") : "";
		System.out.println("OKTA TOKEN RECIEVED: " + token);
		return token;
	}

	public static String generateToken() {
		String token = "";
		RestAssured.baseURI = BASE_URL;
		requestSpecification = RestAssured.given();

		requestSpecification.header("Content-Type", "application/json");
		response = requestSpecification.body("{ \"userName\":\"" + USERNAME + "\", \"password\":\"" + PASSWORD + "\"}")
				.post("/Account/v1/GenerateToken");
		String jsonString = response.asString();
		token = JsonPath.from(jsonString).get("token");
		System.out.println("TOKEN RECIEVED: " + token);
		return token;
	}

	public RequestSpecification requestSpecification() throws IOException {
		if (requestSpecification == null) {
			PrintStream log = new PrintStream(new FileOutputStream("logging.txt"));
			requestSpecification = new RequestSpecBuilder().setBaseUri(getBaseURI("BaseURI"))
					.addQueryParam("key", "qaclick123").addFilter(RequestLoggingFilter.logRequestTo(log))
					.addFilter(ResponseLoggingFilter.logResponseTo(log)).setContentType(ContentType.JSON).build();
			return requestSpecification;
		}
		return requestSpecification;
	}

	public static String getBaseURI(String key) {
		resourceBundle = ResourceBundle.getBundle("config");
		return resourceBundle.getString(key);
	}

	public String getJsonPath(Response response, String key) {
		JsonPath jsonPath = new JsonPath(response.asString());
		return jsonPath.get(key).toString();
	}

	public static String getRequestBody(String filePath) throws FileNotFoundException {
		FileReader fileReader = new FileReader(filePath);
		JSONTokener jsonTokener = new JSONTokener(fileReader);
		JSONObject data = new JSONObject(jsonTokener);
		return data.toString();
	}

}
