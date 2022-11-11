package com.polarbears.capstone.hmskitchen.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CookOrdersMapperTest {

    private CookOrdersMapper cookOrdersMapper;

    @BeforeEach
    public void setUp() {
        cookOrdersMapper = new CookOrdersMapperImpl();
    }
}
