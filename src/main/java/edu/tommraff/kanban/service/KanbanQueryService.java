package edu.tommraff.kanban.service;

import edu.tommraff.kanban.domain.*; // for static metamodels
import edu.tommraff.kanban.domain.Kanban;
import edu.tommraff.kanban.repository.KanbanRepository;
import edu.tommraff.kanban.service.criteria.KanbanCriteria;
import edu.tommraff.kanban.service.dto.KanbanDTO;
import edu.tommraff.kanban.service.mapper.KanbanMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Kanban} entities in the database.
 * The main input is a {@link KanbanCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link KanbanDTO} or a {@link Page} of {@link KanbanDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class KanbanQueryService extends QueryService<Kanban> {

    private final Logger log = LoggerFactory.getLogger(KanbanQueryService.class);

    private final KanbanRepository kanbanRepository;

    private final KanbanMapper kanbanMapper;

    public KanbanQueryService(KanbanRepository kanbanRepository, KanbanMapper kanbanMapper) {
        this.kanbanRepository = kanbanRepository;
        this.kanbanMapper = kanbanMapper;
    }

    /**
     * Return a {@link List} of {@link KanbanDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<KanbanDTO> findByCriteria(KanbanCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Kanban> specification = createSpecification(criteria);
        return kanbanMapper.toDto(kanbanRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link KanbanDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<KanbanDTO> findByCriteria(KanbanCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Kanban> specification = createSpecification(criteria);
        return kanbanRepository.findAll(specification, page).map(kanbanMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(KanbanCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Kanban> specification = createSpecification(criteria);
        return kanbanRepository.count(specification);
    }

    /**
     * Function to convert {@link KanbanCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Kanban> createSpecification(KanbanCriteria criteria) {
        Specification<Kanban> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Kanban_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Kanban_.name));
            }
            if (criteria.getCreated_at() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreated_at(), Kanban_.created_at));
            }
            if (criteria.getLast_edit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLast_edit(), Kanban_.last_edit));
            }
            if (criteria.getUserkanbanId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserkanbanId(), root -> root.join(Kanban_.userkanban, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
