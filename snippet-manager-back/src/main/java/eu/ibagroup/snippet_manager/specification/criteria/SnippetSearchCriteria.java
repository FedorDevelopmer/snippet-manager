package eu.ibagroup.snippet_manager.specification.criteria;

import eu.ibagroup.snippet_manager.entity.LangTag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "Code snippet search & filter criteria")
public class SnippetSearchCriteria {

    @Schema(description = "Snippet title", example = "Binary tree impl")
    private String title;

    @Schema(description = "Snippet creation date", example = "20-06-2025T17:00:00")
    private LocalDateTime creationDate;

    @Schema(description = "Code content fragment", example = "public void addNode(TreeNode node) {...}...")
    private String code;

    @Schema(description = "Selected language tags")
    private Set<String> langTags;

    public boolean isEmpty() {
        return title == null && creationDate == null && code == null && langTags == null;
    }
}