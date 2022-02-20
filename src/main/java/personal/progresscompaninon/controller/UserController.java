package personal.progresscompaninon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.progresscompaninon.dto.PasswordChangeDto;
import personal.progresscompaninon.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PatchMapping("/password")
    public ResponseEntity<String> changePass(@RequestBody @Valid PasswordChangeDto passwords) {
        userService.changePassword(passwords);
        return new ResponseEntity<>("Password Changed Successfully", HttpStatus.OK);

    }
}
