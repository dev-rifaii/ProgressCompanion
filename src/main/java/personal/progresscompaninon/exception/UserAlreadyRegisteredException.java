package personal.progresscompaninon.exception;

import org.springframework.security.core.AuthenticationException;

public class UserAlreadyRegisteredException extends AuthenticationException {
    public UserAlreadyRegisteredException(String msg) {
        super(msg);
    }
}
