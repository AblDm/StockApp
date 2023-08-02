package com.example.stockapp.DTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocksRepository extends JpaRepository<Socks, Long> {
    List<Socks> findByColorAndCottonPart(String color, int cottonPart);

    @Query("SELECT SUM(s.quantity) FROM Socks s WHERE s.color = :color AND s.cottonPart > :cottonPart")
    Integer sumQuantityByColorAndCottonPartGreaterThan(@Param("color") String color, @Param("cottonPart") int cottonPart);

    List<Socks> sumQuantityByColorAndCottonPartLessThan(String color, int cottonPart);

    @Query("SELECT s FROM Socks s WHERE s.color = :color AND s.cottonPart = :cottonPart")
    List<Socks> findMatchingSocks(String color, int cottonPart);
}
