package tests;

import com.github.javafaker.Faker;
import config.Config;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import listeners.RetryAnalyzer;
import listeners.TestListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import user.UserCreate;
import user.UserLocation;
import user.UserResponse;
import utils.Constants;

import java.util.Locale;

import static io.restassured.RestAssured.given;
@Listeners(TestListener.class)
public class UserTests extends Config{

    private static final Logger logger = LogManager.getLogger(UserTests.class);
    SoftAssert softAssert;

    @BeforeMethod(alwaysRun = true)
    public void setUp(){
        softAssert = new SoftAssert();
    }


    @Test(groups = "smoke", description = "Get all users, expected: List of users is returned")
    public void getAllUsersTest(){

        Response response = given()
                .queryParam("page", "1")
                .queryParam("limit", "50")
                .when().get(Constants.GET_ALL_USERS);

        int actualStatusCode = response.statusCode();
        softAssert.assertEquals(actualStatusCode, 200, "Expected status code is 200 but got: " + actualStatusCode);

        String actualLastName = response.jsonPath().get("data[0].lastName");
        logger.info("Actual last name:" + actualLastName);

        softAssert.assertEquals(actualLastName, "Calvo", "Expected last name is Robert but found: " + actualLastName);
        softAssert.assertAll();
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void getAllUsersUsingJsonPathObjectTest(){
        JsonPath jsonPath = given()
                .queryParam("page", "1")
                .queryParam("limit", "50")
                .when().get(Constants.GET_ALL_USERS).jsonPath();

        String actualLastName = jsonPath.get("data[0].lastName");
        logger.info("Actual last name:" + actualLastName);

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

        UserCreate user = UserCreate.createUser();

        UserResponse userResponse = given()
                .body(user)
                .when().post(Constants.CREATE_USER).getBody().as(UserResponse.class);

        String userId = userResponse.getId();

        Response response = given()
                .pathParam("id", userId)
                .when().delete(Constants.DELETE_USER);

        int actualStatusCode = response.statusCode();
        Assert.assertEquals(actualStatusCode, 200, "Expected status code is 200 but got: " + actualStatusCode);

        String actualUserId = response.jsonPath().get("id");
        Assert.assertEquals(actualUserId, userId, "Expected userId is " + userId + "but found: " + actualUserId);
    }

    @Test
    public void createUserTest(){
        String body = "{\n" +
                "    \"firstName\": \"Test\",\n" +
                "    \"lastName\": \"User\",\n" +
                "    \"email\": \"test343@user.com\",\n" +
                "    \"gender\": \"female\",\n" +
                "    \"phone\": \"1-770-736-8031\",\n" +
                "    \"location\": {\n" +
                "        \"street\": \"test\",\n" +
                "        \"city\": \"city\",\n" +
                "        \"state\": \"test\",\n" +
                "        \"country\": \"test\"\n" +
                "    }\n" +
                "}";

        given()
                .body(body)
                .log().all()
                .when().post(Constants.CREATE_USER)
                .then().log().all();

    }

    @Test
    public void createUserUsingJavaObjectTest(){
        Faker faker = new Faker(new Locale("en-US"));

        UserLocation location = UserLocation.builder()
                .city(faker.address().city())
                .state(faker.address().state())
                .street(faker.address().streetAddress())
                .country(faker.address().country())
                .build();

        UserCreate user = UserCreate.builder()
                .email(faker.internet().emailAddress())
                .phone(faker.phoneNumber().phoneNumber())
                .first_name(faker.name().firstName())
                .lastName(faker.name().lastName())
                .userLocation(location)
                .build();

        //UserCreate user = UserCreate.createUser();

        UserResponse userResponse = given()
                .body(user)
                .when().post(Constants.CREATE_USER).getBody().as(UserResponse.class);

        softAssert.assertEquals(userResponse.getEmail(), user.getEmail());
        softAssert.assertEquals(userResponse.getFirstName(), user.getFirst_name());
        softAssert.assertEquals(userResponse.getLastName(), user.getLastName());
        softAssert.assertEquals(userResponse.getLocation().getStreet(), user.getUserLocation().getStreet());
        softAssert.assertAll();

    }

    @Test
    public void updateUserTest(){
        UserCreate user = UserCreate.createUser();

        UserResponse userResponse = given()
                .body(user)
                .when().post(Constants.CREATE_USER).getBody().as(UserResponse.class);

        String updatedFirstName = "updatedFirstName";
        String updatedEmail = "updatedEmail";
        String updatedCity = "updatedCity";

        //Prvi nacin za update podataka za slanje u body-ju
//        user.setEmail(updatedEmail);
//        user.setFirst_name(updatedFirstName);
//        user.getUserLocation().setCity(updatedCity);

        //Drugi nacin za update podataka za slanje u body-ju
        UserCreate updatedUser = user
                .withEmail(updatedEmail)
                .withFirst_name(updatedFirstName)
                .withUserLocation(user.getUserLocation().withCity(updatedCity));

        String userId = userResponse.getId();

        UserResponse updatedUserResponse = given()
                .body(updatedUser)
                .pathParam("id", userId)
                .when().put(Constants.UPDATE_USER).getBody().as(UserResponse.class);

        boolean isUserNameUpdated = updatedUserResponse.getFirstName().equals(updatedFirstName);

        softAssert.assertTrue(isUserNameUpdated, "");
        //softAssert.assertEquals(updatedUserResponse.getEmail(), updatedEmail);//ne moze da se update-uje
        softAssert.assertEquals(updatedUserResponse.getLocation().getCity(), updatedCity);
        softAssert.assertAll();

    }



}
