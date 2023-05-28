package com.cryptocurrency.converter.controller;

import com.cryptocurrency.converter.entity.User;
import com.cryptocurrency.converter.models.CryptoCurrency;
import com.cryptocurrency.converter.models.GeoLocation;
import com.cryptocurrency.converter.models.CryptoCoin;

import com.cryptocurrency.converter.service.CryptoCoinFetcherService;
import com.cryptocurrency.converter.service.HistoryService;
import com.cryptocurrency.converter.service.IPGeoLocationService;
import com.cryptocurrency.converter.service.UserService;
import com.cryptocurrency.converter.service.impl.SecurityServiceImpl;
import com.cryptocurrency.converter.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class CryptoListController {

    private CryptoCoinFetcherService cryptoCoinFetcher;

    private IPGeoLocationService ipGeoLocationService;

    private UserService userService;

    private UserUtils userUtils;

    private HistoryService historyService;

    List<CryptoCoin> cryptos = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(CryptoListController.class);

    @Autowired
    public CryptoListController(CryptoCoinFetcherService cryptoCoinFetcher, IPGeoLocationService ipGeoLocationService, UserService userService, UserUtils userUtils, HistoryService historyService) {
        this.cryptoCoinFetcher = cryptoCoinFetcher;
        this.ipGeoLocationService = ipGeoLocationService;
        this.userService = userService;
        this.userUtils = userUtils;
        this.historyService = historyService;
    }


    @GetMapping("/converter")
    public String converter(Model model) throws ExecutionException {


        //Service to fetch top 25 Crypto coins, Comment this and hard-code in case of rate-limit
        cryptos = cryptoCoinFetcher.getTopCryptocurrencies();

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
            String currencyCode = geoLocation.getCurrency();


            //fetch crypto coin price from API
            double cryptoValue = this.cryptoCoinFetcher.fetchCoinPrice(new CryptoCurrency(cryptoSymbol, currencyCode));

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
