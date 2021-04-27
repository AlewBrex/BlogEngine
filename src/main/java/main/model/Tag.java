package main.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tags")
@Data
@RequiredArgsConstructor
public class Tag {
  @Id
  @NotNull
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(nullable = false, columnDefinition = "VARCHAR(255)")
  private String name;

  public Tag(String name) {
    this.name = name;
  }
}
