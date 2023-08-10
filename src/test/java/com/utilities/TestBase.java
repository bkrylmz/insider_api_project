package com.utilities;

import org.junit.jupiter.api.BeforeAll;
import static io.restassured.RestAssured.*;

public class TestBase {
    @BeforeAll
    public static void init() {
        baseURI = ConfigurationReader.getProperty("InsiderBaseUrl");
        basePath = "/pet";
    }
}
