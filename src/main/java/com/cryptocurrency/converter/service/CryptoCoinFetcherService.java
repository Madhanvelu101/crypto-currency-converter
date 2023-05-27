package com.cryptocurrency.converter.service;

import com.cryptocurrency.converter.exceptions.ConverterException;
import com.cryptocurrency.converter.models.CryptoCoin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

@Service
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

    public CryptoCoinFetcherService() {
        // Default limit
        this.limit = 25;
        this.apiUrl = buildApiUrl();

    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<CryptoCoin> fetchTopCryptocurrencies() throws JSONException {
        try {
            String jsonResponse = fetchApiResponse(apiUrl);
            JSONArray jsonArray = new JSONArray(jsonResponse);
            return fetchAndReturnTopCryptocurrencies(jsonArray);
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

    private String fetchApiResponse(String apiUrl) throws IOException {
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

    private List<CryptoCoin> fetchAndReturnTopCryptocurrencies(JSONArray jsonArray) throws JSONException {

        List<CryptoCoin> cryptoCoins = new ArrayList<CryptoCoin>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject coinObj = jsonArray.getJSONObject(i);
            String coinName = coinObj.getString("name");
            String coinId = coinObj.getString("id");
            cryptoCoins.add(new CryptoCoin(coinName, coinId));
        }
        return cryptoCoins;
    }

    /**
     * Method to fetch crypto coin price
     *
     * @param cryptoSymbol
     * @param currency
     * @return
     */
    public double fetchCoinPrice(String cryptoSymbol, String currency) {

        try {
            RestTemplate restTemplate = new RestTemplate();
            String coinPriceEndpoint = String.format(COIN_PRICE_ENDPOINT, cryptoSymbol, currency);
            String jsonResponse = restTemplate.getForObject(API_URL + coinPriceEndpoint, String.class);

            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject priceObject = jsonObject.getJSONObject(cryptoSymbol.toLowerCase());
            double cryptoValue = priceObject.getDouble(currency.toLowerCase());

            return cryptoValue;
        } catch (Exception e) {
            throw new ConverterException("Error while fetching crypto price, Please try again");
        }


    }

}
