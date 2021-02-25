package main.model.repository;

import main.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query(value = "SELECT * FROM posts AS p WHERE p.id = ?1 AND p.is_active = 1 AND p.moderation_status = 'ACCEPTED' " +
            "AND p.time < NOW()", nativeQuery = true)
    Post getPostById(int id);

    @Query(value = "SELECT COUNT(*) AS count FROM posts AS p WHERE p.moderator_id = ? AND p.is_active = 1 AND p.moderation_status = 'NEW'",
            nativeQuery = true)
    Integer countPostsUserForModerationStatusNew(int id);

    @Query(value = "SELECT COUNT(*) AS count FROM posts AS p WHERE p.is_active = 1 AND p.moderation_status = 'NEW'",
            nativeQuery = true)
    Optional<Integer> countPostsForModerationStatusNew();

    @Query(value = "SELECT COUNT(*) AS count FROM posts AS p WHERE p.is_active = 1 AND p.moderation_status = 'ACCEPTED'",
            nativeQuery = true)
    Optional<Integer> countPostForModStatusAccepted();

    @Query(value = "SELECT * FROM posts AS p WHERE p.is_active = 1 AND p.moderation_status = 'ACCEPTED' AND " +
            "p.time < NOW() ORDER BY p.time DESC LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<Post> modeRecent(int offset, int limit);

    @Query(value = "SELECT * FROM posts AS p WHERE p.is_active = 1 AND p.moderation_status = 'ACCEPTED' AND " +
            "p.time < NOW() ORDER BY p.time DESC", nativeQuery = true)
    List<Post> modeRecentAll();

    @Query(value = "SELECT * FROM posts AS p JOIN (SELECT post_id, COUNT(post_id) AS comment_counts " +
            "FROM post_comments GROUP BY post_id) AS com_counts ON p.id = com_counts.post_id " +
            "WHERE p.is_active = 1 AND p.moderation_status = 'ACCEPTED' " +
            "AND p.time < NOW() ORDER BY comment_counts DESC LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<Post> modePopular(int offset, int limit);

    @Query(value = "SELECT * FROM posts AS p JOIN (SELECT post_id, SUM(value) AS sum_val FROM " +
            "post_votes WHERE value = 1 GROUP BY post_id) AS voter_count ON p.id = voter_count.post_id " +
            "WHERE p.is_active = 1 AND p.moderation_status = 'ACCEPTED' AND " +
            "p.time < NOW() ORDER BY sum_val DESC LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<Post> modeBest(int offset, int limit);

    @Query(value = "SELECT * FROM posts AS p WHERE p.is_active = 1 AND p.moderation_status = 'ACCEPTED' " +
            "AND p.time < NOW() ORDER BY p.time ASC LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<Post> modeEarl(int offset, int limit);

    @Query(value = "SELECT * FROM posts AS p WHERE (p.title LIKE %?3% OR p.text LIKE %?3%) " +
            "AND p.is_active = 1 AND p.moderation_status = 'ACCEPTED' AND p.time < NOW() " +
            "ORDER BY p.time DESC LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<Post> searchPosts(int offset, int limit, String query);

    @Query(value = "SELECT COUNT(*) AS count FROM posts AS p WHERE (p.title LIKE %?1% OR p.text LIKE %?1%) " +
            "AND p.is_active = 1 AND p.moderation_status = 'ACCEPTED' AND p.time < NOW()", nativeQuery = true)
    Optional<Integer> countSearchPosts(String query);

    @Query(value = "SELECT * FROM posts AS p WHERE DATEDIFF(p.time, ?3) AND p.is_active = 1 " +
            "AND p.moderation_status = 'ACCEPTED' AND p.time < NOW() " +
            "ORDER BY p.time DESC LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<Post> listPostsByDate(int offset, int limit, String date);

    @Query(value = "SELECT COUNT(*) AS count FROM posts AS p WHERE DATEDIFF(p.time, ?) = 0 " +
            "AND p.is_active = 1 AND p.moderation_status = 'ACCEPTED' AND p.time < NOW()", nativeQuery = true)
    Optional<Integer> countPostsByDate(String date);

    @Query(value = "SELECT * FROM posts AS p JOIN tag2post AS tp ON p.id = tp.post_id JOIN tags AS t ON t.id = tp" +
            ".tag_id WHERE t.name LIKE %?3% AND p.is_active = 1 AND p.moderation_status = 'ACCEPTED' AND " +
            "p.time < NOW() ORDER BY p.time DESC LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<Post> listPostByTags(int offset, int limit, String tag);

    @Query(value = "SELECT COUNT(*) AS count FROM (SELECT p.* FROM posts AS p JOIN tag2post AS tp ON p.id = " +
            "tp.post_id JOIN tags AS t ON t.id = tp.tag_id WHERE t.name LIKE %?% AND p.is_active = 1 AND " +
            "p.moderation_status = 'ACCEPTED' AND p.time < NOW()) AS tag_posts", nativeQuery = true)
    Optional<Integer> countPostsByTags(String tag);

    @Query(value = "SELECT * FROM posts AS p WHERE p.moderator_id = ?4 AND p.is_active = 1 " +
            "AND p.moderation_status LIKE %?3% ORDER BY p.time DESC LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<Post> postsForModeration(int offset, int limit, String status, int id);

    @Query(value = "SELECT * FROM posts AS p WHERE p.user_id = ?3 AND p.is_active = 0 " +
            "ORDER BY p.time DESC LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<Post> listUserPostInactive(int offset, int limit, int id);

    @Query(value = "SELECT COUNT(*) AS count FROM posts WHERE moderator_id = ? AND is_active = 0", nativeQuery = true)
    Integer countPostInactive(int id);

    @Query(value = "SELECT * FROM posts AS p WHERE p.moderation_status = ?4 AND p.user_id = ?3 AND p.is_active = 1 " +
            "ORDER BY p.time DESC LIMIT ?2 OFFSET ?1", nativeQuery = true)
    List<Post> listUserPostStatus(int offset, int limit, int id, String status);

    @Query(value = "SELECT COUNT(*) AS count FROM posts AS p WHERE p.moderator_id = ?1 AND p.is_active = 1 AND " +
            "p.moderation_status = ?2", nativeQuery = true)
    Optional<Integer> countPostStatus(int id, String status);

    @Query(value = "SELECT COUNT(*) AS count FROM post_votes AS pw JOIN posts AS p ON pw.post_id = p.id WHERE " +
            "p.is_active = 1 AND p.moderation_status = 'ACCEPTED' AND p.user_id = ?1 AND pw.value = 1",
            nativeQuery = true)
    Optional<Integer> countLikesMyPosts(int idUser);

    @Query(value = "SELECT COUNT(*) AS count FROM post_votes AS pw JOIN posts AS p ON pw.post_id = p.id WHERE " +
            "p.is_active = 1 AND p.moderation_status = 'ACCEPTED' AND p.user_id = ?1 AND pw.value = -1",
            nativeQuery = true)
    Optional<Integer> countDislikesMyPosts(int idUser);

    @Query(value = "SELECT SUM(posts.view_count) AS sum FROM posts WHERE is_active  = 1 AND " +
            "moderation_status = 'ACCEPTED' AND user_id = ?1", nativeQuery = true)
    Optional<Integer> countViewsMyPosts(int idUser);

    @Query(value = "SELECT p.time FROM posts AS p WHERE p.is_active = 1 AND p.moderation_status = 'ACCEPTED' AND " +
            "p.user_id = ?1 ORDER BY p.time ASC limit 1", nativeQuery = true)
    LocalDateTime timeMyFirstPublication(int idUser);

    @Query(value = "SELECT COUNT(*) AS count FROM post_votes AS pw JOIN posts AS p ON pw.post_id = p.id WHERE " +
            "p.is_active = 1 AND p.moderation_status = 'ACCEPTED' AND pw.value = 1", nativeQuery = true)
    Optional<Integer> countLikesAllPosts();

    @Query(value = "SELECT COUNT(*) AS count FROM post_votes AS pw JOIN posts AS p ON pw.post_id = p.id WHERE " +
            "p.is_active = 1 AND p.moderation_status = 'ACCEPTED' AND pw.value = -1", nativeQuery = true)
    Optional<Integer> countDislikesAllPosts();

    @Query(value = "SELECT SUM(posts.view_count) AS sum FROM posts WHERE is_active  = 1 AND " +
            "moderation_status = 'ACCEPTED'", nativeQuery = true)
    Optional<Integer> countViewsAllPosts();

    @Query(value = "SELECT p.time FROM posts AS p WHERE p.is_active = 1 AND p.moderation_status = 'ACCEPTED' " +
            "ORDER BY p.time ASC limit 1", nativeQuery = true)
    LocalDateTime timeFirstPublication();

    @Query(value = "SELECT distinct YEAR(p.time) AS posts_years FROM posts AS p WHERE p.is_active = 1 AND " +
            "p.moderation_status = 'ACCEPTED' ORDER BY YEAR(p.time) ASC", nativeQuery = true)
    List<Integer> yearsWithPosts();

    @Query(value = "SELECT  year_post, count(*) AS count FROM(SELECT DATE(p.time) AS year_post FROM posts AS p WHERE " +
            "YEAR(p.time) = ?1 AND p.is_active = 1 AND p.moderation_status = 'ACCEPTED' ORDER BY year_post ASC) " +
            "AS q GROUP BY year_post", nativeQuery = true)
    List<Object[]> daysCountPosts(int year);
}