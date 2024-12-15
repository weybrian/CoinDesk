package com.example.CoinDesk.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

/**
 * 比特幣價格指數
 */
@Entity
@Getter @Setter
public class Bpi {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String code;
	private String name;
	private String symbol;
	private Double rate;
	private String description;
	private Double rate_float;
}
