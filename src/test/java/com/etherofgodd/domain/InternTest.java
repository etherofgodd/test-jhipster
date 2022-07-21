package com.etherofgodd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.etherofgodd.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InternTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Intern.class);
        Intern intern1 = new Intern();
        intern1.setId(1L);
        Intern intern2 = new Intern();
        intern2.setId(intern1.getId());
        assertThat(intern1).isEqualTo(intern2);
        intern2.setId(2L);
        assertThat(intern1).isNotEqualTo(intern2);
        intern1.setId(null);
        assertThat(intern1).isNotEqualTo(intern2);
    }
}
