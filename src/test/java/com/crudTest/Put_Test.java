package com.crudTest;

import com.utilities.TestBase;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

public class Put_Test extends TestBase {

    @DisplayName("Positive PUT to /pet") // status updated as "available" instead of "sold"
    @Test
    public void putRequest1() {

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
                .when().put("")
                .then().statusCode(200).contentType("application/json")
                .body(
                        "id", is(12),
                        "category.name", equalTo("dogs"),
                        "name", is(equalTo("Fido")),
                        "name", startsWithIgnoringCase("Fi"),
                        "status", is("available")); // status updated before sold now available
    }

    @DisplayName("Negative PUT to /pet") // "TEXT" media type is not written swagger document (PUT). So that reason it is unsupported media type
    @Test
    public void putRequest_WithHamcrest2() {

        try {
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
                .when().put("")
                .then().statusCode(415);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
