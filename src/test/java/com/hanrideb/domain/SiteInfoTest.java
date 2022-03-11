package com.hanrideb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hanrideb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SiteInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SiteInfo.class);
        SiteInfo siteInfo1 = new SiteInfo();
        siteInfo1.setId(1L);
        SiteInfo siteInfo2 = new SiteInfo();
        siteInfo2.setId(siteInfo1.getId());
        assertThat(siteInfo1).isEqualTo(siteInfo2);
        siteInfo2.setId(2L);
        assertThat(siteInfo1).isNotEqualTo(siteInfo2);
        siteInfo1.setId(null);
        assertThat(siteInfo1).isNotEqualTo(siteInfo2);
    }
}
