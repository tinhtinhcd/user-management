package miu.edu.usermanagement.controller;

import miu.edu.usermanagement.dto.RegUser;
import miu.edu.usermanagement.entity.User;
import miu.edu.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
//        if(newUser.getUsername() != null){
//            System.out.println("NEW USER: " + newUser.getUsername());
//        }
//        else{
//            return "No user info";
//        }
        User retUser = userService.addNewUser(newUser);
        if(retUser != null){
            return "" + retUser.getId();
        }
        return "";
    }

    @GetMapping(value="/user/getByUserName")
    public @ResponseBody RegUser getUserInfoByUsername(@RequestParam(name="un") String userName){
        RegUser dtoUser = userService.queryUserByUserName(userName);
        return dtoUser;
    }
}
