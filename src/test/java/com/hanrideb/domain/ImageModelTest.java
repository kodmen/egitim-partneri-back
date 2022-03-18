package com.hanrideb.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hanrideb.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImageModelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImageModel.class);
        ImageModel imageModel1 = new ImageModel();
        imageModel1.setId(1L);
        ImageModel imageModel2 = new ImageModel();
        imageModel2.setId(imageModel1.getId());
        assertThat(imageModel1).isEqualTo(imageModel2);
        imageModel2.setId(2L);
        assertThat(imageModel1).isNotEqualTo(imageModel2);
        imageModel1.setId(null);
        assertThat(imageModel1).isNotEqualTo(imageModel2);
    }
}
