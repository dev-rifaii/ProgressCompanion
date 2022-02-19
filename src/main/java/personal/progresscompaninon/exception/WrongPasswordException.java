package personal.progresscompaninon.exception;

import org.springframework.security.core.AuthenticationException;

public class WrongPasswordException extends AuthenticationException {

    public WrongPasswordException(String msg) {
        super(msg);
    }
}
