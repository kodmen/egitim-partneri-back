package com.hanrideb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hanrideb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class KayitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Kayit.class);
        Kayit kayit1 = new Kayit();
        kayit1.setId(1L);
        Kayit kayit2 = new Kayit();
        kayit2.setId(kayit1.getId());
        assertThat(kayit1).isEqualTo(kayit2);
        kayit2.setId(2L);
        assertThat(kayit1).isNotEqualTo(kayit2);
        kayit1.setId(null);
        assertThat(kayit1).isNotEqualTo(kayit2);
    }
}
