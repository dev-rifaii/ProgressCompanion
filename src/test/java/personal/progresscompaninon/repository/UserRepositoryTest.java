package personal.progresscompaninon.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import personal.progresscompaninon.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UserRepository userRepository;

    private final User validUser = User.builder()
            .email("datajpatest@gmail.com")
            .password("test1234")
            .firstName("fn")
            .lastName("ln")
            .build();

    @Test
    void shouldRetrieveUserByEmail() {
        User user = userRepository.findByEmail("admin");
        Assertions.assertNotNull(user);
    }

    @Test
    void shouldSaveUserAndRetrieveIt() {

        userRepository.save(validUser);
        User retrievedUser = userRepository.findByEmail("datajpatest@gmail.com");
        assertNotNull(retrievedUser);
    }

    @Test
    @Transactional
    void shouldSaveUserThenChangePassword() {
        userRepository.save(validUser);
        userRepository.changePassword("newPassword", validUser.getEmail());
        entityManager.clear();
        User retrievedUser = userRepository.findByEmail(validUser.getEmail());
        assertNotEquals(validUser.getPassword(), retrievedUser.getPassword());
    }
}