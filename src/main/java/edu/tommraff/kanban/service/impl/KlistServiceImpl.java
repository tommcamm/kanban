package edu.tommraff.kanban.service.impl;

import edu.tommraff.kanban.domain.Klist;
import edu.tommraff.kanban.repository.KlistRepository;
import edu.tommraff.kanban.service.KlistService;
import edu.tommraff.kanban.service.dto.KlistDTO;
import edu.tommraff.kanban.service.mapper.KlistMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Klist}.
 */
@Service
@Transactional
public class KlistServiceImpl implements KlistService {

    private final Logger log = LoggerFactory.getLogger(KlistServiceImpl.class);

    private final KlistRepository klistRepository;

    private final KlistMapper klistMapper;

    public KlistServiceImpl(KlistRepository klistRepository, KlistMapper klistMapper) {
        this.klistRepository = klistRepository;
        this.klistMapper = klistMapper;
    }

    @Override
    public KlistDTO save(KlistDTO klistDTO) {
        log.debug("Request to save Klist : {}", klistDTO);
        Klist klist = klistMapper.toEntity(klistDTO);
        klist = klistRepository.save(klist);
        return klistMapper.toDto(klist);
    }

    @Override
    public Optional<KlistDTO> partialUpdate(KlistDTO klistDTO) {
        log.debug("Request to partially update Klist : {}", klistDTO);

        return klistRepository
            .findById(klistDTO.getId())
            .map(
                existingKlist -> {
                    klistMapper.partialUpdate(existingKlist, klistDTO);

                    return existingKlist;
                }
            )
            .map(klistRepository::save)
            .map(klistMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<KlistDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Klists");
        return klistRepository.findAll(pageable).map(klistMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<KlistDTO> findOne(Long id) {
        log.debug("Request to get Klist : {}", id);
        return klistRepository.findById(id).map(klistMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Klist : {}", id);
        klistRepository.deleteById(id);
    }
}
