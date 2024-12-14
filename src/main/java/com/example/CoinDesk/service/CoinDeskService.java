package com.example.CoinDesk.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.CoinDesk.dao.BpiDao;
import com.example.CoinDesk.entity.Bpi;

@Service
public class CoinDeskService {
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	BpiDao bpiDao;
	
	public ResponseEntity<String> getNewBpi() {
		return restTemplate.getForEntity("https://api.coindesk.com/v1/bpi/currentprice.json", String.class);
	}

	public void createBpi(JSONObject bpis) {
		String[] codes = JSONObject.getNames(bpis);
		for (String code : codes) {
			Bpi bpi = new Bpi();
			JSONObject props = bpis.getJSONObject(code);
			bpi.setCode(code);
			bpi.setName(getBpiName(code));
			bpi.setSymbol(String.valueOf(props.get("symbol")));
			bpi.setRate(Double.valueOf(String.valueOf(props.get("rate")).replace(",", "")));
			bpi.setDescription(String.valueOf(props.get("description")));
			bpi.setRate_float(Double.valueOf(String.valueOf(props.get("rate_float")).replace(",", "")));
			bpiDao.save(bpi);
		}
	}

	/**
	 * 貨幣中文名
	 * @param code
	 * @return
	 */
	private String getBpiName(String code) {
		switch (code) {
		case "USD":
			return "美元";
		case "GBP":
			return "英鎊";
		case "EUR":
			return "歐元";

		default:
			return "其他";
		}
	}
	
}
