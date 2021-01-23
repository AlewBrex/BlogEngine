package main.repository;

import main.api.response.post.PostResponse;
import main.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query(value = "SELECT COUNT(*) AS count FROM posts WHERE is_active = 1 AND moderation_status = \"NEW\"",
            nativeQuery = true)
    Integer countPostsForModerationStatusNew();

    @Query(value = "SELECT COUNT(*) AS count FROM posts WHERE is_active = 1 AND moderation_status = \"ACCEPTED\"",
            nativeQuery = true)
    Integer countPostForModStatusAccepted();

    @Query(value = "SELECT * FROM posts AS p WHERE p.is_active = 1 AND p.moderation_status = \"ACCEPTED\" AND " +
            "p.time < NOW() ORDER BY p.time DESC LIMIT ?2  OFFSET ?1", nativeQuery = true)
    List<PostResponse> modeRecent(int offset, int limit);

    @Query(value = "SELECT * FROM posts AS p WHERE p.is_active = 1 AND p.moderation_status = \"ACCEPTED\" AND " +
            "p.time < NOW() ORDER BY p.time DESC", nativeQuery = true)
    List<PostResponse> modeRecentAll();

    @Query(value = "SELECT * FROM posts AS p JOIN (SELECT post_id, COUNT(post_id) AS comment_counts " +
            "FROM post_comments GROUP BY post_id) AS com_counts ON p.id = com_counts.post_id " +
            "WHERE p.is_active = 1 AND p.moderation_status = \"ACCEPTED\" " +
            "AND p.time < NOW() ORDER BY comment_counts DESC LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<PostResponse> modePopular(int offset, int limit);

    @Query(value = "SELECT * FROM posts AS p JOIN (SELECT post_id, SUM(value) AS sum_val FROM " +
            "post_voters WHERE value = 1 GROUP BY post_id) AS voter_count ON p.id = voter_count.post_id " +
            "WHERE p.is_active = 1 AND p.moderation_status = \"ACCEPTED\" AND " +
            "p.time < NOW() ORDER BY sum_val DESC LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<PostResponse> modeBest(int offset, int limit);

    @Query(value = "SELECT * FROM posts AS p WHERE p.is_active = 1 AND p.moderation_status = \"ACCEPTED\" " +
            "AND p.time < NOW() ORDER BY p.time ASC LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<PostResponse> modeEarl(int offset, int limit);

    @Query(value = "SELECT * FROM posts AS p WHERE (p.title LIKE \"%?3%\" OR p.text LIKE \"%?3%\") " +
            "AND p.is_active = 1 AND p.moderation_status = \"ACCEPTED\" AND p.time < NOW() " +
            "ORDER BY p.time DESC LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<PostResponse> searchPosts(int offset, int limit, String query);

    @Query(value = "SELECT COUNT(*) AS count FROM posts AS p WHERE (p.title LIKE \"%?%\" OR p.text LIKE \"%?%\") " +
            "AND p.is_active = 1 AND p.moderation_status = \"ACCEPTED\" AND p.time < NOW()", nativeQuery = true)
    Integer countSearchPosts(String query);

    @Query(value = "SELECT * FROM posts AS p WHERE DATEDIFF(p.time, \"%?3%\") AND p.is_active = 1 " +
            "AND p.moderation_status = \"ACCEPTED\" AND p.time < NOW() " +
            "ORDER BY p.time DESC LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<PostResponse> listPostsByDate(int offset, int limit, String date);

    @Query(value = "SELECT COUNT(*) AS count FROM posts AS p WHERE DATEDIFF(p.time, ?) = 0 " +
            "AND p.is_active = 1 AND p.moderation_status = \"ACCEPTED\" AND p.time < NOW()", nativeQuery = true)
    Integer countPostsByDate(String date);

    @Query(value = "SELECT * FROM posts AS p JOIN tag2post AS tp ON p.id = tp.post_id JOIN tags AS t ON t.id = tp" +
            ".tag_id WHERE t.name LIKE \"%?3%\" AND p.is_active = 1 AND p.moderation_status = \"ACCEPTED\" AND " +
            "p.time < NOW() ORDER BY p.time DESC LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<PostResponse> listPostByTags(int offset, int limit, String tag);

    @Query(value = "SELECT COUNT(*) AS count FROM (SELECT p.* FROM posts AS p JOIN tag2post AS tp ON p.id = " +
            "tp.post_id JOIN tags AS t ON t.id = tp.tag_id WHERE t.name LIKE \"%?%\" AND p.is_active = 1 AND " +
            "p.moderation_status = \"ACCEPTED\" AND p.time < NOW()) AS tag_posts", nativeQuery = true)
    Integer countPostsByTags(String tag);

    @Query(value = "SELECT * FROM posts AS p WHERE p.moderator_id = 4 AND p.is_active = 1 " +
            "AND p.moderation_status LIKE \"%?3%\" ORDER BY p.time DESC LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<PostResponse> postsForModeration(int offset, int limit, String status, int id);

    @Query(value = "SELECT * FROM posts AS p WHERE p.user_id = ?3 AND p.is_active = 0 " +
            "ORDER BY p.time DESC LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<PostResponse> listUserPostInactive(int offset, int limit, int id);

    @Query(value = "SELECT COUNT(*) AS count FROM posts WHERE moderator_id = ? AND is_active = 0", nativeQuery = true)
    Integer countPostInactive(int id);

    @Query(value = "SELECT * FROM posts AS p WHERE p.moderation_status = ?4 AND p.user_id = ?3 AND p.is_active = 1 " +
            "ORDER BY p.time DESC LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<PostResponse> listUserPostStatus(int offset, int limit, int id, String status);

    @Query(value = "SELECT COUNT(*) AS count FROM posts WHERE moderator_id = ?1 AND is_active = 1 AND " +
            "moderation_status = \"?2\"", nativeQuery = true)
    Integer countPostStatus(int id, String status);

    @Query(value = "SELECT COUNT(*) AS count FROM post_voters AS pw JOIN posts AS p ON pw.post_id = p.id WHERE " +
            "p.is_active = 1 AND p.moderation_status = \"ACCEPTED\" AND p.user_id = ?1 AND pw.value = 1",
            nativeQuery = true)
    Integer countLikesMyPosts(int idUser);

    @Query(value = "SELECT COUNT(*) AS count FROM post_voters AS pw JOIN posts AS p ON pw.post_id = p.id WHERE " +
            "p.is_active = 1 AND p.moderation_status = \"ACCEPTED\" AND p.user_id = ?1 AND pw.value = -1",
            nativeQuery = true)
    Integer countDislikesMyPosts(int idUser);

    @Query(value = "SELECT SUM(posts.view_count) AS sum FROM posts WHERE is_active  = 1 AND " +
            "moderation_status = \"ACCEPTED\" AND user_id = ?1", nativeQuery = true)
    Integer countViewsMyPosts(int idUser);

    @Query(value = "SELECT p.time FROM posts AS p WHERE p.is_active = 1 AND p.moderation_status = 'ACCEPTED' AND " +
            "p.user_id = ?1 ORDER BY p.time ASC limit 1", nativeQuery = true)
    LocalDateTime timeMyFirstPublication(int idUser);

    @Query(value = "SELECT COUNT(*) AS count FROM post_voters AS pw JOIN posts AS p ON pw.post_id = p.id WHERE " +
            "p.is_active = 1 AND p.moderation_status = \"ACCEPTED\" AND pw.value = 1", nativeQuery = true)
    Integer countLikesAllPosts();

    @Query(value = "SELECT COUNT(*) AS count FROM post_voters AS pw JOIN posts AS p ON pw.post_id = p.id WHERE " +
            "p.is_active = 1 AND p.moderation_status = \"ACCEPTED\" AND pw.value = -1", nativeQuery = true)
    Integer countDislikesAllPosts();

    @Query(value = "SELECT SUM(posts.view_count) AS sum FROM posts WHERE is_active  = 1 AND " +
            "moderation_status = \"ACCEPTED\"", nativeQuery = true)
    Integer countViewsAllPosts();

    @Query(value = "SELECT p.time FROM posts AS p WHERE p.is_active = 1 AND p.moderation_status = \"ACCEPTED\" " +
            "ORDER BY p.time ASC limit 1", nativeQuery = true)
    LocalDateTime timeFirstPublication();

    @Query(value = "SELECT distinct YEAR(p.time) AS posts_years FROM posts AS p WHERE p.is_active = 1 AND " +
            "p.moderation_status = \"ACCEPTED\" ORDER BY p.time ASC", nativeQuery = true)
    List<Integer> yearsWithPosts();

    @Query(value = "SELECT  year_post, count(*) AS count FROM(SELECT DATE(p.time) AS year_post FROM posts AS p WHERE " +
            "p.time LIKE \"%?1%\" AND p.is_active = 1 AND p.moderation_status = \"ACCEPTED\" ORDER BY year_post ASC) " +
            "AS q GROUP BY year_post", nativeQuery = true)
    Map<String, Integer> daysCountPosts(Integer year);
}