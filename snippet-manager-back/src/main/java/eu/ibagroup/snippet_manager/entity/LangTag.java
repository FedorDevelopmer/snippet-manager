package eu.ibagroup.snippet_manager.entity;


import eu.ibagroup.snippet_manager.enumeration.DevLang;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class LangTag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private DevLang language;

    @Column(name = "color")
    private String color;
}
