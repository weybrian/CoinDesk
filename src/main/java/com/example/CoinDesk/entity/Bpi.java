package com.example.CoinDesk.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 比特幣價格指數
 */
@Entity
@Table
@Getter @Setter
public class Bpi {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column
	private String code;
	@Column
	private String name;
	@Column
	private String symbol;
	@Column
	private Double rate;
	@Column
	private String description;
	@Column
	private Double rate_float;
}
