package com.cryptocurrency.converter.controller;

import com.cryptocurrency.converter.entity.User;
import com.cryptocurrency.converter.models.GeoLocation;
import com.cryptocurrency.converter.models.CryptoCoin;

import com.cryptocurrency.converter.service.CryptoCoinFetcherService;
import com.cryptocurrency.converter.service.HistoryService;
import com.cryptocurrency.converter.service.IPGeoLocationService;
import com.cryptocurrency.converter.service.UserService;
import com.cryptocurrency.converter.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CryptoListController {
    @Autowired
    private IPGeoLocationService ipGeoLocationService;

    @Autowired
    private CryptoCoinFetcherService cryptoCoinFetcher;

    @Autowired
    private UserService userService;

    @Autowired
    private UserUtils userUtils;

    private HistoryService historyService;

    private static final String API_URL = "https://api.coingecko.com/api/v3/";
    private static final String COIN_PRICE_ENDPOINT = "simple/price?ids=%s&vs_currencies=%s";

    List<CryptoCoin> cryptos = new ArrayList<>();

    public CryptoListController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/converter")
    public String converter(Model model) {

        //Service to fetch top 25 Crypto coins, Comment this and hard-code in case of rate-limit
        cryptos = cryptoCoinFetcher.fetchTopCryptocurrencies();

//        cryptos.add(new CryptoCoin("Bitcoin", "bitcoin"));
//        cryptos.add(new CryptoCoin("Ethereum", "ethereum"));
//        cryptos.add(new CryptoCoin("Ripple", "XRP"));
        model.addAttribute("cryptos", cryptos);
        return "converter";
    }

    @PostMapping("/converter")
    public String getCryptoValue(@RequestParam("cryptoSymbol") String cryptoSymbol, @RequestParam(value = "ipAddress", required = false, defaultValue = "1.1.64.0") String ipAddress, Model model) {
        String formattedCurrencyAndSymbol = null;
        User user = userService.findByUsername(userUtils.getLoggedInUsername());

        //Get currency from IP Address
        GeoLocation geoLocation = null;
        try {
            geoLocation = ipGeoLocationService.fetchGeoLocationByIp(ipAddress);

            //fetch currency from computed object
            String currency = geoLocation.getCurrency();


            //fetch crypto coin price from API
            double cryptoValue = this.cryptoCoinFetcher.fetchCoinPrice(cryptoSymbol, currency);

            //fetch currency symbol from Locale
            formattedCurrencyAndSymbol = this.ipGeoLocationService.formatCurrenyAndSymbol(geoLocation, cryptoValue);

            //Log history to DB
            this.historyService.logHistory(user.getId(), cryptoSymbol, ipAddress, formattedCurrencyAndSymbol, geoLocation.getCountry());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            model.addAttribute("cryptos", this.cryptos);
            model.addAttribute("error", e.getMessage());
            return "converter";
        }


        model.addAttribute("cryptos", this.cryptos);

        model.addAttribute("cryptoValue", formattedCurrencyAndSymbol);

        return "converter";
    }


}
