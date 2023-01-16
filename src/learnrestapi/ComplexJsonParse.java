package learnrestapi;

import files.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {
	
	public static void main(String[] args) {
	
		//Test case - Print number of courses return by API
		JsonPath js = new JsonPath(Payload.coursePrice());
		//How to get number of items in an array? Answer. Using line below:
		int numberOfCourses = js.getInt("courses.size()");
		System.out.println(numberOfCourses);
		//Test case - Print purchase amount
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println(purchaseAmount);
		//Test case - Print Title of the first course
		String firstCourseTitle = js.getString("courses[0].title");
		//Notes - Get method by default pull of the string. get method would also work.
		System.out.println(firstCourseTitle);
		//Test case - Print Title of the third  course
		String thirdCourseTitle = js.getString("courses[2].title");
		System.out.println(thirdCourseTitle);
		//Test case - Print Title and price of second course
		String secondCourseTitle = js.get("courses[1].title");
		int secondCoursePrice = js.getInt("courses[1].price");
		System.out.println("Second Course Title: "+secondCourseTitle+"\n"+"Second Course Price: "+secondCoursePrice);
		//Test case - Print all course titles and their respective prices
		for (int i = 0; i < numberOfCourses; i++) {
			String courseTitle = js.getString("courses["+i+"].title");
			int coursePrice = js.getInt("courses["+i+"].price");
			//System.out.println(js.get("courses["+i+"].price").toString());
			System.out.println(i+"."+"Course Title: "+courseTitle+"\n"+"Course Price: "+coursePrice);
		}
		//Test case - Print number of copies sold by RPA course
		for (int i = 0; i < numberOfCourses; i++)
		{
			String courseTitle = js.getString("courses["+i+"].title");
			if (courseTitle.equalsIgnoreCase("RPA")) {
				System.out.println("Number of copies sold by RPA Cous "+js.getInt("courses["+i+"].copies"));	
				break;
			}
		}
		
		//Test case - Verify if Sum of all Course prices matches with Purchase Amount
		
		int sumOfAllCoursePrice = 0;
		for (int i = 0; i < numberOfCourses; i++) {
			String courseTitle = js.getString("courses["+i+"].title");
			int coursePrice = js.getInt("courses["+i+"].price");
			int copies = js.getInt("courses["+i+"].copies");
			int coursePricebyCopies = coursePrice*copies;
			sumOfAllCoursePrice = sumOfAllCoursePrice + coursePricebyCopies;
		}
		
		System.out.println("Sum of all course price: "+sumOfAllCoursePrice);
		
		 if (sumOfAllCoursePrice==purchaseAmount) {
			 System.out.println("Sum of all course prices matches with purchase amount.");
		 } else {
			 System.out.println("Sum of all course prices do not match with purchase amount. ");
			 
		 }
		
		
		
		
		
		
		
		
	}

}
