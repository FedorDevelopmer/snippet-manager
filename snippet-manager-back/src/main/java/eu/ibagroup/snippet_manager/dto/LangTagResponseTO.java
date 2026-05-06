package eu.ibagroup.snippet_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LangTagResponseTO {

    private UUID id;

    private String language;

    private String color;

}
