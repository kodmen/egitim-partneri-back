package com.hanrideb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hanrideb.IntegrationTest;
import com.hanrideb.domain.Bolum;
import com.hanrideb.repository.BolumRepository;
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
 * Integration tests for the {@link BolumResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BolumResourceIT {

    private static final String DEFAULT_BOLUM_BASLIK = "AAAAAAAAAA";
    private static final String UPDATED_BOLUM_BASLIK = "BBBBBBBBBB";

    private static final String DEFAULT_DOKUMAN = "AAAAAAAAAA";
    private static final String UPDATED_DOKUMAN = "BBBBBBBBBB";

    private static final Integer DEFAULT_PUAN = 1;
    private static final Integer UPDATED_PUAN = 2;

    private static final String DEFAULT_VIDEO_LINK = "AAAAAAAAAA";
    private static final String UPDATED_VIDEO_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_SURE = "AAAAAAAAAA";
    private static final String UPDATED_SURE = "BBBBBBBBBB";

    private static final Integer DEFAULT_SIRA = 1;
    private static final Integer UPDATED_SIRA = 2;

    private static final String ENTITY_API_URL = "/api/bolums";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BolumRepository bolumRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBolumMockMvc;

    private Bolum bolum;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bolum createEntity(EntityManager em) {
        Bolum bolum = new Bolum()
            .bolumBaslik(DEFAULT_BOLUM_BASLIK)
            .dokuman(DEFAULT_DOKUMAN)
            .puan(DEFAULT_PUAN)
            .videoLink(DEFAULT_VIDEO_LINK)
            .sure(DEFAULT_SURE)
            .sira(DEFAULT_SIRA);
        return bolum;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bolum createUpdatedEntity(EntityManager em) {
        Bolum bolum = new Bolum()
            .bolumBaslik(UPDATED_BOLUM_BASLIK)
            .dokuman(UPDATED_DOKUMAN)
            .puan(UPDATED_PUAN)
            .videoLink(UPDATED_VIDEO_LINK)
            .sure(UPDATED_SURE)
            .sira(UPDATED_SIRA);
        return bolum;
    }

    @BeforeEach
    public void initTest() {
        bolum = createEntity(em);
    }

    @Test
    @Transactional
    void createBolum() throws Exception {
        int databaseSizeBeforeCreate = bolumRepository.findAll().size();
        // Create the Bolum
        restBolumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bolum)))
            .andExpect(status().isCreated());

        // Validate the Bolum in the database
        List<Bolum> bolumList = bolumRepository.findAll();
        assertThat(bolumList).hasSize(databaseSizeBeforeCreate + 1);
        Bolum testBolum = bolumList.get(bolumList.size() - 1);
        assertThat(testBolum.getBolumBaslik()).isEqualTo(DEFAULT_BOLUM_BASLIK);
        assertThat(testBolum.getDokuman()).isEqualTo(DEFAULT_DOKUMAN);
        assertThat(testBolum.getPuan()).isEqualTo(DEFAULT_PUAN);
        assertThat(testBolum.getVideoLink()).isEqualTo(DEFAULT_VIDEO_LINK);
        assertThat(testBolum.getSure()).isEqualTo(DEFAULT_SURE);
        assertThat(testBolum.getSira()).isEqualTo(DEFAULT_SIRA);
    }

    @Test
    @Transactional
    void createBolumWithExistingId() throws Exception {
        // Create the Bolum with an existing ID
        bolum.setId(1L);

        int databaseSizeBeforeCreate = bolumRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBolumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bolum)))
            .andExpect(status().isBadRequest());

        // Validate the Bolum in the database
        List<Bolum> bolumList = bolumRepository.findAll();
        assertThat(bolumList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBolums() throws Exception {
        // Initialize the database
        bolumRepository.saveAndFlush(bolum);

        // Get all the bolumList
        restBolumMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bolum.getId().intValue())))
            .andExpect(jsonPath("$.[*].bolumBaslik").value(hasItem(DEFAULT_BOLUM_BASLIK)))
            .andExpect(jsonPath("$.[*].dokuman").value(hasItem(DEFAULT_DOKUMAN.toString())))
            .andExpect(jsonPath("$.[*].puan").value(hasItem(DEFAULT_PUAN)))
            .andExpect(jsonPath("$.[*].videoLink").value(hasItem(DEFAULT_VIDEO_LINK)))
            .andExpect(jsonPath("$.[*].sure").value(hasItem(DEFAULT_SURE)))
            .andExpect(jsonPath("$.[*].sira").value(hasItem(DEFAULT_SIRA)));
    }

    @Test
    @Transactional
    void getBolum() throws Exception {
        // Initialize the database
        bolumRepository.saveAndFlush(bolum);

        // Get the bolum
        restBolumMockMvc
            .perform(get(ENTITY_API_URL_ID, bolum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bolum.getId().intValue()))
            .andExpect(jsonPath("$.bolumBaslik").value(DEFAULT_BOLUM_BASLIK))
            .andExpect(jsonPath("$.dokuman").value(DEFAULT_DOKUMAN.toString()))
            .andExpect(jsonPath("$.puan").value(DEFAULT_PUAN))
            .andExpect(jsonPath("$.videoLink").value(DEFAULT_VIDEO_LINK))
            .andExpect(jsonPath("$.sure").value(DEFAULT_SURE))
            .andExpect(jsonPath("$.sira").value(DEFAULT_SIRA));
    }

    @Test
    @Transactional
    void getNonExistingBolum() throws Exception {
        // Get the bolum
        restBolumMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBolum() throws Exception {
        // Initialize the database
        bolumRepository.saveAndFlush(bolum);

        int databaseSizeBeforeUpdate = bolumRepository.findAll().size();

        // Update the bolum
        Bolum updatedBolum = bolumRepository.findById(bolum.getId()).get();
        // Disconnect from session so that the updates on updatedBolum are not directly saved in db
        em.detach(updatedBolum);
        updatedBolum
            .bolumBaslik(UPDATED_BOLUM_BASLIK)
            .dokuman(UPDATED_DOKUMAN)
            .puan(UPDATED_PUAN)
            .videoLink(UPDATED_VIDEO_LINK)
            .sure(UPDATED_SURE)
            .sira(UPDATED_SIRA);

        restBolumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBolum.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBolum))
            )
            .andExpect(status().isOk());

        // Validate the Bolum in the database
        List<Bolum> bolumList = bolumRepository.findAll();
        assertThat(bolumList).hasSize(databaseSizeBeforeUpdate);
        Bolum testBolum = bolumList.get(bolumList.size() - 1);
        assertThat(testBolum.getBolumBaslik()).isEqualTo(UPDATED_BOLUM_BASLIK);
        assertThat(testBolum.getDokuman()).isEqualTo(UPDATED_DOKUMAN);
        assertThat(testBolum.getPuan()).isEqualTo(UPDATED_PUAN);
        assertThat(testBolum.getVideoLink()).isEqualTo(UPDATED_VIDEO_LINK);
        assertThat(testBolum.getSure()).isEqualTo(UPDATED_SURE);
        assertThat(testBolum.getSira()).isEqualTo(UPDATED_SIRA);
    }

    @Test
    @Transactional
    void putNonExistingBolum() throws Exception {
        int databaseSizeBeforeUpdate = bolumRepository.findAll().size();
        bolum.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBolumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bolum.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bolum))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bolum in the database
        List<Bolum> bolumList = bolumRepository.findAll();
        assertThat(bolumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBolum() throws Exception {
        int databaseSizeBeforeUpdate = bolumRepository.findAll().size();
        bolum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBolumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bolum))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bolum in the database
        List<Bolum> bolumList = bolumRepository.findAll();
        assertThat(bolumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBolum() throws Exception {
        int databaseSizeBeforeUpdate = bolumRepository.findAll().size();
        bolum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBolumMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bolum)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bolum in the database
        List<Bolum> bolumList = bolumRepository.findAll();
        assertThat(bolumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBolumWithPatch() throws Exception {
        // Initialize the database
        bolumRepository.saveAndFlush(bolum);

        int databaseSizeBeforeUpdate = bolumRepository.findAll().size();

        // Update the bolum using partial update
        Bolum partialUpdatedBolum = new Bolum();
        partialUpdatedBolum.setId(bolum.getId());

        partialUpdatedBolum.puan(UPDATED_PUAN).videoLink(UPDATED_VIDEO_LINK);

        restBolumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBolum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBolum))
            )
            .andExpect(status().isOk());

        // Validate the Bolum in the database
        List<Bolum> bolumList = bolumRepository.findAll();
        assertThat(bolumList).hasSize(databaseSizeBeforeUpdate);
        Bolum testBolum = bolumList.get(bolumList.size() - 1);
        assertThat(testBolum.getBolumBaslik()).isEqualTo(DEFAULT_BOLUM_BASLIK);
        assertThat(testBolum.getDokuman()).isEqualTo(DEFAULT_DOKUMAN);
        assertThat(testBolum.getPuan()).isEqualTo(UPDATED_PUAN);
        assertThat(testBolum.getVideoLink()).isEqualTo(UPDATED_VIDEO_LINK);
        assertThat(testBolum.getSure()).isEqualTo(DEFAULT_SURE);
        assertThat(testBolum.getSira()).isEqualTo(DEFAULT_SIRA);
    }

    @Test
    @Transactional
    void fullUpdateBolumWithPatch() throws Exception {
        // Initialize the database
        bolumRepository.saveAndFlush(bolum);

        int databaseSizeBeforeUpdate = bolumRepository.findAll().size();

        // Update the bolum using partial update
        Bolum partialUpdatedBolum = new Bolum();
        partialUpdatedBolum.setId(bolum.getId());

        partialUpdatedBolum
            .bolumBaslik(UPDATED_BOLUM_BASLIK)
            .dokuman(UPDATED_DOKUMAN)
            .puan(UPDATED_PUAN)
            .videoLink(UPDATED_VIDEO_LINK)
            .sure(UPDATED_SURE)
            .sira(UPDATED_SIRA);

        restBolumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBolum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBolum))
            )
            .andExpect(status().isOk());

        // Validate the Bolum in the database
        List<Bolum> bolumList = bolumRepository.findAll();
        assertThat(bolumList).hasSize(databaseSizeBeforeUpdate);
        Bolum testBolum = bolumList.get(bolumList.size() - 1);
        assertThat(testBolum.getBolumBaslik()).isEqualTo(UPDATED_BOLUM_BASLIK);
        assertThat(testBolum.getDokuman()).isEqualTo(UPDATED_DOKUMAN);
        assertThat(testBolum.getPuan()).isEqualTo(UPDATED_PUAN);
        assertThat(testBolum.getVideoLink()).isEqualTo(UPDATED_VIDEO_LINK);
        assertThat(testBolum.getSure()).isEqualTo(UPDATED_SURE);
        assertThat(testBolum.getSira()).isEqualTo(UPDATED_SIRA);
    }

    @Test
    @Transactional
    void patchNonExistingBolum() throws Exception {
        int databaseSizeBeforeUpdate = bolumRepository.findAll().size();
        bolum.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBolumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bolum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bolum))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bolum in the database
        List<Bolum> bolumList = bolumRepository.findAll();
        assertThat(bolumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBolum() throws Exception {
        int databaseSizeBeforeUpdate = bolumRepository.findAll().size();
        bolum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBolumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bolum))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bolum in the database
        List<Bolum> bolumList = bolumRepository.findAll();
        assertThat(bolumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBolum() throws Exception {
        int databaseSizeBeforeUpdate = bolumRepository.findAll().size();
        bolum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBolumMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bolum)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bolum in the database
        List<Bolum> bolumList = bolumRepository.findAll();
        assertThat(bolumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBolum() throws Exception {
        // Initialize the database
        bolumRepository.saveAndFlush(bolum);

        int databaseSizeBeforeDelete = bolumRepository.findAll().size();

        // Delete the bolum
        restBolumMockMvc
            .perform(delete(ENTITY_API_URL_ID, bolum.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bolum> bolumList = bolumRepository.findAll();
        assertThat(bolumList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
