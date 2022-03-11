package com.hanrideb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hanrideb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RozetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rozet.class);
        Rozet rozet1 = new Rozet();
        rozet1.setId(1L);
        Rozet rozet2 = new Rozet();
        rozet2.setId(rozet1.getId());
        assertThat(rozet1).isEqualTo(rozet2);
        rozet2.setId(2L);
        assertThat(rozet1).isNotEqualTo(rozet2);
        rozet1.setId(null);
        assertThat(rozet1).isNotEqualTo(rozet2);
    }
}
