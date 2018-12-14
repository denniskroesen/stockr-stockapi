package nl.dkroesen.stockr.stockapi.models;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class GenericQuote {

    private String ticker;
    private String companyName;
    private String primaryExchange;
    private String sector;
    private Date openTime;
    private Date closeTime;
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal latestPrice;
    private BigDecimal marketCap;

}
