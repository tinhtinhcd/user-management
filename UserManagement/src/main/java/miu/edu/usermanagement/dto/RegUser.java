package miu.edu.usermanagement.dto;

import lombok.*;

import javax.validation.constraints.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegUser {

    @NotNull
    @Size(max=10)
    private String username;
    @NotNull
    @Size(max=10)
    private String password;
    @Size(max=20)
    private String firstName;
    @Size(max=20)
    private String lastName;
    @Email
    private String email;
    @Pattern(regexp="\\(d{3}\\)\\d{3}-\\d{4}")
    private String phone;
    @Size(min=2, max=6)
    private String houseNumber;
    @Size(max=20)
    private String street;
    @Size(max=20)
    private String city;
    @Size(min=2, max=2)
    private String state;
    @Pattern(regexp = "^\\d{5}$", message = "{error.zipcode}")
    private String zipcode;
    @Size(max=20)
    private String country;

    private List<UserRoleDTO> roles;

    private List<CardDTO> cards;

//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getHouseNumber() {
//        return houseNumber;
//    }
//
//    public void setHouseNumber(String houseNumber) {
//        this.houseNumber = houseNumber;
//    }
//
//    public String getStreet() {
//        return street;
//    }
//
//    public void setStreet(String street) {
//        this.street = street;
//    }
//
//    public String getCity() {
//        return city;
//    }
//
//    public void setCity(String city) {
//        this.city = city;
//    }
//
//    public String getState() {
//        return state;
//    }
//
//    public void setState(String state) {
//        this.state = state;
//    }
//
//    public String getZipcode() {
//        return zipcode;
//    }
//
//    public void setZipcode(String zipcode) {
//        this.zipcode = zipcode;
//    }
//
//    public String getCountry() {
//        return country;
//    }
//
//    public void setCountry(String country) {
//        this.country = country;
//    }
//
//    public List<UserRoleDTO> getListRoles() {
//        return roles;
//    }
//
//    public void setListRoles(List<UserRoleDTO> lstRoles) {
//        this.roles = lstRoles;
//    }
}
