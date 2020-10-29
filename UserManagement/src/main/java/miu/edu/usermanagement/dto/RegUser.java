package miu.edu.usermanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegUser {

    @NotNull
    @Size(max=10, message = "{error.username.length}")
    private String username;
    @NotNull
    @Size(min=8, message = "{error.password.length}")
//    @JsonIgnore
    private String password;

    private List<UserRoleDTO> roles;

}
