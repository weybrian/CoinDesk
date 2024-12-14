package com.example.CoinDesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.CoinDesk.dao.BpiDao;
import com.example.CoinDesk.entity.Bpi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CoinDeskService {
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	BpiDao bpiDao;
	
	public ResponseEntity<String> getNewBpi() {
		return restTemplate.getForEntity("https://api.coindesk.com/v1/bpi/currentprice.json", String.class);
	}

	public int createBpi(String resString) {
		int saveNum = 0;
		
		// 初始化 ObjectMapper
        ObjectMapper mapper = new ObjectMapper();

        // 解析 JSON 字串為 JsonNode
        JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(resString);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

        // 提取 "bpi" 節點
        JsonNode bpiNode = rootNode.get("bpi");
        
        // 儲存
        for (JsonNode jsonNode : bpiNode) {
			Bpi bpi = new Bpi();
			bpi.setCode(jsonNode.get("code").asText());
			bpi.setName(getBpiName(bpi.getCode()));
			bpi.setSymbol(jsonNode.get("symbol").asText());
			bpi.setRate(Double.valueOf(jsonNode.get("rate").asText().replace(",", "")));
			bpi.setDescription(jsonNode.get("description").asText());
			bpi.setRate_float(jsonNode.get("rate_float").asDouble());
			Bpi save = bpiDao.save(bpi);
			if (save.getCode() != null) {
				saveNum++;
			}
		}
        return saveNum;
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

	public String getAllBpis() {
		Iterable<Bpi> bpis = bpiDao.findAll();
		// 使用 ObjectMapper 將 Iterable 轉成 JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
			return objectMapper.writeValueAsString(bpis);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        return "getAllBpis error";
	}
	
}
