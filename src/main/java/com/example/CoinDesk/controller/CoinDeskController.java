package com.example.CoinDesk.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.CoinDesk.service.CoinDeskService;

@RestController
public class CoinDeskController {
	@Autowired
	CoinDeskService coinDeskService;

	private static final Logger logger = LoggerFactory.getLogger(CoinDeskController.class);

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
	public void createBpi() {
		String resBody = getNewBpi().getBody();
		if (resBody != null) {
			// 取出 bpis
			JSONObject resJson = new JSONObject(resBody);
			JSONObject bpis = resJson.getJSONObject("bpi");
			coinDeskService.createBpi(bpis);
		} else {
			logger.error("getNewBpi body is null");
		}
	}
}
