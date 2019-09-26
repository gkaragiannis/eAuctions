package com.dev.e_auctions.APIRequests;

/**
 *
 */
public class RegisterNewUserRequest {

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String country;
    private String city;
    private String address;
    private String afm;

    /**
     *
     * @param username
     * @param password
     * @param firstName
     * @param lastName
     * @param email
     * @param phone
     * @param country
     * @param city
     * @param address
     * @param afm
     */
    public RegisterNewUserRequest(String username, String password, String firstName, String lastName, String email, String phone, String country, String city, String address, String afm) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.country = country;
        this.city = city;
        this.address = address;
        this.afm = afm;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAfm(String afm) {
        this.afm = afm;
    }
}
