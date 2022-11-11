package com.polarbears.capstone.hmskitchen.service;

import com.polarbears.capstone.hmskitchen.domain.CookOrders;
import com.polarbears.capstone.hmskitchen.repository.CookOrdersRepository;
import com.polarbears.capstone.hmskitchen.service.dto.CookOrdersDTO;
import com.polarbears.capstone.hmskitchen.service.mapper.CookOrdersMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CookOrders}.
 */
@Service
@Transactional
public class CookOrdersService {

    private final Logger log = LoggerFactory.getLogger(CookOrdersService.class);

    private final CookOrdersRepository cookOrdersRepository;

    private final CookOrdersMapper cookOrdersMapper;

    public CookOrdersService(CookOrdersRepository cookOrdersRepository, CookOrdersMapper cookOrdersMapper) {
        this.cookOrdersRepository = cookOrdersRepository;
        this.cookOrdersMapper = cookOrdersMapper;
    }

    /**
     * Save a cookOrders.
     *
     * @param cookOrdersDTO the entity to save.
     * @return the persisted entity.
     */
    public CookOrdersDTO save(CookOrdersDTO cookOrdersDTO) {
        log.debug("Request to save CookOrders : {}", cookOrdersDTO);
        CookOrders cookOrders = cookOrdersMapper.toEntity(cookOrdersDTO);
        cookOrders = cookOrdersRepository.save(cookOrders);
        return cookOrdersMapper.toDto(cookOrders);
    }

    /**
     * Update a cookOrders.
     *
     * @param cookOrdersDTO the entity to save.
     * @return the persisted entity.
     */
    public CookOrdersDTO update(CookOrdersDTO cookOrdersDTO) {
        log.debug("Request to update CookOrders : {}", cookOrdersDTO);
        CookOrders cookOrders = cookOrdersMapper.toEntity(cookOrdersDTO);
        cookOrders = cookOrdersRepository.save(cookOrders);
        return cookOrdersMapper.toDto(cookOrders);
    }

    /**
     * Partially update a cookOrders.
     *
     * @param cookOrdersDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CookOrdersDTO> partialUpdate(CookOrdersDTO cookOrdersDTO) {
        log.debug("Request to partially update CookOrders : {}", cookOrdersDTO);

        return cookOrdersRepository
            .findById(cookOrdersDTO.getId())
            .map(existingCookOrders -> {
                cookOrdersMapper.partialUpdate(existingCookOrders, cookOrdersDTO);

                return existingCookOrders;
            })
            .map(cookOrdersRepository::save)
            .map(cookOrdersMapper::toDto);
    }

    /**
     * Get all the cookOrders.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CookOrdersDTO> findAll() {
        log.debug("Request to get all CookOrders");
        return cookOrdersRepository.findAll().stream().map(cookOrdersMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the cookOrders with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CookOrdersDTO> findAllWithEagerRelationships(Pageable pageable) {
        return cookOrdersRepository.findAllWithEagerRelationships(pageable).map(cookOrdersMapper::toDto);
    }

    /**
     * Get one cookOrders by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CookOrdersDTO> findOne(Long id) {
        log.debug("Request to get CookOrders : {}", id);
        return cookOrdersRepository.findOneWithEagerRelationships(id).map(cookOrdersMapper::toDto);
    }

    /**
     * Delete the cookOrders by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CookOrders : {}", id);
        cookOrdersRepository.deleteById(id);
    }
}
