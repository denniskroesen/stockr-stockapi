package nl.dkroesen.stockr.stockapi.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AccountRestIT {

    @Value("${local.server.port}")
    private int port;

    @Test
    public void signupUser() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("username", "user1");
        parameters.put("password", "user1password");
        given()
                .queryParam("username", "user1")
                .queryParam("password", "user1password");
        when()
                .post(getHost() + "/account/signup")
                .then()
                .assertThat()
                .statusCode(200);

        given()
                .queryParam("username", "user1")
                .queryParam("password", "user1password");
        when()
                .post(getHost() + "/auth/login")
                .then()
                .assertThat()
                .statusCode(200)
                .body("token", notNullValue());
    }

    private String getHost() {
        return "http://localhost:" + port;
    }

}
