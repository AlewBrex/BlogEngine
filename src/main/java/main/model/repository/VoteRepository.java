package main.model.repository;

import main.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {
  @Query(
      value = "SELECT count(p.id) AS count FROM post_votes AS p where p.value = 1",
      nativeQuery = true)
  Integer getAllLikes();

  @Query(
      value = "SELECT count(p.id) AS count FROM post_votes AS p where p.value = -1",
      nativeQuery = true)
  Integer getAllDislikes();

  @Query(
      value = "SELECT * FROM post_votes AS p WHERE p.user_id = ?1 AND p.post_id = ?2",
      nativeQuery = true)
  Vote getVoteByUserAndPost(Integer idUser, Integer idPost);

  @Query(
      value = "SELECT count(*) FROM post_votes AS p WHERE p.post_id = ? AND p.value = 1",
      nativeQuery = true)
  Integer getLikeByPostId(Integer postId);

  @Query(
      value = "SELECT count(*) FROM post_votes AS p WHERE p.post_id = ? AND p.value = -1",
      nativeQuery = true)
  Integer getDislikeByPostId(Integer postId);

  @Query(
      value = "DELETE FROM post_votes AS p WHERE p.user_id = ?1 AND p.post_id = ?2",
      nativeQuery = true)
  void deleteValue(Integer idUser, Integer postId);
}
