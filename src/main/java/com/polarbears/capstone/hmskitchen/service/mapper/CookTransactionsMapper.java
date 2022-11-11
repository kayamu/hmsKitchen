package com.polarbears.capstone.hmskitchen.service.mapper;

import com.polarbears.capstone.hmskitchen.domain.CookTransactions;
import com.polarbears.capstone.hmskitchen.service.dto.CookTransactionsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CookTransactions} and its DTO {@link CookTransactionsDTO}.
 */
@Mapper(componentModel = "spring")
public interface CookTransactionsMapper extends EntityMapper<CookTransactionsDTO, CookTransactions> {}
