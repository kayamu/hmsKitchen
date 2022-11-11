package com.polarbears.capstone.hmskitchen.service;

import com.polarbears.capstone.hmskitchen.domain.CookTransactions;
import com.polarbears.capstone.hmskitchen.repository.CookTransactionsRepository;
import com.polarbears.capstone.hmskitchen.service.dto.CookTransactionsDTO;
import com.polarbears.capstone.hmskitchen.service.mapper.CookTransactionsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CookTransactions}.
 */
@Service
@Transactional
public class CookTransactionsService {

    private final Logger log = LoggerFactory.getLogger(CookTransactionsService.class);

    private final CookTransactionsRepository cookTransactionsRepository;

    private final CookTransactionsMapper cookTransactionsMapper;

    public CookTransactionsService(CookTransactionsRepository cookTransactionsRepository, CookTransactionsMapper cookTransactionsMapper) {
        this.cookTransactionsRepository = cookTransactionsRepository;
        this.cookTransactionsMapper = cookTransactionsMapper;
    }

    /**
     * Save a cookTransactions.
     *
     * @param cookTransactionsDTO the entity to save.
     * @return the persisted entity.
     */
    public CookTransactionsDTO save(CookTransactionsDTO cookTransactionsDTO) {
        log.debug("Request to save CookTransactions : {}", cookTransactionsDTO);
        CookTransactions cookTransactions = cookTransactionsMapper.toEntity(cookTransactionsDTO);
        cookTransactions = cookTransactionsRepository.save(cookTransactions);
        return cookTransactionsMapper.toDto(cookTransactions);
    }

    /**
     * Update a cookTransactions.
     *
     * @param cookTransactionsDTO the entity to save.
     * @return the persisted entity.
     */
    public CookTransactionsDTO update(CookTransactionsDTO cookTransactionsDTO) {
        log.debug("Request to update CookTransactions : {}", cookTransactionsDTO);
        CookTransactions cookTransactions = cookTransactionsMapper.toEntity(cookTransactionsDTO);
        cookTransactions = cookTransactionsRepository.save(cookTransactions);
        return cookTransactionsMapper.toDto(cookTransactions);
    }

    /**
     * Partially update a cookTransactions.
     *
     * @param cookTransactionsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CookTransactionsDTO> partialUpdate(CookTransactionsDTO cookTransactionsDTO) {
        log.debug("Request to partially update CookTransactions : {}", cookTransactionsDTO);

        return cookTransactionsRepository
            .findById(cookTransactionsDTO.getId())
            .map(existingCookTransactions -> {
                cookTransactionsMapper.partialUpdate(existingCookTransactions, cookTransactionsDTO);

                return existingCookTransactions;
            })
            .map(cookTransactionsRepository::save)
            .map(cookTransactionsMapper::toDto);
    }

    /**
     * Get all the cookTransactions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CookTransactionsDTO> findAll() {
        log.debug("Request to get all CookTransactions");
        return cookTransactionsRepository
            .findAll()
            .stream()
            .map(cookTransactionsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one cookTransactions by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CookTransactionsDTO> findOne(Long id) {
        log.debug("Request to get CookTransactions : {}", id);
        return cookTransactionsRepository.findById(id).map(cookTransactionsMapper::toDto);
    }

    /**
     * Delete the cookTransactions by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CookTransactions : {}", id);
        cookTransactionsRepository.deleteById(id);
    }
}
