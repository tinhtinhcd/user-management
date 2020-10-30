package miu.edu.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleDTO {
    @NotNull(message = "{error.field.notnull}")
    private long id;

}
