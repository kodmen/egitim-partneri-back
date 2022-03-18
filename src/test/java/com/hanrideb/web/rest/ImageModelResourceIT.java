package com.hanrideb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hanrideb.IntegrationTest;
import com.hanrideb.domain.ImageModel;
import com.hanrideb.repository.ImageModelRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ImageModelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImageModelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMG = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMG = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMG_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMG_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/image-models";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ImageModelRepository imageModelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImageModelMockMvc;

    private ImageModel imageModel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageModel createEntity(EntityManager em) {
        ImageModel imageModel = new ImageModel()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .img(DEFAULT_IMG)
            .imgContentType(DEFAULT_IMG_CONTENT_TYPE);
        return imageModel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageModel createUpdatedEntity(EntityManager em) {
        ImageModel imageModel = new ImageModel()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .img(UPDATED_IMG)
            .imgContentType(UPDATED_IMG_CONTENT_TYPE);
        return imageModel;
    }

    @BeforeEach
    public void initTest() {
        imageModel = createEntity(em);
    }

    @Test
    @Transactional
    void createImageModel() throws Exception {
        int databaseSizeBeforeCreate = imageModelRepository.findAll().size();
        // Create the ImageModel
        restImageModelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageModel)))
            .andExpect(status().isCreated());

        // Validate the ImageModel in the database
        List<ImageModel> imageModelList = imageModelRepository.findAll();
        assertThat(imageModelList).hasSize(databaseSizeBeforeCreate + 1);
        ImageModel testImageModel = imageModelList.get(imageModelList.size() - 1);
        assertThat(testImageModel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testImageModel.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testImageModel.getImg()).isEqualTo(DEFAULT_IMG);
        assertThat(testImageModel.getImgContentType()).isEqualTo(DEFAULT_IMG_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createImageModelWithExistingId() throws Exception {
        // Create the ImageModel with an existing ID
        imageModel.setId(1L);

        int databaseSizeBeforeCreate = imageModelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImageModelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageModel)))
            .andExpect(status().isBadRequest());

        // Validate the ImageModel in the database
        List<ImageModel> imageModelList = imageModelRepository.findAll();
        assertThat(imageModelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllImageModels() throws Exception {
        // Initialize the database
        imageModelRepository.saveAndFlush(imageModel);

        // Get all the imageModelList
        restImageModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].imgContentType").value(hasItem(DEFAULT_IMG_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].img").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMG))));
    }

    @Test
    @Transactional
    void getImageModel() throws Exception {
        // Initialize the database
        imageModelRepository.saveAndFlush(imageModel);

        // Get the imageModel
        restImageModelMockMvc
            .perform(get(ENTITY_API_URL_ID, imageModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(imageModel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.imgContentType").value(DEFAULT_IMG_CONTENT_TYPE))
            .andExpect(jsonPath("$.img").value(Base64Utils.encodeToString(DEFAULT_IMG)));
    }

    @Test
    @Transactional
    void getNonExistingImageModel() throws Exception {
        // Get the imageModel
        restImageModelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewImageModel() throws Exception {
        // Initialize the database
        imageModelRepository.saveAndFlush(imageModel);

        int databaseSizeBeforeUpdate = imageModelRepository.findAll().size();

        // Update the imageModel
        ImageModel updatedImageModel = imageModelRepository.findById(imageModel.getId()).get();
        // Disconnect from session so that the updates on updatedImageModel are not directly saved in db
        em.detach(updatedImageModel);
        updatedImageModel.name(UPDATED_NAME).type(UPDATED_TYPE).img(UPDATED_IMG).imgContentType(UPDATED_IMG_CONTENT_TYPE);

        restImageModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedImageModel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedImageModel))
            )
            .andExpect(status().isOk());

        // Validate the ImageModel in the database
        List<ImageModel> imageModelList = imageModelRepository.findAll();
        assertThat(imageModelList).hasSize(databaseSizeBeforeUpdate);
        ImageModel testImageModel = imageModelList.get(imageModelList.size() - 1);
        assertThat(testImageModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testImageModel.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testImageModel.getImg()).isEqualTo(UPDATED_IMG);
        assertThat(testImageModel.getImgContentType()).isEqualTo(UPDATED_IMG_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingImageModel() throws Exception {
        int databaseSizeBeforeUpdate = imageModelRepository.findAll().size();
        imageModel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imageModel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imageModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageModel in the database
        List<ImageModel> imageModelList = imageModelRepository.findAll();
        assertThat(imageModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImageModel() throws Exception {
        int databaseSizeBeforeUpdate = imageModelRepository.findAll().size();
        imageModel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imageModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageModel in the database
        List<ImageModel> imageModelList = imageModelRepository.findAll();
        assertThat(imageModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImageModel() throws Exception {
        int databaseSizeBeforeUpdate = imageModelRepository.findAll().size();
        imageModel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageModelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageModel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageModel in the database
        List<ImageModel> imageModelList = imageModelRepository.findAll();
        assertThat(imageModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImageModelWithPatch() throws Exception {
        // Initialize the database
        imageModelRepository.saveAndFlush(imageModel);

        int databaseSizeBeforeUpdate = imageModelRepository.findAll().size();

        // Update the imageModel using partial update
        ImageModel partialUpdatedImageModel = new ImageModel();
        partialUpdatedImageModel.setId(imageModel.getId());

        partialUpdatedImageModel.name(UPDATED_NAME).type(UPDATED_TYPE);

        restImageModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImageModel))
            )
            .andExpect(status().isOk());

        // Validate the ImageModel in the database
        List<ImageModel> imageModelList = imageModelRepository.findAll();
        assertThat(imageModelList).hasSize(databaseSizeBeforeUpdate);
        ImageModel testImageModel = imageModelList.get(imageModelList.size() - 1);
        assertThat(testImageModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testImageModel.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testImageModel.getImg()).isEqualTo(DEFAULT_IMG);
        assertThat(testImageModel.getImgContentType()).isEqualTo(DEFAULT_IMG_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateImageModelWithPatch() throws Exception {
        // Initialize the database
        imageModelRepository.saveAndFlush(imageModel);

        int databaseSizeBeforeUpdate = imageModelRepository.findAll().size();

        // Update the imageModel using partial update
        ImageModel partialUpdatedImageModel = new ImageModel();
        partialUpdatedImageModel.setId(imageModel.getId());

        partialUpdatedImageModel.name(UPDATED_NAME).type(UPDATED_TYPE).img(UPDATED_IMG).imgContentType(UPDATED_IMG_CONTENT_TYPE);

        restImageModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImageModel))
            )
            .andExpect(status().isOk());

        // Validate the ImageModel in the database
        List<ImageModel> imageModelList = imageModelRepository.findAll();
        assertThat(imageModelList).hasSize(databaseSizeBeforeUpdate);
        ImageModel testImageModel = imageModelList.get(imageModelList.size() - 1);
        assertThat(testImageModel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testImageModel.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testImageModel.getImg()).isEqualTo(UPDATED_IMG);
        assertThat(testImageModel.getImgContentType()).isEqualTo(UPDATED_IMG_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingImageModel() throws Exception {
        int databaseSizeBeforeUpdate = imageModelRepository.findAll().size();
        imageModel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, imageModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imageModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageModel in the database
        List<ImageModel> imageModelList = imageModelRepository.findAll();
        assertThat(imageModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImageModel() throws Exception {
        int databaseSizeBeforeUpdate = imageModelRepository.findAll().size();
        imageModel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imageModel))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageModel in the database
        List<ImageModel> imageModelList = imageModelRepository.findAll();
        assertThat(imageModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImageModel() throws Exception {
        int databaseSizeBeforeUpdate = imageModelRepository.findAll().size();
        imageModel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageModelMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(imageModel))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageModel in the database
        List<ImageModel> imageModelList = imageModelRepository.findAll();
        assertThat(imageModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImageModel() throws Exception {
        // Initialize the database
        imageModelRepository.saveAndFlush(imageModel);

        int databaseSizeBeforeDelete = imageModelRepository.findAll().size();

        // Delete the imageModel
        restImageModelMockMvc
            .perform(delete(ENTITY_API_URL_ID, imageModel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ImageModel> imageModelList = imageModelRepository.findAll();
        assertThat(imageModelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
