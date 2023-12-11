package com.osorio.microservices.currencyexchangeservice.repositories;

import com.osorio.microservices.currencyexchangeservice.service.CurrencyExchange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyExchangeRepository
        extends JpaRepository<CurrencyExchange,Long> {
    CurrencyExchange findByFromAndTo(String from, String to);
}
