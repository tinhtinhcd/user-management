package miu.edu.usermanagement.controller;

import miu.edu.usermanagement.dto.RegUser;
import miu.edu.usermanagement.dto.RoleDTO;
import miu.edu.usermanagement.dto.UserDTO;
import miu.edu.usermanagement.dto.UserIdDTO;
import miu.edu.usermanagement.entity.User;
import miu.edu.usermanagement.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@CrossOrigin(maxAge = 3600)
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

    @GetMapping("/api")
    public String testAPI(){
        return "Hello API";
    }

//    @GetMapping("/api")
//    public ResponseEntity<String> testAPI(){
//        return new ResponseEntity<>("Hello API", HttpStatus.OK);
//    }

    @PostMapping(value="/user/register", produces="application/json")
    public @ResponseBody String registerUser(@Valid @RequestBody RegUser newUser){//}, BindingResult result){

//        if(result.hasErrors()){
//            //return validation errors
//            return messageSource.getMessage("user.validate.error", null, Locale.US);//"Invalid input data";
//        }

        User retUser = userService.addNewUser(newUser);
        if(retUser != null){
            return messageSource.getMessage("user.new.success", new String[]{newUser.getUsername()}, Locale.US);
        }
        return messageSource.getMessage("user.new.fail", new String[]{newUser.getUsername()}, Locale.US);
    }

    @GetMapping(value="api/user/getByUserName")
    public @ResponseBody UserDTO getUserInfoByUsername(@RequestParam(name="un") String userName){
        UserDTO dtoUser = userService.queryUserByUserName(userName);

        return dtoUser;
    }

    @PostMapping(value="api/user/updateUser")
    public @ResponseBody String updateUser(@RequestParam(name="un") String userName, @Valid @RequestBody UserDTO dtoUser){
        String retMessage = "";
        if(userService.updateUserInfoByUsername(userName, dtoUser)){
            retMessage = messageSource.getMessage("user.update.success", new String[]{userName}, Locale.US);
        }
        else{
            retMessage = messageSource.getMessage("user.update.fail", new String[]{userName}, Locale.US);
        }
        return retMessage;
    }

    @GetMapping(value="api/user/list")
    public @ResponseBody List<UserDTO> listOfUsers(){

        List<UserDTO> listUsers = userService.getListUserInfo();
        return listUsers;
    }

    @GetMapping(value="api/user/listByIDs")
    public @ResponseBody List<UserDTO> listOfUserIDs(@RequestBody List<UserIdDTO> listUserIDs){

        List<UserDTO> listUsers = userService.getListUserInfoByIDs(listUserIDs);
        return listUsers;
    }

    @GetMapping(value="api/user/roles")
    public @ResponseBody List<RoleDTO> listOfRoles(){

        List<RoleDTO> listRoles = userService.getListRoles();
        return listRoles;
    }
}
