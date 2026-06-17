package eu.ibagroup.snippet_manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "Language tag update request DTO")
public class LangTagUpdateTO {

    @NotEmpty
    @Schema(description = "Programming language of tag", example = "C#")
    private String language;

    @NotBlank(message = "Color cannot be empty")
    @Size(min = 7, max = 7)
    @Schema(description = "Language tag color in HEX format", minLength = 7, maxLength = 7 ,example = "#ff00ff")
    private String color;
}
