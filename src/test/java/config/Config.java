package config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;

import java.util.HashMap;
import java.util.Map;

public class Config {


    public String getToken(){
        //request to get token
        return "access token";
    }

    @BeforeClass(alwaysRun = true)
    public void setup(){
        Map<String, String> headers = new HashMap<>();
        headers.put("app-id", "628e9aa43e2c54581058e8aa");
        headers.put("Accept", "application/json");
        headers.put("Authorization", getToken());

        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setRelaxedHTTPSValidation()
                .setBaseUri("https://dummyapi.io/data")
                .setBasePath("/v1/")
                .addHeaders(headers)
                .setContentType(ContentType.JSON)
                .build();

        ResponseSpecification responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();

        RestAssured.requestSpecification = requestSpecification;
        RestAssured.responseSpecification = responseSpecification;
    }



}
