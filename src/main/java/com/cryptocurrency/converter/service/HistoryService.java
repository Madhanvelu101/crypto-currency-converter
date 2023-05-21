package com.cryptocurrency.converter.service;

import com.cryptocurrency.converter.entity.History;

import java.util.List;

public interface HistoryService {
    void logHistory(String userId, String cryptoSymbol, String ipAddress, double cryptoValue, String country);

    List<History> getAllHistory();
}
