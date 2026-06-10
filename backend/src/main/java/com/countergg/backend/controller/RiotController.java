package com.countergg.backend.controller;

import com.countergg.backend.service.RiotApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/riot")
public class RiotController {

    private final RiotApiService riotApiService;

    public RiotController(RiotApiService riotApiService) {
        this.riotApiService = riotApiService;
    }

    @GetMapping("/summoner")
    public String getSummoner(@RequestParam String gameName, @RequestParam String tagLine) {
        return riotApiService.getSummonerByRiotId(gameName, tagLine);
    }
}
