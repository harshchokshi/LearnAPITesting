package learnrestapi;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.testng.Assert;

import files.Payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class Basics {

	public static void main(String[] args) {
		
		//Validate if Add place API is working as expected
		
		//Given - All input details 
		//When  - Submit API resource HTTP method
		//Then -  Validate the response
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body(Payload.addPlace()).when().post("maps/api/place/add/json")
		 .then().statusCode(200).body("scope", equalTo("APP")).header("Server", "Apache/2.4.41 (Ubuntu)").extract().response().asString()
		 ;
		
		 System.out.println(response);
		 
		 JsonPath js = new JsonPath(response);
		 String placeId = js.getString("place_id");
		 
		 System.out.println(placeId);
		 
		 //Update Place
		
			
			//Add Place -> Update place with new address -> Get Place to validate if present new address is present in response.
		 String newAddress = "70 Jacksonville, USA";
		 given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		 .body("{\r\n"
		 		+ "\"place_id\":\""+placeId+"\",\r\n"
		 		+ "\"address\":\""+newAddress+"\",\r\n"
		 		+ "\"key\":\"qaclick123\"\r\n"
		 		+ "}").when().put("/maps/api/place/update/json")
		 .then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));
		 
		 //Get Place
		 
		 String updateResponse= given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
		 .when().get("maps/api/place/get/json").then().assertThat().log().all().statusCode(200).extract().response().asString()
		 ;
		
	    JsonPath jw = ReUsableMethods.rawToJson(updateResponse);
		String getnewAddres = jw.get("address");
		System.out.println(getnewAddres);
		Assert.assertEquals(newAddress, getnewAddres);
		

	}

}
