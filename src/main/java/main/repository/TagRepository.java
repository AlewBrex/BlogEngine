package main.repository;

import main.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    @Query(value = "SELECT DISTINCT t.* FROM tags AS t JOIN tag2post AS tp ON t.id = tp.tag_id JOIN " +
            "posts AS p ON p.id = tp.post_id WHERE p.is_active = 1 AND p.moderation_status = " +
            "\"ACCEPTED\" AND p.time < NOW() ORDER BY t.id", nativeQuery = true)
    List<Tag> findAll();

    @Query(value = "SELECT COUNT(count_post) AS count FROM (SELECT COUNT(post_id) AS count_post FROM tag2post AS tp " +
            "JOIN posts ON tp.post_id = posts.id WHERE posts.is_active = 1 AND posts.moderation_status = " +
            "\"ACCEPTED\" AND posts.time < NOW() GROUP BY tp.tag_id) AS posts_with_tags", nativeQuery = true)
    Integer totalCount();

    @Query(value = "SELECT COUNT(*) AS count FROM tag2post AS tp JOIN posts ON tp.post_id = posts.id WHERE " +
            "posts.is_active = 1 AND posts.moderation_status = \"ACCEPTED\" AND posts.time < NOW() AND tag_id = ?",
            nativeQuery = true)
    Integer countIdTag(int id);

    @Query(value = "SELECT DISTINCT tags.* FROM tags JOIN tag2post AS tp ON tags.id = tp.tag_id JOIN " +
            "posts ON posts.id = tp.post_id WHERE tags.name = ? AND posts.is_active = 1 AND posts" +
            ".moderation_status = \"ACCEPTED\" AND posts.time < NOW() ORDER BY tags.id", nativeQuery = true)
    List<Tag> getTagByQuery(String query);

    @Query(value = "SELECT COUNT(name) AS count FROM tags WHERE name = \"?1\"", nativeQuery = true)
    Integer countTagByName(String tag);

    @Query(value = "SELECT * FROM tags where name = \"?\";", nativeQuery = true)
    Tag getTagByName(String tagName);
}