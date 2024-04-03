import io.restassured.specification.RequestSpecification;
import io.restassured.response.Response;

public class Helpers {

    public static String jsonObjectString(Response response, String path){
        return  response.jsonPath().getJsonObject(path).toString();
    }
}
