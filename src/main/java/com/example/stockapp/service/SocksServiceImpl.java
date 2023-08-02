package com.example.stockapp.service;

import com.example.stockapp.DTO.SocksDTO;
import com.example.stockapp.DTO.SocksRepository;

public class SocksService {

    SocksRepository socksRepo;
SocksDTO socksDTO;


    public void addSocks(SocksDTO socksDTO){
        socksRepo.save(socksDTO);
    }


    public int getTotalSocks(String color, String operation, int cottonPart) {
    }

    public void registerOutcome(SocksRequest request) {
        
    }

    public void registerIncome(SocksRequest request) {
    }
}
