package miu.edu.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends UserIdDTO {

    @Column(nullable = true)
    @Size(max=10, message = "{error.username.length}")
    private String username;
    @Size(max=20)
    private String firstName;
    @Size(max=20)
    private String lastName;
    @Email(regexp = ".+@.+\\..+")
    private String email;
    @Pattern(regexp= "\\(\\d{3}\\)[- .]?\\d{3}-\\d{4}")//"^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$")
    private String phone;
    @Column(nullable = true)
    private Long addressId;
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
}
