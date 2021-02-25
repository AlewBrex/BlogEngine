package main.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "global_settings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GlobalSettings {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code", nullable = false, columnDefinition = "VARCHAR(255)")
    private String code;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(name = "value", nullable = false, columnDefinition = "VARCHAR(255)")
    private String value;
}