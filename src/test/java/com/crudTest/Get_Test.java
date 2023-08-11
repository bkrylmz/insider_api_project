package com.crudTest;

import com.utilities.TestBase;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.List;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class Get_Test extends TestBase {

    @DisplayName("Positive testing GET request to /pet/findByStatus")
    @ParameterizedTest
    @ValueSource(strings = {"available","pending","sold"})
    public void getMethod_queryParam(String s) {
        Response response = given().accept(ContentType.JSON)
                .and().queryParam("status", s)
                .log().all()
                .when()
                .get("/findByStatus");

        assertEquals(200, response.statusCode());
        assertEquals("application/json", response.header("Content-Type"));
        List<String> status = response.path("status");
        for (String stat : status) {
            assertEquals(s,stat);
        }
        response.prettyPrint();
    }

    @DisplayName("Positive testing status GET request to /pet/findByStatus")
    @Test
    public void get_queryParam() {
        given().accept(ContentType.JSON)
                .and()
                .queryParam("status", "sold")
                .when()
                .get("/findByStatus")
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
                .get("/findByStatus")
                .then()
                .statusCode(not(equalTo(200)))
                // According to the swagger pet document, available values for status are "available", "pending" and "sold".
                // "ready" is invalid status value, so the status code can not be 200, but status code is 200
                // because of that reason our test failed. There is a bug
                .and()
                .contentType("application/json");
    }

    @DisplayName("Positive testing GET request to /pet/{petID}")
    @Test
    public void get_pathParam1() {
        Response response = given().accept(ContentType.JSON)
                .and().pathParam("petId", 456765)
                .when()
                .get("/{petId}");

        JsonPath jsonPath = response.jsonPath(); //you are putting the response body to jsonPath Object
        assertEquals(200, response.statusCode());
        assertEquals("application/json", response.contentType());
        assertEquals(456765, jsonPath.getInt("id"));

    }


    @DisplayName("Negative testing GET request to /pet/{petID}")
    @Test // there is no pet wit petID 250000000
    public void getMethod_pathParam2() {
        Response response = given().accept(ContentType.JSON)
                .and().pathParam("petId", 250000000)
                .when()
                .get("/{petId}");

        assertEquals(404, response.statusCode());
        assertEquals("application/json", response.contentType());

        JsonPath jsonPath = response.jsonPath();
        assertEquals("error", jsonPath.getString("type"));

    }

    @DisplayName("Negative testing GET request to /pet/{petID} invalid id")
    @Test // there is a bug according to the swagger,
    // we should get 400 for invalid id (in this test we use bigger than int64)
    // we get 404
    public void getMethod_pathParam3() {
        Response response = given().accept(ContentType.JSON)
                .and().pathParam("petId", 1234567890123456L)
                .when()
                .get("/{petId}");
        assertEquals(400, response.statusCode());
        assertEquals("application/json", response.contentType());

    }


}
