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
    @Size(max=10, message = "{error.length.max}")
    private String username;
    @Size(max=20, message = "{error.length.max}")
    private String firstName;
    @Size(max=20, message = "{error.length.max}")
    private String lastName;
    @Email(regexp = ".+@.+\\..+", message = "{error.email.format}")
    private String email;
    @Pattern(regexp= "\\(\\d{3}\\)[- .]?\\d{3}-\\d{4}", message = "{error.phone.format}")//"^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$")
    private String phone;

    private List<AddressDTO> addresses;
    private List<RoleDTO> roles;
    private List<CardDTO> cards;
}
