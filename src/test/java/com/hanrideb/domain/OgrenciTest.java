package com.hanrideb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hanrideb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OgrenciTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ogrenci.class);
        Ogrenci ogrenci1 = new Ogrenci();
        ogrenci1.setId(1L);
        Ogrenci ogrenci2 = new Ogrenci();
        ogrenci2.setId(ogrenci1.getId());
        assertThat(ogrenci1).isEqualTo(ogrenci2);
        ogrenci2.setId(2L);
        assertThat(ogrenci1).isNotEqualTo(ogrenci2);
        ogrenci1.setId(null);
        assertThat(ogrenci1).isNotEqualTo(ogrenci2);
    }
}
