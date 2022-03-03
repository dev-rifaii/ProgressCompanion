package personal.progresscompaninon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import personal.progresscompaninon.dto.PasswordChangeDto;
import personal.progresscompaninon.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/password")
    public ResponseEntity<String> changePass(@RequestBody @Valid PasswordChangeDto passwords) {
        userService.changePassword(passwords);
        return new ResponseEntity<>("Password Changed Successfully", HttpStatus.OK);

    }
}
