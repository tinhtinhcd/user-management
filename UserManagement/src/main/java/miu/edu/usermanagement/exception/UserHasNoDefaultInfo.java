package miu.edu.usermanagement.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Locale;

public class UserHasNoDefaultInfo extends RuntimeException{

    public UserHasNoDefaultInfo(@NotNull @Size(max = 10, message = "{error.username.length}") String username, String message) {
        super(String.format(message, username));
    }
}
