package eu.ibagroup.snippet_manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(title="Language tag response DTO")
public class LangTagResponseTO {

    @Schema(description = "Language tag unique ID")
    private UUID id;

    @Schema(description = "Programming language", example = "Java")
    private String language;

    @Schema(description = "Language tag color (HEX format)", minLength = 7, maxLength = 7 ,example = "#ff00ff")
    private String color;

}
