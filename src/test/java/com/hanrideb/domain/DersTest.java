package com.hanrideb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hanrideb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ders.class);
        Ders ders1 = new Ders();
        ders1.setId(1L);
        Ders ders2 = new Ders();
        ders2.setId(ders1.getId());
        assertThat(ders1).isEqualTo(ders2);
        ders2.setId(2L);
        assertThat(ders1).isNotEqualTo(ders2);
        ders1.setId(null);
        assertThat(ders1).isNotEqualTo(ders2);
    }
}
