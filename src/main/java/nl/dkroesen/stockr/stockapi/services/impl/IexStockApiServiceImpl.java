package nl.dkroesen.stockr.stockapi.services.impl;

import nl.dkroesen.stockr.stockapi.models.GenericQuote;
import nl.dkroesen.stockr.stockapi.services.StockApiService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pl.zankowski.iextrading4j.api.stocks.Quote;
import pl.zankowski.iextrading4j.client.IEXTradingClient;
import pl.zankowski.iextrading4j.client.rest.request.stocks.QuoteRequestBuilder;

import javax.print.attribute.standard.Destination;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Component
@Qualifier("IEX")
public class IexStockApiServiceImpl implements StockApiService {

    private final ModelMapper modelMapper;
    private final IEXTradingClient iexTradingClient;

    @Autowired
    public IexStockApiServiceImpl(ModelMapper modelMapper, IEXTradingClient iexTradingClient){
        this.modelMapper = modelMapper;
        final PropertyMap<Quote, GenericQuote> quoteMap = new PropertyMap <Quote, GenericQuote>() {
            protected void configure() {
                map().setTicker(source.getSymbol());
            }
        };
        this.modelMapper.addMappings(quoteMap);

        this.iexTradingClient = iexTradingClient;

    }

    @Override
    public GenericQuote getQuote(final String ticker) {
        final Quote quote = sendQuoteRequest(ticker);
        return modelMapper.map(quote, GenericQuote.class);
    }

    @Override
    public Set<GenericQuote> getQuotes(final List<String> tickers) {
        return tickers.parallelStream()
                .map(this::sendQuoteRequest)
                .map(quote -> modelMapper.map(quote, GenericQuote.class))
                .collect(toSet());
    }

    private Quote sendQuoteRequest(final String ticker) {
        return iexTradingClient.executeRequest(new QuoteRequestBuilder()
                    .withSymbol(ticker)
                    .build());
    }
}
