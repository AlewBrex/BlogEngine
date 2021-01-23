package main.repository;

import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT * FROM users u WHERE u.name = ?", nativeQuery = true)
    User getByName(String name);

    @Query(value = "SELECT * FROM users u WHERE u.email = ?", nativeQuery = true)
    User getByEmail(String email);

    @Query(value = "SELECT * FROM users u WHERE u.code = ?", nativeQuery = true)
    User findByCode(String code);
}