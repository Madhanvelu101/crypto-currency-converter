package com.cryptocurrency.converter.service;

import com.cryptocurrency.converter.models.GeoLocation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class IPGeoLocationServiceTest {

    private IPGeoLocationService ipGeoLocationService;

    @BeforeEach
    public void setup() {
        ipGeoLocationService = new IPGeoLocationService();
    }

    @Test
    public void testFormatCurrencyAndSymbol() {
        GeoLocation geoLocation = GeoLocation.builder()
                .countryCode("US")
                .language("en")
                .currency("USD")
                .build();
        double cryptoValue = 1234.56;

        String formattedCurrency = ipGeoLocationService.formatCurrenyAndSymbol(geoLocation, cryptoValue);

        assertEquals("$1,234.56", formattedCurrency);
    }
}
