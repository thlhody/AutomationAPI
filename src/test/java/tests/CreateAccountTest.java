package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import objectData.request.RequestAccount;
import objectData.response.ResponseAccountSucces;
import objectData.response.ResponseTokenSucces;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import propertiesUtility.PropertyUtilitiy;

public class CreateAccountTest {
    public RequestAccount requestAccountBody;
    public String token;
    public String userID;

    @Test
    public void testMethod() {
        System.out.println("===STEP 1: CREATE NEW ACCOUNT");
        createAccount();
        System.out.println("===STEP 2: GENERATE TOKEN");
        generateToken();
        System.out.println("===STEP 3: CHECK ACCOUNT");
        checkAccountExists();
        System.out.println("===STEP 4: DELETE USER");
        deleteUser();
        System.out.println("===STEP 5: CHECK ACCOUNT");
        checkAccountExists();
    }

    public void createAccount() {
        //configuram clientul
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://demoqa.com");
        requestSpecification.contentType("application/json");
        PropertyUtilitiy propertyUtilitiy = new PropertyUtilitiy("request/CreateAccountData");

        requestAccountBody = new RequestAccount(propertyUtilitiy.getAllData());

        //executam requestul
        requestSpecification.body(requestAccountBody);
        Response response = requestSpecification.post("/Account/v1/User");

        //validam response
        Assert.assertTrue(response.getStatusLine().contains("201"));
        Assert.assertTrue(response.getStatusLine().contains("Created"));

        ResponseAccountSucces responseAccountSucces = response.body().as(ResponseAccountSucces.class);

        //extrag user id
        Assert.assertTrue(responseAccountSucces.getUsername().equals(requestAccountBody.getUserName()));
        System.out.println("User ID: " + responseAccountSucces.getUserID());
        userID = responseAccountSucces.getUserID();

    }

    public void generateToken() {
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://demoqa.com");
        requestSpecification.contentType("application/json");

        //executam requestul de generare Token
        requestSpecification.body(requestAccountBody);
        Response responseToken = requestSpecification.post("/Account/v1/GenerateToken");

        //validam response
        Assert.assertTrue(responseToken.getStatusLine().contains("200"));
        Assert.assertTrue(responseToken.getStatusLine().contains("OK"));

        //extrag user id
        ResponseTokenSucces responseTokenSucces = responseToken.body().as(ResponseTokenSucces.class);
        token = responseTokenSucces.getToken();
        Assert.assertEquals(responseTokenSucces.getStatus(), "Success");
        Assert.assertEquals(responseTokenSucces.getResult(), "User authorized successfully.");
        System.out.println("Token: " + responseTokenSucces.getToken());

    }

    public void checkAccountExists() {
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://demoqa.com");
        requestSpecification.contentType("application/json");

        //ne autorizam pe baza la token
        requestSpecification.header("Authorization", "Bearer " + token);
        Response response = requestSpecification.get("/Account/v1/User/" + userID);

        if(response.getStatusLine().contains("200")) {
            Assert.assertTrue(response.getStatusLine().contains("200"));
            Assert.assertTrue(response.getStatusLine().contains("OK"));
            System.out.println(response.getStatusCode());
        } else {
            Assert.assertTrue(response.getStatusLine().contains("401"));
            Assert.assertTrue(response.getStatusLine().contains("Unauthorized"));
            System.out.println(response.getStatusCode());
        }
    }

    public void deleteUser(){
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://demoqa.com");
        requestSpecification.contentType("application/json");

        //ne autorizam pe baza la token
        requestSpecification.header("Authorization", "Bearer " + token);
        Response response = requestSpecification.delete("/Account/v1/User/" + userID);
        System.out.println(response.getStatusLine());
        System.out.println(response.getStatusCode());
        Assert.assertTrue(response.getStatusLine().contains("204"));
        Assert.assertTrue(response.getStatusLine().contains("No Content"));

    }
}
