package com.cryptocurrency.converter.controller;

import com.cryptocurrency.converter.models.GeoLocation;
import com.cryptocurrency.converter.models.CryptoCoin;

import com.cryptocurrency.converter.service.CryptoCoinFetcherService;
import com.cryptocurrency.converter.service.HistoryService;
import com.cryptocurrency.converter.service.IPGeoLocationService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CryptoListController {
    @Autowired
    private IPGeoLocationService ipGeoLocationService;

    @Autowired
    private CryptoCoinFetcherService cryptoCoinFetcher;


    private HistoryService historyService;

    private static final String API_URL = "https://api.coingecko.com/api/v3/";
    private static final String COIN_PRICE_ENDPOINT = "simple/price?ids=%s&vs_currencies=%s";

    List<CryptoCoin> cryptos = new ArrayList<>();

    public CryptoListController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/topCryptoList")
    public String topCryptoList(Model model) {

        //Service to fetch top 25 Crypto coins, Comment this and hard-code in case of rate-limit
        //cryptos = cryptoCoinFetcher.fetchTopCryptocurrencies();

        cryptos.add(new CryptoCoin("Bitcoin", "bitcoin"));
        cryptos.add(new CryptoCoin("Ethereum", "ethereum"));
        cryptos.add(new CryptoCoin("Ripple", "XRP"));
        model.addAttribute("cryptos", cryptos);
        return "topCryptoList";
    }

    @PostMapping("/getCryptoValue")
    public String getCryptoValue(@RequestParam("cryptoSymbol") String cryptoSymbol, @RequestParam(value = "ipAddress", required = false, defaultValue = "1.1.64.0") String ipAddress, Model model) {

        //Get currency from IP Address
        GeoLocation geoLocation = null;
        try {
            geoLocation = ipGeoLocationService.fetchGeoLocationByIp(ipAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //fetch currency from computed object
        String currency = geoLocation.getCurrency();


        //fetch crypto coin price from API
        double cryptoValue = this.cryptoCoinFetcher.fetchCoinPrice(cryptoSymbol, currency);

        //fetch currency symbol from Locale
        String currencySymbol = this.ipGeoLocationService.fetchCurrencySymbol(geoLocation);

        //Log history to DB
        this.historyService.logHistory("1001",cryptoSymbol,ipAddress,cryptoValue,geoLocation.getCountry());

        model.addAttribute("cryptos", this.cryptos);
        model.addAttribute("currencySymbol", currencySymbol);
        model.addAttribute("cryptoValue", cryptoValue);

        return "topCryptoList";
    }


}
