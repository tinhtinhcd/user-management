package miu.edu.usermanagement.service;

import miu.edu.usermanagement.dto.RegUser;
import miu.edu.usermanagement.entity.Address;
import miu.edu.usermanagement.entity.User;
import miu.edu.usermanagement.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
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
        //Mapping RegUser to User
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

        User retUser = userRepo.save(userEntity);
        return retUser;
    }
}
