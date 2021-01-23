package main.repository;

import main.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {
    @Query(value = "SELECT count(p.id) AS count FROM post_voters AS p where p.value = 1", nativeQuery = true)
    int getAllLikes();

    @Query(value = "SELECT count(p.id) AS count FROM post_voters AS p where p.value = -1", nativeQuery = true)
    int getAllDislikes();

    @Query(value = "SELECT * FROM post_voters v WHERE v.user_id = ?1 AND v.post_id = ?2", nativeQuery = true)
    Vote getVoteByUserAndPost(int idUser, int idPost);
}