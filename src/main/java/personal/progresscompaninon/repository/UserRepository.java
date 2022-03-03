package personal.progresscompaninon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import personal.progresscompaninon.model.User;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query(value = "Select u From User u " + "WHERE u.firstName =?1")
    User findUserByName(String name);

    @Modifying
    @Query("update User u set u.password = ?1 where u.email = ?2")
    void changePassword(String password, String email);


}
