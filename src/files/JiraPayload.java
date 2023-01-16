package files;

public class JiraPayload {

	public static String authentication()
	{
		return "{ \"username\": \"harsh.choksi1\", \"password\": \"Admin@12345\" }";
	}

	public static String createIssue()
	{
		return "{\r\n"
				+ "    \"fields\": {       \r\n"
				+ "	\"project\": \r\n"
				+ "        {\r\n"
				+ "            \"key\": \"RES\"\r\n"
				+ "        },\r\n"
				+ "	\"summary\": \"Credit Card Defect\",\r\n"
				+ "        \"issuetype\": \r\n"
				+ "	{\r\n"
				+ "            \"name\": \"Bug\"\r\n"
				+ "        }\r\n"
				+ "      }\r\n"
				+ "}";
	}
	
	public static String addComment() {
		return "\r\n"
				+ "{\r\n"
				+ "    \"body\": \"Hey I have commented from RestAPI.\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}";
	}
	
	public static String addCommentfromDataprovider(String comment) {
		return "\r\n"
				+ "{\r\n"
				+ "    \"body\": \""+comment+"\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}";
	}
}
