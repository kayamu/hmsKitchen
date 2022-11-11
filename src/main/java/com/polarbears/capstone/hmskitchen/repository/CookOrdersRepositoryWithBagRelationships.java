package com.polarbears.capstone.hmskitchen.repository;

import com.polarbears.capstone.hmskitchen.domain.CookOrders;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CookOrdersRepositoryWithBagRelationships {
    Optional<CookOrders> fetchBagRelationships(Optional<CookOrders> cookOrders);

    List<CookOrders> fetchBagRelationships(List<CookOrders> cookOrders);

    Page<CookOrders> fetchBagRelationships(Page<CookOrders> cookOrders);
}
