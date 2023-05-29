package com.cryptocurrency.converter.controller;

import com.cryptocurrency.converter.entity.History;
import com.cryptocurrency.converter.entity.User;
import com.cryptocurrency.converter.service.HistoryService;
import com.cryptocurrency.converter.service.UserService;
import com.cryptocurrency.converter.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HistoryController {
    private HistoryService historyService;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private UserService userService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/history")
    public String getAllHistory(Model model){
        List<History> historyList = new ArrayList<>();

        User user = userService.findByUsername(userUtils.getLoggedInUsername());
        historyList = historyService.findHistoryByUserId(user.getId());

        model.addAttribute("historyList", historyList);
        return "history";




    }

}
