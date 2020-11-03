package miu.edu.usermanagement.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceResourceBundle;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Locale;

public class UsernameExistedException extends RuntimeException {// Throwable {

//    @Autowired
//    private MessageSource messageSrc;

    public UsernameExistedException(@NotNull @Size(max = 10, message = "{error.username.length}") String username) {
        //super(messageSrc.getMessage("user.new.fail", new String[]{username}, Locale.US));
        super(String.format("The username %s existed", username));
    }
}
