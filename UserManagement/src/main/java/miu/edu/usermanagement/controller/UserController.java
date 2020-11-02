package miu.edu.usermanagement.controller;

import miu.edu.usermanagement.dto.*;
import miu.edu.usermanagement.dto.response.ResponseMessage;
import miu.edu.usermanagement.entity.User;
import miu.edu.usermanagement.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@CrossOrigin(maxAge = 60)
@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserService userService;
    @Autowired
    private MessageSource messageSource;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

//    @GetMapping("/api")
//    public String testAPI(){
//        return "Hello API";
//    }

//    @GetMapping("/api")
//    public ResponseEntity<String> testAPI(){
//        return new ResponseEntity<>("Hello API", HttpStatus.OK);
//    }

    @PostMapping(value="/users", produces="application/json")
    public @ResponseBody UserDTO registerUser(@Valid @RequestBody RegUser newUser){//}, BindingResult result){

//        if(result.hasErrors()){
//            //return validation errors
//            return messageSource.getMessage("user.validate.error", null, Locale.US);//"Invalid input data";
//        }

        UserDTO retUser = userService.addNewUser(newUser);
        return retUser;
//        if(retUser != null){
//            return messageSource.getMessage("user.new.success", new String[]{newUser.getUsername()}, Locale.US);
//        }
//        return messageSource.getMessage("user.new.fail", new String[]{newUser.getUsername()}, Locale.US);
    }

    @GetMapping(value="api/users/username/{username}")
    public @ResponseBody UserDTO getUserInfoByUsername(@PathVariable(name="username") String userName){
        UserDTO dtoUser = userService.queryUserByUserName(userName);

        return dtoUser;
    }

    @PutMapping(value="api/users/{username}")
    public ResponseEntity<ResponseMessage> updateUser(@PathVariable(name="username") String userName, @Valid @RequestBody UserDTO dtoUser){
        String retMessage = "Failed while updating";
        if(userService.updateUserInfoByUsername(userName, dtoUser)){
            retMessage = messageSource.getMessage("user.update.success", new String[]{userName}, Locale.US);
        }
//        else{
//            retMessage = messageSource.getMessage("user.update.fail", new String[]{userName}, Locale.US);
//        }
        return ResponseEntity.ok(ResponseMessage.builder().message(retMessage).build());
    }

    @PostMapping(value = "api/users/{username}/addresses")
    public ResponseEntity<ResponseMessage> addNewUserAddress(@PathVariable(name = "username") String userName, @RequestBody AddressDTO dtoAddress){
        String retMessage = "";
        if(userService.addAddressByUsername(userName, dtoAddress)){
            retMessage = messageSource.getMessage("address.add.success", null, Locale.US);
        }

        return ResponseEntity.ok(ResponseMessage.builder().message(retMessage).build());
    }

    @PutMapping(value = "/api/users/{username}/addresses/{addressId}")
    public ResponseEntity<ResponseMessage> updateUserAddress(@PathVariable(name = "username") String userName,
                                                  @PathVariable(name = "addressId") Long addressId,
                                                  @RequestBody AddressDTO dtoAddress){
        String retMessage = "";
        if(userService.updateAddressByUsername(userName, addressId, dtoAddress)){
            retMessage = messageSource.getMessage("address.update.success", null, Locale.US);
        }
        return ResponseEntity.ok(ResponseMessage.builder().message(retMessage).build());
    }

    @DeleteMapping(value = "api/users/{username}/addresses/{addressId}")
    public ResponseEntity<ResponseMessage> removeUserAddress(@PathVariable(name = "username") String userName, @PathVariable(name = "addressId") Long addressId){
        String retMessage = "";
        if(userService.removeAddressByUsername(userName, addressId)){
            retMessage = messageSource.getMessage("address.remove.success", null, Locale.US);
        }
        return ResponseEntity.ok(ResponseMessage.builder().message(retMessage).build());
    }

    @PutMapping(value = "api/users/{username}/addresses/{addressId}/default")
    public ResponseEntity<ResponseMessage> setDefaultAddress(@PathVariable(name = "username") String userName, @PathVariable(name = "addressId") Long addressId){
        String retMessage = "";
        if(userService.setDefaultAddress(userName, addressId)){
            retMessage = messageSource.getMessage("address.default.success", null, Locale.US);
        }
        return ResponseEntity.ok(ResponseMessage.builder().message(retMessage).build());
    }

//    private ResponseEntity makeSuccessResponse(String message) {
//        Map<String, Object> body = new LinkedHashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("message", message);
//
//        return new ResponseEntity<>(body, HttpStatus.OK);
//    }

    @GetMapping(value="api/users")
    public @ResponseBody List<UserDTO> listOfUsers(){ //@PathVariable(name = "status") int status){

        List<UserDTO> listUsers = userService.getListUserInfo(2);
        return listUsers;
    }

    @GetMapping(value="api/users/{ids}")
    public @ResponseBody List<UserDTO> listOfUserIDs(@PathVariable(name = "ids") List<Long> listUserIDs){

        List<UserDTO> listUsers = userService.getListUserInfoByIDs(listUserIDs);
        return listUsers;
    }

    @PutMapping(value="api/users/{username}/{enable}")
    public ResponseEntity<ResponseMessage> setUserStatus(@PathVariable(name = "username") String userName,
                                                             @PathVariable(name = "status") boolean status){
        String retMessage = "";
        if(userService.setUserStatus(userName, status)){
            if(status){
                retMessage = messageSource.getMessage("user.enable.success", null, Locale.US);
            }
            else {
                retMessage = messageSource.getMessage("user.disable.success", null, Locale.US);
            }
        }
        else{
            retMessage = messageSource.getMessage("user.update.fail", null, Locale.US);
        }

        return ResponseEntity.ok(ResponseMessage.builder().message(retMessage).build());
    }

    @GetMapping(value="api/users/roles")
    public @ResponseBody List<RoleDTO> listOfRoles(){

        List<RoleDTO> listRoles = userService.getListRoles();
        return listRoles;
    }
}
