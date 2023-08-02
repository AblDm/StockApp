package com.example.stockapp.DTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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


    }
