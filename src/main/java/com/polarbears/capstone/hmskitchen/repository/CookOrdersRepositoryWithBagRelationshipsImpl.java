package com.polarbears.capstone.hmskitchen.repository;

import com.polarbears.capstone.hmskitchen.domain.CookOrders;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class CookOrdersRepositoryWithBagRelationshipsImpl implements CookOrdersRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<CookOrders> fetchBagRelationships(Optional<CookOrders> cookOrders) {
        return cookOrders.map(this::fetchCookTransactions);
    }

    @Override
    public Page<CookOrders> fetchBagRelationships(Page<CookOrders> cookOrders) {
        return new PageImpl<>(fetchBagRelationships(cookOrders.getContent()), cookOrders.getPageable(), cookOrders.getTotalElements());
    }

    @Override
    public List<CookOrders> fetchBagRelationships(List<CookOrders> cookOrders) {
        return Optional.of(cookOrders).map(this::fetchCookTransactions).orElse(Collections.emptyList());
    }

    CookOrders fetchCookTransactions(CookOrders result) {
        return entityManager
            .createQuery(
                "select cookOrders from CookOrders cookOrders left join fetch cookOrders.cookTransactions where cookOrders is :cookOrders",
                CookOrders.class
            )
            .setParameter("cookOrders", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<CookOrders> fetchCookTransactions(List<CookOrders> cookOrders) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, cookOrders.size()).forEach(index -> order.put(cookOrders.get(index).getId(), index));
        List<CookOrders> result = entityManager
            .createQuery(
                "select distinct cookOrders from CookOrders cookOrders left join fetch cookOrders.cookTransactions where cookOrders in :cookOrders",
                CookOrders.class
            )
            .setParameter("cookOrders", cookOrders)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
