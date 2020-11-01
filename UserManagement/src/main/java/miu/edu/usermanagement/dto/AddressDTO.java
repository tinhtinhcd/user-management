package miu.edu.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    @Column(nullable = true)
    private Long addressId;
    @Size(min=2, max=6, message = "{error.length.minmax}")
    private String houseNumber;
    @Size(max=20, message = "{error.length.max}")
    private String street;
    @Size(max=20, message = "{error.length.max}")
    private String city;
    @Size(min=2, max=2, message = "{error.length.minmax}")
    private String state;
    @Pattern(regexp = "^\\d{5}$", message = "{error.zipcode}")
    private String zipcode;
    @Size(max=20, message = "{error.length.max}")
    private String country;
    boolean defaultAddress;
}
