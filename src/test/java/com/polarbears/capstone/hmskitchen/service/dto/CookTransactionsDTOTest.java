package com.polarbears.capstone.hmskitchen.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmskitchen.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CookTransactionsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CookTransactionsDTO.class);
        CookTransactionsDTO cookTransactionsDTO1 = new CookTransactionsDTO();
        cookTransactionsDTO1.setId(1L);
        CookTransactionsDTO cookTransactionsDTO2 = new CookTransactionsDTO();
        assertThat(cookTransactionsDTO1).isNotEqualTo(cookTransactionsDTO2);
        cookTransactionsDTO2.setId(cookTransactionsDTO1.getId());
        assertThat(cookTransactionsDTO1).isEqualTo(cookTransactionsDTO2);
        cookTransactionsDTO2.setId(2L);
        assertThat(cookTransactionsDTO1).isNotEqualTo(cookTransactionsDTO2);
        cookTransactionsDTO1.setId(null);
        assertThat(cookTransactionsDTO1).isNotEqualTo(cookTransactionsDTO2);
    }
}
