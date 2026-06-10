package com.countergg.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RiotApiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${riot.api.key}")
    private String riotApiKey;

    public RiotApiService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String getSummonerByRiotId(String gameName, String tagLine) {
        // Bước 1: Riot hiện nay yêu cầu tìm PUUID qua Riot ID (tên#tag) thay vì chỉ mỗi tên
        // Sử dụng cluster 'asia' cho các tài khoản VN (hoặc đổi 'americas', 'europe' tùy khu vực của tài khoản)
        String accountUrl = "https://asia.api.riotgames.com/riot/account/v1/accounts/by-riot-id/" + gameName + "/" + tagLine;
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", riotApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // Lấy PUUID
            ResponseEntity<String> accountResponse = restTemplate.exchange(accountUrl, HttpMethod.GET, entity, String.class);
            JsonNode accountNode = objectMapper.readTree(accountResponse.getBody());
            String puuid = accountNode.get("puuid").asText();

            // Bước 2: Từ PUUID lấy thông tin chi tiết Summoner (Level, Icon, ...)
            // Sử dụng endpoint khu vực vn2 (Việt Nam)
            String summonerUrl = "https://vn2.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/" + puuid;
            ResponseEntity<String> summonerResponse = restTemplate.exchange(summonerUrl, HttpMethod.GET, entity, String.class);
            
            return summonerResponse.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Không tìm thấy người chơi hoặc API Key đã hết hạn. Hãy kiểm tra lại Tên và Tag.\"}";
        }
    }
}
