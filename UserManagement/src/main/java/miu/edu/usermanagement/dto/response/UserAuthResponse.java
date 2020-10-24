package miu.edu.usermanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import miu.edu.usermanagement.entity.BaseModel;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthResponse{
    private Long id ;
    private String username;
    private String password;
    private List<RoleResponse> roles;

    public void setPassword(String password) {
        this.password = password;
    }
}
