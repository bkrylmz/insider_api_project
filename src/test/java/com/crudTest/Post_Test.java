package com.crudTest;

import com.utilities.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import java.io.File;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class Post_Test extends TestBase {

    @DisplayName("Positive POST1      /pet/{petId}/uploadImage")
    @Test
    public void postRequestPetId_UploadImage() {

        RestAssured.given()
                .pathParam("petId", 485)
                .contentType(ContentType.MULTIPART)
                .multiPart("additionalMetadata", "cat image3")
                .multiPart("file", new File(ConfigurationReader.getProperty("filePath")))
                .when()
                .log().all()
                .post("/{petId}/uploadImage")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @DisplayName("Positive POST2    /pet")
    @Test
    public void postRequest() {
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
                .when().post("")
                .then().statusCode(200).contentType("application/json")
                .body(
                        "id", is(13233343),
                        "category.name", equalTo("Lemon3"),
                        "name", is(equalTo("doggiessss")),
                        "name", startsWithIgnoringCase("do"),
                        "status", is("sold"));
    }


    @DisplayName("Negative Post     /pet")
    // wrong content type. TEXT is not written swagger document (POST)
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
                .when().post("")
                .then().statusCode(415);
    }

    @DisplayName("Positive POST3      /pet/{petId}")
    @Test //There is a bug,
    // According to the Swagger. Status code should be 200, but not it gets status 415

    public void postRequest3() {

        RestAssured.given()
                .pathParam("petId", 1530)
                .contentType(ContentType.MULTIPART)
                .multiPart("name", "catiee")
                .multiPart("status", "sold")
                .log().all()
                .when()
                .post("/{petId}")
                .then()
                .assertThat()
                .statusCode(200)
                .body("name", is("catiee"))
                .body("status", is("sold"));
    }


}
