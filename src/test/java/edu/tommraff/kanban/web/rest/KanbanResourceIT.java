package edu.tommraff.kanban.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import edu.tommraff.kanban.IntegrationTest;
import edu.tommraff.kanban.domain.Kanban;
import edu.tommraff.kanban.domain.User;
import edu.tommraff.kanban.repository.KanbanRepository;
import edu.tommraff.kanban.service.criteria.KanbanCriteria;
import edu.tommraff.kanban.service.dto.KanbanDTO;
import edu.tommraff.kanban.service.mapper.KanbanMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link KanbanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class KanbanResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_AT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_LAST_EDIT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_EDIT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_LAST_EDIT = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/kanbans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private KanbanRepository kanbanRepository;

    @Autowired
    private KanbanMapper kanbanMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKanbanMockMvc;

    private Kanban kanban;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Kanban createEntity(EntityManager em) {
        Kanban kanban = new Kanban().name(DEFAULT_NAME).created_at(DEFAULT_CREATED_AT).last_edit(DEFAULT_LAST_EDIT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        kanban.setUserkanban(user);
        return kanban;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Kanban createUpdatedEntity(EntityManager em) {
        Kanban kanban = new Kanban().name(UPDATED_NAME).created_at(UPDATED_CREATED_AT).last_edit(UPDATED_LAST_EDIT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        kanban.setUserkanban(user);
        return kanban;
    }

    @BeforeEach
    public void initTest() {
        kanban = createEntity(em);
    }

    @Test
    @Transactional
    void createKanban() throws Exception {
        int databaseSizeBeforeCreate = kanbanRepository.findAll().size();
        // Create the Kanban
        KanbanDTO kanbanDTO = kanbanMapper.toDto(kanban);
        restKanbanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(kanbanDTO)))
            .andExpect(status().isCreated());

        // Validate the Kanban in the database
        List<Kanban> kanbanList = kanbanRepository.findAll();
        assertThat(kanbanList).hasSize(databaseSizeBeforeCreate + 1);
        Kanban testKanban = kanbanList.get(kanbanList.size() - 1);
        assertThat(testKanban.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testKanban.getCreated_at()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testKanban.getLast_edit()).isEqualTo(DEFAULT_LAST_EDIT);
    }

    @Test
    @Transactional
    void createKanbanWithExistingId() throws Exception {
        // Create the Kanban with an existing ID
        kanban.setId(1L);
        KanbanDTO kanbanDTO = kanbanMapper.toDto(kanban);

        int databaseSizeBeforeCreate = kanbanRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restKanbanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(kanbanDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Kanban in the database
        List<Kanban> kanbanList = kanbanRepository.findAll();
        assertThat(kanbanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = kanbanRepository.findAll().size();
        // set the field null
        kanban.setName(null);

        // Create the Kanban, which fails.
        KanbanDTO kanbanDTO = kanbanMapper.toDto(kanban);

        restKanbanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(kanbanDTO)))
            .andExpect(status().isBadRequest());

        List<Kanban> kanbanList = kanbanRepository.findAll();
        assertThat(kanbanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllKanbans() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        // Get all the kanbanList
        restKanbanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kanban.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].created_at").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].last_edit").value(hasItem(DEFAULT_LAST_EDIT.toString())));
    }

    @Test
    @Transactional
    void getKanban() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        // Get the kanban
        restKanbanMockMvc
            .perform(get(ENTITY_API_URL_ID, kanban.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(kanban.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.created_at").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.last_edit").value(DEFAULT_LAST_EDIT.toString()));
    }

    @Test
    @Transactional
    void getKanbansByIdFiltering() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        Long id = kanban.getId();

        defaultKanbanShouldBeFound("id.equals=" + id);
        defaultKanbanShouldNotBeFound("id.notEquals=" + id);

        defaultKanbanShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultKanbanShouldNotBeFound("id.greaterThan=" + id);

        defaultKanbanShouldBeFound("id.lessThanOrEqual=" + id);
        defaultKanbanShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllKanbansByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        // Get all the kanbanList where name equals to DEFAULT_NAME
        defaultKanbanShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the kanbanList where name equals to UPDATED_NAME
        defaultKanbanShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllKanbansByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        // Get all the kanbanList where name not equals to DEFAULT_NAME
        defaultKanbanShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the kanbanList where name not equals to UPDATED_NAME
        defaultKanbanShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllKanbansByNameIsInShouldWork() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        // Get all the kanbanList where name in DEFAULT_NAME or UPDATED_NAME
        defaultKanbanShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the kanbanList where name equals to UPDATED_NAME
        defaultKanbanShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllKanbansByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        // Get all the kanbanList where name is not null
        defaultKanbanShouldBeFound("name.specified=true");

        // Get all the kanbanList where name is null
        defaultKanbanShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllKanbansByNameContainsSomething() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        // Get all the kanbanList where name contains DEFAULT_NAME
        defaultKanbanShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the kanbanList where name contains UPDATED_NAME
        defaultKanbanShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllKanbansByNameNotContainsSomething() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        // Get all the kanbanList where name does not contain DEFAULT_NAME
        defaultKanbanShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the kanbanList where name does not contain UPDATED_NAME
        defaultKanbanShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllKanbansByCreated_atIsEqualToSomething() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        // Get all the kanbanList where created_at equals to DEFAULT_CREATED_AT
        defaultKanbanShouldBeFound("created_at.equals=" + DEFAULT_CREATED_AT);

        // Get all the kanbanList where created_at equals to UPDATED_CREATED_AT
        defaultKanbanShouldNotBeFound("created_at.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllKanbansByCreated_atIsNotEqualToSomething() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        // Get all the kanbanList where created_at not equals to DEFAULT_CREATED_AT
        defaultKanbanShouldNotBeFound("created_at.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the kanbanList where created_at not equals to UPDATED_CREATED_AT
        defaultKanbanShouldBeFound("created_at.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllKanbansByCreated_atIsInShouldWork() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        // Get all the kanbanList where created_at in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultKanbanShouldBeFound("created_at.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the kanbanList where created_at equals to UPDATED_CREATED_AT
        defaultKanbanShouldNotBeFound("created_at.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllKanbansByCreated_atIsNullOrNotNull() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        // Get all the kanbanList where created_at is not null
        defaultKanbanShouldBeFound("created_at.specified=true");

        // Get all the kanbanList where created_at is null
        defaultKanbanShouldNotBeFound("created_at.specified=false");
    }

    @Test
    @Transactional
    void getAllKanbansByCreated_atIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        // Get all the kanbanList where created_at is greater than or equal to DEFAULT_CREATED_AT
        defaultKanbanShouldBeFound("created_at.greaterThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the kanbanList where created_at is greater than or equal to UPDATED_CREATED_AT
        defaultKanbanShouldNotBeFound("created_at.greaterThanOrEqual=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllKanbansByCreated_atIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        // Get all the kanbanList where created_at is less than or equal to DEFAULT_CREATED_AT
        defaultKanbanShouldBeFound("created_at.lessThanOrEqual=" + DEFAULT_CREATED_AT);

        // Get all the kanbanList where created_at is less than or equal to SMALLER_CREATED_AT
        defaultKanbanShouldNotBeFound("created_at.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllKanbansByCreated_atIsLessThanSomething() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        // Get all the kanbanList where created_at is less than DEFAULT_CREATED_AT
        defaultKanbanShouldNotBeFound("created_at.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the kanbanList where created_at is less than UPDATED_CREATED_AT
        defaultKanbanShouldBeFound("created_at.lessThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllKanbansByCreated_atIsGreaterThanSomething() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        // Get all the kanbanList where created_at is greater than DEFAULT_CREATED_AT
        defaultKanbanShouldNotBeFound("created_at.greaterThan=" + DEFAULT_CREATED_AT);

        // Get all the kanbanList where created_at is greater than SMALLER_CREATED_AT
        defaultKanbanShouldBeFound("created_at.greaterThan=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllKanbansByLast_editIsEqualToSomething() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        // Get all the kanbanList where last_edit equals to DEFAULT_LAST_EDIT
        defaultKanbanShouldBeFound("last_edit.equals=" + DEFAULT_LAST_EDIT);

        // Get all the kanbanList where last_edit equals to UPDATED_LAST_EDIT
        defaultKanbanShouldNotBeFound("last_edit.equals=" + UPDATED_LAST_EDIT);
    }

    @Test
    @Transactional
    void getAllKanbansByLast_editIsNotEqualToSomething() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        // Get all the kanbanList where last_edit not equals to DEFAULT_LAST_EDIT
        defaultKanbanShouldNotBeFound("last_edit.notEquals=" + DEFAULT_LAST_EDIT);

        // Get all the kanbanList where last_edit not equals to UPDATED_LAST_EDIT
        defaultKanbanShouldBeFound("last_edit.notEquals=" + UPDATED_LAST_EDIT);
    }

    @Test
    @Transactional
    void getAllKanbansByLast_editIsInShouldWork() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        // Get all the kanbanList where last_edit in DEFAULT_LAST_EDIT or UPDATED_LAST_EDIT
        defaultKanbanShouldBeFound("last_edit.in=" + DEFAULT_LAST_EDIT + "," + UPDATED_LAST_EDIT);

        // Get all the kanbanList where last_edit equals to UPDATED_LAST_EDIT
        defaultKanbanShouldNotBeFound("last_edit.in=" + UPDATED_LAST_EDIT);
    }

    @Test
    @Transactional
    void getAllKanbansByLast_editIsNullOrNotNull() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        // Get all the kanbanList where last_edit is not null
        defaultKanbanShouldBeFound("last_edit.specified=true");

        // Get all the kanbanList where last_edit is null
        defaultKanbanShouldNotBeFound("last_edit.specified=false");
    }

    @Test
    @Transactional
    void getAllKanbansByLast_editIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        // Get all the kanbanList where last_edit is greater than or equal to DEFAULT_LAST_EDIT
        defaultKanbanShouldBeFound("last_edit.greaterThanOrEqual=" + DEFAULT_LAST_EDIT);

        // Get all the kanbanList where last_edit is greater than or equal to UPDATED_LAST_EDIT
        defaultKanbanShouldNotBeFound("last_edit.greaterThanOrEqual=" + UPDATED_LAST_EDIT);
    }

    @Test
    @Transactional
    void getAllKanbansByLast_editIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        // Get all the kanbanList where last_edit is less than or equal to DEFAULT_LAST_EDIT
        defaultKanbanShouldBeFound("last_edit.lessThanOrEqual=" + DEFAULT_LAST_EDIT);

        // Get all the kanbanList where last_edit is less than or equal to SMALLER_LAST_EDIT
        defaultKanbanShouldNotBeFound("last_edit.lessThanOrEqual=" + SMALLER_LAST_EDIT);
    }

    @Test
    @Transactional
    void getAllKanbansByLast_editIsLessThanSomething() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        // Get all the kanbanList where last_edit is less than DEFAULT_LAST_EDIT
        defaultKanbanShouldNotBeFound("last_edit.lessThan=" + DEFAULT_LAST_EDIT);

        // Get all the kanbanList where last_edit is less than UPDATED_LAST_EDIT
        defaultKanbanShouldBeFound("last_edit.lessThan=" + UPDATED_LAST_EDIT);
    }

    @Test
    @Transactional
    void getAllKanbansByLast_editIsGreaterThanSomething() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        // Get all the kanbanList where last_edit is greater than DEFAULT_LAST_EDIT
        defaultKanbanShouldNotBeFound("last_edit.greaterThan=" + DEFAULT_LAST_EDIT);

        // Get all the kanbanList where last_edit is greater than SMALLER_LAST_EDIT
        defaultKanbanShouldBeFound("last_edit.greaterThan=" + SMALLER_LAST_EDIT);
    }

    @Test
    @Transactional
    void getAllKanbansByUserkanbanIsEqualToSomething() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);
        User userkanban = UserResourceIT.createEntity(em);
        em.persist(userkanban);
        em.flush();
        kanban.setUserkanban(userkanban);
        kanbanRepository.saveAndFlush(kanban);
        Long userkanbanId = userkanban.getId();

        // Get all the kanbanList where userkanban equals to userkanbanId
        defaultKanbanShouldBeFound("userkanbanId.equals=" + userkanbanId);

        // Get all the kanbanList where userkanban equals to (userkanbanId + 1)
        defaultKanbanShouldNotBeFound("userkanbanId.equals=" + (userkanbanId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultKanbanShouldBeFound(String filter) throws Exception {
        restKanbanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kanban.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].created_at").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].last_edit").value(hasItem(DEFAULT_LAST_EDIT.toString())));

        // Check, that the count call also returns 1
        restKanbanMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultKanbanShouldNotBeFound(String filter) throws Exception {
        restKanbanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restKanbanMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingKanban() throws Exception {
        // Get the kanban
        restKanbanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewKanban() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        int databaseSizeBeforeUpdate = kanbanRepository.findAll().size();

        // Update the kanban
        Kanban updatedKanban = kanbanRepository.findById(kanban.getId()).get();
        // Disconnect from session so that the updates on updatedKanban are not directly saved in db
        em.detach(updatedKanban);
        updatedKanban.name(UPDATED_NAME).created_at(UPDATED_CREATED_AT).last_edit(UPDATED_LAST_EDIT);
        KanbanDTO kanbanDTO = kanbanMapper.toDto(updatedKanban);

        restKanbanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, kanbanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(kanbanDTO))
            )
            .andExpect(status().isOk());

        // Validate the Kanban in the database
        List<Kanban> kanbanList = kanbanRepository.findAll();
        assertThat(kanbanList).hasSize(databaseSizeBeforeUpdate);
        Kanban testKanban = kanbanList.get(kanbanList.size() - 1);
        assertThat(testKanban.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testKanban.getCreated_at()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testKanban.getLast_edit()).isEqualTo(UPDATED_LAST_EDIT);
    }

    @Test
    @Transactional
    void putNonExistingKanban() throws Exception {
        int databaseSizeBeforeUpdate = kanbanRepository.findAll().size();
        kanban.setId(count.incrementAndGet());

        // Create the Kanban
        KanbanDTO kanbanDTO = kanbanMapper.toDto(kanban);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKanbanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, kanbanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(kanbanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Kanban in the database
        List<Kanban> kanbanList = kanbanRepository.findAll();
        assertThat(kanbanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchKanban() throws Exception {
        int databaseSizeBeforeUpdate = kanbanRepository.findAll().size();
        kanban.setId(count.incrementAndGet());

        // Create the Kanban
        KanbanDTO kanbanDTO = kanbanMapper.toDto(kanban);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKanbanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(kanbanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Kanban in the database
        List<Kanban> kanbanList = kanbanRepository.findAll();
        assertThat(kanbanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamKanban() throws Exception {
        int databaseSizeBeforeUpdate = kanbanRepository.findAll().size();
        kanban.setId(count.incrementAndGet());

        // Create the Kanban
        KanbanDTO kanbanDTO = kanbanMapper.toDto(kanban);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKanbanMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(kanbanDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Kanban in the database
        List<Kanban> kanbanList = kanbanRepository.findAll();
        assertThat(kanbanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateKanbanWithPatch() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        int databaseSizeBeforeUpdate = kanbanRepository.findAll().size();

        // Update the kanban using partial update
        Kanban partialUpdatedKanban = new Kanban();
        partialUpdatedKanban.setId(kanban.getId());

        partialUpdatedKanban.name(UPDATED_NAME).last_edit(UPDATED_LAST_EDIT);

        restKanbanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKanban.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKanban))
            )
            .andExpect(status().isOk());

        // Validate the Kanban in the database
        List<Kanban> kanbanList = kanbanRepository.findAll();
        assertThat(kanbanList).hasSize(databaseSizeBeforeUpdate);
        Kanban testKanban = kanbanList.get(kanbanList.size() - 1);
        assertThat(testKanban.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testKanban.getCreated_at()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testKanban.getLast_edit()).isEqualTo(UPDATED_LAST_EDIT);
    }

    @Test
    @Transactional
    void fullUpdateKanbanWithPatch() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        int databaseSizeBeforeUpdate = kanbanRepository.findAll().size();

        // Update the kanban using partial update
        Kanban partialUpdatedKanban = new Kanban();
        partialUpdatedKanban.setId(kanban.getId());

        partialUpdatedKanban.name(UPDATED_NAME).created_at(UPDATED_CREATED_AT).last_edit(UPDATED_LAST_EDIT);

        restKanbanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKanban.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKanban))
            )
            .andExpect(status().isOk());

        // Validate the Kanban in the database
        List<Kanban> kanbanList = kanbanRepository.findAll();
        assertThat(kanbanList).hasSize(databaseSizeBeforeUpdate);
        Kanban testKanban = kanbanList.get(kanbanList.size() - 1);
        assertThat(testKanban.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testKanban.getCreated_at()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testKanban.getLast_edit()).isEqualTo(UPDATED_LAST_EDIT);
    }

    @Test
    @Transactional
    void patchNonExistingKanban() throws Exception {
        int databaseSizeBeforeUpdate = kanbanRepository.findAll().size();
        kanban.setId(count.incrementAndGet());

        // Create the Kanban
        KanbanDTO kanbanDTO = kanbanMapper.toDto(kanban);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKanbanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, kanbanDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(kanbanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Kanban in the database
        List<Kanban> kanbanList = kanbanRepository.findAll();
        assertThat(kanbanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchKanban() throws Exception {
        int databaseSizeBeforeUpdate = kanbanRepository.findAll().size();
        kanban.setId(count.incrementAndGet());

        // Create the Kanban
        KanbanDTO kanbanDTO = kanbanMapper.toDto(kanban);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKanbanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(kanbanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Kanban in the database
        List<Kanban> kanbanList = kanbanRepository.findAll();
        assertThat(kanbanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamKanban() throws Exception {
        int databaseSizeBeforeUpdate = kanbanRepository.findAll().size();
        kanban.setId(count.incrementAndGet());

        // Create the Kanban
        KanbanDTO kanbanDTO = kanbanMapper.toDto(kanban);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKanbanMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(kanbanDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Kanban in the database
        List<Kanban> kanbanList = kanbanRepository.findAll();
        assertThat(kanbanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteKanban() throws Exception {
        // Initialize the database
        kanbanRepository.saveAndFlush(kanban);

        int databaseSizeBeforeDelete = kanbanRepository.findAll().size();

        // Delete the kanban
        restKanbanMockMvc
            .perform(delete(ENTITY_API_URL_ID, kanban.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Kanban> kanbanList = kanbanRepository.findAll();
        assertThat(kanbanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
