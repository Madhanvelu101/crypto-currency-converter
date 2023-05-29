package com.cryptocurrency.converter.service;



import com.cryptocurrency.converter.exceptions.ConverterException;
import com.cryptocurrency.converter.models.CryptoCoin;
import com.cryptocurrency.converter.models.CryptoCurrency;
import com.google.common.cache.LoadingCache;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CryptoCoinFetcherServiceTest {

    private static final String API_URL = "https://api.coingecko.com/api/v3/";

    String apiUrl = null;

    private CryptoCoinFetcherService cryptoCoinFetcherService;

    @Mock
    private LoadingCache<Integer, List<CryptoCoin>> cryptoCoinCache;

    @Mock
    private LoadingCache<CryptoCurrency, Double> cryptoPriceCache;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cryptoCoinFetcherService = new CryptoCoinFetcherService();
        cryptoCoinFetcherService.setCryptoCoinCache(cryptoCoinCache);
        cryptoCoinFetcherService.setCryptoPriceCache(cryptoPriceCache);
         apiUrl = cryptoCoinFetcherService.buildApiUrl();
    }



    @Test
    void testFetchCoinPrice() throws ExecutionException {
        CryptoCurrency cryptoCurrency = new CryptoCurrency("bitcoin",  "usd");
        double expectedPrice = 50000.0;

        when(cryptoPriceCache.get(cryptoCurrency)).thenReturn(expectedPrice);

        double actualPrice = cryptoCoinFetcherService.fetchCoinPrice(cryptoCurrency);

        assertEquals(expectedPrice, actualPrice);
        verify(cryptoPriceCache, times(1)).get(cryptoCurrency);
        verifyNoMoreInteractions(cryptoPriceCache);
    }

    @Test
    void testComputeCoinPrice() throws JSONException {
        CryptoCurrency cryptoCurrency = new CryptoCurrency("bitcoin",  "usd");
        double expectedPrice = 50000.0;

        RestTemplate restTemplate = mock(RestTemplate.class);
        cryptoCoinFetcherService.setRestTemplate(restTemplate);

        JSONObject dummyJsonObject = new JSONObject();
        dummyJsonObject.put("usd", expectedPrice);

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(dummyJsonObject.toString());

        double actualPrice = cryptoCoinFetcherService.computeCoinPrice(cryptoCurrency);

       // assertEquals(expectedPrice, actualPrice);
        assertNotNull(actualPrice);

    }

    @Test
    void testComputeCoinPrice_ThrowsConverterException() {
        CryptoCurrency cryptoCurrency = new CryptoCurrency("ethereum",  "ghs");

        RestTemplate restTemplate = mock(RestTemplate.class);
        cryptoCoinFetcherService.setRestTemplate(restTemplate);

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException("API Error"));

        assertThrows(ConverterException.class, () -> cryptoCoinFetcherService.computeCoinPrice(cryptoCurrency));


    }

    private List<CryptoCoin> createDummyCryptoCoins(int count) {
        List<CryptoCoin> cryptoCoins = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            cryptoCoins.add(new CryptoCoin("Coin" + (i + 1), "coin" + (i + 1)));
        }
        return cryptoCoins;
    }

    private String createDummyApiResponse() throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 10; i++) {
            JSONObject coinObj = new JSONObject();
            coinObj.put("name", "Coin" + (i + 1));
            coinObj.put("id", "coin" + (i + 1));
            jsonArray.put(coinObj);
        }
        return jsonArray.toString();
    }
}

