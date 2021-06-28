package edu.tommraff.kanban.service;

import edu.tommraff.kanban.service.dto.KlistDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link edu.tommraff.kanban.domain.Klist}.
 */
public interface KlistService {
    /**
     * Save a klist.
     *
     * @param klistDTO the entity to save.
     * @return the persisted entity.
     */
    KlistDTO save(KlistDTO klistDTO);

    /**
     * Partially updates a klist.
     *
     * @param klistDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<KlistDTO> partialUpdate(KlistDTO klistDTO);

    /**
     * Get all the klists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<KlistDTO> findAll(Pageable pageable);

    /**
     * Get the "id" klist.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<KlistDTO> findOne(Long id);

    /**
     * Delete the "id" klist.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
