package miu.edu.usermanagement.service;

import miu.edu.usermanagement.dto.RegUser;
import miu.edu.usermanagement.dto.RoleDTO;
import miu.edu.usermanagement.entity.User;

import java.util.List;

public interface IUserService {
    public User addNewUser(RegUser newUser);
    public RegUser queryUserByUserName(String userName);
    public List<RoleDTO> getListRoles();
    public boolean updateUserInfoByUsername(String userName, RegUser dtoUser);
    public List<RegUser> getListUsers();
}
