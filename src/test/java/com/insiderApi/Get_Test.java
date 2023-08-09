package com.insiderApi;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
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
        Response response = given().accept(ContentType.JSON)
                .and().queryParam("status", "sold")
                .log().all()
                .when()
                .get("/pet/findByStatus");

        assertEquals(200, response.statusCode());
        assertEquals("application/json", response.header("Content-Type"));
        assertTrue(response.body().asString().contains("sold"));
        response.prettyPrint();
    }

    @DisplayName("Negative testing GET request to /pet/findByStatus")
    @Test //as a value "ready" is not written in swagger document for queryParam. You can not get any info in response the body,
    public void getMethod_queryParam2() {
        Response response = given().accept(ContentType.JSON)
                .and().queryParam("status", "ready")
                .log().all()
                .when()
                .get("/pet/findByStatus");

        assertEquals(200, response.statusCode());
        assertEquals("application/json", response.header("Content-Type"));
        assertTrue(response.body().asString().contains(""));
        response.prettyPrint();
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

        JsonPath jsonPath = response.jsonPath(); //you are putting the response body to jsonPath Object
        assertEquals(404, response.statusCode());
        assertEquals("application/json", response.contentType());
        assertEquals("error", jsonPath.getString("type"));

    }

}
