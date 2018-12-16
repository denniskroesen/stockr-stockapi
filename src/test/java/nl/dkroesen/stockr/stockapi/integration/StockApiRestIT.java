package nl.dkroesen.stockr.stockapi.integration;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class StockApiRestIT {

    @Value("${local.server.port}")
    private int port;

    @Test
    public void testSingleQuote_aapl() {
        get("http://localhost:" + port + "/api/stockinfo/quote/aapl")
                .then()
                .assertThat()
                .statusCode(200)
                .body("ticker", equalTo("AAPL"))
                .and()
                .body("companyName", equalTo("Apple Inc."));
    }

    @Test
    public void testMultipleQuotes_aapl_nflx() {
        get("http://localhost:" + port + "/api/stockinfo/quotes/aapl,nflx")
                .then()
                .assertThat()
                .statusCode(200)
                .body("ticker", equalTo(new String[]{ "AAPL", "NFLX" }))
                .and()
                .body("companyName", equalTo("Apple Inc."));
    }


}
