package com.cryptocurrency.converter.service.impl;

import com.cryptocurrency.converter.entity.History;
import com.cryptocurrency.converter.repository.HistoryRepository;
import com.cryptocurrency.converter.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {
    @Autowired
    HistoryRepository historyRepository;

    @Override
    public void logHistory(String userId, String cryptoCoin, String ipAddress, double cryptoValue, String country) {
        History history = new History();
        history.setUserId(Long.valueOf(userId));
        history.setCryptoCoin(cryptoCoin);
        history.setIpAddress(ipAddress);
        history.setCryptoPrice(cryptoValue);
        history.setCountry(country);
        history.setTimeStamp(LocalDateTime.now());
        historyRepository.save(history);
    }

    @Override
    public List<History> getAllHistory() {
        return historyRepository.findAll();
    }
}
