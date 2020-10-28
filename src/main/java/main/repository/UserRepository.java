package main.repository;

import main.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>
{
    User findByName(String name);
    User getByEmail(String email);
    User findByCode(String code);
}
