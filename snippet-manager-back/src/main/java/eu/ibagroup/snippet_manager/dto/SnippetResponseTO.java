package eu.ibagroup.snippet_manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Schema(title = "Code snippet response DTO")
public class SnippetResponseTO {


    @Schema(description = "Snippet unique ID")
    private UUID id;

    @Schema(description = "Code content", minLength = 10, maxLength = 2000, example = "ngOnInit() : void {...}")
    private String code;

    @Schema(description = "Snippets title", minLength = 3, maxLength = 100, example = "On init Angular method impl")
    private String title;

    @Schema(description = "Programming language", example = "JavaScript")
    private String language;

    @Schema(description = "Snippet creation date", example = "2026-01-01T12:00:00")
    private LocalDateTime creationDate;
}
