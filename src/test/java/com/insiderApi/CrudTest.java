package com.insiderApi;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CrudTest {

    @BeforeAll
    public static void init() {
        baseURI = "https://petstore.swagger.io/v2";
    }


    @DisplayName("Positive POST to /pet")
    @Test
    public void postRequest_WithHamcrest() {
        given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"id\": 13233343,\n" +
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
                .when().post("/pet")
                .then().statusCode(200).contentType("application/json")
                .body(
                        "id", is(13233343),
                        "category.name", equalTo("Lemon3"),
                        "name", is(equalTo("doggiessss")),
                        "name", startsWithIgnoringCase("do"),
                        "status", is("sold"));
    }

    @DisplayName("Negative POST to /pet") // wrong type. TEXT is not written swagger document (POST)
    @Test
    public void postRequest2() {
        given().contentType(ContentType.TEXT)
                .body("{\n" +
                        "  \"id\": 987654321,\n" +
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
                        "  \"status\": \"new\"\n" +
                        "}")
                .when().post("/pet")
                .then().statusCode(415);
    }

    @DisplayName("Positive PUT to /pet") // status updated as "available" instead of "sold"
    @Test
    public void putRequest_WithHamcrest() {
        given().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"id\": 12,\n" +
                        "  \"category\": {\n" +
                        "    \"id\": 0,\n" +
                        "    \"name\": \"dogs\"\n" +
                        "  },\n" +
                        "  \"name\": \"Fido\",\n" +
                        "  \"photoUrls\": [\n" +
                        "    \"string\"\n" +
                        "  ],\n" +
                        "  \"tags\": [\n" +
                        "    {\n" +
                        "      \"id\": 0,\n" +
                        "      \"name\": \"string\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"status\": \"available\"\n" +
                        "}")
                .when().put("/pet")
                .then().statusCode(200).contentType("application/json")
                .body(
                        "id", is(12),
                        "category.name", equalTo("dogs"),
                        "name", is(equalTo("Fido")),
                        "name", startsWithIgnoringCase("Fi"),
                        "status", is("available")); // status updated before sold now available
    }


    @DisplayName("Positive testing GET request to /pet/findByStatus")// find sold status pets
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
    //as a value "ready" is not written in swagger document for queryParam. You can not get any info in response the body,
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
                .and().pathParam("petId", 555555)
                .when()
                .get("/pet/{petId}");

        JsonPath jsonPath = response.jsonPath(); //you are putting the response body to jsonPath Object
        assertEquals(200, response.statusCode());
        assertEquals("application/json", response.contentType());
        assertEquals(555555, jsonPath.getInt("id"));
        assertEquals(0, jsonPath.getInt("category.id"));
    }


    @DisplayName("Positive Delete Test /pet/{petId}")
    @Test
    public void delete_JsonPath() {
        Response response = given().accept(ContentType.JSON)
                .and().pathParam("petId", 30)
                .when()
                .delete("/pet/{petId}");

        JsonPath jsonPath = response.jsonPath(); //you are putting the response body to jsonPath Object
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
        assertEquals("30", jsonPath.getString("message"));
    }


    @DisplayName("Negative Delete Test /pet/{petId}")// there is no id parameter which is equal to 40
    @Test
    public void delete2() {
        Response response = given().accept(ContentType.JSON)
                .and().pathParam("petId", 40)
                .when()
                .delete("/pet/{petId}");

        JsonPath jsonPath = response.jsonPath();
        assertEquals(404, response.statusCode());
        assertEquals("application/json", response.contentType());

    }

    @DisplayName("Negative PUT to /pet") // wrong type. TEXT is not written swagger document (PUT)
    @Test
    public void putRequest_WithHamcrest2() {
        given().contentType(ContentType.TEXT)
                .body("{\n" +
                        "  \"id\": 987654321,\n" +
                        "  \"category\": {\n" +
                        "    \"id\": 111222333444,\n" +
                        "    \"name\": \"Lemon18\"\n" +
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
                        "  \"status\": \"new\"\n" +
                        "}")
                .when().put("/pet")
                .then().statusCode(415);
    }


}

