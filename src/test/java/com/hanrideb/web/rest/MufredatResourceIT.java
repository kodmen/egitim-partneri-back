package com.hanrideb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hanrideb.IntegrationTest;
import com.hanrideb.domain.Mufredat;
import com.hanrideb.repository.MufredatRepository;
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

/**
 * Integration tests for the {@link MufredatResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MufredatResourceIT {

    private static final String DEFAULT_MUFREDAT_BASLIK = "AAAAAAAAAA";
    private static final String UPDATED_MUFREDAT_BASLIK = "BBBBBBBBBB";

    private static final String DEFAULT_TOPLAM_SURE = "AAAAAAAAAA";
    private static final String UPDATED_TOPLAM_SURE = "BBBBBBBBBB";

    private static final Integer DEFAULT_BOLUM_SAYI = 1;
    private static final Integer UPDATED_BOLUM_SAYI = 2;

    private static final String ENTITY_API_URL = "/api/mufredats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MufredatRepository mufredatRepository;

    @Mock
    private MufredatRepository mufredatRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMufredatMockMvc;

    private Mufredat mufredat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mufredat createEntity(EntityManager em) {
        Mufredat mufredat = new Mufredat()
            .mufredatBaslik(DEFAULT_MUFREDAT_BASLIK)
            .toplamSure(DEFAULT_TOPLAM_SURE)
            .bolumSayi(DEFAULT_BOLUM_SAYI);
        return mufredat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mufredat createUpdatedEntity(EntityManager em) {
        Mufredat mufredat = new Mufredat()
            .mufredatBaslik(UPDATED_MUFREDAT_BASLIK)
            .toplamSure(UPDATED_TOPLAM_SURE)
            .bolumSayi(UPDATED_BOLUM_SAYI);
        return mufredat;
    }

    @BeforeEach
    public void initTest() {
        mufredat = createEntity(em);
    }

    @Test
    @Transactional
    void createMufredat() throws Exception {
        int databaseSizeBeforeCreate = mufredatRepository.findAll().size();
        // Create the Mufredat
        restMufredatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mufredat)))
            .andExpect(status().isCreated());

        // Validate the Mufredat in the database
        List<Mufredat> mufredatList = mufredatRepository.findAll();
        assertThat(mufredatList).hasSize(databaseSizeBeforeCreate + 1);
        Mufredat testMufredat = mufredatList.get(mufredatList.size() - 1);
        assertThat(testMufredat.getMufredatBaslik()).isEqualTo(DEFAULT_MUFREDAT_BASLIK);
        assertThat(testMufredat.getToplamSure()).isEqualTo(DEFAULT_TOPLAM_SURE);
        assertThat(testMufredat.getBolumSayi()).isEqualTo(DEFAULT_BOLUM_SAYI);
    }

    @Test
    @Transactional
    void createMufredatWithExistingId() throws Exception {
        // Create the Mufredat with an existing ID
        mufredat.setId(1L);

        int databaseSizeBeforeCreate = mufredatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMufredatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mufredat)))
            .andExpect(status().isBadRequest());

        // Validate the Mufredat in the database
        List<Mufredat> mufredatList = mufredatRepository.findAll();
        assertThat(mufredatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMufredats() throws Exception {
        // Initialize the database
        mufredatRepository.saveAndFlush(mufredat);

        // Get all the mufredatList
        restMufredatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mufredat.getId().intValue())))
            .andExpect(jsonPath("$.[*].mufredatBaslik").value(hasItem(DEFAULT_MUFREDAT_BASLIK)))
            .andExpect(jsonPath("$.[*].toplamSure").value(hasItem(DEFAULT_TOPLAM_SURE)))
            .andExpect(jsonPath("$.[*].bolumSayi").value(hasItem(DEFAULT_BOLUM_SAYI)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMufredatsWithEagerRelationshipsIsEnabled() throws Exception {
        when(mufredatRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMufredatMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(mufredatRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMufredatsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(mufredatRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMufredatMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(mufredatRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getMufredat() throws Exception {
        // Initialize the database
        mufredatRepository.saveAndFlush(mufredat);

        // Get the mufredat
        restMufredatMockMvc
            .perform(get(ENTITY_API_URL_ID, mufredat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mufredat.getId().intValue()))
            .andExpect(jsonPath("$.mufredatBaslik").value(DEFAULT_MUFREDAT_BASLIK))
            .andExpect(jsonPath("$.toplamSure").value(DEFAULT_TOPLAM_SURE))
            .andExpect(jsonPath("$.bolumSayi").value(DEFAULT_BOLUM_SAYI));
    }

    @Test
    @Transactional
    void getNonExistingMufredat() throws Exception {
        // Get the mufredat
        restMufredatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMufredat() throws Exception {
        // Initialize the database
        mufredatRepository.saveAndFlush(mufredat);

        int databaseSizeBeforeUpdate = mufredatRepository.findAll().size();

        // Update the mufredat
        Mufredat updatedMufredat = mufredatRepository.findById(mufredat.getId()).get();
        // Disconnect from session so that the updates on updatedMufredat are not directly saved in db
        em.detach(updatedMufredat);
        updatedMufredat.mufredatBaslik(UPDATED_MUFREDAT_BASLIK).toplamSure(UPDATED_TOPLAM_SURE).bolumSayi(UPDATED_BOLUM_SAYI);

        restMufredatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMufredat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMufredat))
            )
            .andExpect(status().isOk());

        // Validate the Mufredat in the database
        List<Mufredat> mufredatList = mufredatRepository.findAll();
        assertThat(mufredatList).hasSize(databaseSizeBeforeUpdate);
        Mufredat testMufredat = mufredatList.get(mufredatList.size() - 1);
        assertThat(testMufredat.getMufredatBaslik()).isEqualTo(UPDATED_MUFREDAT_BASLIK);
        assertThat(testMufredat.getToplamSure()).isEqualTo(UPDATED_TOPLAM_SURE);
        assertThat(testMufredat.getBolumSayi()).isEqualTo(UPDATED_BOLUM_SAYI);
    }

    @Test
    @Transactional
    void putNonExistingMufredat() throws Exception {
        int databaseSizeBeforeUpdate = mufredatRepository.findAll().size();
        mufredat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMufredatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mufredat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mufredat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mufredat in the database
        List<Mufredat> mufredatList = mufredatRepository.findAll();
        assertThat(mufredatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMufredat() throws Exception {
        int databaseSizeBeforeUpdate = mufredatRepository.findAll().size();
        mufredat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMufredatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mufredat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mufredat in the database
        List<Mufredat> mufredatList = mufredatRepository.findAll();
        assertThat(mufredatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMufredat() throws Exception {
        int databaseSizeBeforeUpdate = mufredatRepository.findAll().size();
        mufredat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMufredatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mufredat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mufredat in the database
        List<Mufredat> mufredatList = mufredatRepository.findAll();
        assertThat(mufredatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMufredatWithPatch() throws Exception {
        // Initialize the database
        mufredatRepository.saveAndFlush(mufredat);

        int databaseSizeBeforeUpdate = mufredatRepository.findAll().size();

        // Update the mufredat using partial update
        Mufredat partialUpdatedMufredat = new Mufredat();
        partialUpdatedMufredat.setId(mufredat.getId());

        partialUpdatedMufredat.mufredatBaslik(UPDATED_MUFREDAT_BASLIK).toplamSure(UPDATED_TOPLAM_SURE).bolumSayi(UPDATED_BOLUM_SAYI);

        restMufredatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMufredat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMufredat))
            )
            .andExpect(status().isOk());

        // Validate the Mufredat in the database
        List<Mufredat> mufredatList = mufredatRepository.findAll();
        assertThat(mufredatList).hasSize(databaseSizeBeforeUpdate);
        Mufredat testMufredat = mufredatList.get(mufredatList.size() - 1);
        assertThat(testMufredat.getMufredatBaslik()).isEqualTo(UPDATED_MUFREDAT_BASLIK);
        assertThat(testMufredat.getToplamSure()).isEqualTo(UPDATED_TOPLAM_SURE);
        assertThat(testMufredat.getBolumSayi()).isEqualTo(UPDATED_BOLUM_SAYI);
    }

    @Test
    @Transactional
    void fullUpdateMufredatWithPatch() throws Exception {
        // Initialize the database
        mufredatRepository.saveAndFlush(mufredat);

        int databaseSizeBeforeUpdate = mufredatRepository.findAll().size();

        // Update the mufredat using partial update
        Mufredat partialUpdatedMufredat = new Mufredat();
        partialUpdatedMufredat.setId(mufredat.getId());

        partialUpdatedMufredat.mufredatBaslik(UPDATED_MUFREDAT_BASLIK).toplamSure(UPDATED_TOPLAM_SURE).bolumSayi(UPDATED_BOLUM_SAYI);

        restMufredatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMufredat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMufredat))
            )
            .andExpect(status().isOk());

        // Validate the Mufredat in the database
        List<Mufredat> mufredatList = mufredatRepository.findAll();
        assertThat(mufredatList).hasSize(databaseSizeBeforeUpdate);
        Mufredat testMufredat = mufredatList.get(mufredatList.size() - 1);
        assertThat(testMufredat.getMufredatBaslik()).isEqualTo(UPDATED_MUFREDAT_BASLIK);
        assertThat(testMufredat.getToplamSure()).isEqualTo(UPDATED_TOPLAM_SURE);
        assertThat(testMufredat.getBolumSayi()).isEqualTo(UPDATED_BOLUM_SAYI);
    }

    @Test
    @Transactional
    void patchNonExistingMufredat() throws Exception {
        int databaseSizeBeforeUpdate = mufredatRepository.findAll().size();
        mufredat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMufredatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mufredat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mufredat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mufredat in the database
        List<Mufredat> mufredatList = mufredatRepository.findAll();
        assertThat(mufredatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMufredat() throws Exception {
        int databaseSizeBeforeUpdate = mufredatRepository.findAll().size();
        mufredat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMufredatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mufredat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mufredat in the database
        List<Mufredat> mufredatList = mufredatRepository.findAll();
        assertThat(mufredatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMufredat() throws Exception {
        int databaseSizeBeforeUpdate = mufredatRepository.findAll().size();
        mufredat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMufredatMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mufredat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mufredat in the database
        List<Mufredat> mufredatList = mufredatRepository.findAll();
        assertThat(mufredatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMufredat() throws Exception {
        // Initialize the database
        mufredatRepository.saveAndFlush(mufredat);

        int databaseSizeBeforeDelete = mufredatRepository.findAll().size();

        // Delete the mufredat
        restMufredatMockMvc
            .perform(delete(ENTITY_API_URL_ID, mufredat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mufredat> mufredatList = mufredatRepository.findAll();
        assertThat(mufredatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
