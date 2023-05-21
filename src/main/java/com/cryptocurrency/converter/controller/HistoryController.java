package com.cryptocurrency.converter.controller;

import com.cryptocurrency.converter.entity.History;
import com.cryptocurrency.converter.service.HistoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HistoryController {
    private HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/history")
    public String getAllHistory(Model model){
        List<History> historyList = new ArrayList<>();
        historyList = historyService.getAllHistory();

        model.addAttribute("historyList", historyList);
        return "history";




    }

}
