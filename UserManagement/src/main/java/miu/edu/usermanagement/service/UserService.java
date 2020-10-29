package miu.edu.usermanagement.service;

import miu.edu.usermanagement.dto.*;
import miu.edu.usermanagement.entity.Address;
import miu.edu.usermanagement.entity.Card;
import miu.edu.usermanagement.entity.Role;
import miu.edu.usermanagement.entity.User;
import miu.edu.usermanagement.repository.UserRepo;
import miu.edu.usermanagement.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService{

    private UserRepo userRepo;
    private RoleRepo roleRepo;

//    private PasswordEncoder encoding;
    @Autowired
    private BCryptPasswordEncoder encoding;

    @Autowired
    public UserService(UserRepo userRepo, RoleRepo roleRepo){
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    @Override
    public User addNewUser(RegUser newUser){

        User retUser = null;
        Optional<User> user = userRepo.findUserByUsername(newUser.getUsername());

        if(!user.isPresent()) {
            User userEntity = new User();
            mapNewUserDtoToEntity(newUser, userEntity,true);
            retUser = userRepo.save(userEntity);
        }

        return retUser;
    }

    @Override
    public UserDTO queryUserByUserName(String userName){
        Optional<User> userList = userRepo.findUserByUsername(userName);
        UserDTO dtoUser = null;
        User entityUser = null;
        if(userList.isPresent()){
            entityUser = userList.get();
            dtoUser = mapUserEntityToDto(entityUser);
        }
        return dtoUser;
    }

    //Mapping User object from Entity to DTO
    private void mapNewUserEntityToDTO(User entityUser, RegUser dtoUser){

        dtoUser.setUsername(entityUser.getUsername());
        dtoUser.setPassword(entityUser.getPassword());

        List<UserRoleDTO> dtoRoles = new ArrayList<>();
        List<Role> listRoles = entityUser.getRoles();
        for(Role role : listRoles){
            UserRoleDTO dtoRole = new UserRoleDTO();
            dtoRole.setId(role.getId());
            dtoRoles.add(dtoRole);
        }
        dtoUser.setRoles(dtoRoles);
    }

    private UserDTO mapUserEntityToDto(User entityUser) {
        UserDTO dtoUser = new UserDTO();
        if(entityUser.getId() != null) {
            dtoUser.setId(entityUser.getId());
        }
        if(entityUser.getUsername() != null) {
            dtoUser.setUsername(entityUser.getUsername());
        }
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
            dtoUser.setAddressId(address.getId());
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

        List<Card> listCards = entityUser.getListCards();
        List<CardDTO> dtoCards = new ArrayList<>();
        for(Card card : listCards) {
            if (card.isDefault()) {
                CardDTO dtoCard = new CardDTO();
                dtoCard.setCardNumber(card.getCardNumber());
                dtoCard.setType(card.getType());
                dtoCard.setExpiredDate(card.getExpiredDate());
                dtoCard.setDefault(card.isDefault());
                dtoCards.add(dtoCard);
            }
        }
        dtoUser.setCards(dtoCards);

        return dtoUser;
    }

    //Mapping User object from DTO to Entity
    private void mapNewUserDtoToEntity(RegUser dtoUser, User entityUser, boolean bNewUser){
        entityUser.setUsername(dtoUser.getUsername());
        if(bNewUser) {
            entityUser.setPassword(encoding.encode(dtoUser.getPassword()));
        }
        else{
            entityUser.setPassword(dtoUser.getPassword());
        }
        List<Role> lRoles = new ArrayList<Role>();
        List<UserRoleDTO> listDTORoles = dtoUser.getRoles();
        List<Long> listRoles = listDTORoles.stream().map(o -> o.getId()).collect(Collectors.toList());
        lRoles = getListEntityRole(listRoles);
        entityUser.setRoles(lRoles);

        entityUser.setCreateDate(LocalDateTime.now());

        if(bNewUser) {
            entityUser.setStatus(bNewUser);
        }
    }

    private User mapUserDtoToEntity(UserDTO dtoUser){//}, boolean bNewUser) {
        User userEntity = new User();

//        userEntity.setUsername(dtoUser.getUsername());

//        if(bNewUser) {
//            //encrypt password
////            BCryptPasswordEncoder encoding = new BCryptPasswordEncoder(16);
////            String encodedPass = encoding.encode(dtoUser.getPassword());
////            userEntity.setPassword(encodedPass);
////
////            //try to decrypt
////            String checkedPass = dtoUser.getPassword();
////            if (!encoding.matches(checkedPass, encodedPass)) {
////                //The encoding is wrong
////                return null;
////            }
//        }
//        else{
//            userEntity.setPassword(dtoUser.getPassword());
//        }

        userEntity.setFirstName(dtoUser.getFirstName());
        userEntity.setLastName(dtoUser.getLastName());
        userEntity.setEmail(dtoUser.getEmail());
        userEntity.setPhone(dtoUser.getPhone());
//        userEntity.setStatus(bNewUser);

//        DateTimeFormatter curDateTime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//        LocalDateTime now = LocalDateTime.now();
//        System.out.println(curDateTime.format(now));

//        userEntity.setCreateDate(LocalDateTime.now());

        Address addr = new Address();
        addr.setHouseNumber(dtoUser.getHouseNumber());
        addr.setStreet(dtoUser.getStreet());
        addr.setCity(dtoUser.getCity());
        addr.setZipcode(dtoUser.getZipcode());
        addr.setState(dtoUser.getState());
        addr.setCountry(dtoUser.getCountry());
//        addr.setDefault(bNewUser);

        List<Address> lstAddr = new ArrayList<Address>();
        lstAddr.add(addr);
        userEntity.setLstAddress(lstAddr);

        List<Role> lRoles = new ArrayList<Role>();
        List<UserRoleDTO> listDTORoles = dtoUser.getRoles();
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

    @Override
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

    @Override
    public boolean updateUserInfoByUsername(String userName, UserDTO dtoUser) {
        boolean bUpdate = true;
        Optional<User> entityUser = userRepo.findUserByUsername(userName);
        if(!entityUser.isPresent()){
            bUpdate = false;
        }
        else{
            User user = entityUser.get();
            //Update changes to the current user object in the context
            if(dtoUser.getFirstName() != null) {
                user.setFirstName(dtoUser.getFirstName());
            }
            if(dtoUser.getLastName() != null){
                user.setLastName(dtoUser.getLastName());
            }
            if(dtoUser.getEmail() != null){
                user.setEmail(dtoUser.getEmail());
            }
            if(dtoUser.getPhone() != null){
                user.setPhone(dtoUser.getPhone());
            }

            List<Address> listAddress = user.getLstAddress();
            if(listAddress.size() == 0){
                Address addr = new Address();
                listAddress = new ArrayList<>();
                listAddress.add(addr);
                addr.setDefault(true);
                user.setLstAddress(listAddress);
            }

            //update address: if id is specified, relevant address is updated, otherwise the default address is updated
            Address addr = null;
            if(dtoUser.getAddressId() != null){
                Optional<Address> opAddr = listAddress.stream().filter(a -> a.getId() == dtoUser.getAddressId()).findFirst();
                if (opAddr.isPresent()) {
                    addr = opAddr.get();
                }
                else {//no exist the specified address id
                    bUpdate = false;
                }
            }
            else { // no address id is specified --> just update the address info to the default address
                Optional<Address> opAddr = listAddress.stream().filter(a -> a.isDefault() == true).findFirst();
                if(opAddr.isPresent()){
                    addr = opAddr.get();
                }
                else{// no default address --> pick the first one and set it as default
                    addr = listAddress.get(0);
                    addr.setDefault(true);
                }
            }

            if(bUpdate) {
                if (dtoUser.getHouseNumber() != null) {
                    addr.setHouseNumber(dtoUser.getHouseNumber());
                }
                if (dtoUser.getStreet() != null) {
                    addr.setStreet(dtoUser.getStreet());
                }
                if (dtoUser.getCity() != null) {
                    addr.setCity(dtoUser.getCity());
                }
                if (dtoUser.getState() != null) {
                    addr.setState(dtoUser.getState());
                }
                if (dtoUser.getZipcode() != null) {
                    addr.setZipcode(dtoUser.getZipcode());
                }
                if (dtoUser.getCountry() != null) {
                    addr.setCountry(dtoUser.getCountry());
                }

                //Update roles
                List<UserRoleDTO> listDtoRoles = dtoUser.getRoles();
                if(listDtoRoles != null && listDtoRoles.size() != 0){
                    List<Role> lRoles = user.getRoles();
                    //Empty the list of roles for adding new ones
                    while(lRoles.size() > 0){
                        lRoles.remove(0);
                    }

                    //TODO: Duplicated this code --> should make a common function
                    List<Long> listRoleIDs = listDtoRoles.stream().map(o -> o.getId()).collect(Collectors.toList());
                    lRoles = getListEntityRole(listRoleIDs);

                    user.setRoles(lRoles);
                }

                //save changes to DB
                userRepo.flush();
            }
        }

        return bUpdate;
    }

    @Override
    public List<RegUser> getListUsers() {
        List<RegUser> retListUser = new ArrayList<>();

        List<User> listUser = userRepo.findAll();
        if(listUser != null && listUser.size() != 0){
            for(User user : listUser){
                RegUser dtoUser = new RegUser();
                mapNewUserEntityToDTO(user, dtoUser);
                retListUser.add(dtoUser);
            }
        }

        return retListUser;
    }

    @Override
    public List<UserDTO> getListUserInfo() {
        List<UserDTO> retListUser = new ArrayList<>();

        List<User> listUser = userRepo.findAll();
        if(listUser != null && listUser.size() != 0){
            for(User user : listUser){
                UserDTO dtoUser = new UserDTO();
                dtoUser = mapUserEntityToDto(user);
                retListUser.add(dtoUser);
            }
        }

        return retListUser;
    }

    @Override
    public List<UserDTO> getListUserInfoByIDs(List<UserIdDTO> listUserIDs) {
        List<UserDTO> retListUser = new ArrayList<>();

        List<Integer> listIDs = listUserIDs.stream().map(u -> u.getId()).collect(Collectors.toList());
//        for(UserIdDTO userID : listUserIDs){
//            userRepo.findById(userID.getId());
//        }

        List<User> listUser = userRepo.findAllById(listIDs);
        if(listUser != null && listUser.size() != 0){
            for(User user : listUser){
                UserDTO dtoUser = new UserDTO();
                dtoUser = mapUserEntityToDto(user);
                retListUser.add(dtoUser);
            }
        }

        return retListUser;
    }
}
