package com.polarbears.capstone.hmskitchen.web.rest;

import com.polarbears.capstone.hmskitchen.repository.CookTransactionsRepository;
import com.polarbears.capstone.hmskitchen.service.CookTransactionsQueryService;
import com.polarbears.capstone.hmskitchen.service.CookTransactionsService;
import com.polarbears.capstone.hmskitchen.service.criteria.CookTransactionsCriteria;
import com.polarbears.capstone.hmskitchen.service.dto.CookTransactionsDTO;
import com.polarbears.capstone.hmskitchen.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.polarbears.capstone.hmskitchen.domain.CookTransactions}.
 */
@RestController
@RequestMapping("/api")
public class CookTransactionsResource {

    private final Logger log = LoggerFactory.getLogger(CookTransactionsResource.class);

    private static final String ENTITY_NAME = "hmskitchenCookTransactions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CookTransactionsService cookTransactionsService;

    private final CookTransactionsRepository cookTransactionsRepository;

    private final CookTransactionsQueryService cookTransactionsQueryService;

    public CookTransactionsResource(
        CookTransactionsService cookTransactionsService,
        CookTransactionsRepository cookTransactionsRepository,
        CookTransactionsQueryService cookTransactionsQueryService
    ) {
        this.cookTransactionsService = cookTransactionsService;
        this.cookTransactionsRepository = cookTransactionsRepository;
        this.cookTransactionsQueryService = cookTransactionsQueryService;
    }

    /**
     * {@code POST  /cook-transactions} : Create a new cookTransactions.
     *
     * @param cookTransactionsDTO the cookTransactionsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cookTransactionsDTO, or with status {@code 400 (Bad Request)} if the cookTransactions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cook-transactions")
    public ResponseEntity<CookTransactionsDTO> createCookTransactions(@RequestBody CookTransactionsDTO cookTransactionsDTO)
        throws URISyntaxException {
        log.debug("REST request to save CookTransactions : {}", cookTransactionsDTO);
        if (cookTransactionsDTO.getId() != null) {
            throw new BadRequestAlertException("A new cookTransactions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CookTransactionsDTO result = cookTransactionsService.save(cookTransactionsDTO);
        return ResponseEntity
            .created(new URI("/api/cook-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cook-transactions/:id} : Updates an existing cookTransactions.
     *
     * @param id the id of the cookTransactionsDTO to save.
     * @param cookTransactionsDTO the cookTransactionsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cookTransactionsDTO,
     * or with status {@code 400 (Bad Request)} if the cookTransactionsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cookTransactionsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cook-transactions/{id}")
    public ResponseEntity<CookTransactionsDTO> updateCookTransactions(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CookTransactionsDTO cookTransactionsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CookTransactions : {}, {}", id, cookTransactionsDTO);
        if (cookTransactionsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cookTransactionsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cookTransactionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CookTransactionsDTO result = cookTransactionsService.update(cookTransactionsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cookTransactionsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cook-transactions/:id} : Partial updates given fields of an existing cookTransactions, field will ignore if it is null
     *
     * @param id the id of the cookTransactionsDTO to save.
     * @param cookTransactionsDTO the cookTransactionsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cookTransactionsDTO,
     * or with status {@code 400 (Bad Request)} if the cookTransactionsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cookTransactionsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cookTransactionsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cook-transactions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CookTransactionsDTO> partialUpdateCookTransactions(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CookTransactionsDTO cookTransactionsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CookTransactions partially : {}, {}", id, cookTransactionsDTO);
        if (cookTransactionsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cookTransactionsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cookTransactionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CookTransactionsDTO> result = cookTransactionsService.partialUpdate(cookTransactionsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cookTransactionsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cook-transactions} : get all the cookTransactions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cookTransactions in body.
     */
    @GetMapping("/cook-transactions")
    public ResponseEntity<List<CookTransactionsDTO>> getAllCookTransactions(CookTransactionsCriteria criteria) {
        log.debug("REST request to get CookTransactions by criteria: {}", criteria);
        List<CookTransactionsDTO> entityList = cookTransactionsQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /cook-transactions/count} : count all the cookTransactions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cook-transactions/count")
    public ResponseEntity<Long> countCookTransactions(CookTransactionsCriteria criteria) {
        log.debug("REST request to count CookTransactions by criteria: {}", criteria);
        return ResponseEntity.ok().body(cookTransactionsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cook-transactions/:id} : get the "id" cookTransactions.
     *
     * @param id the id of the cookTransactionsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cookTransactionsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cook-transactions/{id}")
    public ResponseEntity<CookTransactionsDTO> getCookTransactions(@PathVariable Long id) {
        log.debug("REST request to get CookTransactions : {}", id);
        Optional<CookTransactionsDTO> cookTransactionsDTO = cookTransactionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cookTransactionsDTO);
    }

    /**
     * {@code DELETE  /cook-transactions/:id} : delete the "id" cookTransactions.
     *
     * @param id the id of the cookTransactionsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cook-transactions/{id}")
    public ResponseEntity<Void> deleteCookTransactions(@PathVariable Long id) {
        log.debug("REST request to delete CookTransactions : {}", id);
        cookTransactionsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
