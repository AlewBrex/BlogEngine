package main.repository;

import main.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer>
{
    @Query(value = "SELECT DISTINCT tags.* FROM tags JOIN tag2post ON tags.id = tag2post.tag_id JOIN " +
            "posts ON posts.id = tag2post.post_id WHERE posts.is_active = 1 AND posts.moderation_status = " +
            "\"ACCEPTED\" AND posts.time < NOW() ORDER BY tags.id", nativeQuery = true)
    List<Tag> findAll();

    @Query(value = "SELECT COUNT(count_post) AS count FROM (SELECT COUNT(post_id) AS count_post FROM tag2pos JOIN " +
            "posts ON tag2post.post_id = posts.id WHERE posts.is_active = 1 AND posts.moderation_status = " +
            "\"ACCEPTED\" AND posts.time < NOW() GROUP BY tag2post.tag_id)", nativeQuery = true)
    Integer totalCount();

    @Query(value = "SELECT COUNT(*) as count FROM tag2post JOIN posts ON tag2post.post_id = posts.id WHERE posts" +
            ".is_active = 1 AND posts.moderation_status = \"ACCEPTED\" AND posts.time < NOW() AND tag_id = ?", nativeQuery = true)
    Integer countIdTag(int id);

    @Query(value = "SELECT DISTINCT tags.* FROM tags INNER JOIN tag2post ON tags.id = tag2post.tag_id INNER JOIN " +
            "posts ON posts.id = tag2post.post_id WHERE tags.name = ? AND posts.is_active = 1 AND posts" +
            ".moderation_status = \"ACCEPTED\" AND posts.time < NOW() ORDER BY tags.id", nativeQuery = true)
    List<Tag> getTagByQuery(String query);
}