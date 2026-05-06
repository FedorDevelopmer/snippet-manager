package eu.ibagroup.snippet_manager.dto;

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
public class LangTagRequestTO {

    private UUID id;

    @NotEmpty
    private String language;

    @NotBlank(message = "Color cannot be empty")
    @Size(min = 7, max = 7)
    private String color;

}
