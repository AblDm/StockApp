package com.example.stockapp.DTO;

import jakarta.persistence.*;

import java.awt.*;

@Entity
public class SocksOperations {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;
    Color color;
    int cottonPart;
    int quantity;

    public Color getColor() {
        return color;

    }
        public void setColor (Color color) {
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

        public Integer getId () {
            return id;
        }

        public void setId (Integer id){
            this.id = id;
        }


    }
