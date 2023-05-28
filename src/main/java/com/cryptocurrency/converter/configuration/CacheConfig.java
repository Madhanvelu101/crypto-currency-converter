package com.cryptocurrency.converter.configuration;

import com.cryptocurrency.converter.models.CryptoCoin;
import com.cryptocurrency.converter.models.CryptoCurrency;
import com.cryptocurrency.converter.service.CryptoCoinFetcherService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {
    private final CryptoCoinFetcherService cryptoCoinFetcher;

    @Autowired
    public CacheConfig(CryptoCoinFetcherService cryptoCoinFetcher) {
        this.cryptoCoinFetcher = cryptoCoinFetcher;
    }

    @Bean
    public LoadingCache<Integer, List<CryptoCoin>> initCryptoCoinsCache() {
        LoadingCache<Integer, List<CryptoCoin>> cryptoCoinsCache = CacheBuilder.newBuilder().maximumSize(100)
                .expireAfterWrite(10, TimeUnit.HOURS).build(new CacheLoader<Integer, List<CryptoCoin>>() {
                    @Override
                    public List<CryptoCoin> load(Integer integer) throws Exception {
                        return cryptoCoinFetcher.fetchTopCryptocurrencies();
                    }
                });
        cryptoCoinFetcher.setCryptoCoinCache(cryptoCoinsCache);
        return cryptoCoinsCache;
    }

    @Bean
    public LoadingCache<CryptoCurrency, Double> initCryptoPrice() {
        LoadingCache<CryptoCurrency, Double> cryptoPriceCache = CacheBuilder.newBuilder().maximumSize(200)
                .expireAfterWrite(1, TimeUnit.HOURS).build(new CacheLoader<CryptoCurrency, Double>() {
                    @Override
                    public Double load(CryptoCurrency cryptoCurrency) throws Exception {
                        return cryptoCoinFetcher.computeCoinPrice(cryptoCurrency);
                    }
                });
        cryptoCoinFetcher.setCryptoPriceCache(cryptoPriceCache);
        return cryptoPriceCache;
    }


}

