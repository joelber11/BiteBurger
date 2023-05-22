package com.jbm.biteburgerv2.data;

public class Address {
    private String street;
    private int number;
    private String floorStairs;
    private String city;
    private int postCode;
    private String province;
    private boolean favorite;

    public Address() {
    }

    public Address(String street, int number, String city, int postCode, String province, boolean favorite) {
        this.street = street;
        this.number = number;
        this.city = city;
        this.postCode = postCode;
        this.province = province;
        this.favorite = favorite;
    }

    public Address(String street, int number, String floorStairs, String city, int postCode, String province, boolean favorite) {
        this.street = street;
        this.number = number;
        this.floorStairs = floorStairs;
        this.city = city;
        this.postCode = postCode;
        this.province = province;
        this.favorite = favorite;
    }


    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getFloorStairs() {
        return floorStairs;
    }

    public void setFloorStairs(String floorStairs) {
        this.floorStairs = floorStairs;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "Direccion{" +
                "street='" + street + '\'' +
                ", number=" + number +
                ", floorStairs='" + floorStairs + '\'' +
                ", city='" + city + '\'' +
                ", postCode=" + postCode +
                ", province='" + province + '\'' +
                '}';
    }
}
