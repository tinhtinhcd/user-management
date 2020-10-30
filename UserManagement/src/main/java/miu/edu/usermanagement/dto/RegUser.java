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

    @NotNull(message = "{error.field.notnull}")
    @Size(max=10, message = "{error.length.max}")
    private String username;
    @NotNull(message = "{error.field.notnull}")
    @Size(min=8, message = "{error.length.min}")
//    @JsonIgnore
    private String password;

    private List<UserRoleDTO> roles;

}
