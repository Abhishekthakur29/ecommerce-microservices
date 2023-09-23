package com.abhiTech.currencyConversionMS.controller;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.abhiTech.currencyConversionMS.bean.CurrencyConversionBean;
import com.abhiTech.currencyConversionMS.currencyExchangeProxy.CurrencyExchangeProxy;

@RestController
public class classCurrencyConversionController {
	
	@Autowired
	CurrencyExchangeProxy exchangeProxy;

	@GetMapping("Welcome-message")
	public String getMessage() {
		return "Welcome to the currency exchange Service";
	}

	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean currencyConversion(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {

		HashMap<String, String> uriVriables = new HashMap<>();
		uriVriables.put("from", from);
		uriVriables.put("to", to);

		ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity(
				"http://localhost:8002/currency-exchange/from/{from}/to/{to}", CurrencyConversionBean.class,
				uriVriables);

		CurrencyConversionBean currencyConversion = responseEntity.getBody();

			return new CurrencyConversionBean(currencyConversion.getId(), from, to,
				currencyConversion.getConversionMultiple(), quantity,
				quantity.multiply(currencyConversion.getConversionMultiple()),
				currencyConversion.getEnvironment()+ " " + "from rest template");
	}
	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean currencyConversionFegin(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {

		CurrencyConversionBean currencyConversion = exchangeProxy.retrieveExchangeValue(from, to);

			return new CurrencyConversionBean(currencyConversion.getId(), from, to,
				currencyConversion.getConversionMultiple(), quantity,
				quantity.multiply(currencyConversion.getConversionMultiple()),
				currencyConversion.getEnvironment()+ " " + "from feign");
	}
}
