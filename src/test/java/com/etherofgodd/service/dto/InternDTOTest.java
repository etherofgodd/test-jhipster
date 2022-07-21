package com.etherofgodd.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.etherofgodd.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InternDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InternDTO.class);
        InternDTO internDTO1 = new InternDTO();
        internDTO1.setId(1L);
        InternDTO internDTO2 = new InternDTO();
        assertThat(internDTO1).isNotEqualTo(internDTO2);
        internDTO2.setId(internDTO1.getId());
        assertThat(internDTO1).isEqualTo(internDTO2);
        internDTO2.setId(2L);
        assertThat(internDTO1).isNotEqualTo(internDTO2);
        internDTO1.setId(null);
        assertThat(internDTO1).isNotEqualTo(internDTO2);
    }
}
