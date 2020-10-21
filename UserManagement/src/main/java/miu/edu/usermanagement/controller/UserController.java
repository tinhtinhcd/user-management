package miu.edu.usermanagement.controller;

import miu.edu.usermanagement.dto.RegUser;
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
        if(newUser.getUsername() != null){
            System.out.println("NEW USER: " + newUser.getUsername());
        }
        else{
            return "No user info";
        }
        if(userService.addNewUser(newUser) != null){
            return "Success";
        }
        return "";
    }
}
