package main.model.repository;

import main.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Tag2PostRepository extends JpaRepository<Tag, Integer> {

}