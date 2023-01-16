package learnrestapi;
import static io.restassured.RestAssured.given;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import files.Payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class DynamicJSON {
	
	String id = null;
	
	@DataProvider(name = "Bookdata")
	public Object[][] getData()
	{
		return new Object[][] {{"1223", "abbc"},{"4556", "dedf"},{"7889", "gghi"}};
	}

	@Test(dataProvider = "Bookdata")
	public void addBook(String aisle, String isbn) {
		RestAssured.baseURI="http://216.10.245.166";
		String response = given().log().all().header("Content-Type", "application/json")
		.body(Payload.addBook(aisle, isbn))
		.when().post("/Library/Addbook.php")
		.then().log().all().assertThat().statusCode(200)
		.extract().response().asString();
		JsonPath js = ReUsableMethods.rawToJson(response);
		id = js.get("ID");
		getBook();
		deleteBook();
	}
	
	public void getBook() {
		System.out.println("\n"+"//Get Book"+"\n");
		RestAssured.baseURI="http://216.10.245.166";
		given().log().all().queryParam("ID", id).header("Content-Type", "application/json")
		.when().get("Library/GetBook.php")
		.then().log().all().assertThat().statusCode(200)
		.extract().response().asString();
	}
	
	public void deleteBook()
	{
		System.out.println("\n"+"//Delete Book"+"\n");
		RestAssured.baseURI="http://216.10.245.166";
		given().log().all().header("Content-Type", "application/json")
		.body(Payload.deleteBook(id))
		.when().post("/Library/DeleteBook.php")
		.then().log().all().assertThat().statusCode(200)
		.extract().response().asString();
	}
	
	public void addBookWithoutDataProvider() {
		System.out.println("\n"+"//Add Book"+"\n");
		RestAssured.baseURI="http://216.10.245.166";
		String response = given().log().all().header("Content-Type", "application/json")
		.body(Payload.addBook("1777", "lopu"))
		.when().post("/Library/Addbook.php")
		.then().log().all().assertThat().statusCode(200)
		.extract().response().asString();
		JsonPath js = ReUsableMethods.rawToJson(response);
	    id = js.get("ID");
	}
	
}
