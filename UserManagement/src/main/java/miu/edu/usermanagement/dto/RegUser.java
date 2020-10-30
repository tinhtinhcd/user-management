package miu.edu.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
