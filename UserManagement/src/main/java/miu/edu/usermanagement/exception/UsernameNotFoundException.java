package miu.edu.usermanagement.exception;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UsernameNotFoundException extends RuntimeException {

    public UsernameNotFoundException(String username) {
        //super(messageSrc.getMessage("user.new.fail", new String[]{username}, Locale.US));
        super(String.format("The username %s doesn't exist", username));
    }
}
