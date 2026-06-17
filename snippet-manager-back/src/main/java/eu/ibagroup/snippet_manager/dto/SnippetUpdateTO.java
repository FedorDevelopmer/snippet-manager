package eu.ibagroup.snippet_manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "Code snippet update request DTO")
public class SnippetUpdateTO {


    @NotEmpty
    @Length(min = 20, max = 2000)
    @Schema(description = "Code content", minLength = 20, maxLength = 2000, example = "public void save() {...}")
    private String code;

    @NotEmpty
    @Length(min = 3, max = 100)
    @Schema(description = "Snippet title", minLength = 3, maxLength = 100, example = "Autosaving method implementation")
    private String title;

    @NotEmpty
    @Schema(description = "Programming language", example = "Java")
    private String language;

    @PastOrPresent
    @Schema(description = "Snippet creation date", example = "26-11-26T12:00:00")
    private LocalDateTime creationDate;
}
