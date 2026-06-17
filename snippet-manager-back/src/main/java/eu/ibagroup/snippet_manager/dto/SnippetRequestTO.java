package eu.ibagroup.snippet_manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(title = "Code snippet request DTO")
public class SnippetRequestTO {

    @NotEmpty
    @Length(min = 10, max = 2000)
    @Schema(description = "Code content of snippet", minLength = 10, maxLength = 2000, example = "public void quickSort(T[] arr) {...}")
    private String code;

    @NotEmpty
    @Length(min = 3, max = 100)
    @Schema(description = "Snippet title", minLength = 3, maxLength = 100, example = "Quick sort algorithm")
    private String title;

    @NotEmpty
    @Schema(description = "Programming language", example = "Java")
    private String language;
}
