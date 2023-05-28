package com.cryptocurrency.converter.controller;

import com.cryptocurrency.converter.entity.User;
import com.cryptocurrency.converter.models.CryptoCoin;
import com.cryptocurrency.converter.models.GeoLocation;
import com.cryptocurrency.converter.service.CryptoCoinFetcherService;
import com.cryptocurrency.converter.service.HistoryService;
import com.cryptocurrency.converter.service.IPGeoLocationService;
import com.cryptocurrency.converter.service.UserService;
import com.cryptocurrency.converter.utils.UserUtils;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CryptoListControllerTest {

    @Mock
    private CryptoCoinFetcherService cryptoCoinFetcherService;

    @Mock
    private IPGeoLocationService ipGeoLocationService;

    @Mock
    private UserService userService;

    @Mock
    private UserUtils userUtils;

    @Mock
    private HistoryService historyService;

    @Mock
    private Model model;

    private CryptoListController cryptoListController;

    List<CryptoCoin> cryptos = new ArrayList<>();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cryptoListController = new CryptoListController(cryptoCoinFetcherService, ipGeoLocationService,
                userService, userUtils, historyService);
    }

    @Test
    void testConverter() throws ExecutionException, JSONException {
        // Arrange

        cryptos.add(new CryptoCoin("Bitcoin", "bitcoin"));
        cryptos.add(new CryptoCoin("Ethereum", "ethereum"));

        when(cryptoCoinFetcherService.getTopCryptocurrencies()).thenReturn(cryptos);

        // Act
        String viewName = cryptoListController.converter(model);

        // Assert
        assertEquals("converter", viewName);
        verify(model).addAttribute("cryptos", cryptos);
    }

    @Test
    void testGetCryptoValue() throws IOException, Exception {
        // Arrange
        String cryptoSymbol = "bitcoin";
        String ipAddress = "1.1.1.1";
        String currencyCode = "USD";
        double cryptoValue = 50000.0;
        String formattedCurrencyAndSymbol = "$50,000";
        User user = new User();
        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setCurrency(currencyCode);

        when(userUtils.getLoggedInUsername()).thenReturn("username");
        when(userService.findByUsername("username")).thenReturn(user);
        when(ipGeoLocationService.fetchGeoLocationByIp(ipAddress)).thenReturn(geoLocation);
        when(cryptoCoinFetcherService.fetchCoinPrice(any())).thenReturn(cryptoValue);
        when(ipGeoLocationService.formatCurrenyAndSymbol(geoLocation, cryptoValue)).thenReturn(formattedCurrencyAndSymbol);

        // Act
        String viewName = cryptoListController.getCryptoValue(cryptoSymbol, ipAddress, model);

        // Assert
        assertEquals("converter", viewName);
        verify(model).addAttribute("cryptos", this.cryptos);
        verify(model).addAttribute("cryptoValue", formattedCurrencyAndSymbol);
        verify(historyService).logHistory(user.getId(), cryptoSymbol, ipAddress, formattedCurrencyAndSymbol, geoLocation.getCountry());
    }
}

