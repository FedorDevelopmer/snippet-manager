package eu.ibagroup.snippet_manager.repository;

import eu.ibagroup.snippet_manager.entity.LangTag;
import eu.ibagroup.snippet_manager.enumeration.DevLang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface LangTagRepository extends JpaRepository<LangTag, UUID>, JpaSpecificationExecutor<LangTag> {

    Optional<LangTag> findByLanguage(DevLang language);
}
