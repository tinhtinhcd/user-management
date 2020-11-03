package miu.edu.usermanagement.service;

import miu.edu.usermanagement.dto.*;
import miu.edu.usermanagement.entity.User;

import java.util.List;

public interface IUserService {
    public UserDTO addNewUser(RegUser newUser);
    public UserDTO queryUserByUserName(String userName);
    public List<RoleDTO> getListRoles();
    public boolean updateUserInfoByUsername(String userName, UserDTO dtoUser);
    public List<RegUser> getListUsers();
    public List<UserDTO> getListUserInfo(int status);
    public List<UserDTO> getListUserInfoByIDs(List<Long> listUserIDs);
    public PaymentDTO queryDefaultInfoByUserName(String userName);
    public boolean setUserStatus(String userName, boolean status);
    public boolean addAddressByUsername(String userName, AddressDTO dtoAddress);
    public boolean updateAddressByUsername(String userName, Long addressId, AddressDTO dtoAddress);
    public boolean removeAddressByUsername(String userName, Long addressId);
    public boolean setDefaultAddress(String userName, Long addressId);

}
