package com.example.CoinDesk.service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.CoinDesk.dao.BpiRepository;
import com.example.CoinDesk.entity.Bpi;
import com.example.CoinDesk.model.Coininfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CoinDeskService {
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private BpiRepository repository;
	
	public ResponseEntity<String> getNewBpi() {
		return restTemplate.getForEntity("https://api.coindesk.com/v1/bpi/currentprice.json", String.class);
	}

	/**
	 * 貨幣中文名
	 * @param code
	 * @return
	 */
	public String getBpiName(String code) {
		switch (code) {
		case "USD":
			return "美元";
		case "GBP":
			return "英鎊";
		case "EUR":
			return "歐元";

		default:
			return "未定義";
		}
	}

	public ArrayList<Coininfo> getCoininfo(ResponseEntity<String> newBpi) {
		String body = newBpi.getBody();
		
		// 初始化 ObjectMapper
        ObjectMapper mapper = new ObjectMapper();

        // 解析 JSON 字串為 JsonNode
        JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(body);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		// updatetime
		String updatetime = rootNode.get("time").get("updated").asText();

        // 提取 "bpi" 節點
        JsonNode bpiNode = rootNode.get("bpi");
        
        ArrayList<Coininfo> coininfoList = new ArrayList<Coininfo>();
        for (JsonNode jsonNode : bpiNode) {
			Coininfo coininfo = new Coininfo();
			coininfo.setUpdateTimeString(parseDatetime(updatetime));
			coininfo.setCode(jsonNode.get("code").asText());
			coininfo.setName(getBpiName(coininfo.getCode()));
			coininfo.setRate(jsonNode.get("rate").asText());
			coininfoList.add(coininfo);
		}
		return coininfoList;
	}

	/**
	 * 解析api時間
	 * @param updated
	 * @return
	 */
	private String parseDatetime(String updated) {
		// 定義輸入的日期格式
		DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder()
	            .parseCaseInsensitive() // 大小寫不敏感
	            .appendPattern("MMM dd, yyyy HH:mm:ss z")
	            .toFormatter(Locale.ENGLISH); // 使用英文語系解析月份
        
        // 將日期字串解析為 ZonedDateTime
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(updated, inputFormatter);
        
        // 轉換為當地時間（例如 UTC+0 區域）
        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        
        // 定義輸出的日期格式
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        
        // 將 LocalDateTime 格式化為指定的字串格式
        return localDateTime.format(outputFormatter);
	}

	public void initBpi(ResponseEntity<String> newBpi) {
		String body = newBpi.getBody();
		
		// 初始化 ObjectMapper
        ObjectMapper mapper = new ObjectMapper();

        // 解析 JSON 字串為 JsonNode
        JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(body);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

        // 提取 "bpi" 節點
        JsonNode bpiNode = rootNode.get("bpi");
        
        for (JsonNode jsonNode : bpiNode) {
        	Bpi bpi = new Bpi();
			bpi.setCode(jsonNode.get("code").asText());
			bpi.setName(getBpiName(bpi.getCode()));
			bpi.setSymbol(jsonNode.get("symbol").asText());
			bpi.setRate(jsonNode.get("rate").asText());
			bpi.setDescription(jsonNode.get("description").asText());
			bpi.setRate_float(jsonNode.get("rate_float").asDouble());
			repository.save(bpi);
		}
	}
	
}
