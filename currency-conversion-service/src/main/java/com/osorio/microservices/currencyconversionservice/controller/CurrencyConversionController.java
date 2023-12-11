package com.osorio.microservices.currencyconversionservice.controller;

import com.osorio.microservices.currencyconversionservice.entity.CurrencyConversion;
import com.osorio.microservices.currencyconversionservice.proxy.CurrencyExchangeProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

@EnableEurekaServer
@RestController
public class CurrencyConversionController {

    @Autowired
    CurrencyExchangeProxy currencyExchangeProxy;

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversion(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity
    ) {
        HashMap<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);
        ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity(
                "http://localhost:8000/currency-exchange/from/{from}/to/{to}",
                CurrencyConversion.class,
                uriVariables);

        CurrencyConversion currencyConversion = responseEntity.getBody();
        return new CurrencyConversion(
                1001L,
                from,
                to,
                quantity,
                currencyConversion.getConversionMultiple(),
                quantity.multiply(quantity),
                currencyConversion.getEnvironment());
    }

    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversionFeign(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity
    ) {
        CurrencyConversion currencyConversion = currencyExchangeProxy.retrieveExchangeValue(from,to);
        return new CurrencyConversion(
                1001L,
                from,
                to,
                quantity,
                currencyConversion.getConversionMultiple(),
                quantity.multiply(quantity),
                currencyConversion.getEnvironment());
    }

}
