package com.etherofgodd.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.etherofgodd.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProgressReportDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProgressReportDTO.class);
        ProgressReportDTO progressReportDTO1 = new ProgressReportDTO();
        progressReportDTO1.setId(1L);
        ProgressReportDTO progressReportDTO2 = new ProgressReportDTO();
        assertThat(progressReportDTO1).isNotEqualTo(progressReportDTO2);
        progressReportDTO2.setId(progressReportDTO1.getId());
        assertThat(progressReportDTO1).isEqualTo(progressReportDTO2);
        progressReportDTO2.setId(2L);
        assertThat(progressReportDTO1).isNotEqualTo(progressReportDTO2);
        progressReportDTO1.setId(null);
        assertThat(progressReportDTO1).isNotEqualTo(progressReportDTO2);
    }
}
