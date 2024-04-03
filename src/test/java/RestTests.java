import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.testng.annotations.Test;
import static org.testng.Assert.*;



public class RestTests {

    String baseUrl = "https://datausa.io";
    public RequestSpecification httpRequest;
    public Response response;


  @Test
  public void verifyCorrectGETRequest(){
       httpRequest = RestAssured.given();
       response = httpRequest.get(baseUrl+"/api/data?"+TestData.drilldownsParameter+"&"+TestData.measuresParameter);
       assertEquals(response.getStatusCode(), 200);
       assertTrue(Helpers.jsonObjectString(response, "data[0]").contains("ID Nation=01000US"));
       assertEquals(Helpers.jsonObjectString(response, "data[0].Nation"), "United States");
       assertTrue(Helpers.jsonObjectString(response, "data[0]").contains("ID Year=2021"));
       assertEquals(Helpers.jsonObjectString(response, "data[0].Year"), "2021");
       assertEquals(Helpers.jsonObjectString(response, "data[0].Population"), "329725481");
       assertTrue(Helpers.jsonObjectString(response, "data[0]").contains("Slug Nation=united-states"));
       assertTrue(Helpers.jsonObjectString(response, "data[1]").contains("ID Nation=01000US"));
       assertEquals(Helpers.jsonObjectString(response, "data[1].Nation"), "United States");
       assertTrue(Helpers.jsonObjectString(response, "data[1]").contains("ID Year=2020"));
       assertEquals(Helpers.jsonObjectString(response, "data[1].Year"), "2020");
       assertEquals(Helpers.jsonObjectString(response, "data[1].Population"), "326569308");
       assertTrue(Helpers.jsonObjectString(response, "data[1]").contains("Slug Nation=united-states"));
       assertEquals(Helpers.jsonObjectString(response, "source[0].measures[0]"), "Population");
       assertEquals(Helpers.jsonObjectString(response, "source[0].annotations.source_name"), "Census Bureau");
       assertEquals(Helpers.jsonObjectString(response, "source[0].annotations.source_description"), "The American Community Survey (ACS) is conducted by the US Census and sent to a portion of the population every year.");
       assertEquals(Helpers.jsonObjectString(response, "source[0].annotations.dataset_name"), "ACS 5-year Estimate");
       assertEquals(Helpers.jsonObjectString(response, "source[0].annotations.dataset_link"), "http://www.census.gov/programs-surveys/acs/");
       assertEquals(Helpers.jsonObjectString(response, "source[0].annotations.table_id"), "B01003");
       assertEquals(Helpers.jsonObjectString(response, "source[0].annotations.topic"), "Diversity");
       assertEquals(Helpers.jsonObjectString(response, "source[0].annotations.subtopic"), "Demographics");
  }

  @Test
  public void verifyPOSTRequest(){
      httpRequest = RestAssured.given();
      response = httpRequest.post(baseUrl+"/api/data?"+TestData.drilldownsParameter+"&"+TestData.measuresParameter);
      assertEquals(response.getStatusCode(), 415);
      assertTrue(response.getBody().prettyPrint().contains("<title>Error</title>"));
  }

    @Test
    public void verifyPUTRequest(){
        httpRequest = RestAssured.given();
        response = httpRequest.put(baseUrl+"/api/data?"+TestData.drilldownsParameter+"&"+TestData.measuresParameter);
        assertEquals(response.getStatusCode(), 404);
        assertTrue(response.getBody().asString().contains("<title>Error</title>"));
        assertTrue(response.getBody().asString().contains("<pre>Cannot PUT /api/data</pre>"));
    }

    @Test
    public void verifyGETDrilldownsParameterRequest(){
        httpRequest = RestAssured.given();
        response = httpRequest.get(baseUrl+"/api/data?"+TestData.drilldownsParameter);
        assertEquals(response.getStatusCode(), 200); // zły status błąd aplikacji
        assertEquals(Helpers.jsonObjectString(response, "error"), "Query must contain at least one measure.");
    }

    @Test
    public void verifyGETMeasuresParameterRequest(){// Nie podając parametru populacji, zwracane są dane o populacji, do wyjaśnienia z BA :-).
        httpRequest = RestAssured.given();
        response = httpRequest.get(baseUrl+"/api/data?"+TestData.measuresParameter);
        assertEquals(response.getStatusCode(), 200);
        assertTrue(Helpers.jsonObjectString(response, "data[0]").contains("ID Year=2021"));
        assertEquals(Helpers.jsonObjectString(response, "data[0].Year"), "2021");
        assertEquals(Helpers.jsonObjectString(response, "data[0].Population"), "2560865057");
        assertTrue(Helpers.jsonObjectString(response, "data[1]").contains("ID Year=2020"));
        assertEquals(Helpers.jsonObjectString(response, "data[1].Year"), "2020");
        assertEquals(Helpers.jsonObjectString(response, "data[1].Population"), "2535804067");
        assertEquals(Helpers.jsonObjectString(response, "source[0].measures[0]"), "Population");
        assertEquals(Helpers.jsonObjectString(response, "source[0].annotations.source_name"), "Census Bureau");
        assertEquals(Helpers.jsonObjectString(response, "source[0].annotations.source_description"), "The American Community Survey (ACS) is conducted by the US Census and sent to a portion of the population every year.");
        assertEquals(Helpers.jsonObjectString(response, "source[0].annotations.dataset_name"), "ACS 5-year Estimate");
        assertEquals(Helpers.jsonObjectString(response, "source[0].annotations.dataset_link"), "http://www.census.gov/programs-surveys/acs/");
        assertEquals(Helpers.jsonObjectString(response, "source[0].annotations.table_id"), "B01003");
        assertEquals(Helpers.jsonObjectString(response, "source[0].annotations.topic"), "Diversity");
        assertEquals(Helpers.jsonObjectString(response, "source[0].annotations.subtopic"), "Demographics");
    }
}
