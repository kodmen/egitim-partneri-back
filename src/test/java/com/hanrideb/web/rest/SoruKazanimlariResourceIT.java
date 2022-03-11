package com.hanrideb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hanrideb.IntegrationTest;
import com.hanrideb.domain.SoruKazanimlari;
import com.hanrideb.repository.SoruKazanimlariRepository;
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
 * Integration tests for the {@link SoruKazanimlariResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SoruKazanimlariResourceIT {

    private static final String DEFAULT_KAZANIM = "AAAAAAAAAA";
    private static final String UPDATED_KAZANIM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/soru-kazanimlaris";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SoruKazanimlariRepository soruKazanimlariRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSoruKazanimlariMockMvc;

    private SoruKazanimlari soruKazanimlari;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SoruKazanimlari createEntity(EntityManager em) {
        SoruKazanimlari soruKazanimlari = new SoruKazanimlari().kazanim(DEFAULT_KAZANIM);
        return soruKazanimlari;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SoruKazanimlari createUpdatedEntity(EntityManager em) {
        SoruKazanimlari soruKazanimlari = new SoruKazanimlari().kazanim(UPDATED_KAZANIM);
        return soruKazanimlari;
    }

    @BeforeEach
    public void initTest() {
        soruKazanimlari = createEntity(em);
    }

    @Test
    @Transactional
    void createSoruKazanimlari() throws Exception {
        int databaseSizeBeforeCreate = soruKazanimlariRepository.findAll().size();
        // Create the SoruKazanimlari
        restSoruKazanimlariMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(soruKazanimlari))
            )
            .andExpect(status().isCreated());

        // Validate the SoruKazanimlari in the database
        List<SoruKazanimlari> soruKazanimlariList = soruKazanimlariRepository.findAll();
        assertThat(soruKazanimlariList).hasSize(databaseSizeBeforeCreate + 1);
        SoruKazanimlari testSoruKazanimlari = soruKazanimlariList.get(soruKazanimlariList.size() - 1);
        assertThat(testSoruKazanimlari.getKazanim()).isEqualTo(DEFAULT_KAZANIM);
    }

    @Test
    @Transactional
    void createSoruKazanimlariWithExistingId() throws Exception {
        // Create the SoruKazanimlari with an existing ID
        soruKazanimlari.setId(1L);

        int databaseSizeBeforeCreate = soruKazanimlariRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSoruKazanimlariMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(soruKazanimlari))
            )
            .andExpect(status().isBadRequest());

        // Validate the SoruKazanimlari in the database
        List<SoruKazanimlari> soruKazanimlariList = soruKazanimlariRepository.findAll();
        assertThat(soruKazanimlariList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSoruKazanimlaris() throws Exception {
        // Initialize the database
        soruKazanimlariRepository.saveAndFlush(soruKazanimlari);

        // Get all the soruKazanimlariList
        restSoruKazanimlariMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(soruKazanimlari.getId().intValue())))
            .andExpect(jsonPath("$.[*].kazanim").value(hasItem(DEFAULT_KAZANIM)));
    }

    @Test
    @Transactional
    void getSoruKazanimlari() throws Exception {
        // Initialize the database
        soruKazanimlariRepository.saveAndFlush(soruKazanimlari);

        // Get the soruKazanimlari
        restSoruKazanimlariMockMvc
            .perform(get(ENTITY_API_URL_ID, soruKazanimlari.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(soruKazanimlari.getId().intValue()))
            .andExpect(jsonPath("$.kazanim").value(DEFAULT_KAZANIM));
    }

    @Test
    @Transactional
    void getNonExistingSoruKazanimlari() throws Exception {
        // Get the soruKazanimlari
        restSoruKazanimlariMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSoruKazanimlari() throws Exception {
        // Initialize the database
        soruKazanimlariRepository.saveAndFlush(soruKazanimlari);

        int databaseSizeBeforeUpdate = soruKazanimlariRepository.findAll().size();

        // Update the soruKazanimlari
        SoruKazanimlari updatedSoruKazanimlari = soruKazanimlariRepository.findById(soruKazanimlari.getId()).get();
        // Disconnect from session so that the updates on updatedSoruKazanimlari are not directly saved in db
        em.detach(updatedSoruKazanimlari);
        updatedSoruKazanimlari.kazanim(UPDATED_KAZANIM);

        restSoruKazanimlariMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSoruKazanimlari.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSoruKazanimlari))
            )
            .andExpect(status().isOk());

        // Validate the SoruKazanimlari in the database
        List<SoruKazanimlari> soruKazanimlariList = soruKazanimlariRepository.findAll();
        assertThat(soruKazanimlariList).hasSize(databaseSizeBeforeUpdate);
        SoruKazanimlari testSoruKazanimlari = soruKazanimlariList.get(soruKazanimlariList.size() - 1);
        assertThat(testSoruKazanimlari.getKazanim()).isEqualTo(UPDATED_KAZANIM);
    }

    @Test
    @Transactional
    void putNonExistingSoruKazanimlari() throws Exception {
        int databaseSizeBeforeUpdate = soruKazanimlariRepository.findAll().size();
        soruKazanimlari.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSoruKazanimlariMockMvc
            .perform(
                put(ENTITY_API_URL_ID, soruKazanimlari.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(soruKazanimlari))
            )
            .andExpect(status().isBadRequest());

        // Validate the SoruKazanimlari in the database
        List<SoruKazanimlari> soruKazanimlariList = soruKazanimlariRepository.findAll();
        assertThat(soruKazanimlariList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSoruKazanimlari() throws Exception {
        int databaseSizeBeforeUpdate = soruKazanimlariRepository.findAll().size();
        soruKazanimlari.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoruKazanimlariMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(soruKazanimlari))
            )
            .andExpect(status().isBadRequest());

        // Validate the SoruKazanimlari in the database
        List<SoruKazanimlari> soruKazanimlariList = soruKazanimlariRepository.findAll();
        assertThat(soruKazanimlariList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSoruKazanimlari() throws Exception {
        int databaseSizeBeforeUpdate = soruKazanimlariRepository.findAll().size();
        soruKazanimlari.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoruKazanimlariMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(soruKazanimlari))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SoruKazanimlari in the database
        List<SoruKazanimlari> soruKazanimlariList = soruKazanimlariRepository.findAll();
        assertThat(soruKazanimlariList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSoruKazanimlariWithPatch() throws Exception {
        // Initialize the database
        soruKazanimlariRepository.saveAndFlush(soruKazanimlari);

        int databaseSizeBeforeUpdate = soruKazanimlariRepository.findAll().size();

        // Update the soruKazanimlari using partial update
        SoruKazanimlari partialUpdatedSoruKazanimlari = new SoruKazanimlari();
        partialUpdatedSoruKazanimlari.setId(soruKazanimlari.getId());

        partialUpdatedSoruKazanimlari.kazanim(UPDATED_KAZANIM);

        restSoruKazanimlariMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSoruKazanimlari.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSoruKazanimlari))
            )
            .andExpect(status().isOk());

        // Validate the SoruKazanimlari in the database
        List<SoruKazanimlari> soruKazanimlariList = soruKazanimlariRepository.findAll();
        assertThat(soruKazanimlariList).hasSize(databaseSizeBeforeUpdate);
        SoruKazanimlari testSoruKazanimlari = soruKazanimlariList.get(soruKazanimlariList.size() - 1);
        assertThat(testSoruKazanimlari.getKazanim()).isEqualTo(UPDATED_KAZANIM);
    }

    @Test
    @Transactional
    void fullUpdateSoruKazanimlariWithPatch() throws Exception {
        // Initialize the database
        soruKazanimlariRepository.saveAndFlush(soruKazanimlari);

        int databaseSizeBeforeUpdate = soruKazanimlariRepository.findAll().size();

        // Update the soruKazanimlari using partial update
        SoruKazanimlari partialUpdatedSoruKazanimlari = new SoruKazanimlari();
        partialUpdatedSoruKazanimlari.setId(soruKazanimlari.getId());

        partialUpdatedSoruKazanimlari.kazanim(UPDATED_KAZANIM);

        restSoruKazanimlariMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSoruKazanimlari.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSoruKazanimlari))
            )
            .andExpect(status().isOk());

        // Validate the SoruKazanimlari in the database
        List<SoruKazanimlari> soruKazanimlariList = soruKazanimlariRepository.findAll();
        assertThat(soruKazanimlariList).hasSize(databaseSizeBeforeUpdate);
        SoruKazanimlari testSoruKazanimlari = soruKazanimlariList.get(soruKazanimlariList.size() - 1);
        assertThat(testSoruKazanimlari.getKazanim()).isEqualTo(UPDATED_KAZANIM);
    }

    @Test
    @Transactional
    void patchNonExistingSoruKazanimlari() throws Exception {
        int databaseSizeBeforeUpdate = soruKazanimlariRepository.findAll().size();
        soruKazanimlari.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSoruKazanimlariMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, soruKazanimlari.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(soruKazanimlari))
            )
            .andExpect(status().isBadRequest());

        // Validate the SoruKazanimlari in the database
        List<SoruKazanimlari> soruKazanimlariList = soruKazanimlariRepository.findAll();
        assertThat(soruKazanimlariList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSoruKazanimlari() throws Exception {
        int databaseSizeBeforeUpdate = soruKazanimlariRepository.findAll().size();
        soruKazanimlari.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoruKazanimlariMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(soruKazanimlari))
            )
            .andExpect(status().isBadRequest());

        // Validate the SoruKazanimlari in the database
        List<SoruKazanimlari> soruKazanimlariList = soruKazanimlariRepository.findAll();
        assertThat(soruKazanimlariList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSoruKazanimlari() throws Exception {
        int databaseSizeBeforeUpdate = soruKazanimlariRepository.findAll().size();
        soruKazanimlari.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSoruKazanimlariMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(soruKazanimlari))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SoruKazanimlari in the database
        List<SoruKazanimlari> soruKazanimlariList = soruKazanimlariRepository.findAll();
        assertThat(soruKazanimlariList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSoruKazanimlari() throws Exception {
        // Initialize the database
        soruKazanimlariRepository.saveAndFlush(soruKazanimlari);

        int databaseSizeBeforeDelete = soruKazanimlariRepository.findAll().size();

        // Delete the soruKazanimlari
        restSoruKazanimlariMockMvc
            .perform(delete(ENTITY_API_URL_ID, soruKazanimlari.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SoruKazanimlari> soruKazanimlariList = soruKazanimlariRepository.findAll();
        assertThat(soruKazanimlariList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
