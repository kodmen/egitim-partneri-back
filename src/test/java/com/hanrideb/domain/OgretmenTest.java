package com.hanrideb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hanrideb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OgretmenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ogretmen.class);
        Ogretmen ogretmen1 = new Ogretmen();
        ogretmen1.setId(1L);
        Ogretmen ogretmen2 = new Ogretmen();
        ogretmen2.setId(ogretmen1.getId());
        assertThat(ogretmen1).isEqualTo(ogretmen2);
        ogretmen2.setId(2L);
        assertThat(ogretmen1).isNotEqualTo(ogretmen2);
        ogretmen1.setId(null);
        assertThat(ogretmen1).isNotEqualTo(ogretmen2);
    }
}
