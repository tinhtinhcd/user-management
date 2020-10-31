package miu.edu.usermanagement.service;

import miu.edu.usermanagement.dto.RegUser;
import miu.edu.usermanagement.dto.RoleDTO;
import miu.edu.usermanagement.dto.UserDTO;
import miu.edu.usermanagement.dto.UserIdDTO;
import miu.edu.usermanagement.entity.User;

import java.util.List;

public interface IUserService {
    public User addNewUser(RegUser newUser);
    public UserDTO queryUserByUserName(String userName);
    public List<RoleDTO> getListRoles();
    public boolean updateUserInfoByUsername(String userName, UserDTO dtoUser);
    public List<RegUser> getListUsers();
    public List<UserDTO> getListUserInfo();
    public List<UserDTO> getListUserInfoByIDs(List<Long> listUserIDs);
    public boolean addAddressByUsername(String userName, UserDTO dtoUser);
    public boolean removeAddressByUsername(String userName, Long addressId);
    public boolean setDefaultAddress(String userName, Long addressId);
}
