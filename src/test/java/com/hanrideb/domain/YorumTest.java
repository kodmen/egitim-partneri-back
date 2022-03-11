package com.hanrideb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hanrideb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class YorumTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Yorum.class);
        Yorum yorum1 = new Yorum();
        yorum1.setId(1L);
        Yorum yorum2 = new Yorum();
        yorum2.setId(yorum1.getId());
        assertThat(yorum1).isEqualTo(yorum2);
        yorum2.setId(2L);
        assertThat(yorum1).isNotEqualTo(yorum2);
        yorum1.setId(null);
        assertThat(yorum1).isNotEqualTo(yorum2);
    }
}
