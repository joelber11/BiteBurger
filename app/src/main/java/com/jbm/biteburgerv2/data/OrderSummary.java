package com.jbm.biteburgerv2.data;

import java.util.Date;

public class OrderSummary {

    public enum OrderType {A_DOMICILIO, AUTO, LOCAL}

    private OrderType tipoPedido;
    private Date date;
    private double price;
    private int points;

    public OrderSummary() {}

    public OrderSummary(Date date, double price, int points) {
        this.date = date;
        this.price = price;
        this.points = points;
    }

    public OrderSummary(OrderType tipoPedido, Date date, double price, int points) {
        this.tipoPedido = tipoPedido;
        this.date = date;
        this.price = price;
        this.points = points;
    }


    public OrderType getTipoPedido() {
        return tipoPedido;
    }

    public void setTipoPedido(OrderType tipoPedido) {
        this.tipoPedido = tipoPedido;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }


    @Override
    public String toString() {
        return "OrderSummary{" +
                "tipoPedido=" + tipoPedido +
                ", date='" + date + '\'' +
                ", price=" + price +
                ", points=" + points +
                '}';
    }
}
