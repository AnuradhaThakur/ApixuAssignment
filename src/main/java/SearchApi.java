//import com.fasterxml.jackson.databind.util.JSONPObject;
//import com.jayway.restassured.RestAssured;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
//
//public class SearchApi {
//
//    public static final String BASE_URI= "http://api.apixu.com";
//    public static final String BASE_PATH= "/v1";
//    public static final String SEARCH_ENDPOINT= "/search.json";
//    private static final String KEY = "68397af818104cd8afc131820191208";
//    public String response;
//
//
//    @BeforeClass
//    public static void init(){
//        RestAssured.baseURI=BASE_URI;
//        RestAssured.basePath=BASE_PATH;
//
//    }
//    @BeforeMethod
//    public void beforeMethod() {
//        response = null;
//    }
//
//    @Test(description = "get suggestions for keyword Delhi and get climate details for any random suggestion from the list")
//    public void getSuggestion(){
//
//    }
//}
