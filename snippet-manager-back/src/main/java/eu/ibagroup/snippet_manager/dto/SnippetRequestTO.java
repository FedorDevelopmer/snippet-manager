package eu.ibagroup.snippet_manager.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SnippetRequestTO {

    private UUID id;

    @NotEmpty
    @Length(min = 10, max = 2000)
    private String code;

    @NotEmpty
    @Length(min = 3, max = 100)
    private String title;

    @NotEmpty
    private String language;

    private LocalDateTime creationDate;
}
