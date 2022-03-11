package com.hanrideb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hanrideb.IntegrationTest;
import com.hanrideb.domain.Ogretmen;
import com.hanrideb.repository.OgretmenRepository;
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
 * Integration tests for the {@link OgretmenResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OgretmenResourceIT {

    private static final String DEFAULT_ACIKLAMA = "AAAAAAAAAA";
    private static final String UPDATED_ACIKLAMA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ogretmen";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OgretmenRepository ogretmenRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOgretmenMockMvc;

    private Ogretmen ogretmen;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ogretmen createEntity(EntityManager em) {
        Ogretmen ogretmen = new Ogretmen().aciklama(DEFAULT_ACIKLAMA);
        return ogretmen;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ogretmen createUpdatedEntity(EntityManager em) {
        Ogretmen ogretmen = new Ogretmen().aciklama(UPDATED_ACIKLAMA);
        return ogretmen;
    }

    @BeforeEach
    public void initTest() {
        ogretmen = createEntity(em);
    }

    @Test
    @Transactional
    void createOgretmen() throws Exception {
        int databaseSizeBeforeCreate = ogretmenRepository.findAll().size();
        // Create the Ogretmen
        restOgretmenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ogretmen)))
            .andExpect(status().isCreated());

        // Validate the Ogretmen in the database
        List<Ogretmen> ogretmenList = ogretmenRepository.findAll();
        assertThat(ogretmenList).hasSize(databaseSizeBeforeCreate + 1);
        Ogretmen testOgretmen = ogretmenList.get(ogretmenList.size() - 1);
        assertThat(testOgretmen.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
    }

    @Test
    @Transactional
    void createOgretmenWithExistingId() throws Exception {
        // Create the Ogretmen with an existing ID
        ogretmen.setId(1L);

        int databaseSizeBeforeCreate = ogretmenRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOgretmenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ogretmen)))
            .andExpect(status().isBadRequest());

        // Validate the Ogretmen in the database
        List<Ogretmen> ogretmenList = ogretmenRepository.findAll();
        assertThat(ogretmenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOgretmen() throws Exception {
        // Initialize the database
        ogretmenRepository.saveAndFlush(ogretmen);

        // Get all the ogretmenList
        restOgretmenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ogretmen.getId().intValue())))
            .andExpect(jsonPath("$.[*].aciklama").value(hasItem(DEFAULT_ACIKLAMA.toString())));
    }

    @Test
    @Transactional
    void getOgretmen() throws Exception {
        // Initialize the database
        ogretmenRepository.saveAndFlush(ogretmen);

        // Get the ogretmen
        restOgretmenMockMvc
            .perform(get(ENTITY_API_URL_ID, ogretmen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ogretmen.getId().intValue()))
            .andExpect(jsonPath("$.aciklama").value(DEFAULT_ACIKLAMA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOgretmen() throws Exception {
        // Get the ogretmen
        restOgretmenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOgretmen() throws Exception {
        // Initialize the database
        ogretmenRepository.saveAndFlush(ogretmen);

        int databaseSizeBeforeUpdate = ogretmenRepository.findAll().size();

        // Update the ogretmen
        Ogretmen updatedOgretmen = ogretmenRepository.findById(ogretmen.getId()).get();
        // Disconnect from session so that the updates on updatedOgretmen are not directly saved in db
        em.detach(updatedOgretmen);
        updatedOgretmen.aciklama(UPDATED_ACIKLAMA);

        restOgretmenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOgretmen.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOgretmen))
            )
            .andExpect(status().isOk());

        // Validate the Ogretmen in the database
        List<Ogretmen> ogretmenList = ogretmenRepository.findAll();
        assertThat(ogretmenList).hasSize(databaseSizeBeforeUpdate);
        Ogretmen testOgretmen = ogretmenList.get(ogretmenList.size() - 1);
        assertThat(testOgretmen.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
    }

    @Test
    @Transactional
    void putNonExistingOgretmen() throws Exception {
        int databaseSizeBeforeUpdate = ogretmenRepository.findAll().size();
        ogretmen.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOgretmenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ogretmen.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ogretmen))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ogretmen in the database
        List<Ogretmen> ogretmenList = ogretmenRepository.findAll();
        assertThat(ogretmenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOgretmen() throws Exception {
        int databaseSizeBeforeUpdate = ogretmenRepository.findAll().size();
        ogretmen.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOgretmenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ogretmen))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ogretmen in the database
        List<Ogretmen> ogretmenList = ogretmenRepository.findAll();
        assertThat(ogretmenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOgretmen() throws Exception {
        int databaseSizeBeforeUpdate = ogretmenRepository.findAll().size();
        ogretmen.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOgretmenMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ogretmen)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ogretmen in the database
        List<Ogretmen> ogretmenList = ogretmenRepository.findAll();
        assertThat(ogretmenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOgretmenWithPatch() throws Exception {
        // Initialize the database
        ogretmenRepository.saveAndFlush(ogretmen);

        int databaseSizeBeforeUpdate = ogretmenRepository.findAll().size();

        // Update the ogretmen using partial update
        Ogretmen partialUpdatedOgretmen = new Ogretmen();
        partialUpdatedOgretmen.setId(ogretmen.getId());

        restOgretmenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOgretmen.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOgretmen))
            )
            .andExpect(status().isOk());

        // Validate the Ogretmen in the database
        List<Ogretmen> ogretmenList = ogretmenRepository.findAll();
        assertThat(ogretmenList).hasSize(databaseSizeBeforeUpdate);
        Ogretmen testOgretmen = ogretmenList.get(ogretmenList.size() - 1);
        assertThat(testOgretmen.getAciklama()).isEqualTo(DEFAULT_ACIKLAMA);
    }

    @Test
    @Transactional
    void fullUpdateOgretmenWithPatch() throws Exception {
        // Initialize the database
        ogretmenRepository.saveAndFlush(ogretmen);

        int databaseSizeBeforeUpdate = ogretmenRepository.findAll().size();

        // Update the ogretmen using partial update
        Ogretmen partialUpdatedOgretmen = new Ogretmen();
        partialUpdatedOgretmen.setId(ogretmen.getId());

        partialUpdatedOgretmen.aciklama(UPDATED_ACIKLAMA);

        restOgretmenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOgretmen.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOgretmen))
            )
            .andExpect(status().isOk());

        // Validate the Ogretmen in the database
        List<Ogretmen> ogretmenList = ogretmenRepository.findAll();
        assertThat(ogretmenList).hasSize(databaseSizeBeforeUpdate);
        Ogretmen testOgretmen = ogretmenList.get(ogretmenList.size() - 1);
        assertThat(testOgretmen.getAciklama()).isEqualTo(UPDATED_ACIKLAMA);
    }

    @Test
    @Transactional
    void patchNonExistingOgretmen() throws Exception {
        int databaseSizeBeforeUpdate = ogretmenRepository.findAll().size();
        ogretmen.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOgretmenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ogretmen.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ogretmen))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ogretmen in the database
        List<Ogretmen> ogretmenList = ogretmenRepository.findAll();
        assertThat(ogretmenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOgretmen() throws Exception {
        int databaseSizeBeforeUpdate = ogretmenRepository.findAll().size();
        ogretmen.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOgretmenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ogretmen))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ogretmen in the database
        List<Ogretmen> ogretmenList = ogretmenRepository.findAll();
        assertThat(ogretmenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOgretmen() throws Exception {
        int databaseSizeBeforeUpdate = ogretmenRepository.findAll().size();
        ogretmen.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOgretmenMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ogretmen)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ogretmen in the database
        List<Ogretmen> ogretmenList = ogretmenRepository.findAll();
        assertThat(ogretmenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOgretmen() throws Exception {
        // Initialize the database
        ogretmenRepository.saveAndFlush(ogretmen);

        int databaseSizeBeforeDelete = ogretmenRepository.findAll().size();

        // Delete the ogretmen
        restOgretmenMockMvc
            .perform(delete(ENTITY_API_URL_ID, ogretmen.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ogretmen> ogretmenList = ogretmenRepository.findAll();
        assertThat(ogretmenList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
