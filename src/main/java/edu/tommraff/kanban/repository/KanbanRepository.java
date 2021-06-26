package edu.tommraff.kanban.repository;

import edu.tommraff.kanban.domain.Kanban;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Kanban entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KanbanRepository extends JpaRepository<Kanban, Long>, JpaSpecificationExecutor<Kanban> {}
