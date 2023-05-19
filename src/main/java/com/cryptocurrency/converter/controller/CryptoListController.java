package com.cryptocurrency.converter.controller;

import com.cryptocurrency.converter.service.CryptoCoinFetcherService;
import com.cryptocurrency.converter.models.CoinPrice;
import com.cryptocurrency.converter.models.CryptoCoin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class CryptoListController {

    @GetMapping("/topCryptoList")
    public String topCryptoList(Model model) {
        CryptoCoinFetcherService cryptoCoinFetcher = new CryptoCoinFetcherService();
        List<CryptoCoin> cryptos = cryptoCoinFetcher.fetchTopCryptocurrencies();
        model.addAttribute("cryptos", cryptos);
        return "topCryptoList";
    }




}
