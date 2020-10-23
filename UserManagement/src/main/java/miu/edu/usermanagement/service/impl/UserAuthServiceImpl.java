package miu.edu.usermanagement.service.impl;

import miu.edu.usermanagement.dto.response.UserAuthResponse;
import miu.edu.usermanagement.entity.User;
import miu.edu.usermanagement.mapper.PrivilegeMapper;
import miu.edu.usermanagement.mapper.RoleMapper;
import miu.edu.usermanagement.mapper.UserMapper;
import miu.edu.usermanagement.repository.UserRepo;
import miu.edu.usermanagement.service.UserAuthService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    private UserRepo userRepo;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private final RoleMapper roleMapper = Mappers.getMapper(RoleMapper.class);
    private final PrivilegeMapper privilegeMapper = Mappers.getMapper(PrivilegeMapper.class);

    @Autowired
    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserAuthResponse findUserByUsername(String username) {
        AtomicReference<UserAuthResponse> response = new AtomicReference<>(new UserAuthResponse());
        Optional<User> user = userRepo.findUserByUsername(username);
        user.ifPresent(u -> response.set(userMapper.toUserAuthResponse(u)));
        return response.get();
    }
}
