package com.polarbears.capstone.hmskitchen.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.polarbears.capstone.hmskitchen.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CookOrdersDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CookOrdersDTO.class);
        CookOrdersDTO cookOrdersDTO1 = new CookOrdersDTO();
        cookOrdersDTO1.setId(1L);
        CookOrdersDTO cookOrdersDTO2 = new CookOrdersDTO();
        assertThat(cookOrdersDTO1).isNotEqualTo(cookOrdersDTO2);
        cookOrdersDTO2.setId(cookOrdersDTO1.getId());
        assertThat(cookOrdersDTO1).isEqualTo(cookOrdersDTO2);
        cookOrdersDTO2.setId(2L);
        assertThat(cookOrdersDTO1).isNotEqualTo(cookOrdersDTO2);
        cookOrdersDTO1.setId(null);
        assertThat(cookOrdersDTO1).isNotEqualTo(cookOrdersDTO2);
    }
}
