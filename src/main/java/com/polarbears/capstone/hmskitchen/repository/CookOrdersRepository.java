package com.polarbears.capstone.hmskitchen.repository;

import com.polarbears.capstone.hmskitchen.domain.CookOrders;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CookOrders entity.
 *
 * When extending this class, extend CookOrdersRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface CookOrdersRepository
    extends CookOrdersRepositoryWithBagRelationships, JpaRepository<CookOrders, Long>, JpaSpecificationExecutor<CookOrders> {
    default Optional<CookOrders> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<CookOrders> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<CookOrders> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
