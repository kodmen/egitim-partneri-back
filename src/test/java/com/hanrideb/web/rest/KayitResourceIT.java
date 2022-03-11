package com.hanrideb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hanrideb.IntegrationTest;
import com.hanrideb.domain.Kayit;
import com.hanrideb.repository.KayitRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link KayitResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class KayitResourceIT {

    private static final Integer DEFAULT_PUAN = 1;
    private static final Integer UPDATED_PUAN = 2;

    private static final LocalDate DEFAULT_KAYIT_TARIH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_KAYIT_TARIH = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/kayits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private KayitRepository kayitRepository;

    @Mock
    private KayitRepository kayitRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKayitMockMvc;

    private Kayit kayit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Kayit createEntity(EntityManager em) {
        Kayit kayit = new Kayit().puan(DEFAULT_PUAN).kayitTarih(DEFAULT_KAYIT_TARIH);
        return kayit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Kayit createUpdatedEntity(EntityManager em) {
        Kayit kayit = new Kayit().puan(UPDATED_PUAN).kayitTarih(UPDATED_KAYIT_TARIH);
        return kayit;
    }

    @BeforeEach
    public void initTest() {
        kayit = createEntity(em);
    }

    @Test
    @Transactional
    void createKayit() throws Exception {
        int databaseSizeBeforeCreate = kayitRepository.findAll().size();
        // Create the Kayit
        restKayitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(kayit)))
            .andExpect(status().isCreated());

        // Validate the Kayit in the database
        List<Kayit> kayitList = kayitRepository.findAll();
        assertThat(kayitList).hasSize(databaseSizeBeforeCreate + 1);
        Kayit testKayit = kayitList.get(kayitList.size() - 1);
        assertThat(testKayit.getPuan()).isEqualTo(DEFAULT_PUAN);
        assertThat(testKayit.getKayitTarih()).isEqualTo(DEFAULT_KAYIT_TARIH);
    }

    @Test
    @Transactional
    void createKayitWithExistingId() throws Exception {
        // Create the Kayit with an existing ID
        kayit.setId(1L);

        int databaseSizeBeforeCreate = kayitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restKayitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(kayit)))
            .andExpect(status().isBadRequest());

        // Validate the Kayit in the database
        List<Kayit> kayitList = kayitRepository.findAll();
        assertThat(kayitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllKayits() throws Exception {
        // Initialize the database
        kayitRepository.saveAndFlush(kayit);

        // Get all the kayitList
        restKayitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kayit.getId().intValue())))
            .andExpect(jsonPath("$.[*].puan").value(hasItem(DEFAULT_PUAN)))
            .andExpect(jsonPath("$.[*].kayitTarih").value(hasItem(DEFAULT_KAYIT_TARIH.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllKayitsWithEagerRelationshipsIsEnabled() throws Exception {
        when(kayitRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restKayitMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(kayitRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllKayitsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(kayitRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restKayitMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(kayitRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getKayit() throws Exception {
        // Initialize the database
        kayitRepository.saveAndFlush(kayit);

        // Get the kayit
        restKayitMockMvc
            .perform(get(ENTITY_API_URL_ID, kayit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(kayit.getId().intValue()))
            .andExpect(jsonPath("$.puan").value(DEFAULT_PUAN))
            .andExpect(jsonPath("$.kayitTarih").value(DEFAULT_KAYIT_TARIH.toString()));
    }

    @Test
    @Transactional
    void getNonExistingKayit() throws Exception {
        // Get the kayit
        restKayitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewKayit() throws Exception {
        // Initialize the database
        kayitRepository.saveAndFlush(kayit);

        int databaseSizeBeforeUpdate = kayitRepository.findAll().size();

        // Update the kayit
        Kayit updatedKayit = kayitRepository.findById(kayit.getId()).get();
        // Disconnect from session so that the updates on updatedKayit are not directly saved in db
        em.detach(updatedKayit);
        updatedKayit.puan(UPDATED_PUAN).kayitTarih(UPDATED_KAYIT_TARIH);

        restKayitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedKayit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedKayit))
            )
            .andExpect(status().isOk());

        // Validate the Kayit in the database
        List<Kayit> kayitList = kayitRepository.findAll();
        assertThat(kayitList).hasSize(databaseSizeBeforeUpdate);
        Kayit testKayit = kayitList.get(kayitList.size() - 1);
        assertThat(testKayit.getPuan()).isEqualTo(UPDATED_PUAN);
        assertThat(testKayit.getKayitTarih()).isEqualTo(UPDATED_KAYIT_TARIH);
    }

    @Test
    @Transactional
    void putNonExistingKayit() throws Exception {
        int databaseSizeBeforeUpdate = kayitRepository.findAll().size();
        kayit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKayitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, kayit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(kayit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Kayit in the database
        List<Kayit> kayitList = kayitRepository.findAll();
        assertThat(kayitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchKayit() throws Exception {
        int databaseSizeBeforeUpdate = kayitRepository.findAll().size();
        kayit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKayitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(kayit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Kayit in the database
        List<Kayit> kayitList = kayitRepository.findAll();
        assertThat(kayitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamKayit() throws Exception {
        int databaseSizeBeforeUpdate = kayitRepository.findAll().size();
        kayit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKayitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(kayit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Kayit in the database
        List<Kayit> kayitList = kayitRepository.findAll();
        assertThat(kayitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateKayitWithPatch() throws Exception {
        // Initialize the database
        kayitRepository.saveAndFlush(kayit);

        int databaseSizeBeforeUpdate = kayitRepository.findAll().size();

        // Update the kayit using partial update
        Kayit partialUpdatedKayit = new Kayit();
        partialUpdatedKayit.setId(kayit.getId());

        partialUpdatedKayit.kayitTarih(UPDATED_KAYIT_TARIH);

        restKayitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKayit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKayit))
            )
            .andExpect(status().isOk());

        // Validate the Kayit in the database
        List<Kayit> kayitList = kayitRepository.findAll();
        assertThat(kayitList).hasSize(databaseSizeBeforeUpdate);
        Kayit testKayit = kayitList.get(kayitList.size() - 1);
        assertThat(testKayit.getPuan()).isEqualTo(DEFAULT_PUAN);
        assertThat(testKayit.getKayitTarih()).isEqualTo(UPDATED_KAYIT_TARIH);
    }

    @Test
    @Transactional
    void fullUpdateKayitWithPatch() throws Exception {
        // Initialize the database
        kayitRepository.saveAndFlush(kayit);

        int databaseSizeBeforeUpdate = kayitRepository.findAll().size();

        // Update the kayit using partial update
        Kayit partialUpdatedKayit = new Kayit();
        partialUpdatedKayit.setId(kayit.getId());

        partialUpdatedKayit.puan(UPDATED_PUAN).kayitTarih(UPDATED_KAYIT_TARIH);

        restKayitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKayit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKayit))
            )
            .andExpect(status().isOk());

        // Validate the Kayit in the database
        List<Kayit> kayitList = kayitRepository.findAll();
        assertThat(kayitList).hasSize(databaseSizeBeforeUpdate);
        Kayit testKayit = kayitList.get(kayitList.size() - 1);
        assertThat(testKayit.getPuan()).isEqualTo(UPDATED_PUAN);
        assertThat(testKayit.getKayitTarih()).isEqualTo(UPDATED_KAYIT_TARIH);
    }

    @Test
    @Transactional
    void patchNonExistingKayit() throws Exception {
        int databaseSizeBeforeUpdate = kayitRepository.findAll().size();
        kayit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKayitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, kayit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(kayit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Kayit in the database
        List<Kayit> kayitList = kayitRepository.findAll();
        assertThat(kayitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchKayit() throws Exception {
        int databaseSizeBeforeUpdate = kayitRepository.findAll().size();
        kayit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKayitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(kayit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Kayit in the database
        List<Kayit> kayitList = kayitRepository.findAll();
        assertThat(kayitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamKayit() throws Exception {
        int databaseSizeBeforeUpdate = kayitRepository.findAll().size();
        kayit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKayitMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(kayit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Kayit in the database
        List<Kayit> kayitList = kayitRepository.findAll();
        assertThat(kayitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteKayit() throws Exception {
        // Initialize the database
        kayitRepository.saveAndFlush(kayit);

        int databaseSizeBeforeDelete = kayitRepository.findAll().size();

        // Delete the kayit
        restKayitMockMvc
            .perform(delete(ENTITY_API_URL_ID, kayit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Kayit> kayitList = kayitRepository.findAll();
        assertThat(kayitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
