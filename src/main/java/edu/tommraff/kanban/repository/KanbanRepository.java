package edu.tommraff.kanban.repository;

import edu.tommraff.kanban.domain.Kanban;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(value = "SELECT * FROM KANBAN WHERE USERKANBAN_ID = ?1", nativeQuery = true)
    Page<Kanban> findByOwnedKanban(long id, Pageable pageable);

    @Query(value = "SELECT * FROM KANBAN WHERE ID = ?1 AND USERKANBAN_ID = ?2 LIMIT 1", nativeQuery = true)
    Optional<Kanban> findByIdAndOwner(long id, long ownerId);
}
