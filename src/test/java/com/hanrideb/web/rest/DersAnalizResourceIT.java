package com.hanrideb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hanrideb.IntegrationTest;
import com.hanrideb.domain.DersAnaliz;
import com.hanrideb.repository.DersAnalizRepository;
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
 * Integration tests for the {@link DersAnalizResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DersAnalizResourceIT {

    private static final Integer DEFAULT_TOPLAM_YANLIS = 1;
    private static final Integer UPDATED_TOPLAM_YANLIS = 2;

    private static final Integer DEFAULT_TOPLAM_DOGRU = 1;
    private static final Integer UPDATED_TOPLAM_DOGRU = 2;

    private static final Integer DEFAULT_COZULEN_SORU = 1;
    private static final Integer UPDATED_COZULEN_SORU = 2;

    private static final Boolean DEFAULT_TAMAMLANDI = false;
    private static final Boolean UPDATED_TAMAMLANDI = true;

    private static final String ENTITY_API_URL = "/api/ders-analizs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DersAnalizRepository dersAnalizRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDersAnalizMockMvc;

    private DersAnaliz dersAnaliz;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DersAnaliz createEntity(EntityManager em) {
        DersAnaliz dersAnaliz = new DersAnaliz()
            .toplamYanlis(DEFAULT_TOPLAM_YANLIS)
            .toplamDogru(DEFAULT_TOPLAM_DOGRU)
            .cozulenSoru(DEFAULT_COZULEN_SORU)
            .tamamlandi(DEFAULT_TAMAMLANDI);
        return dersAnaliz;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DersAnaliz createUpdatedEntity(EntityManager em) {
        DersAnaliz dersAnaliz = new DersAnaliz()
            .toplamYanlis(UPDATED_TOPLAM_YANLIS)
            .toplamDogru(UPDATED_TOPLAM_DOGRU)
            .cozulenSoru(UPDATED_COZULEN_SORU)
            .tamamlandi(UPDATED_TAMAMLANDI);
        return dersAnaliz;
    }

    @BeforeEach
    public void initTest() {
        dersAnaliz = createEntity(em);
    }

    @Test
    @Transactional
    void createDersAnaliz() throws Exception {
        int databaseSizeBeforeCreate = dersAnalizRepository.findAll().size();
        // Create the DersAnaliz
        restDersAnalizMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dersAnaliz)))
            .andExpect(status().isCreated());

        // Validate the DersAnaliz in the database
        List<DersAnaliz> dersAnalizList = dersAnalizRepository.findAll();
        assertThat(dersAnalizList).hasSize(databaseSizeBeforeCreate + 1);
        DersAnaliz testDersAnaliz = dersAnalizList.get(dersAnalizList.size() - 1);
        assertThat(testDersAnaliz.getToplamYanlis()).isEqualTo(DEFAULT_TOPLAM_YANLIS);
        assertThat(testDersAnaliz.getToplamDogru()).isEqualTo(DEFAULT_TOPLAM_DOGRU);
        assertThat(testDersAnaliz.getCozulenSoru()).isEqualTo(DEFAULT_COZULEN_SORU);
        assertThat(testDersAnaliz.getTamamlandi()).isEqualTo(DEFAULT_TAMAMLANDI);
    }

    @Test
    @Transactional
    void createDersAnalizWithExistingId() throws Exception {
        // Create the DersAnaliz with an existing ID
        dersAnaliz.setId(1L);

        int databaseSizeBeforeCreate = dersAnalizRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDersAnalizMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dersAnaliz)))
            .andExpect(status().isBadRequest());

        // Validate the DersAnaliz in the database
        List<DersAnaliz> dersAnalizList = dersAnalizRepository.findAll();
        assertThat(dersAnalizList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDersAnalizs() throws Exception {
        // Initialize the database
        dersAnalizRepository.saveAndFlush(dersAnaliz);

        // Get all the dersAnalizList
        restDersAnalizMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dersAnaliz.getId().intValue())))
            .andExpect(jsonPath("$.[*].toplamYanlis").value(hasItem(DEFAULT_TOPLAM_YANLIS)))
            .andExpect(jsonPath("$.[*].toplamDogru").value(hasItem(DEFAULT_TOPLAM_DOGRU)))
            .andExpect(jsonPath("$.[*].cozulenSoru").value(hasItem(DEFAULT_COZULEN_SORU)))
            .andExpect(jsonPath("$.[*].tamamlandi").value(hasItem(DEFAULT_TAMAMLANDI.booleanValue())));
    }

    @Test
    @Transactional
    void getDersAnaliz() throws Exception {
        // Initialize the database
        dersAnalizRepository.saveAndFlush(dersAnaliz);

        // Get the dersAnaliz
        restDersAnalizMockMvc
            .perform(get(ENTITY_API_URL_ID, dersAnaliz.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dersAnaliz.getId().intValue()))
            .andExpect(jsonPath("$.toplamYanlis").value(DEFAULT_TOPLAM_YANLIS))
            .andExpect(jsonPath("$.toplamDogru").value(DEFAULT_TOPLAM_DOGRU))
            .andExpect(jsonPath("$.cozulenSoru").value(DEFAULT_COZULEN_SORU))
            .andExpect(jsonPath("$.tamamlandi").value(DEFAULT_TAMAMLANDI.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingDersAnaliz() throws Exception {
        // Get the dersAnaliz
        restDersAnalizMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDersAnaliz() throws Exception {
        // Initialize the database
        dersAnalizRepository.saveAndFlush(dersAnaliz);

        int databaseSizeBeforeUpdate = dersAnalizRepository.findAll().size();

        // Update the dersAnaliz
        DersAnaliz updatedDersAnaliz = dersAnalizRepository.findById(dersAnaliz.getId()).get();
        // Disconnect from session so that the updates on updatedDersAnaliz are not directly saved in db
        em.detach(updatedDersAnaliz);
        updatedDersAnaliz
            .toplamYanlis(UPDATED_TOPLAM_YANLIS)
            .toplamDogru(UPDATED_TOPLAM_DOGRU)
            .cozulenSoru(UPDATED_COZULEN_SORU)
            .tamamlandi(UPDATED_TAMAMLANDI);

        restDersAnalizMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDersAnaliz.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDersAnaliz))
            )
            .andExpect(status().isOk());

        // Validate the DersAnaliz in the database
        List<DersAnaliz> dersAnalizList = dersAnalizRepository.findAll();
        assertThat(dersAnalizList).hasSize(databaseSizeBeforeUpdate);
        DersAnaliz testDersAnaliz = dersAnalizList.get(dersAnalizList.size() - 1);
        assertThat(testDersAnaliz.getToplamYanlis()).isEqualTo(UPDATED_TOPLAM_YANLIS);
        assertThat(testDersAnaliz.getToplamDogru()).isEqualTo(UPDATED_TOPLAM_DOGRU);
        assertThat(testDersAnaliz.getCozulenSoru()).isEqualTo(UPDATED_COZULEN_SORU);
        assertThat(testDersAnaliz.getTamamlandi()).isEqualTo(UPDATED_TAMAMLANDI);
    }

    @Test
    @Transactional
    void putNonExistingDersAnaliz() throws Exception {
        int databaseSizeBeforeUpdate = dersAnalizRepository.findAll().size();
        dersAnaliz.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDersAnalizMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dersAnaliz.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dersAnaliz))
            )
            .andExpect(status().isBadRequest());

        // Validate the DersAnaliz in the database
        List<DersAnaliz> dersAnalizList = dersAnalizRepository.findAll();
        assertThat(dersAnalizList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDersAnaliz() throws Exception {
        int databaseSizeBeforeUpdate = dersAnalizRepository.findAll().size();
        dersAnaliz.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDersAnalizMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dersAnaliz))
            )
            .andExpect(status().isBadRequest());

        // Validate the DersAnaliz in the database
        List<DersAnaliz> dersAnalizList = dersAnalizRepository.findAll();
        assertThat(dersAnalizList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDersAnaliz() throws Exception {
        int databaseSizeBeforeUpdate = dersAnalizRepository.findAll().size();
        dersAnaliz.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDersAnalizMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dersAnaliz)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DersAnaliz in the database
        List<DersAnaliz> dersAnalizList = dersAnalizRepository.findAll();
        assertThat(dersAnalizList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDersAnalizWithPatch() throws Exception {
        // Initialize the database
        dersAnalizRepository.saveAndFlush(dersAnaliz);

        int databaseSizeBeforeUpdate = dersAnalizRepository.findAll().size();

        // Update the dersAnaliz using partial update
        DersAnaliz partialUpdatedDersAnaliz = new DersAnaliz();
        partialUpdatedDersAnaliz.setId(dersAnaliz.getId());

        partialUpdatedDersAnaliz.toplamYanlis(UPDATED_TOPLAM_YANLIS).toplamDogru(UPDATED_TOPLAM_DOGRU);

        restDersAnalizMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDersAnaliz.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDersAnaliz))
            )
            .andExpect(status().isOk());

        // Validate the DersAnaliz in the database
        List<DersAnaliz> dersAnalizList = dersAnalizRepository.findAll();
        assertThat(dersAnalizList).hasSize(databaseSizeBeforeUpdate);
        DersAnaliz testDersAnaliz = dersAnalizList.get(dersAnalizList.size() - 1);
        assertThat(testDersAnaliz.getToplamYanlis()).isEqualTo(UPDATED_TOPLAM_YANLIS);
        assertThat(testDersAnaliz.getToplamDogru()).isEqualTo(UPDATED_TOPLAM_DOGRU);
        assertThat(testDersAnaliz.getCozulenSoru()).isEqualTo(DEFAULT_COZULEN_SORU);
        assertThat(testDersAnaliz.getTamamlandi()).isEqualTo(DEFAULT_TAMAMLANDI);
    }

    @Test
    @Transactional
    void fullUpdateDersAnalizWithPatch() throws Exception {
        // Initialize the database
        dersAnalizRepository.saveAndFlush(dersAnaliz);

        int databaseSizeBeforeUpdate = dersAnalizRepository.findAll().size();

        // Update the dersAnaliz using partial update
        DersAnaliz partialUpdatedDersAnaliz = new DersAnaliz();
        partialUpdatedDersAnaliz.setId(dersAnaliz.getId());

        partialUpdatedDersAnaliz
            .toplamYanlis(UPDATED_TOPLAM_YANLIS)
            .toplamDogru(UPDATED_TOPLAM_DOGRU)
            .cozulenSoru(UPDATED_COZULEN_SORU)
            .tamamlandi(UPDATED_TAMAMLANDI);

        restDersAnalizMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDersAnaliz.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDersAnaliz))
            )
            .andExpect(status().isOk());

        // Validate the DersAnaliz in the database
        List<DersAnaliz> dersAnalizList = dersAnalizRepository.findAll();
        assertThat(dersAnalizList).hasSize(databaseSizeBeforeUpdate);
        DersAnaliz testDersAnaliz = dersAnalizList.get(dersAnalizList.size() - 1);
        assertThat(testDersAnaliz.getToplamYanlis()).isEqualTo(UPDATED_TOPLAM_YANLIS);
        assertThat(testDersAnaliz.getToplamDogru()).isEqualTo(UPDATED_TOPLAM_DOGRU);
        assertThat(testDersAnaliz.getCozulenSoru()).isEqualTo(UPDATED_COZULEN_SORU);
        assertThat(testDersAnaliz.getTamamlandi()).isEqualTo(UPDATED_TAMAMLANDI);
    }

    @Test
    @Transactional
    void patchNonExistingDersAnaliz() throws Exception {
        int databaseSizeBeforeUpdate = dersAnalizRepository.findAll().size();
        dersAnaliz.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDersAnalizMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dersAnaliz.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dersAnaliz))
            )
            .andExpect(status().isBadRequest());

        // Validate the DersAnaliz in the database
        List<DersAnaliz> dersAnalizList = dersAnalizRepository.findAll();
        assertThat(dersAnalizList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDersAnaliz() throws Exception {
        int databaseSizeBeforeUpdate = dersAnalizRepository.findAll().size();
        dersAnaliz.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDersAnalizMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dersAnaliz))
            )
            .andExpect(status().isBadRequest());

        // Validate the DersAnaliz in the database
        List<DersAnaliz> dersAnalizList = dersAnalizRepository.findAll();
        assertThat(dersAnalizList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDersAnaliz() throws Exception {
        int databaseSizeBeforeUpdate = dersAnalizRepository.findAll().size();
        dersAnaliz.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDersAnalizMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dersAnaliz))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DersAnaliz in the database
        List<DersAnaliz> dersAnalizList = dersAnalizRepository.findAll();
        assertThat(dersAnalizList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDersAnaliz() throws Exception {
        // Initialize the database
        dersAnalizRepository.saveAndFlush(dersAnaliz);

        int databaseSizeBeforeDelete = dersAnalizRepository.findAll().size();

        // Delete the dersAnaliz
        restDersAnalizMockMvc
            .perform(delete(ENTITY_API_URL_ID, dersAnaliz.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DersAnaliz> dersAnalizList = dersAnalizRepository.findAll();
        assertThat(dersAnalizList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
