package edu.tommraff.kanban.repository;

import edu.tommraff.kanban.domain.Kanban;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Kanban entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KanbanRepository extends JpaRepository<Kanban, Long>, JpaSpecificationExecutor<Kanban> {
    @Query("select kanban from Kanban kanban where kanban.userkanban.login = ?#{principal.username}")
    List<Kanban> findByUserkanbanIsCurrentUser();
}
