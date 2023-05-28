package com.cryptocurrency.converter.service;


import com.cryptocurrency.converter.entity.History;
import com.cryptocurrency.converter.repository.HistoryRepository;
import com.cryptocurrency.converter.service.HistoryService;
import com.cryptocurrency.converter.service.impl.HistoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class HistoryServiceImplTest {
    @Mock
    private HistoryRepository historyRepository;

    @InjectMocks
    private HistoryServiceImpl historyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void logHistory_shouldSaveHistory() {
        // Prepare test data
        Long userId = 1L;
        String cryptoCoin = "BTC";
        String ipAddress = "192.168.0.1";
        String cryptoValue = "50000";
        String country = "US";

        // Call the method
        historyService.logHistory(userId, cryptoCoin, ipAddress, cryptoValue, country);

        // Verify that the history was saved
        verify(historyRepository, times(1)).save(any(History.class));
    }

    @Test
    void getAllHistory_shouldReturnAllHistory() {
        // Prepare test data
        List<History> historyList = new ArrayList<>();
        historyList.add(new History());
        historyList.add(new History());
        when(historyRepository.findAll()).thenReturn(historyList);

        // Call the method
        List<History> result = historyService.getAllHistory();

        // Verify the result
        assertThat(result).isEqualTo(historyList);
    }

    @Test
    void findHistoryByUserId_shouldReturnMatchingHistory() {
        // Prepare test data
        long userId = 1L;
        List<History> historyList = new ArrayList<>();
        historyList.add(new History());
        historyList.add(new History());
        when(historyRepository.findByUserId(userId)).thenReturn(historyList);

        // Call the method
        List<History> result = historyService.findHistoryByUserId(userId);

        // Verify the result
        assertThat(result).isEqualTo(historyList);
    }
}
