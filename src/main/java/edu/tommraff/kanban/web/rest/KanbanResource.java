package edu.tommraff.kanban.web.rest;

import edu.tommraff.kanban.repository.KanbanRepository;
import edu.tommraff.kanban.service.KanbanQueryService;
import edu.tommraff.kanban.service.KanbanService;
import edu.tommraff.kanban.service.UserService;
import edu.tommraff.kanban.service.criteria.KanbanCriteria;
import edu.tommraff.kanban.service.dto.AdminUserDTO;
import edu.tommraff.kanban.service.dto.KanbanDTO;
import edu.tommraff.kanban.service.dto.UserDTO;
import edu.tommraff.kanban.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
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
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link edu.tommraff.kanban.domain.Kanban}.
 */
@RestController
@RequestMapping("/api")
public class KanbanResource {

    private static class KanbanResourceException extends RuntimeException {

        private KanbanResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(KanbanResource.class);

    private static final String ENTITY_NAME = "kanban";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KanbanService kanbanService;

    private final KanbanRepository kanbanRepository;

    private final KanbanQueryService kanbanQueryService;

    private final UserService userService;

    public KanbanResource(
        KanbanService kanbanService,
        KanbanRepository kanbanRepository,
        KanbanQueryService kanbanQueryService,
        UserService userService
    ) {
        this.kanbanService = kanbanService;
        this.kanbanRepository = kanbanRepository;
        this.kanbanQueryService = kanbanQueryService;
        this.userService = userService;
    }

    /**
     * {@code POST  /kanbans} : Create a new kanban.
     *
     * @param kanbanDTO the kanbanDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new kanbanDTO, or with status {@code 400 (Bad Request)} if the kanban has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/kanbans")
    public ResponseEntity<KanbanDTO> createKanban(@Valid @RequestBody KanbanDTO kanbanDTO) throws URISyntaxException {
        log.debug("REST request to save Kanban : {}", kanbanDTO);
        if (kanbanDTO.getId() != null) {
            throw new BadRequestAlertException("A new kanban cannot already have an ID", ENTITY_NAME, "idexists");
        }

        // We take from userService the current user, and create a kanban with his info
        kanbanDTO.setUserkanban(
            userService.getUserWithAuthorities().map(UserDTO::new).orElseThrow(() -> new KanbanResourceException("User could not be found"))
        );

        // We add a created at field and last edit
        kanbanDTO.setCreated_at(LocalDate.now());
        kanbanDTO.setLast_edit(LocalDate.now());

        KanbanDTO result = kanbanService.save(kanbanDTO);
        return ResponseEntity
            .created(new URI("/api/kanbans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /kanbans/:id} : Updates an existing kanban.
     *
     * @param id the id of the kanbanDTO to save.
     * @param kanbanDTO the kanbanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kanbanDTO,
     * or with status {@code 400 (Bad Request)} if the kanbanDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the kanbanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/kanbans/{id}")
    public ResponseEntity<KanbanDTO> updateKanban(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody KanbanDTO kanbanDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Kanban : {}, {}", id, kanbanDTO);
        if (kanbanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kanbanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kanbanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        kanbanDTO.setLast_edit(LocalDate.now());

        KanbanDTO result = kanbanService.save(kanbanDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, kanbanDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /kanbans/:id} : Partial updates given fields of an existing kanban, field will ignore if it is null
     *
     * @param id the id of the kanbanDTO to save.
     * @param kanbanDTO the kanbanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kanbanDTO,
     * or with status {@code 400 (Bad Request)} if the kanbanDTO is not valid,
     * or with status {@code 404 (Not Found)} if the kanbanDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the kanbanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/kanbans/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<KanbanDTO> partialUpdateKanban(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody KanbanDTO kanbanDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Kanban partially : {}, {}", id, kanbanDTO);
        if (kanbanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kanbanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kanbanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        kanbanDTO.setLast_edit(LocalDate.now());

        Optional<KanbanDTO> result = kanbanService.partialUpdate(kanbanDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, kanbanDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /kanbans} : get all the kanbans for the current user.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of kanbans in body.
     */
    @GetMapping("/kanbans")
    public ResponseEntity<List<KanbanDTO>> getAllKanbans(KanbanCriteria criteria, Pageable pageable) {
        // FIXME: Sistemare questa schifezza
        Long longValue = userService.getUserWithAuthorities().get().getId();
        LongFilter longFilter = new LongFilter();
        longFilter.setEquals(longValue);
        criteria.setUserkanbanId(longFilter);

        log.debug("REST request to get Kanbans by criteria: {}", criteria);
        Page<KanbanDTO> page = kanbanQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /kanbans/count} : count all the kanbans.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/kanbans/count")
    public ResponseEntity<Long> countKanbans(KanbanCriteria criteria) {
        log.debug("REST request to count Kanbans by criteria: {}", criteria);
        return ResponseEntity.ok().body(kanbanQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /kanbans/:id} : get the "id" kanban.
     *
     * @param id the id of the kanbanDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the kanbanDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/kanbans/{id}")
    public ResponseEntity<KanbanDTO> getKanban(@PathVariable Long id) {
        log.debug("REST request to get Kanban : {}", id);
        Optional<KanbanDTO> kanbanDTO = kanbanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(kanbanDTO);
    }

    /**
     * {@code DELETE  /kanbans/:id} : delete the "id" kanban.
     *
     * @param id the id of the kanbanDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/kanbans/{id}")
    public ResponseEntity<Void> deleteKanban(@PathVariable Long id) {
        log.debug("REST request to delete Kanban : {}", id);
        kanbanService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
