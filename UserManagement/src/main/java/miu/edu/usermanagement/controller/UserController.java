package miu.edu.usermanagement.controller;

import miu.edu.usermanagement.dto.RegUser;
import miu.edu.usermanagement.dto.RoleDTO;
import miu.edu.usermanagement.entity.User;
import miu.edu.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/api")
    public String testAPI(){
        return "Hello API";
    }

    @PostMapping(value="/user/register")
    public @ResponseBody String registerUser(@Valid @RequestBody RegUser newUser){

        User retUser = userService.addNewUser(newUser);
        if(retUser != null){
            return "" + retUser.getId();
        }
        return "The username " + newUser.getUsername() + " existed.";
    }

    @GetMapping(value="api/user/getByUserName")
    public @ResponseBody RegUser getUserInfoByUsername(@RequestParam(name="un") String userName){
        RegUser dtoUser = userService.queryUserByUserName(userName);

        return dtoUser;
    }

    @PostMapping(value="api/user/updateUser")
    public @ResponseBody String updateUser(@RequestParam(name="un") String userName, @Valid @RequestBody RegUser dtoUser){
        return userService.updateUserInfoByUsername(userName, dtoUser);
    }

    @GetMapping(value="api/user/list")
    public @ResponseBody List<RegUser> listOfUsers(){

        List<RegUser> listUsers = userService.getListUsers();
        return listUsers;
    }

    @GetMapping(value="api/user/roles")
    public @ResponseBody List<RoleDTO> listOfRoles(){

        List<RoleDTO> listRoles = userService.getListRoles();
        return listRoles;
    }
}
