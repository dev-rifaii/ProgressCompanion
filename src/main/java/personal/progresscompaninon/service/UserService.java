package personal.progresscompaninon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import personal.progresscompaninon.dto.LoginDto;
import personal.progresscompaninon.dto.PasswordChangeDto;
import personal.progresscompaninon.exception.*;
import personal.progresscompaninon.model.User;
import personal.progresscompaninon.repository.RoleRepository;
import personal.progresscompaninon.repository.UserRepository;

import javax.validation.Valid;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public void addUser(@Valid User user) {
        if (findByEmail(user.getEmail()) != null) {
            throw new UserAlreadyRegisteredException("User already registered");
        }
        user.getRoles().add(roleRepository.findByRole("ROLE_USER"));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public boolean authenticate(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        } catch (BadCredentialsException e) {
            return false;
        }
    }

    public void changePassword(@Valid PasswordChangeDto passwords) {
        User user = getLoggedInUser();
        if (bCryptPasswordEncoder.matches(passwords.getOldPassword(), user.getPassword())) {
            if (passwordValidator(passwords.getNewPassword())) {
                userRepository.changePassword(bCryptPasswordEncoder.encode(passwords.getNewPassword()), user.getEmail());
            }
        } else {
            throw new WrongPasswordException("Old password didn't match");
        }
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public boolean passwordValidator(String password) {
        return password.length() > 8;
    }

    public User getLoggedInUser() {
        return userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }


    public User getUser(long id) {
        return userRepository.findById(id).get();
    }


    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

}
