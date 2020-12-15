package main.repository;

import main.api.response.post.PostResponse;
import main.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>
{
    @Query(value = "SELECT count(*) AS count FROM posts where is_active = 1 or moderation_status = \"NEW\"", nativeQuery = true)
    int countPostsForModeration();

    @Query(value = "SELECT count(*) AS count FROM posts where is_active = 1 or moderation_status = \"ACCEPTED\"", nativeQuery = true)
    int countPostForMod();

    @Query(value = "SELECT * FROM posts WHERE posts.is_active = 1 AND posts.moderation_status = " +
            "\"ACCEPTED\" AND posts.time < NOW() ORDER BY posts.time DESC LIMIT ?2  OFFSET ?1", nativeQuery = true)
    List<PostResponse> modeRecent(int offset, int limit);

    @Query(value = "SELECT * FROM posts JOIN (SELECT post_id, COUNT(post_id) AS comment_counts " +
            "FROM post_comments GROUP BY post_id) AS com_counts ON posts.id = com_counts.post_id " +
            "WHERE posts.is_active = 1 AND posts.moderation_status = \"ACCEPTED\" " +
            "AND posts.time < NOW() ORDER BY comment_counts DESC LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<PostResponse> modePopular(int offset, int limit);

    @Query(value = "SELECT * FROM posts JOIN (SELECT post_id, SUM(value) as sum_val FROM " +
            "post_voters WHERE value = 1 GROUP BY post_id) AS voter_count ON posts.id = voter_count" +
            ".post_id WHERE posts.is_active = 1 AND posts.moderation_status = \"ACCEPTED\" AND " +
            "posts.time < NOW() ORDER BY sum_val DESC LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<PostResponse> modeBest(int offset, int limit);

    @Query(value = "SELECT * FROM posts WHERE posts.is_active = 1 AND posts.moderation_status = " +
            "\"ACCEPTED\" AND posts.time < NOW() ORDER BY posts.time ASC LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<PostResponse> modeEarl(int offset, int limit);
}