package com.cryptocurrency.converter.service;

import com.cryptocurrency.converter.models.GeoLocation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;
import java.text.NumberFormat;
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

    private static final Logger logger = LoggerFactory.getLogger(IPGeoLocationService.class);

    public GeoLocation fetchGeoLocationByIp(String ipAddress) throws IOException {
        //If IP is default, then use Locale
        if (isDefaultIP(ipAddress)) {
            logger.debug("Get GeoLocation from locale");
            return getDefaultLocaleGeo();
        }

        // Parse the JSON response
        String jsonResponse = getLocaleFromAPI(ipAddress);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNodeGeoLocation = objectMapper.readTree(jsonResponse);
        JsonNode country = jsonNodeGeoLocation.get("country_code");
        logger.debug("Return Geolocation from API country: {}", country);

        if (country == null) {
            return getDefaultLocaleGeo();
        }

        return GeoLocation.builder().city(jsonNodeGeoLocation.get("city").asText())
                .country(jsonNodeGeoLocation.get("country_name").asText())
                .countryCode(jsonNodeGeoLocation.get("country_code").asText())
                .language(jsonNodeGeoLocation.get("languages").asText())
                .currency(jsonNodeGeoLocation.get("currency").asText()).build();


    }

    /**
     * Fetch GeoLocation from API
     *
     * @param ipAddress
     * @return
     * @throws IOException
     */
    private String getLocaleFromAPI(String ipAddress) throws IOException {
        logger.debug("Get GeoLocation from API");
        URL url = new URL(IP_URL + ipAddress + JSON_STRING);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return response.toString();
    }

    /**
     * Compute default locale and return
     *
     * @return
     */
    private GeoLocation getDefaultLocaleGeo() {
        String currencyCode, countryCode, language;
        Locale defaultLocale = Locale.getDefault();
        logger.debug("Default locale country {}", defaultLocale.getCountry());
        try {
            Currency currency = java.util.Currency.getInstance(new Locale("", defaultLocale.getCountry()));
            currencyCode = currency.getCurrencyCode();
            countryCode = defaultLocale.getCountry();
            language = defaultLocale.getLanguage();
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException, so handling with default case");
            countryCode = "DE";
            currencyCode = "EUR";
            language = "de";
        }
        if (countryCode == null || countryCode.isEmpty()) {
            //Hard coded country and currency, because Locale not working in dockers
            logger.debug("CountryCode is null, so setting default values");
            countryCode = "DE";
            currencyCode = "EUR";
            language = "de";
            logger.debug("Ip address not provided so setting default Locale currencyCode : {}, countryCode : {} ", countryCode, currencyCode);
        }

        return GeoLocation.builder().city("defaultCity")
                .country(defaultLocale.getDisplayCountry())
                .countryCode(countryCode)
                .language(language)
                .currency(currencyCode).build();
    }

    private boolean isDefaultIP(String ipAddress) {
        return ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1");
    }

    /**
     * To fetch currency symbol using Java Locale
     *
     * @param geoLocation
     * @param cryptoValue
     * @return
     */
    public String formatCurrenyAndSymbol(GeoLocation geoLocation, double cryptoValue) {

        String language = geoLocation.getLanguage().substring(0, 2);
        String countryCode = geoLocation.getCountryCode();
        Locale locale = new Locale(language, countryCode);
        Currency currency = Currency.getInstance(locale);
        String currencyAndSymbol = NumberFormat.getCurrencyInstance(locale).format(cryptoValue);
        return currencyAndSymbol;
    }

}
