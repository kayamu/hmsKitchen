package com.polarbears.capstone.hmskitchen.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CookTransactionsMapperTest {

    private CookTransactionsMapper cookTransactionsMapper;

    @BeforeEach
    public void setUp() {
        cookTransactionsMapper = new CookTransactionsMapperImpl();
    }
}
