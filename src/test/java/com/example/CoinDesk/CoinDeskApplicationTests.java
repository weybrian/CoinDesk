package com.example.CoinDesk;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.CoinDesk.entity.Bpi;
import com.example.CoinDesk.service.CoinDeskService;
import com.fasterxml.jackson.databind.ObjectMapper;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@WebAppConfiguration
class CoinDeskApplicationTests {
	
	private static final Logger logger = LoggerFactory.getLogger(CoinDeskApplicationTests.class);
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private CoinDeskService coinDeskService;

	// 創建MockMvc類的物件
	MockMvc mvc;

	@BeforeAll
	public void initBpi() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

		// 新增三筆資料
		try {
			mvc.perform(MockMvcRequestBuilders.get("/initBpi"));
			logger.info("新增三筆資料");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void getNewBpiTest() {
		String uri = "/getNewBpi";
		MvcResult result = null;
		try {
			result = mvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(status().isOk()).andReturn();
			logger.info("呼叫 coindesk api " + result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void newBpiTest() {
		String uri = "/bpis";
		
		Bpi bpi = new Bpi();
		bpi.setCode("USD");
		bpi.setName(coinDeskService.getBpiName(bpi.getCode()));
		bpi.setSymbol("&#36;");
		bpi.setRate("101,954.535");
		bpi.setDescription("United States Dollar");
		bpi.setRate_float(101954.5348);

		MvcResult result = null;
		try {
			// 新增前查詢全部
			result = mvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(status().isOk()).andReturn();
			result.getResponse().setCharacterEncoding("UTF-8");
			logger.info("新增前查詢全部 " + result.getResponse().getContentAsString());
			
			// 新增
			String jsonBody = objectMapper.writeValueAsString(bpi);
			mvc.perform(post(uri)
			        .contentType(MediaType.APPLICATION_JSON)
			        .content(jsonBody))
			        .andExpect(status().isOk());

			// 新增後查詢全部
			result = mvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(status().isOk()).andReturn();
			result.getResponse().setCharacterEncoding("UTF-8");
			logger.info("新增後查詢全部 " + result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void allTest() {
		String uri = "/bpis";
		MvcResult result = null;
		try {
			result = mvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(status().isOk()).andReturn();
	        result.getResponse().setCharacterEncoding("UTF-8");
			logger.info("查詢全部 " + result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void oneTest() {
		String uri = "/bpis/2";
		MvcResult result = null;
		try {
			result = mvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(status().isOk()).andReturn();
	        result.getResponse().setCharacterEncoding("UTF-8");
			logger.info("查詢一筆 id = 2, " + result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void replaceBpiTest() {
		String uri = "/bpis/3";
		
		Bpi bpi = new Bpi();
		bpi.setCode("USD");
		bpi.setName(coinDeskService.getBpiName(bpi.getCode()));
		bpi.setSymbol("&#36;");
		bpi.setRate("101,954.535");
		bpi.setDescription("Replace USD");
		bpi.setRate_float(101954.5348);

		MvcResult result = null;
		try {
			// 更新前
			result = mvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(status().isOk()).andReturn();
	        result.getResponse().setCharacterEncoding("UTF-8");
			logger.info("id = 3 更新前, " + result.getResponse().getContentAsString());
			
			// 更新後
			String jsonBody = objectMapper.writeValueAsString(bpi);
			
			result = mvc.perform(MockMvcRequestBuilders.put(uri)
			        .contentType(MediaType.APPLICATION_JSON)
			        .content(jsonBody))
			        .andExpect(status().isOk()).andReturn();
	        result.getResponse().setCharacterEncoding("UTF-8");
			logger.info("id = 3 更新後, " + result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void deleteBpiTest() {
		String uri = "/bpis/2";
		MvcResult result = null;
		try {
			// 刪除
			result = mvc.perform(MockMvcRequestBuilders.delete(uri)).andExpect(status().isOk()).andReturn();
			
			// 刪除後查詢全部
			result = mvc.perform(MockMvcRequestBuilders.get("/bpis")).andExpect(status().isOk()).andReturn();
	        result.getResponse().setCharacterEncoding("UTF-8");
			logger.info("刪除 id = 2 後查詢全部 " + result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void coininfoTest() {
		String uri = "/coininfo";
		MvcResult result = null;
		try {
			result = mvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(status().isOk()).andReturn();
	        result.getResponse().setCharacterEncoding("UTF-8");
			logger.info("呼叫資料轉換 coininfo api " + result.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
