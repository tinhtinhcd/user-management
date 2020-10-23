package miu.edu.usermanagement.service;

import miu.edu.usermanagement.dto.response.UserAuthResponse;
import miu.edu.usermanagement.entity.User;

public interface UserAuthService {
    UserAuthResponse findUserByUsername(String username);
}
