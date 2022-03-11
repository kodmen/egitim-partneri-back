package com.hanrideb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hanrideb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SoruKazanimlariTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SoruKazanimlari.class);
        SoruKazanimlari soruKazanimlari1 = new SoruKazanimlari();
        soruKazanimlari1.setId(1L);
        SoruKazanimlari soruKazanimlari2 = new SoruKazanimlari();
        soruKazanimlari2.setId(soruKazanimlari1.getId());
        assertThat(soruKazanimlari1).isEqualTo(soruKazanimlari2);
        soruKazanimlari2.setId(2L);
        assertThat(soruKazanimlari1).isNotEqualTo(soruKazanimlari2);
        soruKazanimlari1.setId(null);
        assertThat(soruKazanimlari1).isNotEqualTo(soruKazanimlari2);
    }
}
