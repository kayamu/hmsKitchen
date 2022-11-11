package com.polarbears.capstone.hmskitchen.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmskitchen.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CookOrdersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CookOrders.class);
        CookOrders cookOrders1 = new CookOrders();
        cookOrders1.setId(1L);
        CookOrders cookOrders2 = new CookOrders();
        cookOrders2.setId(cookOrders1.getId());
        assertThat(cookOrders1).isEqualTo(cookOrders2);
        cookOrders2.setId(2L);
        assertThat(cookOrders1).isNotEqualTo(cookOrders2);
        cookOrders1.setId(null);
        assertThat(cookOrders1).isNotEqualTo(cookOrders2);
    }
}
