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
	void testCoindesk() {
		String uri = "/coindesk";
		MvcResult result = null;
		try {
			result = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON)).andReturn();
		} catch (Exception e) {
			e.printStackTrace();
		}
		int status = result.getResponse().getStatus();
		logger.info("/coindesk res status: " + status);
		Assert.assertEquals("/coindesk 錯誤", 200, status);
	}

}
