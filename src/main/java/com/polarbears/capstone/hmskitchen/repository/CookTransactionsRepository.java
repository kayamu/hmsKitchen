package com.polarbears.capstone.hmskitchen.repository;

import com.polarbears.capstone.hmskitchen.domain.CookTransactions;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CookTransactions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CookTransactionsRepository extends JpaRepository<CookTransactions, Long>, JpaSpecificationExecutor<CookTransactions> {}
