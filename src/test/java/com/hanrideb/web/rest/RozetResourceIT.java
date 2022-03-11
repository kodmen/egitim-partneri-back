package com.hanrideb.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hanrideb.IntegrationTest;
import com.hanrideb.domain.Rozet;
import com.hanrideb.repository.RozetRepository;
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
 * Integration tests for the {@link RozetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RozetResourceIT {

    private static final String DEFAULT_ROZET_ISMI = "AAAAAAAAAA";
    private static final String UPDATED_ROZET_ISMI = "BBBBBBBBBB";

    private static final byte[] DEFAULT_ROZET_RESIM = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ROZET_RESIM = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ROZET_RESIM_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ROZET_RESIM_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/rozets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RozetRepository rozetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRozetMockMvc;

    private Rozet rozet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rozet createEntity(EntityManager em) {
        Rozet rozet = new Rozet()
            .rozetIsmi(DEFAULT_ROZET_ISMI)
            .rozetResim(DEFAULT_ROZET_RESIM)
            .rozetResimContentType(DEFAULT_ROZET_RESIM_CONTENT_TYPE);
        return rozet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rozet createUpdatedEntity(EntityManager em) {
        Rozet rozet = new Rozet()
            .rozetIsmi(UPDATED_ROZET_ISMI)
            .rozetResim(UPDATED_ROZET_RESIM)
            .rozetResimContentType(UPDATED_ROZET_RESIM_CONTENT_TYPE);
        return rozet;
    }

    @BeforeEach
    public void initTest() {
        rozet = createEntity(em);
    }

    @Test
    @Transactional
    void createRozet() throws Exception {
        int databaseSizeBeforeCreate = rozetRepository.findAll().size();
        // Create the Rozet
        restRozetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rozet)))
            .andExpect(status().isCreated());

        // Validate the Rozet in the database
        List<Rozet> rozetList = rozetRepository.findAll();
        assertThat(rozetList).hasSize(databaseSizeBeforeCreate + 1);
        Rozet testRozet = rozetList.get(rozetList.size() - 1);
        assertThat(testRozet.getRozetIsmi()).isEqualTo(DEFAULT_ROZET_ISMI);
        assertThat(testRozet.getRozetResim()).isEqualTo(DEFAULT_ROZET_RESIM);
        assertThat(testRozet.getRozetResimContentType()).isEqualTo(DEFAULT_ROZET_RESIM_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createRozetWithExistingId() throws Exception {
        // Create the Rozet with an existing ID
        rozet.setId(1L);

        int databaseSizeBeforeCreate = rozetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRozetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rozet)))
            .andExpect(status().isBadRequest());

        // Validate the Rozet in the database
        List<Rozet> rozetList = rozetRepository.findAll();
        assertThat(rozetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRozets() throws Exception {
        // Initialize the database
        rozetRepository.saveAndFlush(rozet);

        // Get all the rozetList
        restRozetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rozet.getId().intValue())))
            .andExpect(jsonPath("$.[*].rozetIsmi").value(hasItem(DEFAULT_ROZET_ISMI)))
            .andExpect(jsonPath("$.[*].rozetResimContentType").value(hasItem(DEFAULT_ROZET_RESIM_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].rozetResim").value(hasItem(Base64Utils.encodeToString(DEFAULT_ROZET_RESIM))));
    }

    @Test
    @Transactional
    void getRozet() throws Exception {
        // Initialize the database
        rozetRepository.saveAndFlush(rozet);

        // Get the rozet
        restRozetMockMvc
            .perform(get(ENTITY_API_URL_ID, rozet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rozet.getId().intValue()))
            .andExpect(jsonPath("$.rozetIsmi").value(DEFAULT_ROZET_ISMI))
            .andExpect(jsonPath("$.rozetResimContentType").value(DEFAULT_ROZET_RESIM_CONTENT_TYPE))
            .andExpect(jsonPath("$.rozetResim").value(Base64Utils.encodeToString(DEFAULT_ROZET_RESIM)));
    }

    @Test
    @Transactional
    void getNonExistingRozet() throws Exception {
        // Get the rozet
        restRozetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRozet() throws Exception {
        // Initialize the database
        rozetRepository.saveAndFlush(rozet);

        int databaseSizeBeforeUpdate = rozetRepository.findAll().size();

        // Update the rozet
        Rozet updatedRozet = rozetRepository.findById(rozet.getId()).get();
        // Disconnect from session so that the updates on updatedRozet are not directly saved in db
        em.detach(updatedRozet);
        updatedRozet.rozetIsmi(UPDATED_ROZET_ISMI).rozetResim(UPDATED_ROZET_RESIM).rozetResimContentType(UPDATED_ROZET_RESIM_CONTENT_TYPE);

        restRozetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRozet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRozet))
            )
            .andExpect(status().isOk());

        // Validate the Rozet in the database
        List<Rozet> rozetList = rozetRepository.findAll();
        assertThat(rozetList).hasSize(databaseSizeBeforeUpdate);
        Rozet testRozet = rozetList.get(rozetList.size() - 1);
        assertThat(testRozet.getRozetIsmi()).isEqualTo(UPDATED_ROZET_ISMI);
        assertThat(testRozet.getRozetResim()).isEqualTo(UPDATED_ROZET_RESIM);
        assertThat(testRozet.getRozetResimContentType()).isEqualTo(UPDATED_ROZET_RESIM_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingRozet() throws Exception {
        int databaseSizeBeforeUpdate = rozetRepository.findAll().size();
        rozet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRozetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rozet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rozet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rozet in the database
        List<Rozet> rozetList = rozetRepository.findAll();
        assertThat(rozetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRozet() throws Exception {
        int databaseSizeBeforeUpdate = rozetRepository.findAll().size();
        rozet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRozetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rozet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rozet in the database
        List<Rozet> rozetList = rozetRepository.findAll();
        assertThat(rozetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRozet() throws Exception {
        int databaseSizeBeforeUpdate = rozetRepository.findAll().size();
        rozet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRozetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rozet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rozet in the database
        List<Rozet> rozetList = rozetRepository.findAll();
        assertThat(rozetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRozetWithPatch() throws Exception {
        // Initialize the database
        rozetRepository.saveAndFlush(rozet);

        int databaseSizeBeforeUpdate = rozetRepository.findAll().size();

        // Update the rozet using partial update
        Rozet partialUpdatedRozet = new Rozet();
        partialUpdatedRozet.setId(rozet.getId());

        partialUpdatedRozet.rozetResim(UPDATED_ROZET_RESIM).rozetResimContentType(UPDATED_ROZET_RESIM_CONTENT_TYPE);

        restRozetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRozet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRozet))
            )
            .andExpect(status().isOk());

        // Validate the Rozet in the database
        List<Rozet> rozetList = rozetRepository.findAll();
        assertThat(rozetList).hasSize(databaseSizeBeforeUpdate);
        Rozet testRozet = rozetList.get(rozetList.size() - 1);
        assertThat(testRozet.getRozetIsmi()).isEqualTo(DEFAULT_ROZET_ISMI);
        assertThat(testRozet.getRozetResim()).isEqualTo(UPDATED_ROZET_RESIM);
        assertThat(testRozet.getRozetResimContentType()).isEqualTo(UPDATED_ROZET_RESIM_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateRozetWithPatch() throws Exception {
        // Initialize the database
        rozetRepository.saveAndFlush(rozet);

        int databaseSizeBeforeUpdate = rozetRepository.findAll().size();

        // Update the rozet using partial update
        Rozet partialUpdatedRozet = new Rozet();
        partialUpdatedRozet.setId(rozet.getId());

        partialUpdatedRozet
            .rozetIsmi(UPDATED_ROZET_ISMI)
            .rozetResim(UPDATED_ROZET_RESIM)
            .rozetResimContentType(UPDATED_ROZET_RESIM_CONTENT_TYPE);

        restRozetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRozet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRozet))
            )
            .andExpect(status().isOk());

        // Validate the Rozet in the database
        List<Rozet> rozetList = rozetRepository.findAll();
        assertThat(rozetList).hasSize(databaseSizeBeforeUpdate);
        Rozet testRozet = rozetList.get(rozetList.size() - 1);
        assertThat(testRozet.getRozetIsmi()).isEqualTo(UPDATED_ROZET_ISMI);
        assertThat(testRozet.getRozetResim()).isEqualTo(UPDATED_ROZET_RESIM);
        assertThat(testRozet.getRozetResimContentType()).isEqualTo(UPDATED_ROZET_RESIM_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingRozet() throws Exception {
        int databaseSizeBeforeUpdate = rozetRepository.findAll().size();
        rozet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRozetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rozet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rozet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rozet in the database
        List<Rozet> rozetList = rozetRepository.findAll();
        assertThat(rozetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRozet() throws Exception {
        int databaseSizeBeforeUpdate = rozetRepository.findAll().size();
        rozet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRozetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rozet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rozet in the database
        List<Rozet> rozetList = rozetRepository.findAll();
        assertThat(rozetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRozet() throws Exception {
        int databaseSizeBeforeUpdate = rozetRepository.findAll().size();
        rozet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRozetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(rozet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rozet in the database
        List<Rozet> rozetList = rozetRepository.findAll();
        assertThat(rozetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRozet() throws Exception {
        // Initialize the database
        rozetRepository.saveAndFlush(rozet);

        int databaseSizeBeforeDelete = rozetRepository.findAll().size();

        // Delete the rozet
        restRozetMockMvc
            .perform(delete(ENTITY_API_URL_ID, rozet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rozet> rozetList = rozetRepository.findAll();
        assertThat(rozetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
