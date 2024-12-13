package com.example.CoinDesk.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CoinDeskController {
	private final RestTemplate restTemplate = new RestTemplate();

	/**
	 * 發送請求並取得回應
	 * @return
	 */
	@RequestMapping("/coindesk")
    public ResponseEntity<String> coindesk() {
        return restTemplate.getForEntity("https://api.coindesk.com/v1/bpi/currentprice.json", String.class);
    }
	
}
