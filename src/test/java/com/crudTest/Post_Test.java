package com.crudTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.File;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

public class Post_Test {
    @BeforeAll
    public static void init() {
        baseURI = "https://petstore.swagger.io/v2";
    }

    @DisplayName("Positive POST /pet/{petId}/uploadImage")
    @Test
    public void postPetId_UploadImage() {

        RestAssured.given()
                .pathParam("petId", 485)
                .contentType(ContentType.MULTIPART)
                .multiPart("additionalMetadata", "cat image3")
                .multiPart("file", new File("C:\\Users\\bkryl\\OneDrive\\Masaüstü\\Sugar.jpg"))
                .when()
                .post("/pet/{petId}/uploadImage")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }


    @DisplayName("Negative POST /pet/{petId}/uploadImage")
    @Test // it can not be uploaded image because image's path way is not written. Test failed
    public void postPetId_UploadImage2() {

        RestAssured.given()
                .pathParam("petId", 485)
                .contentType(ContentType.MULTIPART)
                .multiPart("additionalMetadata", "cat image3")
                .multiPart("file", new File(""))
                .when()
                .post("/pet/{petId}/uploadImage");
    }



    @DisplayName("Positive POST /pet")
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


    @DisplayName("Negative POST /pet") // wrong content type. TEXT is not written swagger document (POST)
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

}
