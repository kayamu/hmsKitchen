package com.polarbears.capstone.hmskitchen.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmskitchen.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CookTransactionsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CookTransactions.class);
        CookTransactions cookTransactions1 = new CookTransactions();
        cookTransactions1.setId(1L);
        CookTransactions cookTransactions2 = new CookTransactions();
        cookTransactions2.setId(cookTransactions1.getId());
        assertThat(cookTransactions1).isEqualTo(cookTransactions2);
        cookTransactions2.setId(2L);
        assertThat(cookTransactions1).isNotEqualTo(cookTransactions2);
        cookTransactions1.setId(null);
        assertThat(cookTransactions1).isNotEqualTo(cookTransactions2);
    }
}
