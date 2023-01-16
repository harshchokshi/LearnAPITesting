package learnrestapi;

import org.testng.Assert;
import org.testng.annotations.Test;

import files.Payload;
import io.restassured.path.json.JsonPath;

public class SumValidation {
	
	@Test
	public void sumOfCourses() {
		
		int sum = 0;
		JsonPath js = new JsonPath(Payload.coursePrice());
		int numberOfCourses = js.getInt("courses.size()");
		for (int i = 0; i < numberOfCourses ; i++) {
			int price  =  js.getInt("courses["+i+"].price");
			int copies =  js.getInt("courses["+i+"].copies");
			int amount =  price * copies;
			sum = sum + amount;
		}
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
	    Assert.assertEquals(sum, purchaseAmount);
	  
	}

}
