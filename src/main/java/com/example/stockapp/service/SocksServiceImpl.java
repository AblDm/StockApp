package com.example.stockapp.service;

import com.example.stockapp.DTO.Socks;
import com.example.stockapp.DTO.SocksRepository;

import com.example.stockapp.exceptions.InsufficientQuantityException;
import com.example.stockapp.exceptions.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SocksServiceImpl implements SocksService {

    private final SocksRepository socksRepository;

    @Autowired
    public SocksServiceImpl(SocksRepository socksRepository) {
        this.socksRepository = socksRepository;
    }

    @Override
    @Transactional
    public void registerIncome(Socks request) {
        Socks socks = new Socks();
        socks.setColor(request.getColor());
        socks.setCottonPart(request.getCottonPart());
        socks.setQuantity(request.getQuantity());
        socksRepository.save(socks);
    }

    @Override
    @Transactional
    public void registerOutcome(Socks request) {
        String color = request.getColor();
        int cottonPart = request.getCottonPart();
        int quantity = request.getQuantity();

        List<Socks> socksList = socksRepository.findByColorAndCottonPart(color, cottonPart);

        int totalAvailableQuantity = socksList.stream().mapToInt(Socks::getQuantity).sum();

        if (totalAvailableQuantity < quantity) {
            throw new InsufficientQuantityException("Insufficient socks quantity for outcome");
        }

        for (Socks socks : socksList) {
            int socksToDeduct = Math.min(socks.getQuantity(), quantity);
            socks.setQuantity(socks.getQuantity() - socksToDeduct);
            quantity -= socksToDeduct;
            if (quantity == 0) {
                break;
            }
        }

        socksRepository.saveAll(socksList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Socks> getTotalSocks(String color, String operation, int cottonPart) {
        if ("moreThan".equals(operation)) {
            return socksRepository.findByColorAndCottonPartGreaterThan(color, cottonPart);
        } else if ("lessThan".equals(operation)) {
            return socksRepository.findByColorAndCottonPartLessThan(color, cottonPart);
        } else if ("equal".equals(operation)) {
            return socksRepository.findByColorAndCottonPart(color, cottonPart);
        } else {
            throw new InvalidRequestException("Invalid operation: " + operation);
        }
    }
}