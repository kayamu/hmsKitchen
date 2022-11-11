package com.polarbears.capstone.hmskitchen.web.rest;

import com.polarbears.capstone.hmskitchen.repository.CookOrdersRepository;
import com.polarbears.capstone.hmskitchen.service.CookOrdersQueryService;
import com.polarbears.capstone.hmskitchen.service.CookOrdersService;
import com.polarbears.capstone.hmskitchen.service.criteria.CookOrdersCriteria;
import com.polarbears.capstone.hmskitchen.service.dto.CookOrdersDTO;
import com.polarbears.capstone.hmskitchen.web.rest.errors.BadRequestAlertException;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.polarbears.capstone.hmskitchen.domain.CookOrders}.
 */
@RestController
@RequestMapping("/api")
public class CookOrdersResource {

    private final Logger log = LoggerFactory.getLogger(CookOrdersResource.class);

    private static final String ENTITY_NAME = "hmskitchenCookOrders";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CookOrdersService cookOrdersService;

    private final CookOrdersRepository cookOrdersRepository;

    private final CookOrdersQueryService cookOrdersQueryService;

    public CookOrdersResource(
        CookOrdersService cookOrdersService,
        CookOrdersRepository cookOrdersRepository,
        CookOrdersQueryService cookOrdersQueryService
    ) {
        this.cookOrdersService = cookOrdersService;
        this.cookOrdersRepository = cookOrdersRepository;
        this.cookOrdersQueryService = cookOrdersQueryService;
    }

    /**
     * {@code POST  /cook-orders} : Create a new cookOrders.
     *
     * @param cookOrdersDTO the cookOrdersDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cookOrdersDTO, or with status {@code 400 (Bad Request)} if the cookOrders has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cook-orders")
    public ResponseEntity<CookOrdersDTO> createCookOrders(@Valid @RequestBody CookOrdersDTO cookOrdersDTO) throws URISyntaxException {
        log.debug("REST request to save CookOrders : {}", cookOrdersDTO);
        if (cookOrdersDTO.getId() != null) {
            throw new BadRequestAlertException("A new cookOrders cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CookOrdersDTO result = cookOrdersService.save(cookOrdersDTO);
        return ResponseEntity
            .created(new URI("/api/cook-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cook-orders/:id} : Updates an existing cookOrders.
     *
     * @param id the id of the cookOrdersDTO to save.
     * @param cookOrdersDTO the cookOrdersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cookOrdersDTO,
     * or with status {@code 400 (Bad Request)} if the cookOrdersDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cookOrdersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cook-orders/{id}")
    public ResponseEntity<CookOrdersDTO> updateCookOrders(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CookOrdersDTO cookOrdersDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CookOrders : {}, {}", id, cookOrdersDTO);
        if (cookOrdersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cookOrdersDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cookOrdersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CookOrdersDTO result = cookOrdersService.update(cookOrdersDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cookOrdersDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cook-orders/:id} : Partial updates given fields of an existing cookOrders, field will ignore if it is null
     *
     * @param id the id of the cookOrdersDTO to save.
     * @param cookOrdersDTO the cookOrdersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cookOrdersDTO,
     * or with status {@code 400 (Bad Request)} if the cookOrdersDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cookOrdersDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cookOrdersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cook-orders/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CookOrdersDTO> partialUpdateCookOrders(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CookOrdersDTO cookOrdersDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CookOrders partially : {}, {}", id, cookOrdersDTO);
        if (cookOrdersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cookOrdersDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cookOrdersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CookOrdersDTO> result = cookOrdersService.partialUpdate(cookOrdersDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cookOrdersDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cook-orders} : get all the cookOrders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cookOrders in body.
     */
    @GetMapping("/cook-orders")
    public ResponseEntity<List<CookOrdersDTO>> getAllCookOrders(CookOrdersCriteria criteria) {
        log.debug("REST request to get CookOrders by criteria: {}", criteria);
        List<CookOrdersDTO> entityList = cookOrdersQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /cook-orders/count} : count all the cookOrders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cook-orders/count")
    public ResponseEntity<Long> countCookOrders(CookOrdersCriteria criteria) {
        log.debug("REST request to count CookOrders by criteria: {}", criteria);
        return ResponseEntity.ok().body(cookOrdersQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cook-orders/:id} : get the "id" cookOrders.
     *
     * @param id the id of the cookOrdersDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cookOrdersDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cook-orders/{id}")
    public ResponseEntity<CookOrdersDTO> getCookOrders(@PathVariable Long id) {
        log.debug("REST request to get CookOrders : {}", id);
        Optional<CookOrdersDTO> cookOrdersDTO = cookOrdersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cookOrdersDTO);
    }

    /**
     * {@code DELETE  /cook-orders/:id} : delete the "id" cookOrders.
     *
     * @param id the id of the cookOrdersDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cook-orders/{id}")
    public ResponseEntity<Void> deleteCookOrders(@PathVariable Long id) {
        log.debug("REST request to delete CookOrders : {}", id);
        cookOrdersService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
