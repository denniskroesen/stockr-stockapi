package nl.dkroesen.stockr.stockapi.controllers;

import nl.dkroesen.stockr.stockapi.models.GenericQuote;
import nl.dkroesen.stockr.stockapi.services.StockApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/quote/{tickername}", method = RequestMethod.GET)
    public GenericQuote quote(@PathVariable(value = "tickername") String tickername) {
        return stockApiService.getQuote(tickername);
    }

    @RequestMapping(value = "/quotes/{tickernames}", method = RequestMethod.GET)
    public Set<GenericQuote> quotes(@PathVariable(value = "tickernames") String tickernames) {
        String[] tickers = tickernames.split(",");
        return stockApiService.getQuotes(Arrays.asList(tickers));
    }


}
