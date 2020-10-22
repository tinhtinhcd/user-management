package miu.edu.usermanagement.service;

import miu.edu.usermanagement.dto.RegUser;
import miu.edu.usermanagement.entity.Address;
import miu.edu.usermanagement.entity.User;
import miu.edu.usermanagement.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    public User addNewUser(RegUser newUser){
        User userEntity = mapUserDtoToEntity(newUser);
        List<User> userList = userRepo.findUserByUsername(userEntity.getUsername());
        if(userList != null && userList.size() != 0){
            return null;
        }
        User retUser = userRepo.save(userEntity);
        return retUser;
    }

    public RegUser queryUserByUserName(String userName){
        List<User> userList = userRepo.findUserByUsername(userName);
        RegUser dtoUser = null;
        User entityUser = null;
        if(userList != null && userList.size() != 0){
            entityUser = userList.get(0);
            dtoUser = mapUserEntityToDto(entityUser);
        }

        return dtoUser;
    }

    //Mapping User object from Entity to DTO
    private RegUser mapUserEntityToDto(User entityUser) {
        RegUser dtoUser = new RegUser();
        dtoUser.setUsername(entityUser.getUsername());
        dtoUser.setPassword(entityUser.getPassword());
        dtoUser.setFirstName(entityUser.getFirstName());
        dtoUser.setLastName(entityUser.getLastName());
        dtoUser.setEmail(entityUser.getEmail());
        dtoUser.setPhone(entityUser.getPhone());
        Address address = entityUser.getLstAddress().get(0);
        dtoUser.setHouseNumber(address.getHouseNumber());
        dtoUser.setStreet(address.getStreet());
        dtoUser.setCity(address.getCity());
        dtoUser.setState(address.getState());
        dtoUser.setState(address.getZipcode());
        dtoUser.setCountry(address.getCountry());

        return dtoUser;
    }

    //Mapping User object from DTO to Entity
    private User mapUserDtoToEntity(RegUser newUser) {
        User userEntity = new User();
        userEntity.setUsername(newUser.getUsername());
        userEntity.setPassword(newUser.getPassword());
        userEntity.setFirstName(newUser.getFirstName());
        userEntity.setLastName(newUser.getLastName());
        userEntity.setEmail(newUser.getEmail());
        userEntity.setPhone(newUser.getPhone());

        Address addr = new Address();
        addr.setHouseNumber(newUser.getHouseNumber());
        addr.setStreet(newUser.getStreet());
        addr.setCity(newUser.getCity());
        addr.setZipcode(newUser.getZipcode());
        addr.setState(newUser.getState());
        addr.setCountry(newUser.getCountry());

        List<Address> lstAddr = new ArrayList<Address>();
        lstAddr.add(addr);
        userEntity.setLstAddress(lstAddr);

        return userEntity;
    }
}
