package com.cryptocurrency.converter.service;

import com.cryptocurrency.converter.entity.History;

import java.util.List;

public interface HistoryService {
    void logHistory(Long userId, String cryptoSymbol, String ipAddress, String cryptoValue, String country);

    List<History> getAllHistory();

    List<History> findHistoryByUserId(long userId);
}
