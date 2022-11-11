package com.polarbears.capstone.hmskitchen.service.mapper;

import com.polarbears.capstone.hmskitchen.domain.CookOrders;
import com.polarbears.capstone.hmskitchen.domain.CookTransactions;
import com.polarbears.capstone.hmskitchen.service.dto.CookOrdersDTO;
import com.polarbears.capstone.hmskitchen.service.dto.CookTransactionsDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CookOrders} and its DTO {@link CookOrdersDTO}.
 */
@Mapper(componentModel = "spring")
public interface CookOrdersMapper extends EntityMapper<CookOrdersDTO, CookOrders> {
    @Mapping(target = "cookTransactions", source = "cookTransactions", qualifiedByName = "cookTransactionsIdSet")
    CookOrdersDTO toDto(CookOrders s);

    @Mapping(target = "removeCookTransactions", ignore = true)
    CookOrders toEntity(CookOrdersDTO cookOrdersDTO);

    @Named("cookTransactionsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CookTransactionsDTO toDtoCookTransactionsId(CookTransactions cookTransactions);

    @Named("cookTransactionsIdSet")
    default Set<CookTransactionsDTO> toDtoCookTransactionsIdSet(Set<CookTransactions> cookTransactions) {
        return cookTransactions.stream().map(this::toDtoCookTransactionsId).collect(Collectors.toSet());
    }
}
