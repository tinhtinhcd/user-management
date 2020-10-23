package miu.edu.usermanagement.mapper;

import miu.edu.usermanagement.dto.response.PrivilegeResponse;
import miu.edu.usermanagement.entity.Privilege;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface PrivilegeMapper {
    PrivilegeResponse toPrivilege(Privilege privilege);
    List<PrivilegeResponse> toListResponse(List<Privilege> roles);
}
