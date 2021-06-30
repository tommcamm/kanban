package edu.tommraff.kanban.service;

import edu.tommraff.kanban.service.dto.KanbanDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link edu.tommraff.kanban.domain.Kanban}.
 */
public interface KanbanService {
    /**
     * Save a kanban.
     *
     * @param kanbanDTO the entity to save.
     * @return the persisted entity.
     */
    KanbanDTO save(KanbanDTO kanbanDTO);

    /**
     * Partially updates a kanban.
     *
     * @param kanbanDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<KanbanDTO> partialUpdate(KanbanDTO kanbanDTO);

    /**
     * Get all the kanbans.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<KanbanDTO> findAll(Pageable pageable);

    /**
     * Get all the kanbans from a owner id.
     *
     * @param pageable the pagination information.
     * @param id the id of the owner
     * @return the list of entities owned by provided id.
     */
    Page<KanbanDTO> findAllOwned(long id, Pageable pageable);

    /**
     * Get the "id" kanban.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<KanbanDTO> findOne(Long id);

    /**
     * Get the "id" kanban owned by ownerId.
     *
     * @param id the id of the entity
     * @param ownerId the id of the owner
     * @return the entity
     */
    Optional<KanbanDTO> findOneOwned(Long id, Long ownerId);

    /**
     * Delete the "id" kanban.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Checks if the kanban is owned
     *
     * @param kanbanId id of the kanban entity
     * @param ownerId id of the owner user
     * @return true if is owned, false otherwise
     */
    boolean isOwned(Long kanbanId, Long ownerId);
}
