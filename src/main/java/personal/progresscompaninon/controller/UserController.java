package personal.progresscompaninon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import personal.progresscompaninon.dto.PasswordChangeDto;
import personal.progresscompaninon.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/password")
    public ResponseEntity<String> changePass(@RequestBody @Valid PasswordChangeDto passwords) {
        userService.changePassword(passwords);
        return new ResponseEntity<>("Password Changed Successfully", HttpStatus.OK);

    }
}
