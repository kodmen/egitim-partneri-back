package com.hanrideb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hanrideb.IntegrationTest;
import com.hanrideb.domain.Yorum;
import com.hanrideb.repository.YorumRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link YorumResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class YorumResourceIT {

    private static final String DEFAULT_YAZI = "AAAAAAAAAA";
    private static final String UPDATED_YAZI = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/yorums";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private YorumRepository yorumRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restYorumMockMvc;

    private Yorum yorum;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Yorum createEntity(EntityManager em) {
        Yorum yorum = new Yorum().yazi(DEFAULT_YAZI).date(DEFAULT_DATE);
        return yorum;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Yorum createUpdatedEntity(EntityManager em) {
        Yorum yorum = new Yorum().yazi(UPDATED_YAZI).date(UPDATED_DATE);
        return yorum;
    }

    @BeforeEach
    public void initTest() {
        yorum = createEntity(em);
    }

    @Test
    @Transactional
    void createYorum() throws Exception {
        int databaseSizeBeforeCreate = yorumRepository.findAll().size();
        // Create the Yorum
        restYorumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(yorum)))
            .andExpect(status().isCreated());

        // Validate the Yorum in the database
        List<Yorum> yorumList = yorumRepository.findAll();
        assertThat(yorumList).hasSize(databaseSizeBeforeCreate + 1);
        Yorum testYorum = yorumList.get(yorumList.size() - 1);
        assertThat(testYorum.getYazi()).isEqualTo(DEFAULT_YAZI);
        assertThat(testYorum.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createYorumWithExistingId() throws Exception {
        // Create the Yorum with an existing ID
        yorum.setId(1L);

        int databaseSizeBeforeCreate = yorumRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restYorumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(yorum)))
            .andExpect(status().isBadRequest());

        // Validate the Yorum in the database
        List<Yorum> yorumList = yorumRepository.findAll();
        assertThat(yorumList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllYorums() throws Exception {
        // Initialize the database
        yorumRepository.saveAndFlush(yorum);

        // Get all the yorumList
        restYorumMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(yorum.getId().intValue())))
            .andExpect(jsonPath("$.[*].yazi").value(hasItem(DEFAULT_YAZI)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getYorum() throws Exception {
        // Initialize the database
        yorumRepository.saveAndFlush(yorum);

        // Get the yorum
        restYorumMockMvc
            .perform(get(ENTITY_API_URL_ID, yorum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(yorum.getId().intValue()))
            .andExpect(jsonPath("$.yazi").value(DEFAULT_YAZI))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingYorum() throws Exception {
        // Get the yorum
        restYorumMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewYorum() throws Exception {
        // Initialize the database
        yorumRepository.saveAndFlush(yorum);

        int databaseSizeBeforeUpdate = yorumRepository.findAll().size();

        // Update the yorum
        Yorum updatedYorum = yorumRepository.findById(yorum.getId()).get();
        // Disconnect from session so that the updates on updatedYorum are not directly saved in db
        em.detach(updatedYorum);
        updatedYorum.yazi(UPDATED_YAZI).date(UPDATED_DATE);

        restYorumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedYorum.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedYorum))
            )
            .andExpect(status().isOk());

        // Validate the Yorum in the database
        List<Yorum> yorumList = yorumRepository.findAll();
        assertThat(yorumList).hasSize(databaseSizeBeforeUpdate);
        Yorum testYorum = yorumList.get(yorumList.size() - 1);
        assertThat(testYorum.getYazi()).isEqualTo(UPDATED_YAZI);
        assertThat(testYorum.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingYorum() throws Exception {
        int databaseSizeBeforeUpdate = yorumRepository.findAll().size();
        yorum.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restYorumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, yorum.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(yorum))
            )
            .andExpect(status().isBadRequest());

        // Validate the Yorum in the database
        List<Yorum> yorumList = yorumRepository.findAll();
        assertThat(yorumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchYorum() throws Exception {
        int databaseSizeBeforeUpdate = yorumRepository.findAll().size();
        yorum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restYorumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(yorum))
            )
            .andExpect(status().isBadRequest());

        // Validate the Yorum in the database
        List<Yorum> yorumList = yorumRepository.findAll();
        assertThat(yorumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamYorum() throws Exception {
        int databaseSizeBeforeUpdate = yorumRepository.findAll().size();
        yorum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restYorumMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(yorum)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Yorum in the database
        List<Yorum> yorumList = yorumRepository.findAll();
        assertThat(yorumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateYorumWithPatch() throws Exception {
        // Initialize the database
        yorumRepository.saveAndFlush(yorum);

        int databaseSizeBeforeUpdate = yorumRepository.findAll().size();

        // Update the yorum using partial update
        Yorum partialUpdatedYorum = new Yorum();
        partialUpdatedYorum.setId(yorum.getId());

        partialUpdatedYorum.date(UPDATED_DATE);

        restYorumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedYorum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedYorum))
            )
            .andExpect(status().isOk());

        // Validate the Yorum in the database
        List<Yorum> yorumList = yorumRepository.findAll();
        assertThat(yorumList).hasSize(databaseSizeBeforeUpdate);
        Yorum testYorum = yorumList.get(yorumList.size() - 1);
        assertThat(testYorum.getYazi()).isEqualTo(DEFAULT_YAZI);
        assertThat(testYorum.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateYorumWithPatch() throws Exception {
        // Initialize the database
        yorumRepository.saveAndFlush(yorum);

        int databaseSizeBeforeUpdate = yorumRepository.findAll().size();

        // Update the yorum using partial update
        Yorum partialUpdatedYorum = new Yorum();
        partialUpdatedYorum.setId(yorum.getId());

        partialUpdatedYorum.yazi(UPDATED_YAZI).date(UPDATED_DATE);

        restYorumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedYorum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedYorum))
            )
            .andExpect(status().isOk());

        // Validate the Yorum in the database
        List<Yorum> yorumList = yorumRepository.findAll();
        assertThat(yorumList).hasSize(databaseSizeBeforeUpdate);
        Yorum testYorum = yorumList.get(yorumList.size() - 1);
        assertThat(testYorum.getYazi()).isEqualTo(UPDATED_YAZI);
        assertThat(testYorum.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingYorum() throws Exception {
        int databaseSizeBeforeUpdate = yorumRepository.findAll().size();
        yorum.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restYorumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, yorum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(yorum))
            )
            .andExpect(status().isBadRequest());

        // Validate the Yorum in the database
        List<Yorum> yorumList = yorumRepository.findAll();
        assertThat(yorumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchYorum() throws Exception {
        int databaseSizeBeforeUpdate = yorumRepository.findAll().size();
        yorum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restYorumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(yorum))
            )
            .andExpect(status().isBadRequest());

        // Validate the Yorum in the database
        List<Yorum> yorumList = yorumRepository.findAll();
        assertThat(yorumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamYorum() throws Exception {
        int databaseSizeBeforeUpdate = yorumRepository.findAll().size();
        yorum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restYorumMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(yorum)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Yorum in the database
        List<Yorum> yorumList = yorumRepository.findAll();
        assertThat(yorumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteYorum() throws Exception {
        // Initialize the database
        yorumRepository.saveAndFlush(yorum);

        int databaseSizeBeforeDelete = yorumRepository.findAll().size();

        // Delete the yorum
        restYorumMockMvc
            .perform(delete(ENTITY_API_URL_ID, yorum.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Yorum> yorumList = yorumRepository.findAll();
        assertThat(yorumList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
