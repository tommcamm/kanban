package edu.tommraff.kanban.web.rest;

import edu.tommraff.kanban.repository.KlistRepository;
import edu.tommraff.kanban.service.KlistQueryService;
import edu.tommraff.kanban.service.KlistService;
import edu.tommraff.kanban.service.criteria.KlistCriteria;
import edu.tommraff.kanban.service.dto.KlistDTO;
import edu.tommraff.kanban.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link edu.tommraff.kanban.domain.Klist}.
 */
@RestController
@RequestMapping("/api")
public class KlistResource {

    private final Logger log = LoggerFactory.getLogger(KlistResource.class);

    private static final String ENTITY_NAME = "klist";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KlistService klistService;

    private final KlistRepository klistRepository;

    private final KlistQueryService klistQueryService;

    public KlistResource(KlistService klistService, KlistRepository klistRepository, KlistQueryService klistQueryService) {
        this.klistService = klistService;
        this.klistRepository = klistRepository;
        this.klistQueryService = klistQueryService;
    }

    /**
     * {@code POST  /klists} : Create a new klist.
     *
     * @param klistDTO the klistDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new klistDTO, or with status {@code 400 (Bad Request)} if the klist has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/klists")
    public ResponseEntity<KlistDTO> createKlist(@Valid @RequestBody KlistDTO klistDTO) throws URISyntaxException {
        log.debug("REST request to save Klist : {}", klistDTO);
        if (klistDTO.getId() != null) {
            throw new BadRequestAlertException("A new klist cannot already have an ID", ENTITY_NAME, "idexists");
        }
        KlistDTO result = klistService.save(klistDTO);
        return ResponseEntity
            .created(new URI("/api/klists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /klists/:id} : Updates an existing klist.
     *
     * @param id the id of the klistDTO to save.
     * @param klistDTO the klistDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated klistDTO,
     * or with status {@code 400 (Bad Request)} if the klistDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the klistDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/klists/{id}")
    public ResponseEntity<KlistDTO> updateKlist(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody KlistDTO klistDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Klist : {}, {}", id, klistDTO);
        if (klistDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, klistDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!klistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        KlistDTO result = klistService.save(klistDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, klistDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /klists/:id} : Partial updates given fields of an existing klist, field will ignore if it is null
     *
     * @param id the id of the klistDTO to save.
     * @param klistDTO the klistDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated klistDTO,
     * or with status {@code 400 (Bad Request)} if the klistDTO is not valid,
     * or with status {@code 404 (Not Found)} if the klistDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the klistDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/klists/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<KlistDTO> partialUpdateKlist(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody KlistDTO klistDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Klist partially : {}, {}", id, klistDTO);
        if (klistDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, klistDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!klistRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<KlistDTO> result = klistService.partialUpdate(klistDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, klistDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /klists} : get all the klists.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of klists in body.
     */
    @GetMapping("/klists")
    public ResponseEntity<List<KlistDTO>> getAllKlists(KlistCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Klists by criteria: {}", criteria);
        Page<KlistDTO> page = klistQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /klists/count} : count all the klists.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/klists/count")
    public ResponseEntity<Long> countKlists(KlistCriteria criteria) {
        log.debug("REST request to count Klists by criteria: {}", criteria);
        return ResponseEntity.ok().body(klistQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /klists/:id} : get the "id" klist.
     *
     * @param id the id of the klistDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the klistDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/klists/{id}")
    public ResponseEntity<KlistDTO> getKlist(@PathVariable Long id) {
        log.debug("REST request to get Klist : {}", id);
        Optional<KlistDTO> klistDTO = klistService.findOne(id);
        return ResponseUtil.wrapOrNotFound(klistDTO);
    }

    /**
     * {@code DELETE  /klists/:id} : delete the "id" klist.
     *
     * @param id the id of the klistDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/klists/{id}")
    public ResponseEntity<Void> deleteKlist(@PathVariable Long id) {
        log.debug("REST request to delete Klist : {}", id);
        klistService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
