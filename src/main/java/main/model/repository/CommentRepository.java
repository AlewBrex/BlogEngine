package main.model.repository;

import main.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query(value = "SELECT count(*) FROM post_comments AS p WHERE p.post_id = ?", nativeQuery = true)
    Integer getCountCommentsByPostId(int postId);

    @Query(value = "SELECT * FROM post_comments AS p WHERE p.post_id = ?", nativeQuery = true)
    List<Comment> getListCommentsByPostId(int id);
}