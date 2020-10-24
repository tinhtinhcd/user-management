package miu.edu.usermanagement.service;

import miu.edu.usermanagement.dto.RegUser;
import miu.edu.usermanagement.dto.RoleDTO;
import miu.edu.usermanagement.dto.UserRoleDTO;
import miu.edu.usermanagement.entity.Address;
import miu.edu.usermanagement.entity.Role;
import miu.edu.usermanagement.entity.User;
import miu.edu.usermanagement.repository.UserRepo;
import miu.edu.usermanagement.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepo userRepo;
    private RoleRepo roleRepo;

//    @Autowired
//    private PasswordEncoder encoding;
//
//    //    @Bean(name = "bCryptPasswordEncoder")
//    @Bean
//    public PasswordEncoder encoder() {
//        return new BCryptPasswordEncoder();
//    }​​​​​

//    private BCryptPasswordEncoder encoder;

//    @Autowired
//    @Qualifier("bCryptPasswordEncoder")
//    public void setEncoder(BCryptPasswordEncoder encoder) {​​​​​
//        this.encoder = encoder;
//    }​​​​​

    @Autowired
    public UserService(UserRepo userRepo, RoleRepo roleRepo){
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }



    public User addNewUser(RegUser newUser){

        User retUser = null;
        Optional<User> user = userRepo.findUserByUsername(newUser.getUsername());

        if(!user.isPresent()) {
            User userEntity = mapUserDtoToEntity(newUser, true);
            retUser = userRepo.save(userEntity);
        }

        return retUser;
    }

    public RegUser queryUserByUserName(String userName){
        Optional<User> userList = userRepo.findUserByUsername(userName);
        RegUser dtoUser = null;
        User entityUser = null;
        if(userList.isPresent()){
            entityUser = userList.get();
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
        List<Address> lstAddr = entityUser.getLstAddress();
        Address address = null;
        for(Address addr : lstAddr){
            if(addr.isDefault()){
                address = addr;
                break;
            }
        }

        //if no default address, just get the first address as default
        if(address == null && lstAddr.size() > 0) {
            address = lstAddr.get(0);
        }

        if(address != null){
            dtoUser.setHouseNumber(address.getHouseNumber());
            dtoUser.setStreet(address.getStreet());
            dtoUser.setCity(address.getCity());
            dtoUser.setState(address.getState());
            dtoUser.setZipcode(address.getZipcode());
            dtoUser.setCountry(address.getCountry());
        }

        List<UserRoleDTO> dtoRoles = new ArrayList<>();
        List<Role> listRoles = entityUser.getRoles();
        for(Role role : listRoles){
            UserRoleDTO dtoRole = new UserRoleDTO();
            dtoRole.setId(role.getId());
            dtoRoles.add(dtoRole);
        }
        dtoUser.setRoles(dtoRoles);

        return dtoUser;
    }

    //Mapping User object from DTO to Entity
    private User mapUserDtoToEntity(RegUser newUser, boolean bNewUser) {
        User userEntity = new User();
        userEntity.setUsername(newUser.getUsername());

        userEntity.setPassword(newUser.getPassword());
        userEntity.setFirstName(newUser.getFirstName());
        userEntity.setLastName(newUser.getLastName());
        userEntity.setEmail(newUser.getEmail());
        userEntity.setPhone(newUser.getPhone());
        userEntity.setStatus(bNewUser);

//        DateTimeFormatter curDateTime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//        LocalDateTime now = LocalDateTime.now();
//        System.out.println(curDateTime.format(now));

        userEntity.setCreatedDate(LocalDateTime.now());

        Address addr = new Address();
        addr.setHouseNumber(newUser.getHouseNumber());
        addr.setStreet(newUser.getStreet());
        addr.setCity(newUser.getCity());
        addr.setZipcode(newUser.getZipcode());
        addr.setState(newUser.getState());
        addr.setCountry(newUser.getCountry());
        addr.setDefault(bNewUser);

        List<Address> lstAddr = new ArrayList<Address>();
        lstAddr.add(addr);
        userEntity.setLstAddress(lstAddr);

        List<Role> lRoles = new ArrayList<Role>();
        List<UserRoleDTO> listDTORoles = newUser.getRoles();
        List<Long> listRoles = listDTORoles.stream().map(o -> o.getId()).collect(Collectors.toList());
        lRoles = getListEntityRole(listRoles);//getListRoles().stream().filter(o -> listRoles.contains(o.getId())).collect((Collectors.toList()));

//        for(UserRoleDTO dtoRole : listDTORoles){
//            Role role = new Role();
//
//            role.setId(dtoRole.getId());
////            entityRole.setName(role.getName());
////            entityRole.setDescription(role.getDescription());
//            lRoles.add(role);
//        }
        userEntity.setRoles(lRoles);

        return userEntity;
    }

    private List<Role> getListEntityRole(List<Long> listRoles){
        List<Role> lRole = roleRepo.findAllById(listRoles);
        return lRole;
    }

    public List<RoleDTO> getListRoles() {
        List<RoleDTO> listRoles = new ArrayList<>();

        List<Role> listRole = roleRepo.findAll();
        //mapping roles from entity to DTO
        for(Role role : listRole){
            RoleDTO dtoRole = new RoleDTO();
            dtoRole.setId(role.getId());
            dtoRole.setName(role.getName());
            dtoRole.setDescription(role.getDescription());
            listRoles.add(dtoRole);
        }

        return listRoles;
    }

    public String updateUserInfoByUsername(String userName, RegUser dtoUser) {
        String retMessage = "";
        Optional<User> entityUser = userRepo.findUserByUsername(userName);
        if(!entityUser.isPresent()){
            retMessage = "The user " + userName + " doesn't exist";
        }
        else{
            User user = entityUser.get();
            //Update changes to the current user object in the context
            user.setFirstName(dtoUser.getFirstName());
            user.setLastName(dtoUser.getLastName());
            //TODO Add other fields here
            //....

            //save changes to DB
            userRepo.flush();
            retMessage = "The user " + userName + " information was updated successfully";
        }

        return retMessage;
    }

    public List<RegUser> getListUsers() {
        List<RegUser> retListUser = new ArrayList<>();

        List<User> listUser = userRepo.findAll();
        if(listUser != null && listUser.size() != 0){
            for(User user : listUser){
                RegUser dtoUser = mapUserEntityToDto(user);
                retListUser.add(dtoUser);
            }
        }

        return retListUser;
    }
}
