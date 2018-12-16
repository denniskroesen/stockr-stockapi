package nl.dkroesen.stockr.stockapi.controllers;

import nl.dkroesen.stockr.stockapi.models.GenericQuote;
import nl.dkroesen.stockr.stockapi.services.StockApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Set;

@RestController
@RequestMapping("/api/stockinfo")
public class StockController {

    @Autowired
    public StockController(@Qualifier("IEX") StockApiService stockApiService){
        this.stockApiService = stockApiService;
    }

    private StockApiService stockApiService;

    @GetMapping(value = "/quote/{tickername}")
    public GenericQuote quote(@PathVariable(value = "tickername") String tickername) {
        return stockApiService.getQuote(tickername);
    }

    @GetMapping(value = "/quotes/{tickernames}")
    public Set<GenericQuote> quotes(@PathVariable(value = "tickernames") String tickernames) {
        String[] tickers = tickernames.split(",");
        return stockApiService.getQuotes(Arrays.asList(tickers));
    }

}
