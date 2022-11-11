package com.polarbears.capstone.hmskitchen.service;

import com.polarbears.capstone.hmskitchen.domain.*; // for static metamodels
import com.polarbears.capstone.hmskitchen.domain.CookOrders;
import com.polarbears.capstone.hmskitchen.repository.CookOrdersRepository;
import com.polarbears.capstone.hmskitchen.service.criteria.CookOrdersCriteria;
import com.polarbears.capstone.hmskitchen.service.dto.CookOrdersDTO;
import com.polarbears.capstone.hmskitchen.service.mapper.CookOrdersMapper;
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
 * Service for executing complex queries for {@link CookOrders} entities in the database.
 * The main input is a {@link CookOrdersCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CookOrdersDTO} or a {@link Page} of {@link CookOrdersDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CookOrdersQueryService extends QueryService<CookOrders> {

    private final Logger log = LoggerFactory.getLogger(CookOrdersQueryService.class);

    private final CookOrdersRepository cookOrdersRepository;

    private final CookOrdersMapper cookOrdersMapper;

    public CookOrdersQueryService(CookOrdersRepository cookOrdersRepository, CookOrdersMapper cookOrdersMapper) {
        this.cookOrdersRepository = cookOrdersRepository;
        this.cookOrdersMapper = cookOrdersMapper;
    }

    /**
     * Return a {@link List} of {@link CookOrdersDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CookOrdersDTO> findByCriteria(CookOrdersCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CookOrders> specification = createSpecification(criteria);
        return cookOrdersMapper.toDto(cookOrdersRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CookOrdersDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CookOrdersDTO> findByCriteria(CookOrdersCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CookOrders> specification = createSpecification(criteria);
        return cookOrdersRepository.findAll(specification, page).map(cookOrdersMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CookOrdersCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CookOrders> specification = createSpecification(criteria);
        return cookOrdersRepository.count(specification);
    }

    /**
     * Function to convert {@link CookOrdersCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CookOrders> createSpecification(CookOrdersCriteria criteria) {
        Specification<CookOrders> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CookOrders_.id));
            }
            if (criteria.getKitchenId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getKitchenId(), CookOrders_.kitchenId));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCustomerId(), CookOrders_.customerId));
            }
            if (criteria.getCustomerCartId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCustomerCartId(), CookOrders_.customerCartId));
            }
            if (criteria.getMenuItemId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMenuItemId(), CookOrders_.menuItemId));
            }
            if (criteria.getMenuItemName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMenuItemName(), CookOrders_.menuItemName));
            }
            if (criteria.getMenuItemCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMenuItemCode(), CookOrders_.menuItemCode));
            }
            if (criteria.getMealId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMealId(), CookOrders_.mealId));
            }
            if (criteria.getLineNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLineNumber(), CookOrders_.lineNumber));
            }
            if (criteria.getRequestDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRequestDate(), CookOrders_.requestDate));
            }
            if (criteria.getMessage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMessage(), CookOrders_.message));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), CookOrders_.createdDate));
            }
            if (criteria.getCookTransactionsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCookTransactionsId(),
                            root -> root.join(CookOrders_.cookTransactions, JoinType.LEFT).get(CookTransactions_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
