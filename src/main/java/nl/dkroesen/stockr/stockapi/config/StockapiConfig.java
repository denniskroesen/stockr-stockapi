package nl.dkroesen.stockr.stockapi.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.zankowski.iextrading4j.client.IEXTradingClient;

@Configuration
public class StockapiConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public IEXTradingClient iexTradingClient() {
        return IEXTradingClient.create();
    }

}
