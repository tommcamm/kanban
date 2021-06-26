package edu.tommraff.kanban.service.impl;

import edu.tommraff.kanban.domain.Kanban;
import edu.tommraff.kanban.repository.KanbanRepository;
import edu.tommraff.kanban.service.KanbanService;
import edu.tommraff.kanban.service.dto.KanbanDTO;
import edu.tommraff.kanban.service.mapper.KanbanMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Kanban}.
 */
@Service
@Transactional
public class KanbanServiceImpl implements KanbanService {

    private final Logger log = LoggerFactory.getLogger(KanbanServiceImpl.class);

    private final KanbanRepository kanbanRepository;

    private final KanbanMapper kanbanMapper;

    public KanbanServiceImpl(KanbanRepository kanbanRepository, KanbanMapper kanbanMapper) {
        this.kanbanRepository = kanbanRepository;
        this.kanbanMapper = kanbanMapper;
    }

    @Override
    public KanbanDTO save(KanbanDTO kanbanDTO) {
        log.debug("Request to save Kanban : {}", kanbanDTO);
        Kanban kanban = kanbanMapper.toEntity(kanbanDTO);
        kanban = kanbanRepository.save(kanban);
        return kanbanMapper.toDto(kanban);
    }

    @Override
    public Optional<KanbanDTO> partialUpdate(KanbanDTO kanbanDTO) {
        log.debug("Request to partially update Kanban : {}", kanbanDTO);

        return kanbanRepository
            .findById(kanbanDTO.getId())
            .map(
                existingKanban -> {
                    kanbanMapper.partialUpdate(existingKanban, kanbanDTO);

                    return existingKanban;
                }
            )
            .map(kanbanRepository::save)
            .map(kanbanMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<KanbanDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Kanbans");
        return kanbanRepository.findAll(pageable).map(kanbanMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<KanbanDTO> findOne(Long id) {
        log.debug("Request to get Kanban : {}", id);
        return kanbanRepository.findById(id).map(kanbanMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Kanban : {}", id);
        kanbanRepository.deleteById(id);
    }
}
