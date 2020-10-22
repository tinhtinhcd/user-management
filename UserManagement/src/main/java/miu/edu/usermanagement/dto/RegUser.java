package miu.edu.usermanagement.dto;

import lombok.*;

import javax.validation.constraints.*;

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

}
