package com.hanrideb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hanrideb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DersAnalizTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DersAnaliz.class);
        DersAnaliz dersAnaliz1 = new DersAnaliz();
        dersAnaliz1.setId(1L);
        DersAnaliz dersAnaliz2 = new DersAnaliz();
        dersAnaliz2.setId(dersAnaliz1.getId());
        assertThat(dersAnaliz1).isEqualTo(dersAnaliz2);
        dersAnaliz2.setId(2L);
        assertThat(dersAnaliz1).isNotEqualTo(dersAnaliz2);
        dersAnaliz1.setId(null);
        assertThat(dersAnaliz1).isNotEqualTo(dersAnaliz2);
    }
}
