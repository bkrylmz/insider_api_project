package com.crudTest;

import com.utilities.TestBase;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class Get_Test extends TestBase {

    @DisplayName("Positive testing1 GET    /pet/findByStatus")
    @ParameterizedTest
    @ValueSource(strings = {"available", "pending", "sold"})
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
            assertEquals(s, stat);
        }
        response.prettyPrint();
    }

    @DisplayName("Positive testing2 status GET    /pet/findByStatus")
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

    @DisplayName("Bug Negative testing1 GET     /pet/findByStatus")
    @Test
    // There is a bug. ready is not a valid access. "ready" is not written in swagger document as a value.
    // And status must be 400 but not status is 200
    // You can not get any info in response the body,
    public void getMethod_queryParam2() {
        given().accept(ContentType.JSON)
                .and().
                queryParam("status", "ready")
                .when()
                .get("/findByStatus")
                .then()
                .statusCode(not(equalTo(200)));
        // According to the swagger pet document, available values for status are "available", "pending" and "sold".
        // "ready" is invalid status value, so status code can not be 200, but status code is 200
        // because of that reason our test failed. There is a bug
    }

    @DisplayName("Positive testing3 GET    /pet/{petID}")
    @Test
    public void get_pathParam1() {
        Response response = given().accept(ContentType.JSON)
                .and().pathParam("petId", 19)
                .when()
                .get("/{petId}");

        JsonPath jsonPath = response.jsonPath(); //you are putting the response body to jsonPath Object
        assertEquals(200, response.statusCode());
        assertEquals("application/json", response.contentType());
        assertEquals(19, jsonPath.getInt("id"));
    }


    @DisplayName("Negative testing2 GET  /pet/{petID}")
    @Test // there is no pet with petID 258901
    public void getMethod_pathParam2() {
        try {
            Response response = given().accept(ContentType.JSON)
                    .and().pathParam("petId", 258901)
                    .when()
                    .get("/{petId}");

            assertEquals(404, response.statusCode());
            assertEquals("application/json", response.contentType());

            JsonPath jsonPath = response.jsonPath();
            assertEquals("error", jsonPath.getString("type"));
            assertEquals("Pet not found", jsonPath.getString("message"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DisplayName("Bug Negative testing3 GET   /pet/{petID} invalid id")
    @Test // There is a bug
    // According to the swagger,we should get 400 for invalid id (in this test we use bigger than int64)
    // but we get 404
    public void getMethod_pathParam3() {
        Response response = given().accept(ContentType.JSON)
                .and().pathParam("petId", 1234567890123456L)
                .when()
                .get("/{petId}");
        assertEquals(400, response.statusCode());
        assertEquals("application/json", response.contentType());
    }

}
