/***
 @author Anuradha Thakur
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.*;

public class ApiTest{

    public static final String BASE_URI= "http://api.apixu.com";
    public static final String BASE_PATH= "/v1";
    public static final String SEARCH_ENDPOINT= "/search.json";
    public static final String CURRENT_TEMP_ENDPOINT= "/current.json";
    public static final String FORECAST_ENDPOINT= "/forecast.json";
    private static final String KEY = "68397af818104cd8afc131820191208";
    public Response response;
    public List<Double> heatIndexList = new ArrayList<Double>();
    public List<Double> avgtempList_c = new ArrayList<Double>();
    int temperature_c;
    int humidity;
    double index_c;
    double standardDeviation;


    @BeforeClass
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

    /* 1. Calculate and sort in ascending order w.r.t Heat Index of Delhi and Chennai using given formula.
       Display the result in console or save in a file.
    */
    @Test(dataProvider = "City", description = "Get the current temparature details of a city",priority = 1)
    public void getCurrentTemp(String city){

        Map<String,Integer> currentMap;
        response = RestAssured.given().header("Content-Type", ContentType.JSON)
                .param("key", KEY)
                .param("q", city)
                .when()
                .get(BASE_URI+BASE_PATH+CURRENT_TEMP_ENDPOINT);
        Assert.assertEquals(response.statusCode(),200);

        JsonPath jsonPathEvaluator= response.body().jsonPath();
        currentMap = jsonPathEvaluator.get("current");
            humidity =currentMap.get("humidity");
            temperature_c = ((Number)currentMap.get("temp_c")).intValue();
            index_c = -42.379+(2.0490153*temperature_c)+(10.14333127*humidity)-(0.22475541*temperature_c*humidity)-(6.83783*Math.pow(10,-3)*temperature_c*temperature_c)
                    -(5.481717*Math.pow(10,-2)*humidity*humidity)+(1.22874*Math.pow(10,-3)*temperature_c*temperature_c*humidity)+(8.5282*Math.pow(10,-4)*temperature_c*humidity*humidity)
                    -(1.99*Math.pow(10,-6)*temperature_c*temperature_c*humidity*humidity);
        heatIndexList.add(index_c);


    }

    @Test(dependsOnMethods = {"getCurrentTemp"},priority = 1)
    public void getMaxHeatIndex(){
        System.out.println("=======================SOLUTION 1==========================="+"\n");
        Collections.sort(heatIndexList);
        System.out.println("Sorted Heat index List: "+ heatIndexList);
        System.out.println("Maximum value of Heat index list: " +Collections.max(heatIndexList));
    }

    /* 2. Calculate the Standard Deviation in temperature of Bengaluru for upcoming week
      starting from tomorrow(w.r.t the date to receiving assignment). And display the result in console/ file.
    */
    @Test(description = "Get the weather forecast for Bengaluru for next 10 days", priority = 2)
    public void getWeatherForecast(){
        Map<String, List> ForcastMap;
        response = RestAssured.given().header("Content-Type", ContentType.JSON)
                .param("key", KEY)
                .param("q", "Bengaluru")
                .param("days", "7")
                .when()
                .get(BASE_URI+BASE_PATH+FORECAST_ENDPOINT);
        Assert.assertEquals(response.statusCode(),200);
        JsonPath jsonPathEvaluator= response.body().jsonPath();
        ForcastMap = jsonPathEvaluator.get("forecast");
        ArrayList forecastday = (ArrayList) ForcastMap.get("forecastday");

        for (Object item : forecastday){
            ObjectMapper obj =new ObjectMapper();
            Map day = obj.convertValue(item,Map.class);
            Object dayDataObject = day.get("day");
            Map dayDataMap =obj.convertValue(dayDataObject,Map.class);
            avgtempList_c.add(Double.parseDouble(dayDataMap.get("avgtemp_c").toString()));
        }
    }
    @Test(dependsOnMethods = {"getWeatherForecast"}, description = "Calculate Standard Deviation for forcasted weather", priority = 2)
    public void calculateSD()
    {
        double sum = 0.0, sd = 0.0;
        int length = avgtempList_c.size();
        for(double num : avgtempList_c) {
            sum += num;
        }
        double mean = sum/length;
        for(double num: avgtempList_c) {
            sd += Math.pow(num - mean, 2);
        }
        standardDeviation= Math.sqrt(sd/length);
        System.out.println("=======================SOLUTION 2==========================="+"\n");
        System.out.println("Standard Deviation for forcasted temperature is: "+ standardDeviation);
    }

    /* 3. Navigate to https://www.apixu.com/api-explorer.aspx, and get suggestions for keyword
      “Delhi”. From the results, select any(randomly) and get climate details for that suggestion(listed from previous list).
    */
    @Test(description = "get suggestions for keyword Delhi and get climate details for any random suggestion from the list", priority = 3)
    public void getSuggestion() {

        response = RestAssured.given()
                .header("Content-Type", ContentType.JSON)
                .param("key", KEY)
                .param("q", "delhi")
                .when()
                .get(BASE_URI + BASE_PATH + SEARCH_ENDPOINT);

        Assert.assertEquals(response.statusCode(),200);

        JsonPath jsonPathEvaluator = response.jsonPath();
        List<String> region = jsonPathEvaluator.getList("region");
        int size = region.size();
        int i = (int)new Random().nextInt(size);
        String city = region.get(i);

        response = RestAssured.given().header("Content-Type", ContentType.JSON)
                .param("key", KEY)
                .param("q", city)
                .when()
                .get(BASE_URI + BASE_PATH + CURRENT_TEMP_ENDPOINT);
        System.out.println("=======================SOLUTION 3==========================="+"\n");
        System.out.println("Climate Details of the Region:"+response.prettyPrint());

    }

}
