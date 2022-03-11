package com.hanrideb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hanrideb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MufredatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mufredat.class);
        Mufredat mufredat1 = new Mufredat();
        mufredat1.setId(1L);
        Mufredat mufredat2 = new Mufredat();
        mufredat2.setId(mufredat1.getId());
        assertThat(mufredat1).isEqualTo(mufredat2);
        mufredat2.setId(2L);
        assertThat(mufredat1).isNotEqualTo(mufredat2);
        mufredat1.setId(null);
        assertThat(mufredat1).isNotEqualTo(mufredat2);
    }
}
