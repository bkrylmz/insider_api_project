package com.crudTest;

import com.utilities.TestBase;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class Delete_Test extends TestBase {

    @DisplayName("Positive Delete Test /pet/{petId}")
    @Test
    public void deleteTest1() {
        Response response = given().accept(ContentType.JSON)
                .and().pathParam("petId", 19)
                .when()
                .delete("/{petId}");

        JsonPath jsonPath = response.jsonPath();
        int code = jsonPath.getInt("code");
        String type = jsonPath.getString("type");
        String message = jsonPath.getString("message");

        System.out.println("code = " + code);
        System.out.println("type = " + type);
        System.out.println("message = " + message);

        assertEquals(200, response.statusCode());
        assertEquals("application/json", response.contentType());
        assertEquals(200, jsonPath.getInt("code"));
        assertEquals("unknown", jsonPath.getString("type"));
        assertEquals("19", jsonPath.getString("message"));

        // When we run this test, the pet with ID number 767767 will be deleted,
        // I am recreating a pet with the same ID number using a POST request.
        // This way, when we run this test again, it will not fail.
        given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"id\": 19,\n" +
                        "  \"category\": {\n" +
                        "    \"id\": 111222333444,\n" +
                        "    \"name\": \"Lemon3\"\n" +
                        "  },\n" +
                        "  \"name\": \"doggiessss\",\n" +
                        "  \"photoUrls\": [\n" +
                        "    \"string\"\n" +
                        "  ],\n" +
                        "  \"tags\": [\n" +
                        "    {\n" +
                        "      \"id\": 111222333444555,\n" +
                        "      \"name\": \"string\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"status\": \"sold\"\n" +
                        "}")
                .when().post("")
                .then().statusCode(200).contentType("application/json")
                .body(
                        "id", is(19),
                        "category.name", equalTo("Lemon3"),
                        "name", is(equalTo("doggiessss")),
                        "name", startsWithIgnoringCase("do"),
                        "status", is("sold"));
    }


    @DisplayName("Negative Delete Test1 /pet/{petId}")
    // There is no id parameter which is equal to 40 it can not be deleted
    @Test
    public void deleteTest2() {

        try {
            Response response = given().accept(ContentType.JSON)
                    .and()
                    .pathParam("petId", 40)
                    .when().delete("/{petId}");
            assertEquals(404, response.getStatusCode());
            assertEquals("application/json", response.contentType());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DisplayName("Bug Negative Delete Test2 /pet/{petId}")
    @Test//There is a bug.According to the Swagger status should be 400 invalid id supplied.
    // (Because ıd accept int64, but we pass more than that) But We get as a status 404.
    public void deleteTest3() {

        Response response = given().accept(ContentType.JSON)
                .and()
                .pathParam("petId", 1234567890123456L)
                .log().all()
                .when().delete("/{petId}");

        assertEquals(400, response.getStatusCode());
        assertEquals("application/json", response.contentType());

    }
}
