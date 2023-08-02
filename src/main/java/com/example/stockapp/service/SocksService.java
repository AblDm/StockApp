package com.example.stockapp.service;

import com.example.stockapp.DTO.Socks;

import java.util.List;


public interface SocksService {
    void registerIncome(Socks request);
    void registerOutcome(Socks request);
    List<Socks> getTotalSocks(String color, String operation, int cottonPart);
}
