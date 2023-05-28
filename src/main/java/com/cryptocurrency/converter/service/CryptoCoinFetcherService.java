package com.cryptocurrency.converter.service;

import com.cryptocurrency.converter.exceptions.ConverterException;
import com.cryptocurrency.converter.models.CryptoCoin;
import com.cryptocurrency.converter.models.CryptoCurrency;
import com.google.common.cache.LoadingCache;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@NoArgsConstructor
public class CryptoCoinFetcherService {
    private static final String BASE_API_URL = "https://api.coingecko.com/api/v3/coins/markets";
    private static final String VS_CURRENCY = "usd";
    private static final String ORDER = "market_cap_desc";
    private static final boolean INCLUDE_SPARKLINE = false;
    private static final String API_URL = "https://api.coingecko.com/api/v3/";
    private static final String COIN_PRICE_ENDPOINT = "simple/price?ids=%s&vs_currencies=%s";

    private String apiUrl;

    @Value("${coin.fetch.limit}")
    private int limit;

    private LoadingCache<Integer, List<CryptoCoin>> cryptoCoinCache;

    private LoadingCache<CryptoCurrency, Double> cryptoPriceCache;

    private void init() {
        this.apiUrl = buildApiUrl();
    }

    public void setCryptoPriceCache(LoadingCache<CryptoCurrency, Double> cryptoPriceCache) {
        this.cryptoPriceCache = cryptoPriceCache;
        init();
    }


    public void setCryptoCoinCache(LoadingCache<Integer, List<CryptoCoin>> cryptoCoinCache) {
        this.cryptoCoinCache = cryptoCoinCache;
        init();
    }

    /**
     * Get List of top crypto coins list from cryptoCoinCache.
     * @return
     * @throws JSONException
     * @throws ExecutionException
     */
    public List<CryptoCoin> getTopCryptocurrencies() throws JSONException, ExecutionException {
        return cryptoCoinCache.get(limit);
    }

    public List<CryptoCoin> fetchTopCryptocurrencies() throws JSONException {
        try {
            String topCryptos = fetchTopCryptos(apiUrl);
            JSONArray jsonArray = new JSONArray(topCryptos);
            return computeCryptoList(jsonArray);
        } catch (IOException e) {
            System.err.println("Error occurred while fetching the API response: " + e.getMessage());
        }
        return null;
    }

    private String buildApiUrl() {
        StringBuilder sb = new StringBuilder(BASE_API_URL);
        sb.append("?vs_currency=").append(VS_CURRENCY);
        sb.append("&order=").append(ORDER);
        sb.append("&per_page=").append(limit);
        sb.append("&page=1");
        sb.append("&sparkline=").append(INCLUDE_SPARKLINE);
        return sb.toString();
    }

    private String fetchTopCryptos(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();
            return response.toString();
        } else {
            throw new IOException("Failed to fetch API response. Response code: " + responseCode);
        }
    }

    /**
     * Method to init cryptoCoinCache, if there is a cache miss
     * @param jsonArray
     * @return
     * @throws JSONException
     */
    private List<CryptoCoin> computeCryptoList(JSONArray jsonArray) throws JSONException {
        List<CryptoCoin> cryptoCoins = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject coinObj = jsonArray.getJSONObject(i);
            String coinName = coinObj.getString("name");
            String coinId = coinObj.getString("id");
            cryptoCoins.add(new CryptoCoin(coinName, coinId));
        }
        return cryptoCoins;
    }

    /**
     * Fetch coin price for the given location from cache
     * @param cryptoCurrency
     * @return
     * @throws ExecutionException
     */
    public double fetchCoinPrice(CryptoCurrency cryptoCurrency) throws ExecutionException {
        return this.cryptoPriceCache.get(cryptoCurrency);
    }

    /**
     * Method to init coin coinPriceCache, if there is a cache miss
     * @param cryptoCurrency
     * @return
     */
    public double computeCoinPrice(CryptoCurrency cryptoCurrency) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String coinPriceEndpoint = String.format(COIN_PRICE_ENDPOINT, cryptoCurrency.getCryptoSymbol(), cryptoCurrency.getCurrencyCode());
            String jsonResponse = restTemplate.getForObject(API_URL + coinPriceEndpoint, String.class);

            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject priceObject = jsonObject.getJSONObject(cryptoCurrency.getCryptoSymbol().toLowerCase());
            return priceObject.getDouble(cryptoCurrency.getCurrencyCode().toLowerCase());
        } catch (Exception e) {
            throw new ConverterException("Crypto Currency is unavailable for the given location. Please try with other location.");
        }
    }
}

