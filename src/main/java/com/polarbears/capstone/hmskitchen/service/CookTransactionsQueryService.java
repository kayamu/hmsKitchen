package com.polarbears.capstone.hmskitchen.service;

import com.polarbears.capstone.hmskitchen.domain.*; // for static metamodels
import com.polarbears.capstone.hmskitchen.domain.CookTransactions;
import com.polarbears.capstone.hmskitchen.repository.CookTransactionsRepository;
import com.polarbears.capstone.hmskitchen.service.criteria.CookTransactionsCriteria;
import com.polarbears.capstone.hmskitchen.service.dto.CookTransactionsDTO;
import com.polarbears.capstone.hmskitchen.service.mapper.CookTransactionsMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CookTransactions} entities in the database.
 * The main input is a {@link CookTransactionsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CookTransactionsDTO} or a {@link Page} of {@link CookTransactionsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CookTransactionsQueryService extends QueryService<CookTransactions> {

    private final Logger log = LoggerFactory.getLogger(CookTransactionsQueryService.class);

    private final CookTransactionsRepository cookTransactionsRepository;

    private final CookTransactionsMapper cookTransactionsMapper;

    public CookTransactionsQueryService(
        CookTransactionsRepository cookTransactionsRepository,
        CookTransactionsMapper cookTransactionsMapper
    ) {
        this.cookTransactionsRepository = cookTransactionsRepository;
        this.cookTransactionsMapper = cookTransactionsMapper;
    }

    /**
     * Return a {@link List} of {@link CookTransactionsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CookTransactionsDTO> findByCriteria(CookTransactionsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CookTransactions> specification = createSpecification(criteria);
        return cookTransactionsMapper.toDto(cookTransactionsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CookTransactionsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CookTransactionsDTO> findByCriteria(CookTransactionsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CookTransactions> specification = createSpecification(criteria);
        return cookTransactionsRepository.findAll(specification, page).map(cookTransactionsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CookTransactionsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CookTransactions> specification = createSpecification(criteria);
        return cookTransactionsRepository.count(specification);
    }

    /**
     * Function to convert {@link CookTransactionsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CookTransactions> createSpecification(CookTransactionsCriteria criteria) {
        Specification<CookTransactions> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CookTransactions_.id));
            }
            if (criteria.getKitchenId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getKitchenId(), CookTransactions_.kitchenId));
            }
            if (criteria.getStatusChangedDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getStatusChangedDate(), CookTransactions_.statusChangedDate));
            }
            if (criteria.getTransactionDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTransactionDate(), CookTransactions_.transactionDate));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), CookTransactions_.type));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), CookTransactions_.createdDate));
            }
            if (criteria.getCookOrdersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCookOrdersId(),
                            root -> root.join(CookTransactions_.cookOrders, JoinType.LEFT).get(CookOrders_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
