package com.example.CoinDesk.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.CoinDesk.dao.BpiRepository;
import com.example.CoinDesk.entity.Bpi;
import com.example.CoinDesk.model.Coininfo;
import com.example.CoinDesk.service.CoinDeskService;

@RestController
public class CoinDeskController {
	
	@Autowired
	private CoinDeskService coinDeskService;
	private final BpiRepository repository;
	
	CoinDeskController(BpiRepository repository) {
		this.repository = repository;
	}

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
	@PostMapping("/bpis")
	public Bpi newBpi(@RequestBody Bpi newBpi) {
		if (newBpi.getCode() != null) {
			newBpi.setName(coinDeskService.getBpiName(newBpi.getCode()));
		}
		return repository.save(newBpi);
	}
	
	/**
	 * 查詢資料庫全部 bpi
	 * @return
	 */
	@GetMapping("/bpis")
	public List<Bpi> all() {
		return repository.findAll();
	}
	
	/**
	 * 查詢單一 bpi
	 * @param id
	 * @return
	 */
	@GetMapping("/bpis/{id}")
	public Bpi one(@PathVariable Long id) {
		return repository.findById(id)
			      .orElseThrow(() -> new IllegalArgumentException("Bpi not found"));
	}
	
	/**
	 * 更新
	 * @param newBpi
	 * @param id
	 * @return
	 */
	@PutMapping("/bpis/{id}")
	public Bpi replaceBpi(@RequestBody Bpi newBpi, @PathVariable Long id) {
		return repository.findById(id)
			      .map(bpi -> {
			    	  bpi.setCode(newBpi.getCode());
			    	  bpi.setName(newBpi.getName());
			          bpi.setSymbol(newBpi.getSymbol());
			          bpi.setRate(newBpi.getRate());
			          bpi.setDescription(newBpi.getDescription());
			          bpi.setRate_float(newBpi.getRate_float());
			        return repository.save(bpi);
			      })
			      .orElseGet(() -> {
			        return repository.save(newBpi);
			      });
	}
	
	/**
	 * 刪除
	 * @param id
	 */
	@DeleteMapping("/bpis/{id}")
	public void deleteBpi(@PathVariable Long id) {
	    repository.deleteById(id);
	}
	
	/**
	 * 幣別資訊 api
	 * @return
	 */
	@RequestMapping("/coininfo")
	public ArrayList<Coininfo> getCoininfo() {
		return coinDeskService.getCoininfo(getNewBpi());
	}
}
