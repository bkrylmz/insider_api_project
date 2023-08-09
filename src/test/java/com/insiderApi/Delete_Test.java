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

public class Delete_Test {
    @BeforeAll
    public static void init() {
        baseURI = "https://petstore.swagger.io/v2";
    }


    @DisplayName("Positive Delete Test /pet/{petId}")
    @Test
    public void delete_JsonPath() {
        Response response = given().accept(ContentType.JSON)
                .and().pathParam("petId", 767767)
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
        assertEquals("767767", jsonPath.getString("message"));
    }


    @DisplayName("Negative Delete Test /pet/{petId}")//there is no id parameter which is equal to 40
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


}
