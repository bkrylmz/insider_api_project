package com.insiderApi;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Get_Test {
    @BeforeAll
    public static void init() {
        baseURI = "https://petstore.swagger.io/v2";
    }

    @DisplayName("Positive testing GET request to /pet/findByStatus")
    @Test
    public void getMethod_queryParam() {
        given().accept(ContentType.JSON)
                .and()
                .queryParam("status", "sold")
                .when()
                .get("/pet/findByStatus")
                .then()
                .statusCode(200)
                .and()
                .contentType("application/json")
                .and()
                .body("status", everyItem(is("sold")))
                .log().all();


    }

    @DisplayName("Negative testing GET request to /pet/findByStatus")
    @Test
    //as a value "ready" is not written in swagger document for queryParam. You can not get any info in response the body,
    public void getMethod_queryParam2() {
        given().accept(ContentType.JSON)
                .and().
                queryParam("status", "ready")
                .when()
                .get("/pet/findByStatus")
                .then()
                .statusCode(not(equalTo(200))) // because according to swagger pet document "available" "pending" "sold" parameters are only the available values so the status can not be 200
                .and()
                .contentType("application/json")
                .log().all();

    }

    @DisplayName("Positive testing GET request to /pet/{petID}")
    @Test
    public void getMethod_pathParam() {
        Response response = given().accept(ContentType.JSON)
                .and().pathParam("petId", 157246)
                .when()
                .get("/pet/{petId}");

        JsonPath jsonPath = response.jsonPath(); //you are putting the response body to jsonPath Object
        assertEquals(200, response.statusCode());
        assertEquals("application/json", response.contentType());
        assertEquals(157246, jsonPath.getInt("id"));

    }


    @DisplayName("Negative testing GET request to /pet/{petID}")
    @Test // there is no pet wit petID 250000000
    public void getMethod_pathParam2() {
        Response response = given().accept(ContentType.JSON)
                .and().pathParam("petId", 250000000)
                .when()
                .get("/pet/{petId}");

        assertEquals(404, response.statusCode());
        assertEquals("application/json", response.contentType());

        JsonPath jsonPath = response.jsonPath();
        assertEquals("error", jsonPath.getString("type"));

    }

}
