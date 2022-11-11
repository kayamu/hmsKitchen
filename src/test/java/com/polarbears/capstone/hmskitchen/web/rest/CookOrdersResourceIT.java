package com.polarbears.capstone.hmskitchen.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.polarbears.capstone.hmskitchen.IntegrationTest;
import com.polarbears.capstone.hmskitchen.domain.CookOrders;
import com.polarbears.capstone.hmskitchen.domain.CookTransactions;
import com.polarbears.capstone.hmskitchen.repository.CookOrdersRepository;
import com.polarbears.capstone.hmskitchen.service.CookOrdersService;
import com.polarbears.capstone.hmskitchen.service.criteria.CookOrdersCriteria;
import com.polarbears.capstone.hmskitchen.service.dto.CookOrdersDTO;
import com.polarbears.capstone.hmskitchen.service.mapper.CookOrdersMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CookOrdersResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CookOrdersResourceIT {

    private static final Long DEFAULT_KITCHEN_ID = 1L;
    private static final Long UPDATED_KITCHEN_ID = 2L;
    private static final Long SMALLER_KITCHEN_ID = 1L - 1L;

    private static final Long DEFAULT_CUSTOMER_ID = 1L;
    private static final Long UPDATED_CUSTOMER_ID = 2L;
    private static final Long SMALLER_CUSTOMER_ID = 1L - 1L;

    private static final Long DEFAULT_CUSTOMER_CART_ID = 1L;
    private static final Long UPDATED_CUSTOMER_CART_ID = 2L;
    private static final Long SMALLER_CUSTOMER_CART_ID = 1L - 1L;

    private static final Long DEFAULT_MENU_ITEM_ID = 1L;
    private static final Long UPDATED_MENU_ITEM_ID = 2L;
    private static final Long SMALLER_MENU_ITEM_ID = 1L - 1L;

    private static final String DEFAULT_MENU_ITEM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MENU_ITEM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MENU_ITEM_CODE = "AAAAAAAAAA";
    private static final String UPDATED_MENU_ITEM_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_MEAL_ID = 1L;
    private static final Long UPDATED_MEAL_ID = 2L;
    private static final Long SMALLER_MEAL_ID = 1L - 1L;

    private static final Integer DEFAULT_LINE_NUMBER = 1;
    private static final Integer UPDATED_LINE_NUMBER = 2;
    private static final Integer SMALLER_LINE_NUMBER = 1 - 1;

    private static final LocalDate DEFAULT_REQUEST_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REQUEST_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REQUEST_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/cook-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CookOrdersRepository cookOrdersRepository;

    @Mock
    private CookOrdersRepository cookOrdersRepositoryMock;

    @Autowired
    private CookOrdersMapper cookOrdersMapper;

    @Mock
    private CookOrdersService cookOrdersServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCookOrdersMockMvc;

    private CookOrders cookOrders;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CookOrders createEntity(EntityManager em) {
        CookOrders cookOrders = new CookOrders()
            .kitchenId(DEFAULT_KITCHEN_ID)
            .customerId(DEFAULT_CUSTOMER_ID)
            .customerCartId(DEFAULT_CUSTOMER_CART_ID)
            .menuItemId(DEFAULT_MENU_ITEM_ID)
            .menuItemName(DEFAULT_MENU_ITEM_NAME)
            .menuItemCode(DEFAULT_MENU_ITEM_CODE)
            .mealId(DEFAULT_MEAL_ID)
            .lineNumber(DEFAULT_LINE_NUMBER)
            .requestDate(DEFAULT_REQUEST_DATE)
            .message(DEFAULT_MESSAGE)
            .createdDate(DEFAULT_CREATED_DATE);
        return cookOrders;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CookOrders createUpdatedEntity(EntityManager em) {
        CookOrders cookOrders = new CookOrders()
            .kitchenId(UPDATED_KITCHEN_ID)
            .customerId(UPDATED_CUSTOMER_ID)
            .customerCartId(UPDATED_CUSTOMER_CART_ID)
            .menuItemId(UPDATED_MENU_ITEM_ID)
            .menuItemName(UPDATED_MENU_ITEM_NAME)
            .menuItemCode(UPDATED_MENU_ITEM_CODE)
            .mealId(UPDATED_MEAL_ID)
            .lineNumber(UPDATED_LINE_NUMBER)
            .requestDate(UPDATED_REQUEST_DATE)
            .message(UPDATED_MESSAGE)
            .createdDate(UPDATED_CREATED_DATE);
        return cookOrders;
    }

    @BeforeEach
    public void initTest() {
        cookOrders = createEntity(em);
    }

    @Test
    @Transactional
    void createCookOrders() throws Exception {
        int databaseSizeBeforeCreate = cookOrdersRepository.findAll().size();
        // Create the CookOrders
        CookOrdersDTO cookOrdersDTO = cookOrdersMapper.toDto(cookOrders);
        restCookOrdersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cookOrdersDTO)))
            .andExpect(status().isCreated());

        // Validate the CookOrders in the database
        List<CookOrders> cookOrdersList = cookOrdersRepository.findAll();
        assertThat(cookOrdersList).hasSize(databaseSizeBeforeCreate + 1);
        CookOrders testCookOrders = cookOrdersList.get(cookOrdersList.size() - 1);
        assertThat(testCookOrders.getKitchenId()).isEqualTo(DEFAULT_KITCHEN_ID);
        assertThat(testCookOrders.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testCookOrders.getCustomerCartId()).isEqualTo(DEFAULT_CUSTOMER_CART_ID);
        assertThat(testCookOrders.getMenuItemId()).isEqualTo(DEFAULT_MENU_ITEM_ID);
        assertThat(testCookOrders.getMenuItemName()).isEqualTo(DEFAULT_MENU_ITEM_NAME);
        assertThat(testCookOrders.getMenuItemCode()).isEqualTo(DEFAULT_MENU_ITEM_CODE);
        assertThat(testCookOrders.getMealId()).isEqualTo(DEFAULT_MEAL_ID);
        assertThat(testCookOrders.getLineNumber()).isEqualTo(DEFAULT_LINE_NUMBER);
        assertThat(testCookOrders.getRequestDate()).isEqualTo(DEFAULT_REQUEST_DATE);
        assertThat(testCookOrders.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testCookOrders.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void createCookOrdersWithExistingId() throws Exception {
        // Create the CookOrders with an existing ID
        cookOrders.setId(1L);
        CookOrdersDTO cookOrdersDTO = cookOrdersMapper.toDto(cookOrders);

        int databaseSizeBeforeCreate = cookOrdersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCookOrdersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cookOrdersDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CookOrders in the database
        List<CookOrders> cookOrdersList = cookOrdersRepository.findAll();
        assertThat(cookOrdersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCookOrders() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList
        restCookOrdersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cookOrders.getId().intValue())))
            .andExpect(jsonPath("$.[*].kitchenId").value(hasItem(DEFAULT_KITCHEN_ID.intValue())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.intValue())))
            .andExpect(jsonPath("$.[*].customerCartId").value(hasItem(DEFAULT_CUSTOMER_CART_ID.intValue())))
            .andExpect(jsonPath("$.[*].menuItemId").value(hasItem(DEFAULT_MENU_ITEM_ID.intValue())))
            .andExpect(jsonPath("$.[*].menuItemName").value(hasItem(DEFAULT_MENU_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].menuItemCode").value(hasItem(DEFAULT_MENU_ITEM_CODE)))
            .andExpect(jsonPath("$.[*].mealId").value(hasItem(DEFAULT_MEAL_ID.intValue())))
            .andExpect(jsonPath("$.[*].lineNumber").value(hasItem(DEFAULT_LINE_NUMBER)))
            .andExpect(jsonPath("$.[*].requestDate").value(hasItem(DEFAULT_REQUEST_DATE.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCookOrdersWithEagerRelationshipsIsEnabled() throws Exception {
        when(cookOrdersServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCookOrdersMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cookOrdersServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCookOrdersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cookOrdersServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCookOrdersMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cookOrdersRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCookOrders() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get the cookOrders
        restCookOrdersMockMvc
            .perform(get(ENTITY_API_URL_ID, cookOrders.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cookOrders.getId().intValue()))
            .andExpect(jsonPath("$.kitchenId").value(DEFAULT_KITCHEN_ID.intValue()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID.intValue()))
            .andExpect(jsonPath("$.customerCartId").value(DEFAULT_CUSTOMER_CART_ID.intValue()))
            .andExpect(jsonPath("$.menuItemId").value(DEFAULT_MENU_ITEM_ID.intValue()))
            .andExpect(jsonPath("$.menuItemName").value(DEFAULT_MENU_ITEM_NAME))
            .andExpect(jsonPath("$.menuItemCode").value(DEFAULT_MENU_ITEM_CODE))
            .andExpect(jsonPath("$.mealId").value(DEFAULT_MEAL_ID.intValue()))
            .andExpect(jsonPath("$.lineNumber").value(DEFAULT_LINE_NUMBER))
            .andExpect(jsonPath("$.requestDate").value(DEFAULT_REQUEST_DATE.toString()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getCookOrdersByIdFiltering() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        Long id = cookOrders.getId();

        defaultCookOrdersShouldBeFound("id.equals=" + id);
        defaultCookOrdersShouldNotBeFound("id.notEquals=" + id);

        defaultCookOrdersShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCookOrdersShouldNotBeFound("id.greaterThan=" + id);

        defaultCookOrdersShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCookOrdersShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCookOrdersByKitchenIdIsEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where kitchenId equals to DEFAULT_KITCHEN_ID
        defaultCookOrdersShouldBeFound("kitchenId.equals=" + DEFAULT_KITCHEN_ID);

        // Get all the cookOrdersList where kitchenId equals to UPDATED_KITCHEN_ID
        defaultCookOrdersShouldNotBeFound("kitchenId.equals=" + UPDATED_KITCHEN_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByKitchenIdIsInShouldWork() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where kitchenId in DEFAULT_KITCHEN_ID or UPDATED_KITCHEN_ID
        defaultCookOrdersShouldBeFound("kitchenId.in=" + DEFAULT_KITCHEN_ID + "," + UPDATED_KITCHEN_ID);

        // Get all the cookOrdersList where kitchenId equals to UPDATED_KITCHEN_ID
        defaultCookOrdersShouldNotBeFound("kitchenId.in=" + UPDATED_KITCHEN_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByKitchenIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where kitchenId is not null
        defaultCookOrdersShouldBeFound("kitchenId.specified=true");

        // Get all the cookOrdersList where kitchenId is null
        defaultCookOrdersShouldNotBeFound("kitchenId.specified=false");
    }

    @Test
    @Transactional
    void getAllCookOrdersByKitchenIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where kitchenId is greater than or equal to DEFAULT_KITCHEN_ID
        defaultCookOrdersShouldBeFound("kitchenId.greaterThanOrEqual=" + DEFAULT_KITCHEN_ID);

        // Get all the cookOrdersList where kitchenId is greater than or equal to UPDATED_KITCHEN_ID
        defaultCookOrdersShouldNotBeFound("kitchenId.greaterThanOrEqual=" + UPDATED_KITCHEN_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByKitchenIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where kitchenId is less than or equal to DEFAULT_KITCHEN_ID
        defaultCookOrdersShouldBeFound("kitchenId.lessThanOrEqual=" + DEFAULT_KITCHEN_ID);

        // Get all the cookOrdersList where kitchenId is less than or equal to SMALLER_KITCHEN_ID
        defaultCookOrdersShouldNotBeFound("kitchenId.lessThanOrEqual=" + SMALLER_KITCHEN_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByKitchenIdIsLessThanSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where kitchenId is less than DEFAULT_KITCHEN_ID
        defaultCookOrdersShouldNotBeFound("kitchenId.lessThan=" + DEFAULT_KITCHEN_ID);

        // Get all the cookOrdersList where kitchenId is less than UPDATED_KITCHEN_ID
        defaultCookOrdersShouldBeFound("kitchenId.lessThan=" + UPDATED_KITCHEN_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByKitchenIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where kitchenId is greater than DEFAULT_KITCHEN_ID
        defaultCookOrdersShouldNotBeFound("kitchenId.greaterThan=" + DEFAULT_KITCHEN_ID);

        // Get all the cookOrdersList where kitchenId is greater than SMALLER_KITCHEN_ID
        defaultCookOrdersShouldBeFound("kitchenId.greaterThan=" + SMALLER_KITCHEN_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByCustomerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where customerId equals to DEFAULT_CUSTOMER_ID
        defaultCookOrdersShouldBeFound("customerId.equals=" + DEFAULT_CUSTOMER_ID);

        // Get all the cookOrdersList where customerId equals to UPDATED_CUSTOMER_ID
        defaultCookOrdersShouldNotBeFound("customerId.equals=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByCustomerIdIsInShouldWork() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where customerId in DEFAULT_CUSTOMER_ID or UPDATED_CUSTOMER_ID
        defaultCookOrdersShouldBeFound("customerId.in=" + DEFAULT_CUSTOMER_ID + "," + UPDATED_CUSTOMER_ID);

        // Get all the cookOrdersList where customerId equals to UPDATED_CUSTOMER_ID
        defaultCookOrdersShouldNotBeFound("customerId.in=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByCustomerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where customerId is not null
        defaultCookOrdersShouldBeFound("customerId.specified=true");

        // Get all the cookOrdersList where customerId is null
        defaultCookOrdersShouldNotBeFound("customerId.specified=false");
    }

    @Test
    @Transactional
    void getAllCookOrdersByCustomerIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where customerId is greater than or equal to DEFAULT_CUSTOMER_ID
        defaultCookOrdersShouldBeFound("customerId.greaterThanOrEqual=" + DEFAULT_CUSTOMER_ID);

        // Get all the cookOrdersList where customerId is greater than or equal to UPDATED_CUSTOMER_ID
        defaultCookOrdersShouldNotBeFound("customerId.greaterThanOrEqual=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByCustomerIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where customerId is less than or equal to DEFAULT_CUSTOMER_ID
        defaultCookOrdersShouldBeFound("customerId.lessThanOrEqual=" + DEFAULT_CUSTOMER_ID);

        // Get all the cookOrdersList where customerId is less than or equal to SMALLER_CUSTOMER_ID
        defaultCookOrdersShouldNotBeFound("customerId.lessThanOrEqual=" + SMALLER_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByCustomerIdIsLessThanSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where customerId is less than DEFAULT_CUSTOMER_ID
        defaultCookOrdersShouldNotBeFound("customerId.lessThan=" + DEFAULT_CUSTOMER_ID);

        // Get all the cookOrdersList where customerId is less than UPDATED_CUSTOMER_ID
        defaultCookOrdersShouldBeFound("customerId.lessThan=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByCustomerIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where customerId is greater than DEFAULT_CUSTOMER_ID
        defaultCookOrdersShouldNotBeFound("customerId.greaterThan=" + DEFAULT_CUSTOMER_ID);

        // Get all the cookOrdersList where customerId is greater than SMALLER_CUSTOMER_ID
        defaultCookOrdersShouldBeFound("customerId.greaterThan=" + SMALLER_CUSTOMER_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByCustomerCartIdIsEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where customerCartId equals to DEFAULT_CUSTOMER_CART_ID
        defaultCookOrdersShouldBeFound("customerCartId.equals=" + DEFAULT_CUSTOMER_CART_ID);

        // Get all the cookOrdersList where customerCartId equals to UPDATED_CUSTOMER_CART_ID
        defaultCookOrdersShouldNotBeFound("customerCartId.equals=" + UPDATED_CUSTOMER_CART_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByCustomerCartIdIsInShouldWork() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where customerCartId in DEFAULT_CUSTOMER_CART_ID or UPDATED_CUSTOMER_CART_ID
        defaultCookOrdersShouldBeFound("customerCartId.in=" + DEFAULT_CUSTOMER_CART_ID + "," + UPDATED_CUSTOMER_CART_ID);

        // Get all the cookOrdersList where customerCartId equals to UPDATED_CUSTOMER_CART_ID
        defaultCookOrdersShouldNotBeFound("customerCartId.in=" + UPDATED_CUSTOMER_CART_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByCustomerCartIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where customerCartId is not null
        defaultCookOrdersShouldBeFound("customerCartId.specified=true");

        // Get all the cookOrdersList where customerCartId is null
        defaultCookOrdersShouldNotBeFound("customerCartId.specified=false");
    }

    @Test
    @Transactional
    void getAllCookOrdersByCustomerCartIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where customerCartId is greater than or equal to DEFAULT_CUSTOMER_CART_ID
        defaultCookOrdersShouldBeFound("customerCartId.greaterThanOrEqual=" + DEFAULT_CUSTOMER_CART_ID);

        // Get all the cookOrdersList where customerCartId is greater than or equal to UPDATED_CUSTOMER_CART_ID
        defaultCookOrdersShouldNotBeFound("customerCartId.greaterThanOrEqual=" + UPDATED_CUSTOMER_CART_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByCustomerCartIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where customerCartId is less than or equal to DEFAULT_CUSTOMER_CART_ID
        defaultCookOrdersShouldBeFound("customerCartId.lessThanOrEqual=" + DEFAULT_CUSTOMER_CART_ID);

        // Get all the cookOrdersList where customerCartId is less than or equal to SMALLER_CUSTOMER_CART_ID
        defaultCookOrdersShouldNotBeFound("customerCartId.lessThanOrEqual=" + SMALLER_CUSTOMER_CART_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByCustomerCartIdIsLessThanSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where customerCartId is less than DEFAULT_CUSTOMER_CART_ID
        defaultCookOrdersShouldNotBeFound("customerCartId.lessThan=" + DEFAULT_CUSTOMER_CART_ID);

        // Get all the cookOrdersList where customerCartId is less than UPDATED_CUSTOMER_CART_ID
        defaultCookOrdersShouldBeFound("customerCartId.lessThan=" + UPDATED_CUSTOMER_CART_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByCustomerCartIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where customerCartId is greater than DEFAULT_CUSTOMER_CART_ID
        defaultCookOrdersShouldNotBeFound("customerCartId.greaterThan=" + DEFAULT_CUSTOMER_CART_ID);

        // Get all the cookOrdersList where customerCartId is greater than SMALLER_CUSTOMER_CART_ID
        defaultCookOrdersShouldBeFound("customerCartId.greaterThan=" + SMALLER_CUSTOMER_CART_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByMenuItemIdIsEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where menuItemId equals to DEFAULT_MENU_ITEM_ID
        defaultCookOrdersShouldBeFound("menuItemId.equals=" + DEFAULT_MENU_ITEM_ID);

        // Get all the cookOrdersList where menuItemId equals to UPDATED_MENU_ITEM_ID
        defaultCookOrdersShouldNotBeFound("menuItemId.equals=" + UPDATED_MENU_ITEM_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByMenuItemIdIsInShouldWork() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where menuItemId in DEFAULT_MENU_ITEM_ID or UPDATED_MENU_ITEM_ID
        defaultCookOrdersShouldBeFound("menuItemId.in=" + DEFAULT_MENU_ITEM_ID + "," + UPDATED_MENU_ITEM_ID);

        // Get all the cookOrdersList where menuItemId equals to UPDATED_MENU_ITEM_ID
        defaultCookOrdersShouldNotBeFound("menuItemId.in=" + UPDATED_MENU_ITEM_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByMenuItemIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where menuItemId is not null
        defaultCookOrdersShouldBeFound("menuItemId.specified=true");

        // Get all the cookOrdersList where menuItemId is null
        defaultCookOrdersShouldNotBeFound("menuItemId.specified=false");
    }

    @Test
    @Transactional
    void getAllCookOrdersByMenuItemIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where menuItemId is greater than or equal to DEFAULT_MENU_ITEM_ID
        defaultCookOrdersShouldBeFound("menuItemId.greaterThanOrEqual=" + DEFAULT_MENU_ITEM_ID);

        // Get all the cookOrdersList where menuItemId is greater than or equal to UPDATED_MENU_ITEM_ID
        defaultCookOrdersShouldNotBeFound("menuItemId.greaterThanOrEqual=" + UPDATED_MENU_ITEM_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByMenuItemIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where menuItemId is less than or equal to DEFAULT_MENU_ITEM_ID
        defaultCookOrdersShouldBeFound("menuItemId.lessThanOrEqual=" + DEFAULT_MENU_ITEM_ID);

        // Get all the cookOrdersList where menuItemId is less than or equal to SMALLER_MENU_ITEM_ID
        defaultCookOrdersShouldNotBeFound("menuItemId.lessThanOrEqual=" + SMALLER_MENU_ITEM_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByMenuItemIdIsLessThanSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where menuItemId is less than DEFAULT_MENU_ITEM_ID
        defaultCookOrdersShouldNotBeFound("menuItemId.lessThan=" + DEFAULT_MENU_ITEM_ID);

        // Get all the cookOrdersList where menuItemId is less than UPDATED_MENU_ITEM_ID
        defaultCookOrdersShouldBeFound("menuItemId.lessThan=" + UPDATED_MENU_ITEM_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByMenuItemIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where menuItemId is greater than DEFAULT_MENU_ITEM_ID
        defaultCookOrdersShouldNotBeFound("menuItemId.greaterThan=" + DEFAULT_MENU_ITEM_ID);

        // Get all the cookOrdersList where menuItemId is greater than SMALLER_MENU_ITEM_ID
        defaultCookOrdersShouldBeFound("menuItemId.greaterThan=" + SMALLER_MENU_ITEM_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByMenuItemNameIsEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where menuItemName equals to DEFAULT_MENU_ITEM_NAME
        defaultCookOrdersShouldBeFound("menuItemName.equals=" + DEFAULT_MENU_ITEM_NAME);

        // Get all the cookOrdersList where menuItemName equals to UPDATED_MENU_ITEM_NAME
        defaultCookOrdersShouldNotBeFound("menuItemName.equals=" + UPDATED_MENU_ITEM_NAME);
    }

    @Test
    @Transactional
    void getAllCookOrdersByMenuItemNameIsInShouldWork() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where menuItemName in DEFAULT_MENU_ITEM_NAME or UPDATED_MENU_ITEM_NAME
        defaultCookOrdersShouldBeFound("menuItemName.in=" + DEFAULT_MENU_ITEM_NAME + "," + UPDATED_MENU_ITEM_NAME);

        // Get all the cookOrdersList where menuItemName equals to UPDATED_MENU_ITEM_NAME
        defaultCookOrdersShouldNotBeFound("menuItemName.in=" + UPDATED_MENU_ITEM_NAME);
    }

    @Test
    @Transactional
    void getAllCookOrdersByMenuItemNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where menuItemName is not null
        defaultCookOrdersShouldBeFound("menuItemName.specified=true");

        // Get all the cookOrdersList where menuItemName is null
        defaultCookOrdersShouldNotBeFound("menuItemName.specified=false");
    }

    @Test
    @Transactional
    void getAllCookOrdersByMenuItemNameContainsSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where menuItemName contains DEFAULT_MENU_ITEM_NAME
        defaultCookOrdersShouldBeFound("menuItemName.contains=" + DEFAULT_MENU_ITEM_NAME);

        // Get all the cookOrdersList where menuItemName contains UPDATED_MENU_ITEM_NAME
        defaultCookOrdersShouldNotBeFound("menuItemName.contains=" + UPDATED_MENU_ITEM_NAME);
    }

    @Test
    @Transactional
    void getAllCookOrdersByMenuItemNameNotContainsSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where menuItemName does not contain DEFAULT_MENU_ITEM_NAME
        defaultCookOrdersShouldNotBeFound("menuItemName.doesNotContain=" + DEFAULT_MENU_ITEM_NAME);

        // Get all the cookOrdersList where menuItemName does not contain UPDATED_MENU_ITEM_NAME
        defaultCookOrdersShouldBeFound("menuItemName.doesNotContain=" + UPDATED_MENU_ITEM_NAME);
    }

    @Test
    @Transactional
    void getAllCookOrdersByMenuItemCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where menuItemCode equals to DEFAULT_MENU_ITEM_CODE
        defaultCookOrdersShouldBeFound("menuItemCode.equals=" + DEFAULT_MENU_ITEM_CODE);

        // Get all the cookOrdersList where menuItemCode equals to UPDATED_MENU_ITEM_CODE
        defaultCookOrdersShouldNotBeFound("menuItemCode.equals=" + UPDATED_MENU_ITEM_CODE);
    }

    @Test
    @Transactional
    void getAllCookOrdersByMenuItemCodeIsInShouldWork() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where menuItemCode in DEFAULT_MENU_ITEM_CODE or UPDATED_MENU_ITEM_CODE
        defaultCookOrdersShouldBeFound("menuItemCode.in=" + DEFAULT_MENU_ITEM_CODE + "," + UPDATED_MENU_ITEM_CODE);

        // Get all the cookOrdersList where menuItemCode equals to UPDATED_MENU_ITEM_CODE
        defaultCookOrdersShouldNotBeFound("menuItemCode.in=" + UPDATED_MENU_ITEM_CODE);
    }

    @Test
    @Transactional
    void getAllCookOrdersByMenuItemCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where menuItemCode is not null
        defaultCookOrdersShouldBeFound("menuItemCode.specified=true");

        // Get all the cookOrdersList where menuItemCode is null
        defaultCookOrdersShouldNotBeFound("menuItemCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCookOrdersByMenuItemCodeContainsSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where menuItemCode contains DEFAULT_MENU_ITEM_CODE
        defaultCookOrdersShouldBeFound("menuItemCode.contains=" + DEFAULT_MENU_ITEM_CODE);

        // Get all the cookOrdersList where menuItemCode contains UPDATED_MENU_ITEM_CODE
        defaultCookOrdersShouldNotBeFound("menuItemCode.contains=" + UPDATED_MENU_ITEM_CODE);
    }

    @Test
    @Transactional
    void getAllCookOrdersByMenuItemCodeNotContainsSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where menuItemCode does not contain DEFAULT_MENU_ITEM_CODE
        defaultCookOrdersShouldNotBeFound("menuItemCode.doesNotContain=" + DEFAULT_MENU_ITEM_CODE);

        // Get all the cookOrdersList where menuItemCode does not contain UPDATED_MENU_ITEM_CODE
        defaultCookOrdersShouldBeFound("menuItemCode.doesNotContain=" + UPDATED_MENU_ITEM_CODE);
    }

    @Test
    @Transactional
    void getAllCookOrdersByMealIdIsEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where mealId equals to DEFAULT_MEAL_ID
        defaultCookOrdersShouldBeFound("mealId.equals=" + DEFAULT_MEAL_ID);

        // Get all the cookOrdersList where mealId equals to UPDATED_MEAL_ID
        defaultCookOrdersShouldNotBeFound("mealId.equals=" + UPDATED_MEAL_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByMealIdIsInShouldWork() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where mealId in DEFAULT_MEAL_ID or UPDATED_MEAL_ID
        defaultCookOrdersShouldBeFound("mealId.in=" + DEFAULT_MEAL_ID + "," + UPDATED_MEAL_ID);

        // Get all the cookOrdersList where mealId equals to UPDATED_MEAL_ID
        defaultCookOrdersShouldNotBeFound("mealId.in=" + UPDATED_MEAL_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByMealIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where mealId is not null
        defaultCookOrdersShouldBeFound("mealId.specified=true");

        // Get all the cookOrdersList where mealId is null
        defaultCookOrdersShouldNotBeFound("mealId.specified=false");
    }

    @Test
    @Transactional
    void getAllCookOrdersByMealIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where mealId is greater than or equal to DEFAULT_MEAL_ID
        defaultCookOrdersShouldBeFound("mealId.greaterThanOrEqual=" + DEFAULT_MEAL_ID);

        // Get all the cookOrdersList where mealId is greater than or equal to UPDATED_MEAL_ID
        defaultCookOrdersShouldNotBeFound("mealId.greaterThanOrEqual=" + UPDATED_MEAL_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByMealIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where mealId is less than or equal to DEFAULT_MEAL_ID
        defaultCookOrdersShouldBeFound("mealId.lessThanOrEqual=" + DEFAULT_MEAL_ID);

        // Get all the cookOrdersList where mealId is less than or equal to SMALLER_MEAL_ID
        defaultCookOrdersShouldNotBeFound("mealId.lessThanOrEqual=" + SMALLER_MEAL_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByMealIdIsLessThanSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where mealId is less than DEFAULT_MEAL_ID
        defaultCookOrdersShouldNotBeFound("mealId.lessThan=" + DEFAULT_MEAL_ID);

        // Get all the cookOrdersList where mealId is less than UPDATED_MEAL_ID
        defaultCookOrdersShouldBeFound("mealId.lessThan=" + UPDATED_MEAL_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByMealIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where mealId is greater than DEFAULT_MEAL_ID
        defaultCookOrdersShouldNotBeFound("mealId.greaterThan=" + DEFAULT_MEAL_ID);

        // Get all the cookOrdersList where mealId is greater than SMALLER_MEAL_ID
        defaultCookOrdersShouldBeFound("mealId.greaterThan=" + SMALLER_MEAL_ID);
    }

    @Test
    @Transactional
    void getAllCookOrdersByLineNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where lineNumber equals to DEFAULT_LINE_NUMBER
        defaultCookOrdersShouldBeFound("lineNumber.equals=" + DEFAULT_LINE_NUMBER);

        // Get all the cookOrdersList where lineNumber equals to UPDATED_LINE_NUMBER
        defaultCookOrdersShouldNotBeFound("lineNumber.equals=" + UPDATED_LINE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCookOrdersByLineNumberIsInShouldWork() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where lineNumber in DEFAULT_LINE_NUMBER or UPDATED_LINE_NUMBER
        defaultCookOrdersShouldBeFound("lineNumber.in=" + DEFAULT_LINE_NUMBER + "," + UPDATED_LINE_NUMBER);

        // Get all the cookOrdersList where lineNumber equals to UPDATED_LINE_NUMBER
        defaultCookOrdersShouldNotBeFound("lineNumber.in=" + UPDATED_LINE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCookOrdersByLineNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where lineNumber is not null
        defaultCookOrdersShouldBeFound("lineNumber.specified=true");

        // Get all the cookOrdersList where lineNumber is null
        defaultCookOrdersShouldNotBeFound("lineNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCookOrdersByLineNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where lineNumber is greater than or equal to DEFAULT_LINE_NUMBER
        defaultCookOrdersShouldBeFound("lineNumber.greaterThanOrEqual=" + DEFAULT_LINE_NUMBER);

        // Get all the cookOrdersList where lineNumber is greater than or equal to UPDATED_LINE_NUMBER
        defaultCookOrdersShouldNotBeFound("lineNumber.greaterThanOrEqual=" + UPDATED_LINE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCookOrdersByLineNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where lineNumber is less than or equal to DEFAULT_LINE_NUMBER
        defaultCookOrdersShouldBeFound("lineNumber.lessThanOrEqual=" + DEFAULT_LINE_NUMBER);

        // Get all the cookOrdersList where lineNumber is less than or equal to SMALLER_LINE_NUMBER
        defaultCookOrdersShouldNotBeFound("lineNumber.lessThanOrEqual=" + SMALLER_LINE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCookOrdersByLineNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where lineNumber is less than DEFAULT_LINE_NUMBER
        defaultCookOrdersShouldNotBeFound("lineNumber.lessThan=" + DEFAULT_LINE_NUMBER);

        // Get all the cookOrdersList where lineNumber is less than UPDATED_LINE_NUMBER
        defaultCookOrdersShouldBeFound("lineNumber.lessThan=" + UPDATED_LINE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCookOrdersByLineNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where lineNumber is greater than DEFAULT_LINE_NUMBER
        defaultCookOrdersShouldNotBeFound("lineNumber.greaterThan=" + DEFAULT_LINE_NUMBER);

        // Get all the cookOrdersList where lineNumber is greater than SMALLER_LINE_NUMBER
        defaultCookOrdersShouldBeFound("lineNumber.greaterThan=" + SMALLER_LINE_NUMBER);
    }

    @Test
    @Transactional
    void getAllCookOrdersByRequestDateIsEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where requestDate equals to DEFAULT_REQUEST_DATE
        defaultCookOrdersShouldBeFound("requestDate.equals=" + DEFAULT_REQUEST_DATE);

        // Get all the cookOrdersList where requestDate equals to UPDATED_REQUEST_DATE
        defaultCookOrdersShouldNotBeFound("requestDate.equals=" + UPDATED_REQUEST_DATE);
    }

    @Test
    @Transactional
    void getAllCookOrdersByRequestDateIsInShouldWork() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where requestDate in DEFAULT_REQUEST_DATE or UPDATED_REQUEST_DATE
        defaultCookOrdersShouldBeFound("requestDate.in=" + DEFAULT_REQUEST_DATE + "," + UPDATED_REQUEST_DATE);

        // Get all the cookOrdersList where requestDate equals to UPDATED_REQUEST_DATE
        defaultCookOrdersShouldNotBeFound("requestDate.in=" + UPDATED_REQUEST_DATE);
    }

    @Test
    @Transactional
    void getAllCookOrdersByRequestDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where requestDate is not null
        defaultCookOrdersShouldBeFound("requestDate.specified=true");

        // Get all the cookOrdersList where requestDate is null
        defaultCookOrdersShouldNotBeFound("requestDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCookOrdersByRequestDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where requestDate is greater than or equal to DEFAULT_REQUEST_DATE
        defaultCookOrdersShouldBeFound("requestDate.greaterThanOrEqual=" + DEFAULT_REQUEST_DATE);

        // Get all the cookOrdersList where requestDate is greater than or equal to UPDATED_REQUEST_DATE
        defaultCookOrdersShouldNotBeFound("requestDate.greaterThanOrEqual=" + UPDATED_REQUEST_DATE);
    }

    @Test
    @Transactional
    void getAllCookOrdersByRequestDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where requestDate is less than or equal to DEFAULT_REQUEST_DATE
        defaultCookOrdersShouldBeFound("requestDate.lessThanOrEqual=" + DEFAULT_REQUEST_DATE);

        // Get all the cookOrdersList where requestDate is less than or equal to SMALLER_REQUEST_DATE
        defaultCookOrdersShouldNotBeFound("requestDate.lessThanOrEqual=" + SMALLER_REQUEST_DATE);
    }

    @Test
    @Transactional
    void getAllCookOrdersByRequestDateIsLessThanSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where requestDate is less than DEFAULT_REQUEST_DATE
        defaultCookOrdersShouldNotBeFound("requestDate.lessThan=" + DEFAULT_REQUEST_DATE);

        // Get all the cookOrdersList where requestDate is less than UPDATED_REQUEST_DATE
        defaultCookOrdersShouldBeFound("requestDate.lessThan=" + UPDATED_REQUEST_DATE);
    }

    @Test
    @Transactional
    void getAllCookOrdersByRequestDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where requestDate is greater than DEFAULT_REQUEST_DATE
        defaultCookOrdersShouldNotBeFound("requestDate.greaterThan=" + DEFAULT_REQUEST_DATE);

        // Get all the cookOrdersList where requestDate is greater than SMALLER_REQUEST_DATE
        defaultCookOrdersShouldBeFound("requestDate.greaterThan=" + SMALLER_REQUEST_DATE);
    }

    @Test
    @Transactional
    void getAllCookOrdersByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where message equals to DEFAULT_MESSAGE
        defaultCookOrdersShouldBeFound("message.equals=" + DEFAULT_MESSAGE);

        // Get all the cookOrdersList where message equals to UPDATED_MESSAGE
        defaultCookOrdersShouldNotBeFound("message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllCookOrdersByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where message in DEFAULT_MESSAGE or UPDATED_MESSAGE
        defaultCookOrdersShouldBeFound("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE);

        // Get all the cookOrdersList where message equals to UPDATED_MESSAGE
        defaultCookOrdersShouldNotBeFound("message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllCookOrdersByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where message is not null
        defaultCookOrdersShouldBeFound("message.specified=true");

        // Get all the cookOrdersList where message is null
        defaultCookOrdersShouldNotBeFound("message.specified=false");
    }

    @Test
    @Transactional
    void getAllCookOrdersByMessageContainsSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where message contains DEFAULT_MESSAGE
        defaultCookOrdersShouldBeFound("message.contains=" + DEFAULT_MESSAGE);

        // Get all the cookOrdersList where message contains UPDATED_MESSAGE
        defaultCookOrdersShouldNotBeFound("message.contains=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllCookOrdersByMessageNotContainsSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where message does not contain DEFAULT_MESSAGE
        defaultCookOrdersShouldNotBeFound("message.doesNotContain=" + DEFAULT_MESSAGE);

        // Get all the cookOrdersList where message does not contain UPDATED_MESSAGE
        defaultCookOrdersShouldBeFound("message.doesNotContain=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllCookOrdersByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where createdDate equals to DEFAULT_CREATED_DATE
        defaultCookOrdersShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the cookOrdersList where createdDate equals to UPDATED_CREATED_DATE
        defaultCookOrdersShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCookOrdersByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultCookOrdersShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the cookOrdersList where createdDate equals to UPDATED_CREATED_DATE
        defaultCookOrdersShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCookOrdersByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where createdDate is not null
        defaultCookOrdersShouldBeFound("createdDate.specified=true");

        // Get all the cookOrdersList where createdDate is null
        defaultCookOrdersShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCookOrdersByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where createdDate is greater than or equal to DEFAULT_CREATED_DATE
        defaultCookOrdersShouldBeFound("createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the cookOrdersList where createdDate is greater than or equal to UPDATED_CREATED_DATE
        defaultCookOrdersShouldNotBeFound("createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCookOrdersByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where createdDate is less than or equal to DEFAULT_CREATED_DATE
        defaultCookOrdersShouldBeFound("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE);

        // Get all the cookOrdersList where createdDate is less than or equal to SMALLER_CREATED_DATE
        defaultCookOrdersShouldNotBeFound("createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCookOrdersByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where createdDate is less than DEFAULT_CREATED_DATE
        defaultCookOrdersShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the cookOrdersList where createdDate is less than UPDATED_CREATED_DATE
        defaultCookOrdersShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCookOrdersByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        // Get all the cookOrdersList where createdDate is greater than DEFAULT_CREATED_DATE
        defaultCookOrdersShouldNotBeFound("createdDate.greaterThan=" + DEFAULT_CREATED_DATE);

        // Get all the cookOrdersList where createdDate is greater than SMALLER_CREATED_DATE
        defaultCookOrdersShouldBeFound("createdDate.greaterThan=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCookOrdersByCookTransactionsIsEqualToSomething() throws Exception {
        CookTransactions cookTransactions;
        if (TestUtil.findAll(em, CookTransactions.class).isEmpty()) {
            cookOrdersRepository.saveAndFlush(cookOrders);
            cookTransactions = CookTransactionsResourceIT.createEntity(em);
        } else {
            cookTransactions = TestUtil.findAll(em, CookTransactions.class).get(0);
        }
        em.persist(cookTransactions);
        em.flush();
        cookOrders.addCookTransactions(cookTransactions);
        cookOrdersRepository.saveAndFlush(cookOrders);
        Long cookTransactionsId = cookTransactions.getId();

        // Get all the cookOrdersList where cookTransactions equals to cookTransactionsId
        defaultCookOrdersShouldBeFound("cookTransactionsId.equals=" + cookTransactionsId);

        // Get all the cookOrdersList where cookTransactions equals to (cookTransactionsId + 1)
        defaultCookOrdersShouldNotBeFound("cookTransactionsId.equals=" + (cookTransactionsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCookOrdersShouldBeFound(String filter) throws Exception {
        restCookOrdersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cookOrders.getId().intValue())))
            .andExpect(jsonPath("$.[*].kitchenId").value(hasItem(DEFAULT_KITCHEN_ID.intValue())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.intValue())))
            .andExpect(jsonPath("$.[*].customerCartId").value(hasItem(DEFAULT_CUSTOMER_CART_ID.intValue())))
            .andExpect(jsonPath("$.[*].menuItemId").value(hasItem(DEFAULT_MENU_ITEM_ID.intValue())))
            .andExpect(jsonPath("$.[*].menuItemName").value(hasItem(DEFAULT_MENU_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].menuItemCode").value(hasItem(DEFAULT_MENU_ITEM_CODE)))
            .andExpect(jsonPath("$.[*].mealId").value(hasItem(DEFAULT_MEAL_ID.intValue())))
            .andExpect(jsonPath("$.[*].lineNumber").value(hasItem(DEFAULT_LINE_NUMBER)))
            .andExpect(jsonPath("$.[*].requestDate").value(hasItem(DEFAULT_REQUEST_DATE.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restCookOrdersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCookOrdersShouldNotBeFound(String filter) throws Exception {
        restCookOrdersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCookOrdersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCookOrders() throws Exception {
        // Get the cookOrders
        restCookOrdersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCookOrders() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        int databaseSizeBeforeUpdate = cookOrdersRepository.findAll().size();

        // Update the cookOrders
        CookOrders updatedCookOrders = cookOrdersRepository.findById(cookOrders.getId()).get();
        // Disconnect from session so that the updates on updatedCookOrders are not directly saved in db
        em.detach(updatedCookOrders);
        updatedCookOrders
            .kitchenId(UPDATED_KITCHEN_ID)
            .customerId(UPDATED_CUSTOMER_ID)
            .customerCartId(UPDATED_CUSTOMER_CART_ID)
            .menuItemId(UPDATED_MENU_ITEM_ID)
            .menuItemName(UPDATED_MENU_ITEM_NAME)
            .menuItemCode(UPDATED_MENU_ITEM_CODE)
            .mealId(UPDATED_MEAL_ID)
            .lineNumber(UPDATED_LINE_NUMBER)
            .requestDate(UPDATED_REQUEST_DATE)
            .message(UPDATED_MESSAGE)
            .createdDate(UPDATED_CREATED_DATE);
        CookOrdersDTO cookOrdersDTO = cookOrdersMapper.toDto(updatedCookOrders);

        restCookOrdersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cookOrdersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cookOrdersDTO))
            )
            .andExpect(status().isOk());

        // Validate the CookOrders in the database
        List<CookOrders> cookOrdersList = cookOrdersRepository.findAll();
        assertThat(cookOrdersList).hasSize(databaseSizeBeforeUpdate);
        CookOrders testCookOrders = cookOrdersList.get(cookOrdersList.size() - 1);
        assertThat(testCookOrders.getKitchenId()).isEqualTo(UPDATED_KITCHEN_ID);
        assertThat(testCookOrders.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testCookOrders.getCustomerCartId()).isEqualTo(UPDATED_CUSTOMER_CART_ID);
        assertThat(testCookOrders.getMenuItemId()).isEqualTo(UPDATED_MENU_ITEM_ID);
        assertThat(testCookOrders.getMenuItemName()).isEqualTo(UPDATED_MENU_ITEM_NAME);
        assertThat(testCookOrders.getMenuItemCode()).isEqualTo(UPDATED_MENU_ITEM_CODE);
        assertThat(testCookOrders.getMealId()).isEqualTo(UPDATED_MEAL_ID);
        assertThat(testCookOrders.getLineNumber()).isEqualTo(UPDATED_LINE_NUMBER);
        assertThat(testCookOrders.getRequestDate()).isEqualTo(UPDATED_REQUEST_DATE);
        assertThat(testCookOrders.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testCookOrders.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingCookOrders() throws Exception {
        int databaseSizeBeforeUpdate = cookOrdersRepository.findAll().size();
        cookOrders.setId(count.incrementAndGet());

        // Create the CookOrders
        CookOrdersDTO cookOrdersDTO = cookOrdersMapper.toDto(cookOrders);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCookOrdersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cookOrdersDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cookOrdersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CookOrders in the database
        List<CookOrders> cookOrdersList = cookOrdersRepository.findAll();
        assertThat(cookOrdersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCookOrders() throws Exception {
        int databaseSizeBeforeUpdate = cookOrdersRepository.findAll().size();
        cookOrders.setId(count.incrementAndGet());

        // Create the CookOrders
        CookOrdersDTO cookOrdersDTO = cookOrdersMapper.toDto(cookOrders);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCookOrdersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cookOrdersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CookOrders in the database
        List<CookOrders> cookOrdersList = cookOrdersRepository.findAll();
        assertThat(cookOrdersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCookOrders() throws Exception {
        int databaseSizeBeforeUpdate = cookOrdersRepository.findAll().size();
        cookOrders.setId(count.incrementAndGet());

        // Create the CookOrders
        CookOrdersDTO cookOrdersDTO = cookOrdersMapper.toDto(cookOrders);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCookOrdersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cookOrdersDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CookOrders in the database
        List<CookOrders> cookOrdersList = cookOrdersRepository.findAll();
        assertThat(cookOrdersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCookOrdersWithPatch() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        int databaseSizeBeforeUpdate = cookOrdersRepository.findAll().size();

        // Update the cookOrders using partial update
        CookOrders partialUpdatedCookOrders = new CookOrders();
        partialUpdatedCookOrders.setId(cookOrders.getId());

        partialUpdatedCookOrders
            .kitchenId(UPDATED_KITCHEN_ID)
            .customerId(UPDATED_CUSTOMER_ID)
            .customerCartId(UPDATED_CUSTOMER_CART_ID)
            .menuItemId(UPDATED_MENU_ITEM_ID)
            .menuItemCode(UPDATED_MENU_ITEM_CODE)
            .mealId(UPDATED_MEAL_ID)
            .requestDate(UPDATED_REQUEST_DATE);

        restCookOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCookOrders.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCookOrders))
            )
            .andExpect(status().isOk());

        // Validate the CookOrders in the database
        List<CookOrders> cookOrdersList = cookOrdersRepository.findAll();
        assertThat(cookOrdersList).hasSize(databaseSizeBeforeUpdate);
        CookOrders testCookOrders = cookOrdersList.get(cookOrdersList.size() - 1);
        assertThat(testCookOrders.getKitchenId()).isEqualTo(UPDATED_KITCHEN_ID);
        assertThat(testCookOrders.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testCookOrders.getCustomerCartId()).isEqualTo(UPDATED_CUSTOMER_CART_ID);
        assertThat(testCookOrders.getMenuItemId()).isEqualTo(UPDATED_MENU_ITEM_ID);
        assertThat(testCookOrders.getMenuItemName()).isEqualTo(DEFAULT_MENU_ITEM_NAME);
        assertThat(testCookOrders.getMenuItemCode()).isEqualTo(UPDATED_MENU_ITEM_CODE);
        assertThat(testCookOrders.getMealId()).isEqualTo(UPDATED_MEAL_ID);
        assertThat(testCookOrders.getLineNumber()).isEqualTo(DEFAULT_LINE_NUMBER);
        assertThat(testCookOrders.getRequestDate()).isEqualTo(UPDATED_REQUEST_DATE);
        assertThat(testCookOrders.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testCookOrders.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateCookOrdersWithPatch() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        int databaseSizeBeforeUpdate = cookOrdersRepository.findAll().size();

        // Update the cookOrders using partial update
        CookOrders partialUpdatedCookOrders = new CookOrders();
        partialUpdatedCookOrders.setId(cookOrders.getId());

        partialUpdatedCookOrders
            .kitchenId(UPDATED_KITCHEN_ID)
            .customerId(UPDATED_CUSTOMER_ID)
            .customerCartId(UPDATED_CUSTOMER_CART_ID)
            .menuItemId(UPDATED_MENU_ITEM_ID)
            .menuItemName(UPDATED_MENU_ITEM_NAME)
            .menuItemCode(UPDATED_MENU_ITEM_CODE)
            .mealId(UPDATED_MEAL_ID)
            .lineNumber(UPDATED_LINE_NUMBER)
            .requestDate(UPDATED_REQUEST_DATE)
            .message(UPDATED_MESSAGE)
            .createdDate(UPDATED_CREATED_DATE);

        restCookOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCookOrders.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCookOrders))
            )
            .andExpect(status().isOk());

        // Validate the CookOrders in the database
        List<CookOrders> cookOrdersList = cookOrdersRepository.findAll();
        assertThat(cookOrdersList).hasSize(databaseSizeBeforeUpdate);
        CookOrders testCookOrders = cookOrdersList.get(cookOrdersList.size() - 1);
        assertThat(testCookOrders.getKitchenId()).isEqualTo(UPDATED_KITCHEN_ID);
        assertThat(testCookOrders.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testCookOrders.getCustomerCartId()).isEqualTo(UPDATED_CUSTOMER_CART_ID);
        assertThat(testCookOrders.getMenuItemId()).isEqualTo(UPDATED_MENU_ITEM_ID);
        assertThat(testCookOrders.getMenuItemName()).isEqualTo(UPDATED_MENU_ITEM_NAME);
        assertThat(testCookOrders.getMenuItemCode()).isEqualTo(UPDATED_MENU_ITEM_CODE);
        assertThat(testCookOrders.getMealId()).isEqualTo(UPDATED_MEAL_ID);
        assertThat(testCookOrders.getLineNumber()).isEqualTo(UPDATED_LINE_NUMBER);
        assertThat(testCookOrders.getRequestDate()).isEqualTo(UPDATED_REQUEST_DATE);
        assertThat(testCookOrders.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testCookOrders.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingCookOrders() throws Exception {
        int databaseSizeBeforeUpdate = cookOrdersRepository.findAll().size();
        cookOrders.setId(count.incrementAndGet());

        // Create the CookOrders
        CookOrdersDTO cookOrdersDTO = cookOrdersMapper.toDto(cookOrders);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCookOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cookOrdersDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cookOrdersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CookOrders in the database
        List<CookOrders> cookOrdersList = cookOrdersRepository.findAll();
        assertThat(cookOrdersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCookOrders() throws Exception {
        int databaseSizeBeforeUpdate = cookOrdersRepository.findAll().size();
        cookOrders.setId(count.incrementAndGet());

        // Create the CookOrders
        CookOrdersDTO cookOrdersDTO = cookOrdersMapper.toDto(cookOrders);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCookOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cookOrdersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CookOrders in the database
        List<CookOrders> cookOrdersList = cookOrdersRepository.findAll();
        assertThat(cookOrdersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCookOrders() throws Exception {
        int databaseSizeBeforeUpdate = cookOrdersRepository.findAll().size();
        cookOrders.setId(count.incrementAndGet());

        // Create the CookOrders
        CookOrdersDTO cookOrdersDTO = cookOrdersMapper.toDto(cookOrders);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCookOrdersMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cookOrdersDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CookOrders in the database
        List<CookOrders> cookOrdersList = cookOrdersRepository.findAll();
        assertThat(cookOrdersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCookOrders() throws Exception {
        // Initialize the database
        cookOrdersRepository.saveAndFlush(cookOrders);

        int databaseSizeBeforeDelete = cookOrdersRepository.findAll().size();

        // Delete the cookOrders
        restCookOrdersMockMvc
            .perform(delete(ENTITY_API_URL_ID, cookOrders.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CookOrders> cookOrdersList = cookOrdersRepository.findAll();
        assertThat(cookOrdersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
