package edu.tommraff.kanban.service;

import edu.tommraff.kanban.domain.*; // for static metamodels
import edu.tommraff.kanban.domain.Klist;
import edu.tommraff.kanban.repository.KlistRepository;
import edu.tommraff.kanban.service.criteria.KlistCriteria;
import edu.tommraff.kanban.service.dto.KlistDTO;
import edu.tommraff.kanban.service.mapper.KlistMapper;
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
 * Service for executing complex queries for {@link Klist} entities in the database.
 * The main input is a {@link KlistCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link KlistDTO} or a {@link Page} of {@link KlistDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class KlistQueryService extends QueryService<Klist> {

    private final Logger log = LoggerFactory.getLogger(KlistQueryService.class);

    private final KlistRepository klistRepository;

    private final KlistMapper klistMapper;

    public KlistQueryService(KlistRepository klistRepository, KlistMapper klistMapper) {
        this.klistRepository = klistRepository;
        this.klistMapper = klistMapper;
    }

    /**
     * Return a {@link List} of {@link KlistDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<KlistDTO> findByCriteria(KlistCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Klist> specification = createSpecification(criteria);
        return klistMapper.toDto(klistRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link KlistDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<KlistDTO> findByCriteria(KlistCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Klist> specification = createSpecification(criteria);
        return klistRepository.findAll(specification, page).map(klistMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(KlistCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Klist> specification = createSpecification(criteria);
        return klistRepository.count(specification);
    }

    /**
     * Function to convert {@link KlistCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Klist> createSpecification(KlistCriteria criteria) {
        Specification<Klist> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Klist_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Klist_.title));
            }
            if (criteria.getOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrder(), Klist_.order));
            }
            if (criteria.getKanbanlistId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getKanbanlistId(), root -> root.join(Klist_.kanbanlist, JoinType.LEFT).get(Kanban_.id))
                    );
            }
        }
        return specification;
    }
}
