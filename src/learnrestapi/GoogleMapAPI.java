package learnrestapi;

import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.annotations.Test;

import files.Payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

public class GoogleMapAPI {
	
	String baseURI = "https://rahulshettyacademy.com";
	String addPlaceResource = "/maps/api/place/add/json";
	String getPlaceResource = "/maps/api/place/get/json";
	String deletePlaceResource = "/maps/api/place/delete/json";
	String postresponse = null;
	String placeId = null;
	String file = "C:\\Users\\Harsh Chokshi\\OneDrive\\Software Testing Course\\Learn API\\Udemy Course\\Section 7\\addPlace.json";

	
	public void addPlace() {
		RestAssured.baseURI = baseURI;
		postresponse = given().log().all().queryParam("key", "qaclick123").header("Content-type", "application/json")
		.body(Payload.addPlace())
		.when().post(addPlaceResource)
		 .then().log().all().statusCode(200).body("scope", equalTo("APP")).header("Server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();
	}
	
	
	public String getFile() {
		try {
			return new String (Files.readAllBytes(Paths.get(file)));
		} catch (IOException e) {
			return "Invalid file.";
		}		
	}

	@Test(priority=1)
	public void addPlaceFromFile()  {
		System.out.println("\n"+"//Add Place from file."+"\n");
		RestAssured.baseURI = baseURI;
		postresponse = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body(getFile())
		.when().post(addPlaceResource)
		.then().log().all().statusCode(200).body("scope", equalTo("APP")).header("Server", "Apache/2.4.41 (Ubuntu)")
		.extract().response().asString();
	}
	
	@Test(priority=2)
	public void getPlace() {
		System.out.println("\n"+"//Get added place."+"\n");
		RestAssured.baseURI = baseURI;
		JsonPath js = new JsonPath(postresponse);
		placeId = js.getString("place_id");
		given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId).header("Content-Type", "application/json")
		.when().get(getPlaceResource)
		.then().log().all().statusCode(200)
		.extract().response().asString();
		
	}
	
	@Test(priority=3)
	public void deletePlace() {
		System.out.println("\n"+"//Delete added place."+"\n");
		RestAssured.baseURI = baseURI;
		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body(Payload.deletePlace(placeId))
		.when().delete(deletePlaceResource)
		.then().log().all().assertThat().statusCode(200)
		.extract().response().asString();
	}
	

	
}
	