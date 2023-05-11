package tests;

import config.Config;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.Constants;

import static io.restassured.RestAssured.given;

public class UserTests extends Config {


    @Test()
    public void getAllUsersTest(){

        Response response = given()
                .queryParam("page", "1")
                .queryParam("limit", "50")
                .when().get(Constants.GET_ALL_USERS);

        int actualStatusCode = response.statusCode();
        Assert.assertEquals(actualStatusCode, 200, "Expected status code is 200 but got: " + actualStatusCode);

        String actualLastName = response.jsonPath().get("data[0].lastName");
        System.out.println("Actual last name:" + actualLastName);

        Assert.assertEquals(actualLastName, "Robert", "Expected last name is Robert but found: " + actualLastName);

    }

    @Test
    public void getAllUsersUsingJsonPathObjectTest(){
        JsonPath jsonPath = given()
                .queryParam("page", "1")
                .queryParam("limit", "50")
                .when().get(Constants.GET_ALL_USERS).jsonPath();

        String actualLastName = jsonPath.get("data[0].lastName");
        System.out.println("Actual last name:" + actualLastName);

        Assert.assertEquals(actualLastName, "Robert", "Expected last name is Robert but found: " + actualLastName);

    }

    @Test
    public void getUserByIdTest(){

        Response response = given()
                .pathParam("id", "60d0fe4f5311236168a109fe")
                .when().get(Constants.GET_USER_BY_ID);

        int actualStatusCode = response.statusCode();
        Assert.assertEquals(actualStatusCode, 200, "Expected status code is 200 but got: " + actualStatusCode);

        String actualLastName = response.jsonPath().get("lastName");
        System.out.println("Actual last name:" + actualLastName);

        Assert.assertEquals(actualLastName, "Robert", "Expected last name is Robert but found: " + actualLastName);
    }

    @Test
    public void deleteUserTest(){

        String userId = "60d0fe4f5311236168a109fe";
        Response response = given()
                .pathParam("id", userId)
                .when().delete(Constants.DELETE_USER);

        int actualStatusCode = response.statusCode();
        Assert.assertEquals(actualStatusCode, 200, "Expected status code is 200 but got: " + actualStatusCode);

        String actualUserId = response.jsonPath().get("id");
        Assert.assertEquals(actualUserId, userId, "Expected userId is " + userId + "but found: " + actualUserId);
    }



}
