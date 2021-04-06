package main.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post {
  @Id
  @NotNull
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT")
  private int isActive;

  @Enumerated(EnumType.STRING)
  @Column(name = "moderation_status", nullable = false, columnDefinition = "enum default 'NEW'")
  private ModerationStatus moderationStatus;

  @JoinColumn(name = "moderator_id", columnDefinition = "INT")
  private Integer moderatorId;

  @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", nullable = false, columnDefinition = "INT")
  private User users;

  @Column(nullable = false, columnDefinition = "DATETIME")
  private LocalDateTime time;

  @Column(nullable = false, columnDefinition = "VARCHAR(255)")
  private String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String text;

  @Column(name = "view_count", nullable = false, columnDefinition = "INT")
  private int viewCount;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "tag2post",
      joinColumns = {@JoinColumn(name = "post_id")},
      inverseJoinColumns = {@JoinColumn(name = "tag_id")})
  private List<Tag> tags;

  @OneToMany(fetch = FetchType.LAZY)
  @Column(nullable = false)
  private List<Comment> comments;

  @OneToMany(fetch = FetchType.LAZY)
  @Column(nullable = false)
  private List<Vote> votes;

  public Post(
      int isActive, User users, LocalDateTime time, String title, String text, List<Tag> tags) {
    this.isActive = isActive;
    this.users = users;
    this.time = time;
    this.title = title;
    this.text = text;
    this.tags = tags;
  }

  public Boolean activePost() {
    return isActive == 1;
  }

  public enum ModerationStatus {
    NEW,
    ACCEPTED,
    DECLINED
  }
}
