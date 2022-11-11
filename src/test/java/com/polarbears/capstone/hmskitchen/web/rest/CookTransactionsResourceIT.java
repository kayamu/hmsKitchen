package com.polarbears.capstone.hmskitchen.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.polarbears.capstone.hmskitchen.IntegrationTest;
import com.polarbears.capstone.hmskitchen.domain.CookOrders;
import com.polarbears.capstone.hmskitchen.domain.CookTransactions;
import com.polarbears.capstone.hmskitchen.domain.enumeration.KITCHENTYPES;
import com.polarbears.capstone.hmskitchen.repository.CookTransactionsRepository;
import com.polarbears.capstone.hmskitchen.service.criteria.CookTransactionsCriteria;
import com.polarbears.capstone.hmskitchen.service.dto.CookTransactionsDTO;
import com.polarbears.capstone.hmskitchen.service.mapper.CookTransactionsMapper;
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
 * Integration tests for the {@link CookTransactionsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CookTransactionsResourceIT {

    private static final Long DEFAULT_KITCHEN_ID = 1L;
    private static final Long UPDATED_KITCHEN_ID = 2L;
    private static final Long SMALLER_KITCHEN_ID = 1L - 1L;

    private static final LocalDate DEFAULT_STATUS_CHANGED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STATUS_CHANGED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_STATUS_CHANGED_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSACTION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_TRANSACTION_DATE = LocalDate.ofEpochDay(-1L);

    private static final KITCHENTYPES DEFAULT_TYPE = KITCHENTYPES.PREPARING;
    private static final KITCHENTYPES UPDATED_TYPE = KITCHENTYPES.ONTHEWAY;

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/cook-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CookTransactionsRepository cookTransactionsRepository;

    @Autowired
    private CookTransactionsMapper cookTransactionsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCookTransactionsMockMvc;

    private CookTransactions cookTransactions;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CookTransactions createEntity(EntityManager em) {
        CookTransactions cookTransactions = new CookTransactions()
            .kitchenId(DEFAULT_KITCHEN_ID)
            .statusChangedDate(DEFAULT_STATUS_CHANGED_DATE)
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .type(DEFAULT_TYPE)
            .createdDate(DEFAULT_CREATED_DATE);
        return cookTransactions;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CookTransactions createUpdatedEntity(EntityManager em) {
        CookTransactions cookTransactions = new CookTransactions()
            .kitchenId(UPDATED_KITCHEN_ID)
            .statusChangedDate(UPDATED_STATUS_CHANGED_DATE)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .type(UPDATED_TYPE)
            .createdDate(UPDATED_CREATED_DATE);
        return cookTransactions;
    }

    @BeforeEach
    public void initTest() {
        cookTransactions = createEntity(em);
    }

    @Test
    @Transactional
    void createCookTransactions() throws Exception {
        int databaseSizeBeforeCreate = cookTransactionsRepository.findAll().size();
        // Create the CookTransactions
        CookTransactionsDTO cookTransactionsDTO = cookTransactionsMapper.toDto(cookTransactions);
        restCookTransactionsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cookTransactionsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CookTransactions in the database
        List<CookTransactions> cookTransactionsList = cookTransactionsRepository.findAll();
        assertThat(cookTransactionsList).hasSize(databaseSizeBeforeCreate + 1);
        CookTransactions testCookTransactions = cookTransactionsList.get(cookTransactionsList.size() - 1);
        assertThat(testCookTransactions.getKitchenId()).isEqualTo(DEFAULT_KITCHEN_ID);
        assertThat(testCookTransactions.getStatusChangedDate()).isEqualTo(DEFAULT_STATUS_CHANGED_DATE);
        assertThat(testCookTransactions.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testCookTransactions.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCookTransactions.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void createCookTransactionsWithExistingId() throws Exception {
        // Create the CookTransactions with an existing ID
        cookTransactions.setId(1L);
        CookTransactionsDTO cookTransactionsDTO = cookTransactionsMapper.toDto(cookTransactions);

        int databaseSizeBeforeCreate = cookTransactionsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCookTransactionsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cookTransactionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CookTransactions in the database
        List<CookTransactions> cookTransactionsList = cookTransactionsRepository.findAll();
        assertThat(cookTransactionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCookTransactions() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList
        restCookTransactionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cookTransactions.getId().intValue())))
            .andExpect(jsonPath("$.[*].kitchenId").value(hasItem(DEFAULT_KITCHEN_ID.intValue())))
            .andExpect(jsonPath("$.[*].statusChangedDate").value(hasItem(DEFAULT_STATUS_CHANGED_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getCookTransactions() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get the cookTransactions
        restCookTransactionsMockMvc
            .perform(get(ENTITY_API_URL_ID, cookTransactions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cookTransactions.getId().intValue()))
            .andExpect(jsonPath("$.kitchenId").value(DEFAULT_KITCHEN_ID.intValue()))
            .andExpect(jsonPath("$.statusChangedDate").value(DEFAULT_STATUS_CHANGED_DATE.toString()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getCookTransactionsByIdFiltering() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        Long id = cookTransactions.getId();

        defaultCookTransactionsShouldBeFound("id.equals=" + id);
        defaultCookTransactionsShouldNotBeFound("id.notEquals=" + id);

        defaultCookTransactionsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCookTransactionsShouldNotBeFound("id.greaterThan=" + id);

        defaultCookTransactionsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCookTransactionsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByKitchenIdIsEqualToSomething() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where kitchenId equals to DEFAULT_KITCHEN_ID
        defaultCookTransactionsShouldBeFound("kitchenId.equals=" + DEFAULT_KITCHEN_ID);

        // Get all the cookTransactionsList where kitchenId equals to UPDATED_KITCHEN_ID
        defaultCookTransactionsShouldNotBeFound("kitchenId.equals=" + UPDATED_KITCHEN_ID);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByKitchenIdIsInShouldWork() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where kitchenId in DEFAULT_KITCHEN_ID or UPDATED_KITCHEN_ID
        defaultCookTransactionsShouldBeFound("kitchenId.in=" + DEFAULT_KITCHEN_ID + "," + UPDATED_KITCHEN_ID);

        // Get all the cookTransactionsList where kitchenId equals to UPDATED_KITCHEN_ID
        defaultCookTransactionsShouldNotBeFound("kitchenId.in=" + UPDATED_KITCHEN_ID);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByKitchenIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where kitchenId is not null
        defaultCookTransactionsShouldBeFound("kitchenId.specified=true");

        // Get all the cookTransactionsList where kitchenId is null
        defaultCookTransactionsShouldNotBeFound("kitchenId.specified=false");
    }

    @Test
    @Transactional
    void getAllCookTransactionsByKitchenIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where kitchenId is greater than or equal to DEFAULT_KITCHEN_ID
        defaultCookTransactionsShouldBeFound("kitchenId.greaterThanOrEqual=" + DEFAULT_KITCHEN_ID);

        // Get all the cookTransactionsList where kitchenId is greater than or equal to UPDATED_KITCHEN_ID
        defaultCookTransactionsShouldNotBeFound("kitchenId.greaterThanOrEqual=" + UPDATED_KITCHEN_ID);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByKitchenIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where kitchenId is less than or equal to DEFAULT_KITCHEN_ID
        defaultCookTransactionsShouldBeFound("kitchenId.lessThanOrEqual=" + DEFAULT_KITCHEN_ID);

        // Get all the cookTransactionsList where kitchenId is less than or equal to SMALLER_KITCHEN_ID
        defaultCookTransactionsShouldNotBeFound("kitchenId.lessThanOrEqual=" + SMALLER_KITCHEN_ID);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByKitchenIdIsLessThanSomething() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where kitchenId is less than DEFAULT_KITCHEN_ID
        defaultCookTransactionsShouldNotBeFound("kitchenId.lessThan=" + DEFAULT_KITCHEN_ID);

        // Get all the cookTransactionsList where kitchenId is less than UPDATED_KITCHEN_ID
        defaultCookTransactionsShouldBeFound("kitchenId.lessThan=" + UPDATED_KITCHEN_ID);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByKitchenIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where kitchenId is greater than DEFAULT_KITCHEN_ID
        defaultCookTransactionsShouldNotBeFound("kitchenId.greaterThan=" + DEFAULT_KITCHEN_ID);

        // Get all the cookTransactionsList where kitchenId is greater than SMALLER_KITCHEN_ID
        defaultCookTransactionsShouldBeFound("kitchenId.greaterThan=" + SMALLER_KITCHEN_ID);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByStatusChangedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where statusChangedDate equals to DEFAULT_STATUS_CHANGED_DATE
        defaultCookTransactionsShouldBeFound("statusChangedDate.equals=" + DEFAULT_STATUS_CHANGED_DATE);

        // Get all the cookTransactionsList where statusChangedDate equals to UPDATED_STATUS_CHANGED_DATE
        defaultCookTransactionsShouldNotBeFound("statusChangedDate.equals=" + UPDATED_STATUS_CHANGED_DATE);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByStatusChangedDateIsInShouldWork() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where statusChangedDate in DEFAULT_STATUS_CHANGED_DATE or UPDATED_STATUS_CHANGED_DATE
        defaultCookTransactionsShouldBeFound("statusChangedDate.in=" + DEFAULT_STATUS_CHANGED_DATE + "," + UPDATED_STATUS_CHANGED_DATE);

        // Get all the cookTransactionsList where statusChangedDate equals to UPDATED_STATUS_CHANGED_DATE
        defaultCookTransactionsShouldNotBeFound("statusChangedDate.in=" + UPDATED_STATUS_CHANGED_DATE);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByStatusChangedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where statusChangedDate is not null
        defaultCookTransactionsShouldBeFound("statusChangedDate.specified=true");

        // Get all the cookTransactionsList where statusChangedDate is null
        defaultCookTransactionsShouldNotBeFound("statusChangedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCookTransactionsByStatusChangedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where statusChangedDate is greater than or equal to DEFAULT_STATUS_CHANGED_DATE
        defaultCookTransactionsShouldBeFound("statusChangedDate.greaterThanOrEqual=" + DEFAULT_STATUS_CHANGED_DATE);

        // Get all the cookTransactionsList where statusChangedDate is greater than or equal to UPDATED_STATUS_CHANGED_DATE
        defaultCookTransactionsShouldNotBeFound("statusChangedDate.greaterThanOrEqual=" + UPDATED_STATUS_CHANGED_DATE);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByStatusChangedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where statusChangedDate is less than or equal to DEFAULT_STATUS_CHANGED_DATE
        defaultCookTransactionsShouldBeFound("statusChangedDate.lessThanOrEqual=" + DEFAULT_STATUS_CHANGED_DATE);

        // Get all the cookTransactionsList where statusChangedDate is less than or equal to SMALLER_STATUS_CHANGED_DATE
        defaultCookTransactionsShouldNotBeFound("statusChangedDate.lessThanOrEqual=" + SMALLER_STATUS_CHANGED_DATE);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByStatusChangedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where statusChangedDate is less than DEFAULT_STATUS_CHANGED_DATE
        defaultCookTransactionsShouldNotBeFound("statusChangedDate.lessThan=" + DEFAULT_STATUS_CHANGED_DATE);

        // Get all the cookTransactionsList where statusChangedDate is less than UPDATED_STATUS_CHANGED_DATE
        defaultCookTransactionsShouldBeFound("statusChangedDate.lessThan=" + UPDATED_STATUS_CHANGED_DATE);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByStatusChangedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where statusChangedDate is greater than DEFAULT_STATUS_CHANGED_DATE
        defaultCookTransactionsShouldNotBeFound("statusChangedDate.greaterThan=" + DEFAULT_STATUS_CHANGED_DATE);

        // Get all the cookTransactionsList where statusChangedDate is greater than SMALLER_STATUS_CHANGED_DATE
        defaultCookTransactionsShouldBeFound("statusChangedDate.greaterThan=" + SMALLER_STATUS_CHANGED_DATE);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByTransactionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where transactionDate equals to DEFAULT_TRANSACTION_DATE
        defaultCookTransactionsShouldBeFound("transactionDate.equals=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cookTransactionsList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultCookTransactionsShouldNotBeFound("transactionDate.equals=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByTransactionDateIsInShouldWork() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where transactionDate in DEFAULT_TRANSACTION_DATE or UPDATED_TRANSACTION_DATE
        defaultCookTransactionsShouldBeFound("transactionDate.in=" + DEFAULT_TRANSACTION_DATE + "," + UPDATED_TRANSACTION_DATE);

        // Get all the cookTransactionsList where transactionDate equals to UPDATED_TRANSACTION_DATE
        defaultCookTransactionsShouldNotBeFound("transactionDate.in=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByTransactionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where transactionDate is not null
        defaultCookTransactionsShouldBeFound("transactionDate.specified=true");

        // Get all the cookTransactionsList where transactionDate is null
        defaultCookTransactionsShouldNotBeFound("transactionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCookTransactionsByTransactionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where transactionDate is greater than or equal to DEFAULT_TRANSACTION_DATE
        defaultCookTransactionsShouldBeFound("transactionDate.greaterThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cookTransactionsList where transactionDate is greater than or equal to UPDATED_TRANSACTION_DATE
        defaultCookTransactionsShouldNotBeFound("transactionDate.greaterThanOrEqual=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByTransactionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where transactionDate is less than or equal to DEFAULT_TRANSACTION_DATE
        defaultCookTransactionsShouldBeFound("transactionDate.lessThanOrEqual=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cookTransactionsList where transactionDate is less than or equal to SMALLER_TRANSACTION_DATE
        defaultCookTransactionsShouldNotBeFound("transactionDate.lessThanOrEqual=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByTransactionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where transactionDate is less than DEFAULT_TRANSACTION_DATE
        defaultCookTransactionsShouldNotBeFound("transactionDate.lessThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cookTransactionsList where transactionDate is less than UPDATED_TRANSACTION_DATE
        defaultCookTransactionsShouldBeFound("transactionDate.lessThan=" + UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByTransactionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where transactionDate is greater than DEFAULT_TRANSACTION_DATE
        defaultCookTransactionsShouldNotBeFound("transactionDate.greaterThan=" + DEFAULT_TRANSACTION_DATE);

        // Get all the cookTransactionsList where transactionDate is greater than SMALLER_TRANSACTION_DATE
        defaultCookTransactionsShouldBeFound("transactionDate.greaterThan=" + SMALLER_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where type equals to DEFAULT_TYPE
        defaultCookTransactionsShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the cookTransactionsList where type equals to UPDATED_TYPE
        defaultCookTransactionsShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultCookTransactionsShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the cookTransactionsList where type equals to UPDATED_TYPE
        defaultCookTransactionsShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where type is not null
        defaultCookTransactionsShouldBeFound("type.specified=true");

        // Get all the cookTransactionsList where type is null
        defaultCookTransactionsShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllCookTransactionsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where createdDate equals to DEFAULT_CREATED_DATE
        defaultCookTransactionsShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the cookTransactionsList where createdDate equals to UPDATED_CREATED_DATE
        defaultCookTransactionsShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultCookTransactionsShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the cookTransactionsList where createdDate equals to UPDATED_CREATED_DATE
        defaultCookTransactionsShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where createdDate is not null
        defaultCookTransactionsShouldBeFound("createdDate.specified=true");

        // Get all the cookTransactionsList where createdDate is null
        defaultCookTransactionsShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCookTransactionsByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultCookTransactionsShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the cookTransactionsList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultCookTransactionsShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultCookTransactionsShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the cookTransactionsList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultCookTransactionsShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where createdDate is less than DEFAULT_CREATED_DATE
        defaultCookTransactionsShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the cookTransactionsList where createdDate is less than UPDATED_CREATED_DATE
        defaultCookTransactionsShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        // Get all the cookTransactionsList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultCookTransactionsShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the cookTransactionsList where createdDate is greater than SMALLER_CREATED_DATE
        defaultCookTransactionsShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCookTransactionsByCookOrdersIsEqualToSomething() throws Exception {
        CookOrders cookOrders;
        if (TestUtil.findAll(em, CookOrders.class).isEmpty()) {
            cookTransactionsRepository.saveAndFlush(cookTransactions);
            cookOrders = CookOrdersResourceIT.createEntity(em);
        } else {
            cookOrders = TestUtil.findAll(em, CookOrders.class).get(0);
        }
        em.persist(cookOrders);
        em.flush();
        cookTransactions.addCookOrders(cookOrders);
        cookTransactionsRepository.saveAndFlush(cookTransactions);
        Long cookOrdersId = cookOrders.getId();

        // Get all the cookTransactionsList where cookOrders equals to cookOrdersId
        defaultCookTransactionsShouldBeFound("cookOrdersId.equals=" + cookOrdersId);

        // Get all the cookTransactionsList where cookOrders equals to (cookOrdersId + 1)
        defaultCookTransactionsShouldNotBeFound("cookOrdersId.equals=" + (cookOrdersId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCookTransactionsShouldBeFound(String filter) throws Exception {
        restCookTransactionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cookTransactions.getId().intValue())))
            .andExpect(jsonPath("$.[*].kitchenId").value(hasItem(DEFAULT_KITCHEN_ID.intValue())))
            .andExpect(jsonPath("$.[*].statusChangedDate").value(hasItem(DEFAULT_STATUS_CHANGED_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restCookTransactionsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCookTransactionsShouldNotBeFound(String filter) throws Exception {
        restCookTransactionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCookTransactionsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCookTransactions() throws Exception {
        // Get the cookTransactions
        restCookTransactionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCookTransactions() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        int databaseSizeBeforeUpdate = cookTransactionsRepository.findAll().size();

        // Update the cookTransactions
        CookTransactions updatedCookTransactions = cookTransactionsRepository.findById(cookTransactions.getId()).get();
        // Disconnect from session so that the updates on updatedCookTransactions are not directly saved in db
        em.detach(updatedCookTransactions);
        updatedCookTransactions
            .kitchenId(UPDATED_KITCHEN_ID)
            .statusChangedDate(UPDATED_STATUS_CHANGED_DATE)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .type(UPDATED_TYPE)
            .createdDate(UPDATED_CREATED_DATE);
        CookTransactionsDTO cookTransactionsDTO = cookTransactionsMapper.toDto(updatedCookTransactions);

        restCookTransactionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cookTransactionsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cookTransactionsDTO))
            )
            .andExpect(status().isOk());

        // Validate the CookTransactions in the database
        List<CookTransactions> cookTransactionsList = cookTransactionsRepository.findAll();
        assertThat(cookTransactionsList).hasSize(databaseSizeBeforeUpdate);
        CookTransactions testCookTransactions = cookTransactionsList.get(cookTransactionsList.size() - 1);
        assertThat(testCookTransactions.getKitchenId()).isEqualTo(UPDATED_KITCHEN_ID);
        assertThat(testCookTransactions.getStatusChangedDate()).isEqualTo(UPDATED_STATUS_CHANGED_DATE);
        assertThat(testCookTransactions.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testCookTransactions.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCookTransactions.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingCookTransactions() throws Exception {
        int databaseSizeBeforeUpdate = cookTransactionsRepository.findAll().size();
        cookTransactions.setId(count.incrementAndGet());

        // Create the CookTransactions
        CookTransactionsDTO cookTransactionsDTO = cookTransactionsMapper.toDto(cookTransactions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCookTransactionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cookTransactionsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cookTransactionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CookTransactions in the database
        List<CookTransactions> cookTransactionsList = cookTransactionsRepository.findAll();
        assertThat(cookTransactionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCookTransactions() throws Exception {
        int databaseSizeBeforeUpdate = cookTransactionsRepository.findAll().size();
        cookTransactions.setId(count.incrementAndGet());

        // Create the CookTransactions
        CookTransactionsDTO cookTransactionsDTO = cookTransactionsMapper.toDto(cookTransactions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCookTransactionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cookTransactionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CookTransactions in the database
        List<CookTransactions> cookTransactionsList = cookTransactionsRepository.findAll();
        assertThat(cookTransactionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCookTransactions() throws Exception {
        int databaseSizeBeforeUpdate = cookTransactionsRepository.findAll().size();
        cookTransactions.setId(count.incrementAndGet());

        // Create the CookTransactions
        CookTransactionsDTO cookTransactionsDTO = cookTransactionsMapper.toDto(cookTransactions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCookTransactionsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cookTransactionsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CookTransactions in the database
        List<CookTransactions> cookTransactionsList = cookTransactionsRepository.findAll();
        assertThat(cookTransactionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCookTransactionsWithPatch() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        int databaseSizeBeforeUpdate = cookTransactionsRepository.findAll().size();

        // Update the cookTransactions using partial update
        CookTransactions partialUpdatedCookTransactions = new CookTransactions();
        partialUpdatedCookTransactions.setId(cookTransactions.getId());

        partialUpdatedCookTransactions
            .kitchenId(UPDATED_KITCHEN_ID)
            .statusChangedDate(UPDATED_STATUS_CHANGED_DATE)
            .type(UPDATED_TYPE)
            .createdDate(UPDATED_CREATED_DATE);

        restCookTransactionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCookTransactions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCookTransactions))
            )
            .andExpect(status().isOk());

        // Validate the CookTransactions in the database
        List<CookTransactions> cookTransactionsList = cookTransactionsRepository.findAll();
        assertThat(cookTransactionsList).hasSize(databaseSizeBeforeUpdate);
        CookTransactions testCookTransactions = cookTransactionsList.get(cookTransactionsList.size() - 1);
        assertThat(testCookTransactions.getKitchenId()).isEqualTo(UPDATED_KITCHEN_ID);
        assertThat(testCookTransactions.getStatusChangedDate()).isEqualTo(UPDATED_STATUS_CHANGED_DATE);
        assertThat(testCookTransactions.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testCookTransactions.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCookTransactions.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateCookTransactionsWithPatch() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        int databaseSizeBeforeUpdate = cookTransactionsRepository.findAll().size();

        // Update the cookTransactions using partial update
        CookTransactions partialUpdatedCookTransactions = new CookTransactions();
        partialUpdatedCookTransactions.setId(cookTransactions.getId());

        partialUpdatedCookTransactions
            .kitchenId(UPDATED_KITCHEN_ID)
            .statusChangedDate(UPDATED_STATUS_CHANGED_DATE)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .type(UPDATED_TYPE)
            .createdDate(UPDATED_CREATED_DATE);

        restCookTransactionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCookTransactions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCookTransactions))
            )
            .andExpect(status().isOk());

        // Validate the CookTransactions in the database
        List<CookTransactions> cookTransactionsList = cookTransactionsRepository.findAll();
        assertThat(cookTransactionsList).hasSize(databaseSizeBeforeUpdate);
        CookTransactions testCookTransactions = cookTransactionsList.get(cookTransactionsList.size() - 1);
        assertThat(testCookTransactions.getKitchenId()).isEqualTo(UPDATED_KITCHEN_ID);
        assertThat(testCookTransactions.getStatusChangedDate()).isEqualTo(UPDATED_STATUS_CHANGED_DATE);
        assertThat(testCookTransactions.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testCookTransactions.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCookTransactions.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingCookTransactions() throws Exception {
        int databaseSizeBeforeUpdate = cookTransactionsRepository.findAll().size();
        cookTransactions.setId(count.incrementAndGet());

        // Create the CookTransactions
        CookTransactionsDTO cookTransactionsDTO = cookTransactionsMapper.toDto(cookTransactions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCookTransactionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cookTransactionsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cookTransactionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CookTransactions in the database
        List<CookTransactions> cookTransactionsList = cookTransactionsRepository.findAll();
        assertThat(cookTransactionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCookTransactions() throws Exception {
        int databaseSizeBeforeUpdate = cookTransactionsRepository.findAll().size();
        cookTransactions.setId(count.incrementAndGet());

        // Create the CookTransactions
        CookTransactionsDTO cookTransactionsDTO = cookTransactionsMapper.toDto(cookTransactions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCookTransactionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cookTransactionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CookTransactions in the database
        List<CookTransactions> cookTransactionsList = cookTransactionsRepository.findAll();
        assertThat(cookTransactionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCookTransactions() throws Exception {
        int databaseSizeBeforeUpdate = cookTransactionsRepository.findAll().size();
        cookTransactions.setId(count.incrementAndGet());

        // Create the CookTransactions
        CookTransactionsDTO cookTransactionsDTO = cookTransactionsMapper.toDto(cookTransactions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCookTransactionsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cookTransactionsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CookTransactions in the database
        List<CookTransactions> cookTransactionsList = cookTransactionsRepository.findAll();
        assertThat(cookTransactionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCookTransactions() throws Exception {
        // Initialize the database
        cookTransactionsRepository.saveAndFlush(cookTransactions);

        int databaseSizeBeforeDelete = cookTransactionsRepository.findAll().size();

        // Delete the cookTransactions
        restCookTransactionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, cookTransactions.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CookTransactions> cookTransactionsList = cookTransactionsRepository.findAll();
        assertThat(cookTransactionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
