package miu.edu.usermanagement.mapper;

import miu.edu.usermanagement.dto.response.RoleResponse;
import miu.edu.usermanagement.entity.Role;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper {
    RoleResponse toResponse(Role role);
    List<RoleResponse> toListResponse(List<Role> roles);
}
