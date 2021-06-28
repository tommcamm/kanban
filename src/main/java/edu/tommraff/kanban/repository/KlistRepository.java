package edu.tommraff.kanban.repository;

import edu.tommraff.kanban.domain.Klist;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Klist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KlistRepository extends JpaRepository<Klist, Long>, JpaSpecificationExecutor<Klist> {}
