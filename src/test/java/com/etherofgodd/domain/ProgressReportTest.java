package com.etherofgodd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.etherofgodd.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProgressReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProgressReport.class);
        ProgressReport progressReport1 = new ProgressReport();
        progressReport1.setId(1L);
        ProgressReport progressReport2 = new ProgressReport();
        progressReport2.setId(progressReport1.getId());
        assertThat(progressReport1).isEqualTo(progressReport2);
        progressReport2.setId(2L);
        assertThat(progressReport1).isNotEqualTo(progressReport2);
        progressReport1.setId(null);
        assertThat(progressReport1).isNotEqualTo(progressReport2);
    }
}
