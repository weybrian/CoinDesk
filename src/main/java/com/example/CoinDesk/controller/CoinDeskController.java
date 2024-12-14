package com.example.CoinDesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.CoinDesk.service.CoinDeskService;

@RestController
public class CoinDeskController {
	@Autowired
	CoinDeskService coinDeskService;

	/**
	 * 發送請求並取得回應
	 * @return
	 */
	@RequestMapping("/getNewBpi")
    public ResponseEntity<String> getNewBpi() {
        return coinDeskService.getNewBpi();
    }
	
	/**
	 * 新增 bpi
	 */
	@RequestMapping("/createBpi")
	public int createBpi() {
		return coinDeskService.createBpi(getNewBpi().getBody());
	}
	
	/**
	 * 查詢資料庫全部 bpi
	 * @return
	 */
	@RequestMapping("/getAllBpis")
	public String getAllBpis() {
		return coinDeskService.getAllBpis();
	}
}
