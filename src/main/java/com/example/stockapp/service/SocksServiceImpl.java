package com.example.stockapp.service;

import com.example.stockapp.DTO.Socks;
import com.example.stockapp.DTO.SocksRepository;

import com.example.stockapp.exceptions.InsufficientQuantityException;
import com.example.stockapp.exceptions.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
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
        String color = request.getColor();
        int cottonPart = request.getCottonPart();
        int quantity = request.getQuantity();

        List<Socks> existingSocks = socksRepository.findByColorAndCottonPart(color, cottonPart);

        if (!existingSocks.isEmpty()) {
            Socks aggregatedSocks = existingSocks.get(0);
            aggregatedSocks.setQuantity(aggregatedSocks.getQuantity() + quantity);
            socksRepository.save(aggregatedSocks);
        } else {
            Socks newSocks = new Socks();
            newSocks.setColor(color);
            newSocks.setCottonPart(cottonPart);
            newSocks.setQuantity(quantity);
            socksRepository.save(newSocks);
        }
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
    @Transactional
    public List<Socks> getTotalSocks(String color, String operation, int cottonPart) {
        if ("moreThan".equals(operation)) {
            int totalQuantity = socksRepository.sumQuantityByColorAndCottonPartGreaterThan(color, cottonPart);

            Socks aggregatedSocks = new Socks();
            aggregatedSocks.setColor(color);
            aggregatedSocks.setCottonPart(cottonPart);
            aggregatedSocks.setQuantity(totalQuantity);

            return Collections.singletonList(aggregatedSocks);
        } else if ("lessThan".equals(operation)) {
            int totalQuantity = socksRepository.sumQuantityByColorAndCottonPartLessThan(color, cottonPart);

            Socks aggregatedSocks = new Socks();
            aggregatedSocks.setColor(color);
            aggregatedSocks.setCottonPart(cottonPart);
            aggregatedSocks.setQuantity(totalQuantity);

            return Collections.singletonList(aggregatedSocks);
        } else if ("equal".equals(operation)) {
            List<Socks> existingSocks = socksRepository.findByColorAndCottonPart(color, cottonPart);

            if (!existingSocks.isEmpty()) {
                int totalQuantity = existingSocks.stream().mapToInt(Socks::getQuantity).sum();
                Socks aggregatedSocks = existingSocks.get(0);
                aggregatedSocks.setQuantity(totalQuantity);
                return Collections.singletonList(socksRepository.save(aggregatedSocks));
            } else {
                return existingSocks;
            }
        } else {
            throw new InvalidRequestException("Invalid operation: " + operation);
        }
    }
}