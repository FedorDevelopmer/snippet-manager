package eu.ibagroup.snippet_manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "Language tag request DTO")
public class LangTagRequestTO {

    @NotEmpty
    @Schema(description = "Programming language", example = "Java")
    private String language;

    @NotBlank(message = "Color cannot be empty")
    @Size(min = 7, max = 7)
    @Schema(description = "Language tag color (HEX format)", minLength = 7, maxLength = 7, example = "#FFAAEE")
    private String color;

}
