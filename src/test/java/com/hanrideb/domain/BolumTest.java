package com.hanrideb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hanrideb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BolumTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bolum.class);
        Bolum bolum1 = new Bolum();
        bolum1.setId(1L);
        Bolum bolum2 = new Bolum();
        bolum2.setId(bolum1.getId());
        assertThat(bolum1).isEqualTo(bolum2);
        bolum2.setId(2L);
        assertThat(bolum1).isNotEqualTo(bolum2);
        bolum1.setId(null);
        assertThat(bolum1).isNotEqualTo(bolum2);
    }
}
