package com.hanrideb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hanrideb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestAnalizTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestAnaliz.class);
        TestAnaliz testAnaliz1 = new TestAnaliz();
        testAnaliz1.setId(1L);
        TestAnaliz testAnaliz2 = new TestAnaliz();
        testAnaliz2.setId(testAnaliz1.getId());
        assertThat(testAnaliz1).isEqualTo(testAnaliz2);
        testAnaliz2.setId(2L);
        assertThat(testAnaliz1).isNotEqualTo(testAnaliz2);
        testAnaliz1.setId(null);
        assertThat(testAnaliz1).isNotEqualTo(testAnaliz2);
    }
}
