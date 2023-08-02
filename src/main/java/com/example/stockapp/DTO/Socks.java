package com.example.stockapp.DTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "Socks")
public class Socks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "color")
    private String color;

    @Column(name = "cottonPart")
    private int cottonPart;

    @Column(name = "quantity")
    private int quantity;

    public Socks(Long id, String color, int cottonPart, int quantity) {
        this.id = id;
        this.color = color;
        this.cottonPart = cottonPart;
        this.quantity = quantity;
    }

    public Socks(String color, int cottonPart, int quantity) {

        this.color = color;
        this.cottonPart = cottonPart;
        this.quantity = quantity;
    }

    public Socks() {
    }

    public String  getColor() {
        return color;

    }
        public void setColor (String color) {
                this.color = color;
    }

        public int getCottonPart () {
            return cottonPart;
        }

        public void setCottonPart ( int cottonPart){
            this.cottonPart = cottonPart;
        }

        public int getQuantity () {
            return quantity;
        }

        public void setQuantity ( int quantity){
            this.quantity = quantity;
        }

        public Long getId () {
            return id;
        }

        public void setId (Long id){
            this.id = id;
        }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Socks socks = (Socks) o;
        return cottonPart == socks.cottonPart && quantity == socks.quantity && Objects.equals(id, socks.id) && Objects.equals(color, socks.color);
    }

    @Override
    public String toString() {
        if ("Total".equals(color)) {
            return "Total = " + quantity;
        } else {
            return "Socks{" +
                    "color='" + color + '\'' +
                    ", cottonPart=" + cottonPart +
                    ", quantity=" + quantity +
                    '}';
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, color, cottonPart, quantity);
    }
}
