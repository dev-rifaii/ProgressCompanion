package personal.progresscompaninon.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import personal.progresscompaninon.dto.LoginDto;
import personal.progresscompaninon.model.User;
import personal.progresscompaninon.service.UserService;

import javax.validation.Valid;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> signIn(@RequestBody LoginDto loginDto) {
        if (userService.authenticate(loginDto)) {
            return new ResponseEntity<>("User signed-in successfully!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody @Valid User user) {
        userService.addUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }


}
