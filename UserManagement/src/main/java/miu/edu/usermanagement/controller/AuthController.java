package miu.edu.usermanagement.controller;

import com.sun.istack.Nullable;
import miu.edu.usermanagement.dto.response.UserAuthResponse;
import miu.edu.usermanagement.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/")
public class AuthController {

    private UserAuthService userAuthService;

    @Autowired
    public void setUserAuthService(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @GetMapping(value="user/{username}")
    public ResponseEntity<UserAuthResponse> getUserInfoByUsername(@PathVariable String username){
        UserAuthResponse response = userAuthService.findUserByUsername(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
