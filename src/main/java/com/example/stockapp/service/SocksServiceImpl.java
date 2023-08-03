package com.example.stockapp.service;

import com.example.stockapp.DTO.Socks;
import com.example.stockapp.repository.SocksRepository;

import com.example.stockapp.exceptions.InsufficientQuantityException;
import com.example.stockapp.exceptions.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

        List<Socks> socksToDeduct = new ArrayList<>(socksList);

        for (Socks socks : socksToDeduct) {
            int socksToDeductAmount = Math.min(socks.getQuantity(), quantity);
            socks.setQuantity(socks.getQuantity() - socksToDeductAmount);
            quantity -= socksToDeductAmount;

            if (quantity <= 0) {
                if (socks.getQuantity() <= 0) {
                    socksRepository.delete(socks);
                } else {
                    socksRepository.save(socks);
                }
                break;
            }
        }
    }

    public String formatSocksList(List<Socks> socksList) {
        StringBuilder responseBuilder = new StringBuilder();
        for (Socks socks : socksList) {
            responseBuilder.append(socks.toString()).append("\n");
        }
        return responseBuilder.toString();
    }
    public List<Socks> getTotalSocks(String color, String operation, int cottonPart) {
        List<Socks> socksList;
        Integer totalQuantity = 0;

        if ("moreThan".equals(operation)) {
            socksList = socksRepository.findByColorAndCottonPartGreaterThan(color, cottonPart);
            totalQuantity = socksRepository.sumQuantityByColorAndCottonPartGreaterThan(color, cottonPart);
        } else if ("lessThan".equals(operation)) {
            socksList = socksRepository.findByColorAndCottonPartLessThan(color, cottonPart);
            totalQuantity = socksRepository.sumQuantityByColorAndCottonPartLessThan(color, cottonPart);
        } else if ("equal".equals(operation)) {
            socksList = socksRepository.findByColorAndCottonPart(color, cottonPart);
            totalQuantity = socksRepository.sumQuantityByColorAndCottonPart(color, cottonPart);
        } else {
            throw new InvalidRequestException("Invalid operation: " + operation);
        }

        Map<String, Socks> socksMap = new HashMap<>();
        for (Socks socks : socksList) {
            String key = socks.getColor() + "-" + socks.getCottonPart();
            if (socksMap.containsKey(key)) {
                Socks existingSocks = socksMap.get(key);
                existingSocks.setQuantity(existingSocks.getQuantity() + socks.getQuantity());
            } else {
                socksMap.put(key, new Socks(socks.getColor(), socks.getCottonPart(), socks.getQuantity()));
            }
        }

        List<Socks> totalSocksList = new ArrayList<>(socksMap.values());
        totalSocksList.add(new Socks("Total", 0, totalQuantity));
        return totalSocksList;
    }


    public void deleteSocks(Long id) {
        socksRepository.deleteById(id);
    }

}