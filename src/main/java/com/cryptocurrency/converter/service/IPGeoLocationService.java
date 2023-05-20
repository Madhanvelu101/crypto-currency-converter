package com.cryptocurrency.converter.service;

import com.cryptocurrency.converter.models.GeoLocation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;
import java.util.Currency;
import java.util.Locale;

/**
 * Service class to fetch geolocation from IP address
 */
@Service
public class IPGeoLocationService {
    //API to fetch geolocation
    public static final String IP_URL = "https://ipapi.co/";
    public static final String JSON_STRING = "/json/";


    public GeoLocation fetchGeoLocationByIp(String ipAddress) throws IOException {

        URL url = new URL(IP_URL + ipAddress + JSON_STRING);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Parse the JSON response
        String jsonResponse = response.toString();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNodeGeoLocation = objectMapper.readTree(jsonResponse);

        return GeoLocation.builder().city(jsonNodeGeoLocation.get("city").asText())
                .country(jsonNodeGeoLocation.get("country_name").asText())
                .countryCode(jsonNodeGeoLocation.get("country_code").asText())
                .language(jsonNodeGeoLocation.get("languages").asText())
                .currency(jsonNodeGeoLocation.get("currency").asText()).build();


    }

    /**
     * To fetch currency symbol using Java Locale
     * @param geoLocation
     * @return
     */
    public String fetchCurrencySymbol(GeoLocation geoLocation){

        String language = geoLocation.getLanguage().substring(0,2);
        String countryCode = geoLocation.getCountryCode();
        Locale locale = new Locale(language,countryCode);
        Currency currency = Currency.getInstance(locale);
        return currency.getSymbol(locale);
    }

}
