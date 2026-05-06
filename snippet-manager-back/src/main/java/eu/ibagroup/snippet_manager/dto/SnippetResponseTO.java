package eu.ibagroup.snippet_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SnippetResponseTO {

    private UUID id;

    private String code;

    private String title;

    private String language;

    private LocalDateTime creationDate;
}
