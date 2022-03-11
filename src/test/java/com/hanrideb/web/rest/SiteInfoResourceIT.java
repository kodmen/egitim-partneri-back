package com.hanrideb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hanrideb.IntegrationTest;
import com.hanrideb.domain.SiteInfo;
import com.hanrideb.repository.SiteInfoRepository;
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

/**
 * Integration tests for the {@link SiteInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SiteInfoResourceIT {

    private static final String DEFAULT_BASLIK = "AAAAAAAAAA";
    private static final String UPDATED_BASLIK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/site-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SiteInfoRepository siteInfoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSiteInfoMockMvc;

    private SiteInfo siteInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SiteInfo createEntity(EntityManager em) {
        SiteInfo siteInfo = new SiteInfo().baslik(DEFAULT_BASLIK);
        return siteInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SiteInfo createUpdatedEntity(EntityManager em) {
        SiteInfo siteInfo = new SiteInfo().baslik(UPDATED_BASLIK);
        return siteInfo;
    }

    @BeforeEach
    public void initTest() {
        siteInfo = createEntity(em);
    }

    @Test
    @Transactional
    void createSiteInfo() throws Exception {
        int databaseSizeBeforeCreate = siteInfoRepository.findAll().size();
        // Create the SiteInfo
        restSiteInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteInfo)))
            .andExpect(status().isCreated());

        // Validate the SiteInfo in the database
        List<SiteInfo> siteInfoList = siteInfoRepository.findAll();
        assertThat(siteInfoList).hasSize(databaseSizeBeforeCreate + 1);
        SiteInfo testSiteInfo = siteInfoList.get(siteInfoList.size() - 1);
        assertThat(testSiteInfo.getBaslik()).isEqualTo(DEFAULT_BASLIK);
    }

    @Test
    @Transactional
    void createSiteInfoWithExistingId() throws Exception {
        // Create the SiteInfo with an existing ID
        siteInfo.setId(1L);

        int databaseSizeBeforeCreate = siteInfoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSiteInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteInfo)))
            .andExpect(status().isBadRequest());

        // Validate the SiteInfo in the database
        List<SiteInfo> siteInfoList = siteInfoRepository.findAll();
        assertThat(siteInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSiteInfos() throws Exception {
        // Initialize the database
        siteInfoRepository.saveAndFlush(siteInfo);

        // Get all the siteInfoList
        restSiteInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(siteInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].baslik").value(hasItem(DEFAULT_BASLIK)));
    }

    @Test
    @Transactional
    void getSiteInfo() throws Exception {
        // Initialize the database
        siteInfoRepository.saveAndFlush(siteInfo);

        // Get the siteInfo
        restSiteInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, siteInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(siteInfo.getId().intValue()))
            .andExpect(jsonPath("$.baslik").value(DEFAULT_BASLIK));
    }

    @Test
    @Transactional
    void getNonExistingSiteInfo() throws Exception {
        // Get the siteInfo
        restSiteInfoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSiteInfo() throws Exception {
        // Initialize the database
        siteInfoRepository.saveAndFlush(siteInfo);

        int databaseSizeBeforeUpdate = siteInfoRepository.findAll().size();

        // Update the siteInfo
        SiteInfo updatedSiteInfo = siteInfoRepository.findById(siteInfo.getId()).get();
        // Disconnect from session so that the updates on updatedSiteInfo are not directly saved in db
        em.detach(updatedSiteInfo);
        updatedSiteInfo.baslik(UPDATED_BASLIK);

        restSiteInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSiteInfo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSiteInfo))
            )
            .andExpect(status().isOk());

        // Validate the SiteInfo in the database
        List<SiteInfo> siteInfoList = siteInfoRepository.findAll();
        assertThat(siteInfoList).hasSize(databaseSizeBeforeUpdate);
        SiteInfo testSiteInfo = siteInfoList.get(siteInfoList.size() - 1);
        assertThat(testSiteInfo.getBaslik()).isEqualTo(UPDATED_BASLIK);
    }

    @Test
    @Transactional
    void putNonExistingSiteInfo() throws Exception {
        int databaseSizeBeforeUpdate = siteInfoRepository.findAll().size();
        siteInfo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, siteInfo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(siteInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteInfo in the database
        List<SiteInfo> siteInfoList = siteInfoRepository.findAll();
        assertThat(siteInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSiteInfo() throws Exception {
        int databaseSizeBeforeUpdate = siteInfoRepository.findAll().size();
        siteInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(siteInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteInfo in the database
        List<SiteInfo> siteInfoList = siteInfoRepository.findAll();
        assertThat(siteInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSiteInfo() throws Exception {
        int databaseSizeBeforeUpdate = siteInfoRepository.findAll().size();
        siteInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteInfoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(siteInfo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SiteInfo in the database
        List<SiteInfo> siteInfoList = siteInfoRepository.findAll();
        assertThat(siteInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSiteInfoWithPatch() throws Exception {
        // Initialize the database
        siteInfoRepository.saveAndFlush(siteInfo);

        int databaseSizeBeforeUpdate = siteInfoRepository.findAll().size();

        // Update the siteInfo using partial update
        SiteInfo partialUpdatedSiteInfo = new SiteInfo();
        partialUpdatedSiteInfo.setId(siteInfo.getId());

        partialUpdatedSiteInfo.baslik(UPDATED_BASLIK);

        restSiteInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSiteInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSiteInfo))
            )
            .andExpect(status().isOk());

        // Validate the SiteInfo in the database
        List<SiteInfo> siteInfoList = siteInfoRepository.findAll();
        assertThat(siteInfoList).hasSize(databaseSizeBeforeUpdate);
        SiteInfo testSiteInfo = siteInfoList.get(siteInfoList.size() - 1);
        assertThat(testSiteInfo.getBaslik()).isEqualTo(UPDATED_BASLIK);
    }

    @Test
    @Transactional
    void fullUpdateSiteInfoWithPatch() throws Exception {
        // Initialize the database
        siteInfoRepository.saveAndFlush(siteInfo);

        int databaseSizeBeforeUpdate = siteInfoRepository.findAll().size();

        // Update the siteInfo using partial update
        SiteInfo partialUpdatedSiteInfo = new SiteInfo();
        partialUpdatedSiteInfo.setId(siteInfo.getId());

        partialUpdatedSiteInfo.baslik(UPDATED_BASLIK);

        restSiteInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSiteInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSiteInfo))
            )
            .andExpect(status().isOk());

        // Validate the SiteInfo in the database
        List<SiteInfo> siteInfoList = siteInfoRepository.findAll();
        assertThat(siteInfoList).hasSize(databaseSizeBeforeUpdate);
        SiteInfo testSiteInfo = siteInfoList.get(siteInfoList.size() - 1);
        assertThat(testSiteInfo.getBaslik()).isEqualTo(UPDATED_BASLIK);
    }

    @Test
    @Transactional
    void patchNonExistingSiteInfo() throws Exception {
        int databaseSizeBeforeUpdate = siteInfoRepository.findAll().size();
        siteInfo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, siteInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(siteInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteInfo in the database
        List<SiteInfo> siteInfoList = siteInfoRepository.findAll();
        assertThat(siteInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSiteInfo() throws Exception {
        int databaseSizeBeforeUpdate = siteInfoRepository.findAll().size();
        siteInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(siteInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the SiteInfo in the database
        List<SiteInfo> siteInfoList = siteInfoRepository.findAll();
        assertThat(siteInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSiteInfo() throws Exception {
        int databaseSizeBeforeUpdate = siteInfoRepository.findAll().size();
        siteInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteInfoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(siteInfo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SiteInfo in the database
        List<SiteInfo> siteInfoList = siteInfoRepository.findAll();
        assertThat(siteInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSiteInfo() throws Exception {
        // Initialize the database
        siteInfoRepository.saveAndFlush(siteInfo);

        int databaseSizeBeforeDelete = siteInfoRepository.findAll().size();

        // Delete the siteInfo
        restSiteInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, siteInfo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SiteInfo> siteInfoList = siteInfoRepository.findAll();
        assertThat(siteInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
