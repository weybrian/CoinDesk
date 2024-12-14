package com.example.CoinDesk;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

@SpringBootTest
@WebAppConfiguration
class CoinDeskApplicationTests {
	@Autowired
	private WebApplicationContext webApplicationContext;

	// 創建MockMvc類的物件
	MockMvc mvc;

	private static final Logger logger = LoggerFactory.getLogger(CoinDeskApplicationTests.class);

	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void getNewBpiTest() {
		String uri = "/getNewBpi";
		MvcResult result = null;
		try {
			result = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
		} catch (Exception e) {
			e.printStackTrace();
		}
		int status = result.getResponse().getStatus();
		logger.info(uri + " res status: " + status);
		Assert.assertEquals(uri + " 錯誤", 200, status);
	}

	@Test
	void createBpiTest() {
		String uri = "/createBpi";
		String saveNum = null;
		try {
			// 執行 GET 請求並取得結果
			MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn();
			// 獲取回傳的 Response Body
			saveNum = result.getResponse().getContentAsString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info(uri + " saveNum: " + saveNum);
		Assert.assertEquals(uri + " 錯誤", "3", saveNum);
	}
}
