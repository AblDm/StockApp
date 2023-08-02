package com.example.stockapp.DTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocksRepository extends JpaRepository<Socks, Long> {
    Socks findByColorAndCottonPart(String color, int cottonPart);
    int countByColorAndCottonPartGreaterThan(String color, int cottonPart);
    int countByColorAndCottonPartLessThan(String color, int cottonPart);
    int countByColorAndCottonPart(String color, int cottonPart);
}
