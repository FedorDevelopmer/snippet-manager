package eu.ibagroup.snippet_manager.specification;

import eu.ibagroup.snippet_manager.entity.Snippet;
import eu.ibagroup.snippet_manager.enumeration.DevLang;
import eu.ibagroup.snippet_manager.specification.criteria.SnippetSearchCriteria;
import jakarta.persistence.criteria.*;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SnippetSpecification implements Specification<Snippet> {

    private SnippetSearchCriteria criteria;

    public SnippetSpecification(SnippetSearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public @Nullable Predicate toPredicate(Root<Snippet> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if(criteria.getLangTags() != null){
            predicates.add(root.get("language").get("language").in(criteria.getLangTags().stream().map(DevLang::fromLanguage).collect(Collectors.toSet())));
        }
        if(criteria.getCreationDate() != null && root.get("creation_date").getJavaType().equals(LocalDateTime.class)){
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("creation_date"), criteriaBuilder.literal(criteria.getCreationDate())));
        }
        if(criteria.getTitle() != null && root.get("title").getJavaType().equals(String.class)){
            predicates.add(criteriaBuilder.like(root.get("title"),"%" + criteria.getTitle() + "%"));
        }
        if(criteria.getCode() != null && root.get("code").getJavaType().equals(String.class)){
            predicates.add(criteriaBuilder.like(root.get("code"),"%" + criteria.getCode() + "%"));
        }
        return criteriaBuilder.and(predicates);
    }
}
