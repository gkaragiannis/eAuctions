package com.dev.e_auctions.Model;

import java.util.Date;

public class User {
    private int id;
    private String username;
    private String name;
    private String surname;
    private String gender;
    private String date_of_birth;
    private int tax_id;
    private String email;
    private String phone_number;
    private String address;
    private int street_Number;
    private int postal_Code;
    private String location;
    private String country;


    public User() {
    }

    public User(int id, String username, String name, String surname, String gender, String date_of_birth, int tax_id, String email, String phone_number, String address, int street_Number, int postal_Code, String location, String country) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.tax_id = tax_id;
        this.email = email;
        this.phone_number = phone_number;
        this.address = address;
        this.street_Number = street_Number;
        this.postal_Code = postal_Code;
        this.location = location;
        this.country = country;
    }


    //Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public void setTax_id(int tax_id) {
        this.tax_id = tax_id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStreet_Number(int street_Number) {
        this.street_Number = street_Number;
    }

    public void setPostal_Code(int postal_Code) {
        this.postal_Code = postal_Code;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    //Getters

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getGender() {
        return gender;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public int getTax_id() {
        return tax_id;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getAddress() {
        return address;
    }

    public int getStreet_Number() {
        return street_Number;
    }

    public int getPostal_Code() {
        return postal_Code;
    }

    public String getLocation() {
        return location;
    }

    public String getCountry() {
        return country;
    }
}
