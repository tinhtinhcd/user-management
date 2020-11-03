package miu.edu.usermanagement.service;

import miu.edu.usermanagement.dto.*;
import miu.edu.usermanagement.entity.Address;
import miu.edu.usermanagement.entity.Card;
import miu.edu.usermanagement.entity.Role;
import miu.edu.usermanagement.entity.User;
import miu.edu.usermanagement.exception.AddressNotFoundException;
import miu.edu.usermanagement.exception.UserHasNoDefaultInfo;
import miu.edu.usermanagement.exception.UsernameExistedException;
import miu.edu.usermanagement.exception.UsernameNotFoundException;
import miu.edu.usermanagement.repository.AddressRepo;
import miu.edu.usermanagement.repository.UserRepo;
import miu.edu.usermanagement.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService{

    private UserRepo userRepo;
    private RoleRepo roleRepo;
    private AddressRepo addressRepo;

//    private PasswordEncoder encoding;
    @Autowired
    private BCryptPasswordEncoder encoding;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    public UserService(UserRepo userRepo, RoleRepo roleRepo, AddressRepo addressRepo){
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.addressRepo = addressRepo;
    }

    @Override
    public UserDTO addNewUser(RegUser newUser){

        UserDTO dtoUser = new UserDTO();
        Optional<User> user = userRepo.findUserByUsername(newUser.getUsername());

        if(!user.isPresent()) {
            User userEntity = new User();
            mapNewUserDtoToEntity(newUser, userEntity,true);
            userEntity = userRepo.save(userEntity);
            //mapping user from entity to dto
            dtoUser = mapUserEntityToDto(userEntity, null, null);
        }
        else{
            throw new UsernameExistedException(newUser.getUsername());
        }

        return dtoUser;
    }

    @Override
    public UserDTO queryUserByUserName(String userName){
        Optional<User> userList = userRepo.findUserByUsername(userName);
        UserDTO dtoUser = null;
        User entityUser = null;
        if(userList.isPresent()){
            entityUser = userList.get();
            dtoUser = mapUserEntityToDto(entityUser, null, null);
        }
        else{
            throw new UsernameNotFoundException(userName);
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

    private UserDTO mapUserEntityToDto(User entityUser, Address defAddress, Card defCard) {
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
        dtoUser.setEnable(entityUser.isStatus());

        //mapping addresses from entity to dto
        List<AddressDTO> lstAddrDTO = new ArrayList<>();
        if(defAddress != null){
            lstAddrDTO.add(mapAddressEntityToDto(defAddress));
        }
        else {
            List<Address> lstAddr = entityUser.getLstAddress();
            if (lstAddr != null) {
                for (Address address : lstAddr) {
                    lstAddrDTO.add(mapAddressEntityToDto(address));
                }
            }
        }
        dtoUser.setAddresses(lstAddrDTO);

        //TODO 8 lines of code are duplicated
        List<RoleDTO> dtoRoles = new ArrayList<>();
        List<Role> listRoles = entityUser.getRoles();
        for(Role role : listRoles){
            RoleDTO dtoRole = new RoleDTO();
            dtoRole.setId(role.getId());
            dtoRole.setName(role.getName());
            dtoRole.setDescription(role.getDescription());
            dtoRoles.add(dtoRole);
        }
        dtoUser.setRoles(dtoRoles);

        List<CardDTO> dtoCards = new ArrayList<>();
        if(defCard != null){
            dtoCards.add(mapCardEntityToDto(defCard));
        }
        else {
            List<Card> listCards = entityUser.getListCards();
            if (listCards != null) {
                for (Card card : listCards) {
                    dtoCards.add(mapCardEntityToDto(card));
                }
            }
        }
        dtoUser.setCards(dtoCards);

        return dtoUser;
    }

    private AddressDTO mapAddressEntityToDto(Address address){
        AddressDTO dtoAddr = new AddressDTO();
        dtoAddr.setAddressId(address.getId());
        dtoAddr.setHouseNumber(address.getHouseNumber());
        dtoAddr.setStreet(address.getStreet());
        dtoAddr.setCity(address.getCity());
        dtoAddr.setState(address.getState());
        dtoAddr.setZipcode(address.getZipcode());
        dtoAddr.setCountry(address.getCountry());
        dtoAddr.setDefaultAddress(address.isDefault());

        return dtoAddr;
    }

    private CardDTO mapCardEntityToDto(Card card){
        CardDTO dtoCard = new CardDTO();
        dtoCard.setCardNumber(card.getCardNumber());
        dtoCard.setName(card.getName());
        dtoCard.setCvv(card.getCvv());
        dtoCard.setExpiredDate(card.getExpiredDate());
        dtoCard.setDefault(card.isDefault());

        return dtoCard;
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

        userEntity.setFirstName(dtoUser.getFirstName());
        userEntity.setLastName(dtoUser.getLastName());
        userEntity.setEmail(dtoUser.getEmail());
        userEntity.setPhone(dtoUser.getPhone());

        List<Address> lstAddr = new ArrayList<Address>();
        List<AddressDTO> listAddressDTO = dtoUser.getAddresses();
        for(AddressDTO dtoAddress : listAddressDTO) {
            Address addr = new Address();
            addr.setHouseNumber(dtoAddress.getHouseNumber());
            addr.setStreet(dtoAddress.getStreet());
            addr.setCity(dtoAddress.getCity());
            addr.setZipcode(dtoAddress.getZipcode());
            addr.setState(dtoAddress.getState());
            addr.setCountry(dtoAddress.getCountry());
            addr.setDefault(dtoAddress.isDefaultAddress());
            lstAddr.add(addr);
        }

        userEntity.setLstAddress(lstAddr);

        List<RoleDTO> listDTORoles = dtoUser.getRoles();
        List<Long> listRoles = listDTORoles.stream().map(o -> o.getId()).collect(Collectors.toList());
        List<Role> lRoles = getListEntityRole(listRoles);//getListRoles().stream().filter(o -> listRoles.contains(o.getId())).collect((Collectors.toList()));

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
            throw new UsernameNotFoundException(userName);
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
            List<AddressDTO> listAddressDto = dtoUser.getAddresses();
            if(listAddress.size() != 0 && listAddressDto != null && listAddressDto.size() != 0){
                for(AddressDTO addressDto : listAddressDto){
                    Optional<Address> opAddress = listAddress.stream().filter(a -> a.getId() == addressDto.getAddressId()).findFirst();
                    if(opAddress.isPresent()){ //if the same address id --> update address data
                        Address address = opAddress.get();
                        if (addressDto.getHouseNumber() != null) {
                            address.setHouseNumber(addressDto.getHouseNumber());
                        }
                        if (addressDto.getStreet() != null) {
                            address.setStreet(addressDto.getStreet());
                        }
                        if (addressDto.getCity() != null) {
                            address.setCity(addressDto.getCity());
                        }
                        if (addressDto.getState() != null) {
                            address.setState(addressDto.getState());
                        }
                        if (addressDto.getZipcode() != null) {
                            address.setZipcode(addressDto.getZipcode());
                        }
                        if (addressDto.getCountry() != null) {
                            address.setCountry(addressDto.getCountry());
                        }
                        if (addressDto.isDefaultAddress()){
                            address.setDefault(addressDto.isDefaultAddress());
                        }
                    }
                    else{
                        throw new AddressNotFoundException(addressDto.getAddressId());
                    }
                }
            }

            //Update roles
            List<RoleDTO> listDtoRoles = dtoUser.getRoles();
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
    public List<UserDTO> getListUserInfo(int status) {
        List<UserDTO> retListUser = new ArrayList<>();
        List<User> listUser = null;
        switch (status) {
            case 2:
                listUser = userRepo.findAll();
                break;
            case 1:
                listUser = userRepo.findAllByStatus(true);
                break;
            case 0:
                listUser = userRepo.findAllByStatus(false);
                break;
            default:
                //wrong param
        }
        if(listUser != null && listUser.size() != 0){
            for(User user : listUser){
                UserDTO dtoUser = new UserDTO();
                dtoUser = mapUserEntityToDto(user, null, null);
                retListUser.add(dtoUser);
            }
        }

        return retListUser;
    }

    @Override
    public List<UserDTO> getListUserInfoByIDs(List<Long> listIDs) {
        List<UserDTO> retListUser = new ArrayList<>();

        List<User> listUser = userRepo.findByIdIn(listIDs);
        if(listUser != null && listUser.size() != 0){
            for(User user : listUser){
                UserDTO dtoUser = new UserDTO();
                dtoUser = mapUserEntityToDto(user, null, null);
                retListUser.add(dtoUser);
            }
        }

        return retListUser;
    }

    @Override
    public UserDTO queryDefaultInfoByUserName(String userName) {
        Optional<User> userList = userRepo.findUserByUsername(userName);
        UserDTO dtoUser = null;
        User entityUser = null;
        Address defAddr = null;
        Card defCard = null;

        if(userList.isPresent()){
            entityUser = userList.get();
            //checking default address
            List<Address> listAddr = entityUser.getLstAddress();
            if(listAddr == null || listAddr.size() == 0){
                throw new UserHasNoDefaultInfo(userName, messageSource.getMessage("user.address.default", null, Locale.US));
            }
            else{
                Optional<Address> opAddr = listAddr.stream().filter(a -> a.isDefault() == true).findFirst();
                if(opAddr.isPresent()){
                    defAddr = opAddr.get();
                }
                else{
                    throw new UserHasNoDefaultInfo(userName, messageSource.getMessage("user.address.default", null, Locale.US));
                }
            }

            //checking default card
            List<Card> listCard = entityUser.getListCards();
            if(listCard == null || listCard.size() == 0){
                throw new UserHasNoDefaultInfo(userName, messageSource.getMessage("user.card.default", null, Locale.US));
            }
            else{
                Optional<Card> opCard = listCard.stream().filter(a -> a.isDefault() == true).findFirst();
                if(opCard.isPresent()){
                    defCard = opCard.get();
                }
                else{
                    throw new UserHasNoDefaultInfo(userName, messageSource.getMessage("user.card.default", null, Locale.US));
                }
            }

            dtoUser = mapUserEntityToDto(entityUser, defAddr, defCard);
        }
        else{
            throw new UsernameNotFoundException(userName);
        }
        return dtoUser;
    }

    @Override
    public boolean setUserStatus(String userName, boolean status) {
        Optional<User> opUser = userRepo.findUserByUsername(userName);
        if(opUser.isPresent()){
            User user = opUser.get();
            user.setStatus(status);
            userRepo.flush();
        }
        else{
            throw new UsernameNotFoundException(userName);
        }
        return true;
    }

    @Override
    public boolean addAddressByUsername(String userName, AddressDTO dtoAddress) {
        Optional<User> opUser = userRepo.findUserByUsername(userName);
        User user = null;
        if(opUser.isPresent()){
            user = opUser.get();
            List<Address> listAddress = user.getLstAddress();
            if(listAddress == null){
                listAddress = new ArrayList<>();
            }
            Address addr = new Address();
            addr.setHouseNumber(dtoAddress.getHouseNumber());
            addr.setStreet(dtoAddress.getStreet());
            addr.setCity(dtoAddress.getCity());
            addr.setState(dtoAddress.getState());
            addr.setZipcode(dtoAddress.getZipcode());
            addr.setCountry(dtoAddress.getCountry());
            if(listAddress.size() == 0){
                addr.setDefault(true);
            }

            listAddress.add(addr);
            userRepo.flush();
        }
        else{
            throw new UsernameNotFoundException(userName);
        }
        return true;
    }

    @Override
    public boolean updateAddressByUsername(String userName, Long addressId, AddressDTO dtoAddress) {
        Optional<User> opUser = userRepo.findUserByUsername(userName);
        if(opUser.isPresent()){
            User user = opUser.get();
            List<Address> addresses = user.getLstAddress();
            Optional<Address> opAddr = addresses.stream().filter(a -> a.getId() == addressId).findFirst();
            if(opAddr.isPresent()){
                //update address info
                Address addr = opAddr.get();
                addr.setHouseNumber(dtoAddress.getHouseNumber());
                addr.setHouseNumber(dtoAddress.getStreet());
                addr.setCity(dtoAddress.getCity());
                addr.setState(dtoAddress.getState());
                addr.setZipcode(dtoAddress.getZipcode());
                addr.setCountry(dtoAddress.getCountry());
                userRepo.flush();
            }
            else {
                throw new AddressNotFoundException(addressId);
            }
        }
        else{
            throw new UsernameNotFoundException(userName);
        }
        return true;
    }

    @Override
    public boolean removeAddressByUsername(String userName, Long addressId) {
        Optional<User> opUser = userRepo.findUserByUsername(userName);
        User user = null;
        if(opUser.isPresent()){
            user = opUser.get();

            List<Address> listAddress = user.getLstAddress();
            if(listAddress == null){
                return false;
            }
            else {
                Optional<Address> addr = listAddress.stream().filter(a -> a.getId() == addressId).findFirst();
                if (addr.isPresent()) {
                    int iPos = -1;
                    for (int i = 0; i < listAddress.size(); i++) {
                        if (listAddress.get(i).getId() == addressId) {
                            iPos = i;
                            break;
                        }
                    }
                    if (iPos != -1) {
                        listAddress.remove(iPos); //Just remove user_id from the address table that not remove the address
                        userRepo.flush();
                        addressRepo.deleteById(addressId); //this is used to remove the address actually
                    }
                    else {
                        throw new AddressNotFoundException(addressId);
                    }
                }
            }
        }
        else {
            throw new UsernameNotFoundException(userName);
        }
        return true;
    }

    @Override
    public boolean setDefaultAddress(String userName, Long addressId) {

        Optional<User> opUser = userRepo.findUserByUsername(userName);
        User user = null;

        if(opUser.isPresent()){
            user = opUser.get();
            List<Address> listAddress = user.getLstAddress();
            if(listAddress == null){
                return false;
            }
            else {
                //TODO Optional<Address> addr = listAddress.stream().filter(a -> a.getId() == addressId).findFirst();
                for(Address addr : listAddress){
                    if(addr.isDefault()){
                        addr.setDefault(false);
                    }
                    if(addr.getId() == addressId){
                        addr.setDefault(true);
                    }
                }
                userRepo.flush();
            }
        }
        else {
            throw new UsernameNotFoundException(userName);
        }
        return true;
    }
}
