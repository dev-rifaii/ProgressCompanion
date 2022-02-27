package personal.progresscompaninon.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import personal.progresscompaninon.dto.PasswordChangeDto;
import personal.progresscompaninon.exception.UserAlreadyRegisteredException;
import personal.progresscompaninon.model.Role;
import personal.progresscompaninon.model.User;
import personal.progresscompaninon.repository.RoleRepository;
import personal.progresscompaninon.repository.UserRepository;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private RoleRepository roleRepositoryMock;

    private UserService userService;


    @Captor
    ArgumentCaptor<User> userCaptor;

    @Captor
    ArgumentCaptor<String> stringCaptor;

    @BeforeEach
    public void setup() {
        userService = new UserService(userRepositoryMock, roleRepositoryMock);
    }

    @Test
    void whenAddingNewUserItShouldBeValidatedAndSentToRepository() {
        String role = "ROLE_USER";
        String password = "yesyesyes";
        User user = User.builder()
                .firstName("fn")
                .lastName("ln")
                .email("test@gmail.com")
                .password(password)
                .roles(new ArrayList<>())
                .build();
        when(roleRepositoryMock.findByRole(role)).thenReturn(new Role(1L, role, null));
        userService.addUser(user);
        verify(roleRepositoryMock).findByRole(role);
        verify(userRepositoryMock).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertNotEquals(savedUser.getPassword(), password);
    }


    @Test
    void shouldThrowUserAlreadyRegisteredException() {
        UserService userService = new UserService(userRepositoryMock, roleRepositoryMock);
        String email = "yes@gmail.com";
        User user = User.builder()
                .firstName("fn")
                .lastName("ln")
                .email(email)
                .password("testyestest")
                .roles(new ArrayList<>())
                .build();
        when(userRepositoryMock.findByEmail(email)).thenReturn(user);
        Exception exception = assertThrows(UserAlreadyRegisteredException.class, () -> userService.addUser(user));
        assertEquals("User already registered", exception.getMessage());
    }


    @Test
    void shouldReturnFalseWhenPasswordIsInvalid() {
        UserService userService = new UserService(null, null);
        String password = "qwert";
        assertFalse(userService.passwordValidator(password));
    }

    @Test
    void shouldChangePasswordAndEncryptIt() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String oldPasswordEncoded = userService.bCryptPasswordEncoder.encode("oldPassword");
        User user = User.builder()
                .firstName("fn")
                .lastName("ln")
                .email("test@gmail.com")
                .password(oldPasswordEncoded)
                .build();
        when(userService.getLoggedInUser()).thenReturn(user);
        PasswordChangeDto passwords = new PasswordChangeDto("oldPassword", "newPassword");
        userService.changePassword(passwords);
        verify(userRepositoryMock).changePassword(stringCaptor.capture(), stringCaptor.capture());
        String newPasswordEncoded = stringCaptor.getAllValues().get(0);
        assertNotEquals(newPasswordEncoded, passwords.getNewPassword());
        assertNotEquals(oldPasswordEncoded, newPasswordEncoded);
    }
}