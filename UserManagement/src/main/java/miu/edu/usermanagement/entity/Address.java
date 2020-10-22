package miu.edu.usermanagement.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Address {
    @Id
    @GeneratedValue
    private int id;
    private String houseNumber;
    private String street;
    private String city;
    private String state;
    private String zipcode;
    private String country;
}
