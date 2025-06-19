import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class AuthIntegrationTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:4004";
    }

    @Test
    public void shouldReturnOKWithValidToken() {
        String payload = """
                {
                "email":"testuser@test.com",
                "password":"password123"
                }
                """;
        Response response = given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract()
                .response();

        System.out.println("generated token: " + response.getBody().jsonPath().getString("token") + "");
    }

    @Test
    public void shouldReturnUnauthorizedOnInvalidLogin() {
        String payload = """
                {
                "email":"invalid_user@test.com",
                "password":"password123"
                }
                """;
        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);

    }
}
