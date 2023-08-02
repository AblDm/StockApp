package com.example.stockapp.service;

import com.example.stockapp.DTO.Socks;


public interface SocksService {
    void registerIncome(Socks request);
    void registerOutcome(Socks request);
    int getTotalSocks(String color, String operation, int cottonPart);
}
