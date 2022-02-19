package personal.progresscompaninon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import personal.progresscompaninon.dto.LoginDto;
import personal.progresscompaninon.dto.PasswordChangeDto;
import personal.progresscompaninon.exception.UserAlreadyRegisteredException;
import personal.progresscompaninon.model.User;
import personal.progresscompaninon.service.UserService;

import javax.validation.Valid;


@RestController
@RequestMapping("/auth")
public class RegistrationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> signIn(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("User signed-in successfully!.", HttpStatus.OK);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> signup(@RequestBody @Valid User user) {

        userService.addUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }


}
