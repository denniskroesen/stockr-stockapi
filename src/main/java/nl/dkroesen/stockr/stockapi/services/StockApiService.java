package nl.dkroesen.stockr.stockapi.services;

import nl.dkroesen.stockr.stockapi.models.GenericQuote;

import java.util.List;
import java.util.Set;

public interface StockApiService {

    GenericQuote getQuote(final String ticker);

    Set<GenericQuote> getQuotes(final List<String> tickers);
}
