package miu.edu.usermanagement.service;

import miu.edu.usermanagement.dto.CardDTO;
import miu.edu.usermanagement.dto.RegUser;
import miu.edu.usermanagement.dto.RoleDTO;
import miu.edu.usermanagement.dto.UserRoleDTO;
import miu.edu.usermanagement.entity.Address;
import miu.edu.usermanagement.entity.Card;
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
public class UserService implements IUserService{

    private UserRepo userRepo;
    private RoleRepo roleRepo;

//    @Autowired
//    private PasswordEncoder encoding;

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
    private User mapUserDtoToEntity(RegUser dtoUser, boolean bNewUser) {
        User userEntity = new User();
        userEntity.setUsername(dtoUser.getUsername());

        if(bNewUser) {
            //encrypt password
            BCryptPasswordEncoder encoding = new BCryptPasswordEncoder(16);
            String encodedPass = encoding.encode(dtoUser.getPassword());
            userEntity.setPassword(encodedPass);

            //try to decrypt
            String checkedPass = dtoUser.getPassword();
            if (!encoding.matches(checkedPass, encodedPass)) {
                //The encoding is wrong
                return null;
            }
        }
        else{
            userEntity.setPassword(dtoUser.getPassword());
        }

        userEntity.setFirstName(dtoUser.getFirstName());
        userEntity.setLastName(dtoUser.getLastName());
        userEntity.setEmail(dtoUser.getEmail());
        userEntity.setPhone(dtoUser.getPhone());
        userEntity.setStatus(bNewUser);

//        DateTimeFormatter curDateTime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//        LocalDateTime now = LocalDateTime.now();
//        System.out.println(curDateTime.format(now));

        userEntity.setCreateDate(LocalDateTime.now());

        Address addr = new Address();
        addr.setHouseNumber(dtoUser.getHouseNumber());
        addr.setStreet(dtoUser.getStreet());
        addr.setCity(dtoUser.getCity());
        addr.setZipcode(dtoUser.getZipcode());
        addr.setState(dtoUser.getState());
        addr.setCountry(dtoUser.getCountry());
        addr.setDefault(bNewUser);

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

    public boolean updateUserInfoByUsername(String userName, RegUser dtoUser) {
        boolean bUpdate = true;
        Optional<User> entityUser = userRepo.findUserByUsername(userName);
        if(!entityUser.isPresent()){
            bUpdate = false;
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
        }

        return bUpdate;
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
