package uy.edu.ucu.security.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uy.edu.ucu.security.persistence.entities.UserEntity;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query(value = "SELECT * FROM user WHERE email = : email", nativeQuery = true)
    Optional<UserEntity> findByEmail(String email);


}
