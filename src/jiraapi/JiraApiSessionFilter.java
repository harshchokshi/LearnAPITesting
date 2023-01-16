package jiraapi;

import static io.restassured.RestAssured.given;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.JiraPayload;
import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

public class JiraApiSessionFilter {
	String postResponse = null;
	String bugID = null;
	SessionFilter session = null;
	
	@Test(priority=1)
	public void authentication() {
		RestAssured.baseURI = "http://localhost:8080";
		session = new SessionFilter();
		postResponse = given().log().all()
				   	   .header("Content-Type", "application/json")
				       .body(JiraPayload.authentication())
				       .filter(session)
				       .when().post("/rest/auth/1/session")
				       .then().log().all()
				       .assertThat().statusCode(200)
				       .extract().response().asString();
	}

	@Test(priority=2)
	public void createIssue() {
		postResponse =	given().log().all()
				       .filter(session)
					   .header("Content-Type", "application/json")
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
				       .filter(session)
					   .header("Content-Type", "application/json")
					   .pathParam("issueIdOrKey", bugID)
				       .body(JiraPayload.addComment())
				       .when().post("/rest/api/2/issue/{issueIdOrKey}/comment")
				       .then().log().all().assertThat().statusCode(201)
				       .extract().response().asString();
	}
	
	@Test(priority=4)
	public void addAttachment() {
		postResponse =	given().log().all()
				       .filter(session)
					   .header("Content-Type", "multipart/form-data")
					   .header("X-Atlassian-Token", "no-check")
					   .pathParam("issueIdOrKey", bugID)
				       .multiPart("file", new File("Presentation Image.PNG"))
				       .when().post("/rest/api/2/issue/{issueIdOrKey}/attachments")
				       .then().log().all().assertThat().statusCode(200)
				       .extract().response().asString();
	}
	
	@DataProvider(name = "addComments")
	public Object[] getComments() {
		return new Object[] {"This is second comment.", "This is third comment."};
	}
	
	@Test(dataProvider = "addComments", priority=4)
	public void addMultipleComment(String comment) {
		postResponse =	given().log().all()
				       .filter(session)
					   .header("Content-Type", "application/json")
					   .pathParam("issueIdOrKey", bugID)
				       .body(JiraPayload.addCommentfromDataprovider(comment))
				       .when().post("/rest/api/2/issue/{issueIdOrKey}/comment")
				       .then().log().all().assertThat().statusCode(201)
				       .extract().response().asString();
	}
	
	@Test(priority=5)
	public void getIssueDetail() {
		postResponse = given().log().all()
				       .filter(session)
				       .pathParam("issueIdOrKey", bugID)
				       .when().get("/rest/api/2/issue/{issueIdOrKey}")
				       .then().log().all()
				       .assertThat().statusCode(200)
				       .extract().response().asString();
	}
	
	@Test(priority=6)
	public void filterIssuebyAttachment() {
					given().log().all()
			       .filter(session)
			       .pathParam("issueIdOrKey", bugID)
			       .queryParam("fields","attachment")
			       .when().get("/rest/api/2/issue/{issueIdOrKey}")
			       .then().log().all()
			       .assertThat().statusCode(200)
			       .extract().response().asString();
	}
	
	@Test(priority=7)
	public void verifyCommentSuccessfulladded() {
	String expectedComment = "This is second comment.";
	postResponse = given().log().all()
		         .filter(session)
		         .pathParam("issueIdOrKey", bugID)
		         .queryParam("fields", "comment")
		         .when().get("/rest/api/2/issue/{issueIdOrKey}")
		         .then().log().all()
		         .assertThat().statusCode(200)
		         .extract().response().asString();
	JsonPath js = new JsonPath(postResponse);
	String comments = "";
	int numOfComments = js.getInt(("fields.comment.comments.size()"));
	System.out.println(numOfComments);
	for (int i = 0; i < numOfComments; i++) {
		comments = js.getString("fields.comment.comments["+i+"].body");
		if (comments.equals(expectedComment)) {
			break;
		}
	}
		Assert.assertEquals(comments, expectedComment);
	}
	
	@Test(priority=8)
	public void handlingHTTPSCertificationvalidation() {
		RestAssured.baseURI = "http://localhost:8080";
		session = new SessionFilter();
		postResponse = given().relaxedHTTPSValidation().log().all()
				   	   .header("Content-Type", "application/json")
				       .body(JiraPayload.authentication())
				       .filter(session)
				       .when().post("/rest/auth/1/session")
				       .then().log().all()
				       .assertThat().statusCode(200)
				       .extract().response().asString();
	}
	
	
	
	
}
