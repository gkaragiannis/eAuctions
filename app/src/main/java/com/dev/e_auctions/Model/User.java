package com.dev.e_auctions.Model;

import java.util.Date;

public class User {
    private String Username;
    private String Name;
    private String Surname;
    private String Gender;
    private Date DateOfBirth;
    private Integer TaxId;
    private String Email;
    private String Phone;
    private Address UserAddress;

    public User(){

    }

    public User (String username, String name, String surname, String gender,
                 Date dateOfBirth, Integer taxId, String email, String phone, Address userAddress){
        this.setUsername(username);
        this.setName(name);
        this.setSurname(surname);
        this.setGender(gender);
        this.setDateOfBirth(dateOfBirth);
        this.setTaxId(taxId);
        this.setEmail(email);
        this.setPhone(phone);
        this.setUserAddress(userAddress);
    }

    //Setters
    public void setUsername(String username) {
        this.Username = username;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setSurname(String surname) {
        this.Surname = surname;
    }

    public void setGender(String gender) {
        this.Gender = gender;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.DateOfBirth = dateOfBirth;
    }

    public void setTaxId(Integer taxId) {
        this.TaxId = taxId;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public void setUserAddress(Address userAddress) {
        this.UserAddress = userAddress;
    }

    //Getters
    public String getUsername() {
        return this.Username;
    }

    public String getName() {
        return this.Name;
    }

    public String getSurname() {
        return this.Surname;
    }

    public String getGender() {
        return this.Gender;
    }

    public Date getDateOfBirth() {
        return this.DateOfBirth;
    }

    public Integer getTaxId() {
        return this.TaxId;
    }

    public String getEmail() {
        return this.Email;
    }

    public String getPhone() {
        return this.Phone;
    }

    public Address getUserAddress() {
        return this.UserAddress;
    }
}
