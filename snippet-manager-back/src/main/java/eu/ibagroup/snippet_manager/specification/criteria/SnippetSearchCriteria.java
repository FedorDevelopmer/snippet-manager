package eu.ibagroup.snippet_manager.specification.criteria;

import eu.ibagroup.snippet_manager.entity.LangTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SnippetSearchCriteria {
    private String title;
    private LocalDateTime creationDate;
    private String code;
    private Set<String> langTags;

    public boolean isEmpty() {
        return title == null && creationDate == null && code == null && langTags == null;
    }
}