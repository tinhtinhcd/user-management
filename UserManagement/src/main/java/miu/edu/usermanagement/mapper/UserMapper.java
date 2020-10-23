package miu.edu.usermanagement.mapper;

import miu.edu.usermanagement.dto.response.UserAuthResponse;
import miu.edu.usermanagement.entity.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface UserMapper {

    UserAuthResponse toUserAuthResponse(User user);

    @AfterMapping
    default void setPassword(@MappingTarget UserAuthResponse response, User user) {
        response.setPassword(user.getPassword());
    }
}
