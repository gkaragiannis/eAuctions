package com.dev.e_auctions.Model;

import java.lang.reflect.Constructor;

public class Address {

    private String Street;
    private String City;
    private String Country;
    private Integer StreetNumber;
    private Integer PostalCode;

    public Address(){

    }

    public Address(String street, String city, String country, Integer streetNumber, Integer postalCode){
        this.setCountry(country);
        this.setCity(city);
        this.setPostalCode(postalCode);
        this.setStreet(street);
        this.setStreetNumber(streetNumber);
    }

    //Setters
    public void setCountry(String country) {
        this.Country = country;
    }

    public void setCity(String city) {
        this.City = city;
    }

    public void setPostalCode(Integer postalCode) {
        this.PostalCode = postalCode;
    }

    public void setStreet(String street) {
        this.Street = street;
    }

    public void setStreetNumber(Integer streetNumber) {
        this.StreetNumber = streetNumber;
    }

    //Getter
    public String getCountry() {
        return this.Country;
    }

    public String getCity() {
        return this.City;
    }

    public Integer getPostalCode() {
        return this.PostalCode;
    }

    public String getStreet() {
        return this.Street;
    }

    public Integer getStreetNumber() {
        return this.StreetNumber;
    }
}
