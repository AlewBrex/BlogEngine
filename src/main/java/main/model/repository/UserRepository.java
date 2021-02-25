package main.model.repository;

import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT * FROM users AS u WHERE u.id = ?1", nativeQuery = true)
    User getById(Integer id);

    @Query(value = "SELECT * FROM users u WHERE u.name = ?", nativeQuery = true)
    User getByName(String name);

    @Query(value = "SELECT * FROM users u WHERE u.email = ?", nativeQuery = true)
    Optional<User> getByEmail(String email);

    @Query(value = "SELECT COUNT(*) FROM users u WHERE u.email = ?", nativeQuery = true)
    Integer existEmailOrNot(String email);

    @Query(value = "SELECT * FROM users u WHERE u.code = ?", nativeQuery = true)
    User findByCode(String code);
}