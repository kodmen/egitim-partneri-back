package com.hanrideb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hanrideb.IntegrationTest;
import com.hanrideb.domain.Ogrenci;
import com.hanrideb.repository.OgrenciRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link OgrenciResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OgrenciResourceIT {

    private static final Long DEFAULT_LEVEL = 1L;
    private static final Long UPDATED_LEVEL = 2L;

    private static final String DEFAULT_ACIKLAMA = "AAAAAAAAAA";
    private static final String UPDATED_ACIKLAMA = "BBBBBBBBBB";

    private static final Integer DEFAULT_TOPLAM_PUAN = 1;
    private static final Integer UPDATED_TOPLAM_PUAN = 2;

    private static final String ENTITY_API_URL = "/api/ogrencis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OgrenciRepository ogrenciRepository;

    @Mock
    private OgrenciRepository ogrenciRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOgrenciMockMvc;

    private Ogrenci ogrenci;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ogrenci createEntity(EntityManager em) {
        Ogrenci ogrenci = new Ogrenci().level(DEFAULT_LEVEL).aciklama(DEFAULT_ACIKLAMA).toplamPuan(DEFAULT_TOPLAM_PUAN);
        return ogrenci;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ogrenci createUpdatedEntity(EntityManager em) {
        Ogrenci ogrenci = new Ogrenci().level(UPDATED_LEVEL).aciklama(UPDATED_ACIKLAMA).toplamPuan(UPDATED_TOPLAM_PUAN);
        return ogrenci;
    }

    @BeforeEach
    public void initTest() {
        ogrenci = createEntity(em);
    }

    @Test
    @Transactional
    void createOgrenci() throws Exception {
        int databaseSizeBeforeCreate = ogrenciRepository.findAll().size();
        // Create the Ogrenci
        restOgrenciMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ogrenci)))
            .andExpect(status().isCreated());

        // Validate the Ogrenci in the database
        List<Ogrenci> ogrenciList = ogrenciRepository.findAll();
        assertThat(ogrenciList).hasSize(databaseSizeBeforeCreate + 1);
        Ogrenci testOgrenci = ogrenciList.get(ogrenciList.size() - 1);
        assertThat(testOgrenci.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testOgrenci.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
        assertThat(testOgrenci.getToplamPuan()).isEqualTo(DEFAULT_TOPLAM_PUAN);
    }

    @Test
    @Transactional
    void createOgrenciWithExistingId() throws Exception {
        // Create the Ogrenci with an existing ID
        ogrenci.setId(1L);

        int databaseSizeBeforeCreate = ogrenciRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOgrenciMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ogrenci)))
            .andExpect(status().isBadRequest());

        // Validate the Ogrenci in the database
        List<Ogrenci> ogrenciList = ogrenciRepository.findAll();
        assertThat(ogrenciList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOgrencis() throws Exception {
        // Initialize the database
        ogrenciRepository.saveAndFlush(ogrenci);

        // Get all the ogrenciList
        restOgrenciMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ogrenci.getId().intValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.intValue())))
            .andExpect(jsonPath("$.[*].aciklama").value(hasItem(DEFAULT_ACIKLAMA.toString())))
            .andExpect(jsonPath("$.[*].toplamPuan").value(hasItem(DEFAULT_TOPLAM_PUAN)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOgrencisWithEagerRelationshipsIsEnabled() throws Exception {
        when(ogrenciRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOgrenciMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ogrenciRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOgrencisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ogrenciRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOgrenciMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ogrenciRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getOgrenci() throws Exception {
        // Initialize the database
        ogrenciRepository.saveAndFlush(ogrenci);

        // Get the ogrenci
        restOgrenciMockMvc
            .perform(get(ENTITY_API_URL_ID, ogrenci.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ogrenci.getId().intValue()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.intValue()))
            .andExpect(jsonPath("$.aciklama").value(DEFAULT_ACIKLAMA.toString()))
            .andExpect(jsonPath("$.toplamPuan").value(DEFAULT_TOPLAM_PUAN));
    }

    @Test
    @Transactional
    void getNonExistingOgrenci() throws Exception {
        // Get the ogrenci
        restOgrenciMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOgrenci() throws Exception {
        // Initialize the database
        ogrenciRepository.saveAndFlush(ogrenci);

        int databaseSizeBeforeUpdate = ogrenciRepository.findAll().size();

        // Update the ogrenci
        Ogrenci updatedOgrenci = ogrenciRepository.findById(ogrenci.getId()).get();
        // Disconnect from session so that the updates on updatedOgrenci are not directly saved in db
        em.detach(updatedOgrenci);
        updatedOgrenci.level(UPDATED_LEVEL).aciklama(UPDATED_ACIKLAMA).toplamPuan(UPDATED_TOPLAM_PUAN);

        restOgrenciMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOgrenci.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOgrenci))
            )
            .andExpect(status().isOk());

        // Validate the Ogrenci in the database
        List<Ogrenci> ogrenciList = ogrenciRepository.findAll();
        assertThat(ogrenciList).hasSize(databaseSizeBeforeUpdate);
        Ogrenci testOgrenci = ogrenciList.get(ogrenciList.size() - 1);
        assertThat(testOgrenci.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testOgrenci.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
        assertThat(testOgrenci.getToplamPuan()).isEqualTo(UPDATED_TOPLAM_PUAN);
    }

    @Test
    @Transactional
    void putNonExistingOgrenci() throws Exception {
        int databaseSizeBeforeUpdate = ogrenciRepository.findAll().size();
        ogrenci.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOgrenciMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ogrenci.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ogrenci))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ogrenci in the database
        List<Ogrenci> ogrenciList = ogrenciRepository.findAll();
        assertThat(ogrenciList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOgrenci() throws Exception {
        int databaseSizeBeforeUpdate = ogrenciRepository.findAll().size();
        ogrenci.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOgrenciMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ogrenci))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ogrenci in the database
        List<Ogrenci> ogrenciList = ogrenciRepository.findAll();
        assertThat(ogrenciList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOgrenci() throws Exception {
        int databaseSizeBeforeUpdate = ogrenciRepository.findAll().size();
        ogrenci.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOgrenciMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ogrenci)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ogrenci in the database
        List<Ogrenci> ogrenciList = ogrenciRepository.findAll();
        assertThat(ogrenciList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOgrenciWithPatch() throws Exception {
        // Initialize the database
        ogrenciRepository.saveAndFlush(ogrenci);

        int databaseSizeBeforeUpdate = ogrenciRepository.findAll().size();

        // Update the ogrenci using partial update
        Ogrenci partialUpdatedOgrenci = new Ogrenci();
        partialUpdatedOgrenci.setId(ogrenci.getId());

        partialUpdatedOgrenci.level(UPDATED_LEVEL).aciklama(UPDATED_ACIKLAMA);

        restOgrenciMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOgrenci.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOgrenci))
            )
            .andExpect(status().isOk());

        // Validate the Ogrenci in the database
        List<Ogrenci> ogrenciList = ogrenciRepository.findAll();
        assertThat(ogrenciList).hasSize(databaseSizeBeforeUpdate);
        Ogrenci testOgrenci = ogrenciList.get(ogrenciList.size() - 1);
        assertThat(testOgrenci.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testOgrenci.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
        assertThat(testOgrenci.getToplamPuan()).isEqualTo(DEFAULT_TOPLAM_PUAN);
    }

    @Test
    @Transactional
    void fullUpdateOgrenciWithPatch() throws Exception {
        // Initialize the database
        ogrenciRepository.saveAndFlush(ogrenci);

        int databaseSizeBeforeUpdate = ogrenciRepository.findAll().size();

        // Update the ogrenci using partial update
        Ogrenci partialUpdatedOgrenci = new Ogrenci();
        partialUpdatedOgrenci.setId(ogrenci.getId());

        partialUpdatedOgrenci.level(UPDATED_LEVEL).aciklama(UPDATED_ACIKLAMA).toplamPuan(UPDATED_TOPLAM_PUAN);

        restOgrenciMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOgrenci.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOgrenci))
            )
            .andExpect(status().isOk());

        // Validate the Ogrenci in the database
        List<Ogrenci> ogrenciList = ogrenciRepository.findAll();
        assertThat(ogrenciList).hasSize(databaseSizeBeforeUpdate);
        Ogrenci testOgrenci = ogrenciList.get(ogrenciList.size() - 1);
        assertThat(testOgrenci.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testOgrenci.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
        assertThat(testOgrenci.getToplamPuan()).isEqualTo(UPDATED_TOPLAM_PUAN);
    }

    @Test
    @Transactional
    void patchNonExistingOgrenci() throws Exception {
        int databaseSizeBeforeUpdate = ogrenciRepository.findAll().size();
        ogrenci.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOgrenciMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ogrenci.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ogrenci))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ogrenci in the database
        List<Ogrenci> ogrenciList = ogrenciRepository.findAll();
        assertThat(ogrenciList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOgrenci() throws Exception {
        int databaseSizeBeforeUpdate = ogrenciRepository.findAll().size();
        ogrenci.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOgrenciMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ogrenci))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ogrenci in the database
        List<Ogrenci> ogrenciList = ogrenciRepository.findAll();
        assertThat(ogrenciList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOgrenci() throws Exception {
        int databaseSizeBeforeUpdate = ogrenciRepository.findAll().size();
        ogrenci.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOgrenciMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ogrenci)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ogrenci in the database
        List<Ogrenci> ogrenciList = ogrenciRepository.findAll();
        assertThat(ogrenciList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOgrenci() throws Exception {
        // Initialize the database
        ogrenciRepository.saveAndFlush(ogrenci);

        int databaseSizeBeforeDelete = ogrenciRepository.findAll().size();

        // Delete the ogrenci
        restOgrenciMockMvc
            .perform(delete(ENTITY_API_URL_ID, ogrenci.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ogrenci> ogrenciList = ogrenciRepository.findAll();
        assertThat(ogrenciList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
