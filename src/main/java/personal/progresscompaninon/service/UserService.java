package personal.progresscompaninon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import personal.progresscompaninon.dto.PasswordChangeDto;
import personal.progresscompaninon.exception.*;
import personal.progresscompaninon.model.Role;
import personal.progresscompaninon.model.User;
import personal.progresscompaninon.repository.RoleRepository;
import personal.progresscompaninon.repository.UserRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found in the database");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
