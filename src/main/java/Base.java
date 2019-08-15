
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

public class Base {

    public static final String BASE_URI= "http://api.apixu.com";
    public static final String BASE_PATH= "/v1";
    public static final String SEARCH_ENDPOINT= "/search.json";
    public static final String CURRENT_TEMP_ENDPOINT= "/current.json";
    public static final String FORECAST_ENDPOINT= "/forecast.json";
    protected static final String KEY = "68397af818104cd8afc131820191208";
    public Response response;
    public List<Double> heatIndexList = new ArrayList<Double>();
    public List<Double> avgtempList_c = new ArrayList<Double>();
    int temperature_c;
    int humidity;
    double index_c;
    double standardDeviation;

    @BeforeTest
     public static void init(){
        RestAssured.baseURI=BASE_URI;
        RestAssured.basePath=BASE_PATH;

    }
    @BeforeMethod
    public void beforeMethod() {
        response = null;
    }

    @DataProvider(name="City")
    public Object[][] data(){
        return new Object[][]{
                {"delhi"},
                {"chennai"}
        };
    }
}
