package com.crudTest;

import com.utilities.TestBase;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.*;

public class Delete_Test extends TestBase {


    @DisplayName("Positive Delete Test /pet/{petId}")
    @Test
    public void delete_JsonPath() {
        Response response = given().accept(ContentType.JSON)
                .and().pathParam("petId", 767767)
                .when()
                .delete("/{petId}");

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


    @DisplayName("Negative Delete Test /pet/{petId}")//there is no id parameter which is equal to 40 it can not be deleted
    @Test
    public void delete2() {
        try { Response response = given().accept(ContentType.JSON)
                .and()
                .pathParam("petId", 40)
                .when().delete("/{petId}");
        assertEquals(404, response.getStatusCode());
        assertEquals("application/json", response.contentType());

        } catch (Exception e){
            e.printStackTrace();
        }

    }

}
