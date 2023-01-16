package jiraapi;
import org.testng.annotations.Test;

import files.JiraPayload;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import io.restassured.path.json.JsonPath;

public class JiraApi {
	
	String postResponse = null;
	String sessionID = null;
	String bugID = null;
	
	@Test(priority=1)
	public void authentication() {
		RestAssured.baseURI = "http://localhost:8080";
		postResponse = given().log().all().header("Content-Type", "application/json")
				       .body(JiraPayload.authentication())
				       .when().post("/rest/auth/1/session")
				       .then().log().all().assertThat().statusCode(200)
				       .extract().response().asString();
	}

	@Test(priority=2)
	public void createIssue() {
		JsonPath js = new JsonPath(postResponse);
		sessionID = js.getString("session.value");
		postResponse =	given().log().all()
					   .header("Content-Type", "application/json")
					   .header("Cookie", "JSESSIONID="+sessionID)
				       .body(JiraPayload.createIssue())
				       .when().post("/rest/api/2/issue")
				       .then().log().all().assertThat().statusCode(201)
				       .extract().response().asString();
	}
	
	@Test(priority=3)
	public void addComment() {
		JsonPath js = new JsonPath(postResponse);
		bugID = js.getString("id");
		postResponse =	given().log().all()
					   .header("Content-Type", "application/json")
					   .header("Cookie", "JSESSIONID="+sessionID)
					   .pathParam("issueIdOrKey", bugID)
				       .body(JiraPayload.addComment())
				       .when().post("/rest/api/2/issue/{issueIdOrKey}/comment")
				       .then().log().all().assertThat().statusCode(201)
				       .extract().response().asString();
	}
}
