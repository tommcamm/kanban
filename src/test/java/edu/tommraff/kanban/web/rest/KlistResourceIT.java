package edu.tommraff.kanban.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import edu.tommraff.kanban.IntegrationTest;
import edu.tommraff.kanban.domain.Kanban;
import edu.tommraff.kanban.domain.Klist;
import edu.tommraff.kanban.repository.KlistRepository;
import edu.tommraff.kanban.service.criteria.KlistCriteria;
import edu.tommraff.kanban.service.dto.KlistDTO;
import edu.tommraff.kanban.service.mapper.KlistMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link KlistResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class KlistResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;
    private static final Integer SMALLER_ORDER = 1 - 1;

    private static final String ENTITY_API_URL = "/api/klists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private KlistRepository klistRepository;

    @Autowired
    private KlistMapper klistMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKlistMockMvc;

    private Klist klist;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Klist createEntity(EntityManager em) {
        Klist klist = new Klist().title(DEFAULT_TITLE).order(DEFAULT_ORDER);
        // Add required entity
        Kanban kanban;
        if (TestUtil.findAll(em, Kanban.class).isEmpty()) {
            kanban = KanbanResourceIT.createEntity(em);
            em.persist(kanban);
            em.flush();
        } else {
            kanban = TestUtil.findAll(em, Kanban.class).get(0);
        }
        klist.setKanbanlist(kanban);
        return klist;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Klist createUpdatedEntity(EntityManager em) {
        Klist klist = new Klist().title(UPDATED_TITLE).order(UPDATED_ORDER);
        // Add required entity
        Kanban kanban;
        if (TestUtil.findAll(em, Kanban.class).isEmpty()) {
            kanban = KanbanResourceIT.createUpdatedEntity(em);
            em.persist(kanban);
            em.flush();
        } else {
            kanban = TestUtil.findAll(em, Kanban.class).get(0);
        }
        klist.setKanbanlist(kanban);
        return klist;
    }

    @BeforeEach
    public void initTest() {
        klist = createEntity(em);
    }

    @Test
    @Transactional
    void createKlist() throws Exception {
        int databaseSizeBeforeCreate = klistRepository.findAll().size();
        // Create the Klist
        KlistDTO klistDTO = klistMapper.toDto(klist);
        restKlistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(klistDTO)))
            .andExpect(status().isCreated());

        // Validate the Klist in the database
        List<Klist> klistList = klistRepository.findAll();
        assertThat(klistList).hasSize(databaseSizeBeforeCreate + 1);
        Klist testKlist = klistList.get(klistList.size() - 1);
        assertThat(testKlist.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testKlist.getOrder()).isEqualTo(DEFAULT_ORDER);
    }

    @Test
    @Transactional
    void createKlistWithExistingId() throws Exception {
        // Create the Klist with an existing ID
        klist.setId(1L);
        KlistDTO klistDTO = klistMapper.toDto(klist);

        int databaseSizeBeforeCreate = klistRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restKlistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(klistDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Klist in the database
        List<Klist> klistList = klistRepository.findAll();
        assertThat(klistList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = klistRepository.findAll().size();
        // set the field null
        klist.setTitle(null);

        // Create the Klist, which fails.
        KlistDTO klistDTO = klistMapper.toDto(klist);

        restKlistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(klistDTO)))
            .andExpect(status().isBadRequest());

        List<Klist> klistList = klistRepository.findAll();
        assertThat(klistList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllKlists() throws Exception {
        // Initialize the database
        klistRepository.saveAndFlush(klist);

        // Get all the klistList
        restKlistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(klist.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)));
    }

    @Test
    @Transactional
    void getKlist() throws Exception {
        // Initialize the database
        klistRepository.saveAndFlush(klist);

        // Get the klist
        restKlistMockMvc
            .perform(get(ENTITY_API_URL_ID, klist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(klist.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER));
    }

    @Test
    @Transactional
    void getKlistsByIdFiltering() throws Exception {
        // Initialize the database
        klistRepository.saveAndFlush(klist);

        Long id = klist.getId();

        defaultKlistShouldBeFound("id.equals=" + id);
        defaultKlistShouldNotBeFound("id.notEquals=" + id);

        defaultKlistShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultKlistShouldNotBeFound("id.greaterThan=" + id);

        defaultKlistShouldBeFound("id.lessThanOrEqual=" + id);
        defaultKlistShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllKlistsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        klistRepository.saveAndFlush(klist);

        // Get all the klistList where title equals to DEFAULT_TITLE
        defaultKlistShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the klistList where title equals to UPDATED_TITLE
        defaultKlistShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllKlistsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        klistRepository.saveAndFlush(klist);

        // Get all the klistList where title not equals to DEFAULT_TITLE
        defaultKlistShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the klistList where title not equals to UPDATED_TITLE
        defaultKlistShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllKlistsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        klistRepository.saveAndFlush(klist);

        // Get all the klistList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultKlistShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the klistList where title equals to UPDATED_TITLE
        defaultKlistShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllKlistsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        klistRepository.saveAndFlush(klist);

        // Get all the klistList where title is not null
        defaultKlistShouldBeFound("title.specified=true");

        // Get all the klistList where title is null
        defaultKlistShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllKlistsByTitleContainsSomething() throws Exception {
        // Initialize the database
        klistRepository.saveAndFlush(klist);

        // Get all the klistList where title contains DEFAULT_TITLE
        defaultKlistShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the klistList where title contains UPDATED_TITLE
        defaultKlistShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllKlistsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        klistRepository.saveAndFlush(klist);

        // Get all the klistList where title does not contain DEFAULT_TITLE
        defaultKlistShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the klistList where title does not contain UPDATED_TITLE
        defaultKlistShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllKlistsByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        klistRepository.saveAndFlush(klist);

        // Get all the klistList where order equals to DEFAULT_ORDER
        defaultKlistShouldBeFound("order.equals=" + DEFAULT_ORDER);

        // Get all the klistList where order equals to UPDATED_ORDER
        defaultKlistShouldNotBeFound("order.equals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllKlistsByOrderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        klistRepository.saveAndFlush(klist);

        // Get all the klistList where order not equals to DEFAULT_ORDER
        defaultKlistShouldNotBeFound("order.notEquals=" + DEFAULT_ORDER);

        // Get all the klistList where order not equals to UPDATED_ORDER
        defaultKlistShouldBeFound("order.notEquals=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllKlistsByOrderIsInShouldWork() throws Exception {
        // Initialize the database
        klistRepository.saveAndFlush(klist);

        // Get all the klistList where order in DEFAULT_ORDER or UPDATED_ORDER
        defaultKlistShouldBeFound("order.in=" + DEFAULT_ORDER + "," + UPDATED_ORDER);

        // Get all the klistList where order equals to UPDATED_ORDER
        defaultKlistShouldNotBeFound("order.in=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllKlistsByOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        klistRepository.saveAndFlush(klist);

        // Get all the klistList where order is not null
        defaultKlistShouldBeFound("order.specified=true");

        // Get all the klistList where order is null
        defaultKlistShouldNotBeFound("order.specified=false");
    }

    @Test
    @Transactional
    void getAllKlistsByOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        klistRepository.saveAndFlush(klist);

        // Get all the klistList where order is greater than or equal to DEFAULT_ORDER
        defaultKlistShouldBeFound("order.greaterThanOrEqual=" + DEFAULT_ORDER);

        // Get all the klistList where order is greater than or equal to UPDATED_ORDER
        defaultKlistShouldNotBeFound("order.greaterThanOrEqual=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllKlistsByOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        klistRepository.saveAndFlush(klist);

        // Get all the klistList where order is less than or equal to DEFAULT_ORDER
        defaultKlistShouldBeFound("order.lessThanOrEqual=" + DEFAULT_ORDER);

        // Get all the klistList where order is less than or equal to SMALLER_ORDER
        defaultKlistShouldNotBeFound("order.lessThanOrEqual=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    void getAllKlistsByOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        klistRepository.saveAndFlush(klist);

        // Get all the klistList where order is less than DEFAULT_ORDER
        defaultKlistShouldNotBeFound("order.lessThan=" + DEFAULT_ORDER);

        // Get all the klistList where order is less than UPDATED_ORDER
        defaultKlistShouldBeFound("order.lessThan=" + UPDATED_ORDER);
    }

    @Test
    @Transactional
    void getAllKlistsByOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        klistRepository.saveAndFlush(klist);

        // Get all the klistList where order is greater than DEFAULT_ORDER
        defaultKlistShouldNotBeFound("order.greaterThan=" + DEFAULT_ORDER);

        // Get all the klistList where order is greater than SMALLER_ORDER
        defaultKlistShouldBeFound("order.greaterThan=" + SMALLER_ORDER);
    }

    @Test
    @Transactional
    void getAllKlistsByKanbanlistIsEqualToSomething() throws Exception {
        // Initialize the database
        klistRepository.saveAndFlush(klist);
        Kanban kanbanlist = KanbanResourceIT.createEntity(em);
        em.persist(kanbanlist);
        em.flush();
        klist.setKanbanlist(kanbanlist);
        klistRepository.saveAndFlush(klist);
        Long kanbanlistId = kanbanlist.getId();

        // Get all the klistList where kanbanlist equals to kanbanlistId
        defaultKlistShouldBeFound("kanbanlistId.equals=" + kanbanlistId);

        // Get all the klistList where kanbanlist equals to (kanbanlistId + 1)
        defaultKlistShouldNotBeFound("kanbanlistId.equals=" + (kanbanlistId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultKlistShouldBeFound(String filter) throws Exception {
        restKlistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(klist.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)));

        // Check, that the count call also returns 1
        restKlistMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultKlistShouldNotBeFound(String filter) throws Exception {
        restKlistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restKlistMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingKlist() throws Exception {
        // Get the klist
        restKlistMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewKlist() throws Exception {
        // Initialize the database
        klistRepository.saveAndFlush(klist);

        int databaseSizeBeforeUpdate = klistRepository.findAll().size();

        // Update the klist
        Klist updatedKlist = klistRepository.findById(klist.getId()).get();
        // Disconnect from session so that the updates on updatedKlist are not directly saved in db
        em.detach(updatedKlist);
        updatedKlist.title(UPDATED_TITLE).order(UPDATED_ORDER);
        KlistDTO klistDTO = klistMapper.toDto(updatedKlist);

        restKlistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, klistDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(klistDTO))
            )
            .andExpect(status().isOk());

        // Validate the Klist in the database
        List<Klist> klistList = klistRepository.findAll();
        assertThat(klistList).hasSize(databaseSizeBeforeUpdate);
        Klist testKlist = klistList.get(klistList.size() - 1);
        assertThat(testKlist.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testKlist.getOrder()).isEqualTo(UPDATED_ORDER);
    }

    @Test
    @Transactional
    void putNonExistingKlist() throws Exception {
        int databaseSizeBeforeUpdate = klistRepository.findAll().size();
        klist.setId(count.incrementAndGet());

        // Create the Klist
        KlistDTO klistDTO = klistMapper.toDto(klist);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKlistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, klistDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(klistDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Klist in the database
        List<Klist> klistList = klistRepository.findAll();
        assertThat(klistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchKlist() throws Exception {
        int databaseSizeBeforeUpdate = klistRepository.findAll().size();
        klist.setId(count.incrementAndGet());

        // Create the Klist
        KlistDTO klistDTO = klistMapper.toDto(klist);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKlistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(klistDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Klist in the database
        List<Klist> klistList = klistRepository.findAll();
        assertThat(klistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamKlist() throws Exception {
        int databaseSizeBeforeUpdate = klistRepository.findAll().size();
        klist.setId(count.incrementAndGet());

        // Create the Klist
        KlistDTO klistDTO = klistMapper.toDto(klist);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKlistMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(klistDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Klist in the database
        List<Klist> klistList = klistRepository.findAll();
        assertThat(klistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateKlistWithPatch() throws Exception {
        // Initialize the database
        klistRepository.saveAndFlush(klist);

        int databaseSizeBeforeUpdate = klistRepository.findAll().size();

        // Update the klist using partial update
        Klist partialUpdatedKlist = new Klist();
        partialUpdatedKlist.setId(klist.getId());

        partialUpdatedKlist.title(UPDATED_TITLE).order(UPDATED_ORDER);

        restKlistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKlist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKlist))
            )
            .andExpect(status().isOk());

        // Validate the Klist in the database
        List<Klist> klistList = klistRepository.findAll();
        assertThat(klistList).hasSize(databaseSizeBeforeUpdate);
        Klist testKlist = klistList.get(klistList.size() - 1);
        assertThat(testKlist.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testKlist.getOrder()).isEqualTo(UPDATED_ORDER);
    }

    @Test
    @Transactional
    void fullUpdateKlistWithPatch() throws Exception {
        // Initialize the database
        klistRepository.saveAndFlush(klist);

        int databaseSizeBeforeUpdate = klistRepository.findAll().size();

        // Update the klist using partial update
        Klist partialUpdatedKlist = new Klist();
        partialUpdatedKlist.setId(klist.getId());

        partialUpdatedKlist.title(UPDATED_TITLE).order(UPDATED_ORDER);

        restKlistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKlist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKlist))
            )
            .andExpect(status().isOk());

        // Validate the Klist in the database
        List<Klist> klistList = klistRepository.findAll();
        assertThat(klistList).hasSize(databaseSizeBeforeUpdate);
        Klist testKlist = klistList.get(klistList.size() - 1);
        assertThat(testKlist.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testKlist.getOrder()).isEqualTo(UPDATED_ORDER);
    }

    @Test
    @Transactional
    void patchNonExistingKlist() throws Exception {
        int databaseSizeBeforeUpdate = klistRepository.findAll().size();
        klist.setId(count.incrementAndGet());

        // Create the Klist
        KlistDTO klistDTO = klistMapper.toDto(klist);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKlistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, klistDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(klistDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Klist in the database
        List<Klist> klistList = klistRepository.findAll();
        assertThat(klistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchKlist() throws Exception {
        int databaseSizeBeforeUpdate = klistRepository.findAll().size();
        klist.setId(count.incrementAndGet());

        // Create the Klist
        KlistDTO klistDTO = klistMapper.toDto(klist);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKlistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(klistDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Klist in the database
        List<Klist> klistList = klistRepository.findAll();
        assertThat(klistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamKlist() throws Exception {
        int databaseSizeBeforeUpdate = klistRepository.findAll().size();
        klist.setId(count.incrementAndGet());

        // Create the Klist
        KlistDTO klistDTO = klistMapper.toDto(klist);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKlistMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(klistDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Klist in the database
        List<Klist> klistList = klistRepository.findAll();
        assertThat(klistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteKlist() throws Exception {
        // Initialize the database
        klistRepository.saveAndFlush(klist);

        int databaseSizeBeforeDelete = klistRepository.findAll().size();

        // Delete the klist
        restKlistMockMvc
            .perform(delete(ENTITY_API_URL_ID, klist.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Klist> klistList = klistRepository.findAll();
        assertThat(klistList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
