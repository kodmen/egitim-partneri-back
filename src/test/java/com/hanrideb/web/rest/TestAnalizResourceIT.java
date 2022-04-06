package com.hanrideb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hanrideb.IntegrationTest;
import com.hanrideb.domain.TestAnaliz;
import com.hanrideb.repository.TestAnalizRepository;
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
 * Integration tests for the {@link TestAnalizResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestAnalizResourceIT {

    private static final Integer DEFAULT_DOGRU = 1;
    private static final Integer UPDATED_DOGRU = 2;

    private static final Integer DEFAULT_YANLIS = 1;
    private static final Integer UPDATED_YANLIS = 2;

    private static final Integer DEFAULT_BOS = 1;
    private static final Integer UPDATED_BOS = 2;

    private static final Float DEFAULT_NET = 1F;
    private static final Float UPDATED_NET = 2F;

    private static final Boolean DEFAULT_TAMAMLANDI = false;
    private static final Boolean UPDATED_TAMAMLANDI = true;

    private static final Long DEFAULT_TEST_ID = 1L;
    private static final Long UPDATED_TEST_ID = 2L;

    private static final String ENTITY_API_URL = "/api/test-analizs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TestAnalizRepository testAnalizRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestAnalizMockMvc;

    private TestAnaliz testAnaliz;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestAnaliz createEntity(EntityManager em) {
        TestAnaliz testAnaliz = new TestAnaliz()
            .dogru(DEFAULT_DOGRU)
            .yanlis(DEFAULT_YANLIS)
            .bos(DEFAULT_BOS)
            .net(DEFAULT_NET)
            .tamamlandi(DEFAULT_TAMAMLANDI)
            .testId(DEFAULT_TEST_ID);
        return testAnaliz;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestAnaliz createUpdatedEntity(EntityManager em) {
        TestAnaliz testAnaliz = new TestAnaliz()
            .dogru(UPDATED_DOGRU)
            .yanlis(UPDATED_YANLIS)
            .bos(UPDATED_BOS)
            .net(UPDATED_NET)
            .tamamlandi(UPDATED_TAMAMLANDI)
            .testId(UPDATED_TEST_ID);
        return testAnaliz;
    }

    @BeforeEach
    public void initTest() {
        testAnaliz = createEntity(em);
    }

    @Test
    @Transactional
    void createTestAnaliz() throws Exception {
        int databaseSizeBeforeCreate = testAnalizRepository.findAll().size();
        // Create the TestAnaliz
        restTestAnalizMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testAnaliz)))
            .andExpect(status().isCreated());

        // Validate the TestAnaliz in the database
        List<TestAnaliz> testAnalizList = testAnalizRepository.findAll();
        assertThat(testAnalizList).hasSize(databaseSizeBeforeCreate + 1);
        TestAnaliz testTestAnaliz = testAnalizList.get(testAnalizList.size() - 1);
        assertThat(testTestAnaliz.getDogru()).isEqualTo(DEFAULT_DOGRU);
        assertThat(testTestAnaliz.getYanlis()).isEqualTo(DEFAULT_YANLIS);
        assertThat(testTestAnaliz.getBos()).isEqualTo(DEFAULT_BOS);
        assertThat(testTestAnaliz.getNet()).isEqualTo(DEFAULT_NET);
        assertThat(testTestAnaliz.getTamamlandi()).isEqualTo(DEFAULT_TAMAMLANDI);
        assertThat(testTestAnaliz.getTestId()).isEqualTo(DEFAULT_TEST_ID);
    }

    @Test
    @Transactional
    void createTestAnalizWithExistingId() throws Exception {
        // Create the TestAnaliz with an existing ID
        testAnaliz.setId(1L);

        int databaseSizeBeforeCreate = testAnalizRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestAnalizMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testAnaliz)))
            .andExpect(status().isBadRequest());

        // Validate the TestAnaliz in the database
        List<TestAnaliz> testAnalizList = testAnalizRepository.findAll();
        assertThat(testAnalizList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestAnalizs() throws Exception {
        // Initialize the database
        testAnalizRepository.saveAndFlush(testAnaliz);

        // Get all the testAnalizList
        restTestAnalizMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testAnaliz.getId().intValue())))
            .andExpect(jsonPath("$.[*].dogru").value(hasItem(DEFAULT_DOGRU)))
            .andExpect(jsonPath("$.[*].yanlis").value(hasItem(DEFAULT_YANLIS)))
            .andExpect(jsonPath("$.[*].bos").value(hasItem(DEFAULT_BOS)))
            .andExpect(jsonPath("$.[*].net").value(hasItem(DEFAULT_NET.doubleValue())))
            .andExpect(jsonPath("$.[*].tamamlandi").value(hasItem(DEFAULT_TAMAMLANDI.booleanValue())))
            .andExpect(jsonPath("$.[*].testId").value(hasItem(DEFAULT_TEST_ID.intValue())));
    }

    @Test
    @Transactional
    void getTestAnaliz() throws Exception {
        // Initialize the database
        testAnalizRepository.saveAndFlush(testAnaliz);

        // Get the testAnaliz
        restTestAnalizMockMvc
            .perform(get(ENTITY_API_URL_ID, testAnaliz.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testAnaliz.getId().intValue()))
            .andExpect(jsonPath("$.dogru").value(DEFAULT_DOGRU))
            .andExpect(jsonPath("$.yanlis").value(DEFAULT_YANLIS))
            .andExpect(jsonPath("$.bos").value(DEFAULT_BOS))
            .andExpect(jsonPath("$.net").value(DEFAULT_NET.doubleValue()))
            .andExpect(jsonPath("$.tamamlandi").value(DEFAULT_TAMAMLANDI.booleanValue()))
            .andExpect(jsonPath("$.testId").value(DEFAULT_TEST_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingTestAnaliz() throws Exception {
        // Get the testAnaliz
        restTestAnalizMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTestAnaliz() throws Exception {
        // Initialize the database
        testAnalizRepository.saveAndFlush(testAnaliz);

        int databaseSizeBeforeUpdate = testAnalizRepository.findAll().size();

        // Update the testAnaliz
        TestAnaliz updatedTestAnaliz = testAnalizRepository.findById(testAnaliz.getId()).get();
        // Disconnect from session so that the updates on updatedTestAnaliz are not directly saved in db
        em.detach(updatedTestAnaliz);
        updatedTestAnaliz
            .dogru(UPDATED_DOGRU)
            .yanlis(UPDATED_YANLIS)
            .bos(UPDATED_BOS)
            .net(UPDATED_NET)
            .tamamlandi(UPDATED_TAMAMLANDI)
            .testId(UPDATED_TEST_ID);

        restTestAnalizMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestAnaliz.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTestAnaliz))
            )
            .andExpect(status().isOk());

        // Validate the TestAnaliz in the database
        List<TestAnaliz> testAnalizList = testAnalizRepository.findAll();
        assertThat(testAnalizList).hasSize(databaseSizeBeforeUpdate);
        TestAnaliz testTestAnaliz = testAnalizList.get(testAnalizList.size() - 1);
        assertThat(testTestAnaliz.getDogru()).isEqualTo(UPDATED_DOGRU);
        assertThat(testTestAnaliz.getYanlis()).isEqualTo(UPDATED_YANLIS);
        assertThat(testTestAnaliz.getBos()).isEqualTo(UPDATED_BOS);
        assertThat(testTestAnaliz.getNet()).isEqualTo(UPDATED_NET);
        assertThat(testTestAnaliz.getTamamlandi()).isEqualTo(UPDATED_TAMAMLANDI);
        assertThat(testTestAnaliz.getTestId()).isEqualTo(UPDATED_TEST_ID);
    }

    @Test
    @Transactional
    void putNonExistingTestAnaliz() throws Exception {
        int databaseSizeBeforeUpdate = testAnalizRepository.findAll().size();
        testAnaliz.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestAnalizMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testAnaliz.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testAnaliz))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestAnaliz in the database
        List<TestAnaliz> testAnalizList = testAnalizRepository.findAll();
        assertThat(testAnalizList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestAnaliz() throws Exception {
        int databaseSizeBeforeUpdate = testAnalizRepository.findAll().size();
        testAnaliz.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestAnalizMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(testAnaliz))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestAnaliz in the database
        List<TestAnaliz> testAnalizList = testAnalizRepository.findAll();
        assertThat(testAnalizList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestAnaliz() throws Exception {
        int databaseSizeBeforeUpdate = testAnalizRepository.findAll().size();
        testAnaliz.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestAnalizMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(testAnaliz)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestAnaliz in the database
        List<TestAnaliz> testAnalizList = testAnalizRepository.findAll();
        assertThat(testAnalizList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestAnalizWithPatch() throws Exception {
        // Initialize the database
        testAnalizRepository.saveAndFlush(testAnaliz);

        int databaseSizeBeforeUpdate = testAnalizRepository.findAll().size();

        // Update the testAnaliz using partial update
        TestAnaliz partialUpdatedTestAnaliz = new TestAnaliz();
        partialUpdatedTestAnaliz.setId(testAnaliz.getId());

        partialUpdatedTestAnaliz.yanlis(UPDATED_YANLIS).tamamlandi(UPDATED_TAMAMLANDI);

        restTestAnalizMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestAnaliz.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestAnaliz))
            )
            .andExpect(status().isOk());

        // Validate the TestAnaliz in the database
        List<TestAnaliz> testAnalizList = testAnalizRepository.findAll();
        assertThat(testAnalizList).hasSize(databaseSizeBeforeUpdate);
        TestAnaliz testTestAnaliz = testAnalizList.get(testAnalizList.size() - 1);
        assertThat(testTestAnaliz.getDogru()).isEqualTo(DEFAULT_DOGRU);
        assertThat(testTestAnaliz.getYanlis()).isEqualTo(UPDATED_YANLIS);
        assertThat(testTestAnaliz.getBos()).isEqualTo(DEFAULT_BOS);
        assertThat(testTestAnaliz.getNet()).isEqualTo(DEFAULT_NET);
        assertThat(testTestAnaliz.getTamamlandi()).isEqualTo(UPDATED_TAMAMLANDI);
        assertThat(testTestAnaliz.getTestId()).isEqualTo(DEFAULT_TEST_ID);
    }

    @Test
    @Transactional
    void fullUpdateTestAnalizWithPatch() throws Exception {
        // Initialize the database
        testAnalizRepository.saveAndFlush(testAnaliz);

        int databaseSizeBeforeUpdate = testAnalizRepository.findAll().size();

        // Update the testAnaliz using partial update
        TestAnaliz partialUpdatedTestAnaliz = new TestAnaliz();
        partialUpdatedTestAnaliz.setId(testAnaliz.getId());

        partialUpdatedTestAnaliz
            .dogru(UPDATED_DOGRU)
            .yanlis(UPDATED_YANLIS)
            .bos(UPDATED_BOS)
            .net(UPDATED_NET)
            .tamamlandi(UPDATED_TAMAMLANDI)
            .testId(UPDATED_TEST_ID);

        restTestAnalizMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestAnaliz.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTestAnaliz))
            )
            .andExpect(status().isOk());

        // Validate the TestAnaliz in the database
        List<TestAnaliz> testAnalizList = testAnalizRepository.findAll();
        assertThat(testAnalizList).hasSize(databaseSizeBeforeUpdate);
        TestAnaliz testTestAnaliz = testAnalizList.get(testAnalizList.size() - 1);
        assertThat(testTestAnaliz.getDogru()).isEqualTo(UPDATED_DOGRU);
        assertThat(testTestAnaliz.getYanlis()).isEqualTo(UPDATED_YANLIS);
        assertThat(testTestAnaliz.getBos()).isEqualTo(UPDATED_BOS);
        assertThat(testTestAnaliz.getNet()).isEqualTo(UPDATED_NET);
        assertThat(testTestAnaliz.getTamamlandi()).isEqualTo(UPDATED_TAMAMLANDI);
        assertThat(testTestAnaliz.getTestId()).isEqualTo(UPDATED_TEST_ID);
    }

    @Test
    @Transactional
    void patchNonExistingTestAnaliz() throws Exception {
        int databaseSizeBeforeUpdate = testAnalizRepository.findAll().size();
        testAnaliz.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestAnalizMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testAnaliz.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testAnaliz))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestAnaliz in the database
        List<TestAnaliz> testAnalizList = testAnalizRepository.findAll();
        assertThat(testAnalizList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestAnaliz() throws Exception {
        int databaseSizeBeforeUpdate = testAnalizRepository.findAll().size();
        testAnaliz.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestAnalizMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(testAnaliz))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestAnaliz in the database
        List<TestAnaliz> testAnalizList = testAnalizRepository.findAll();
        assertThat(testAnalizList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestAnaliz() throws Exception {
        int databaseSizeBeforeUpdate = testAnalizRepository.findAll().size();
        testAnaliz.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestAnalizMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(testAnaliz))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestAnaliz in the database
        List<TestAnaliz> testAnalizList = testAnalizRepository.findAll();
        assertThat(testAnalizList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestAnaliz() throws Exception {
        // Initialize the database
        testAnalizRepository.saveAndFlush(testAnaliz);

        int databaseSizeBeforeDelete = testAnalizRepository.findAll().size();

        // Delete the testAnaliz
        restTestAnalizMockMvc
            .perform(delete(ENTITY_API_URL_ID, testAnaliz.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TestAnaliz> testAnalizList = testAnalizRepository.findAll();
        assertThat(testAnalizList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
