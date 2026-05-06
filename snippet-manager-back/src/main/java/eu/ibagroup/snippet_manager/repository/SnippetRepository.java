package eu.ibagroup.snippet_manager.repository;

import eu.ibagroup.snippet_manager.entity.Snippet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SnippetRepository extends JpaRepository<Snippet, UUID>, JpaSpecificationExecutor<Snippet> {
    boolean existsByTitle(String title);
}
