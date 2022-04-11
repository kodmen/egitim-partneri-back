//package com.hanrideb.web.rest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.hasItem;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import com.hanrideb.IntegrationTest;
//import com.hanrideb.domain.SoruTest;
//import com.hanrideb.repository.SoruTestRepository;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.concurrent.atomic.AtomicLong;
//import javax.persistence.EntityManager;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.Base64Utils;
//
///**
// * Integration tests for the {@link SoruTestResource} REST controller.
// */
//@IntegrationTest
//@ExtendWith(MockitoExtension.class)
//@AutoConfigureMockMvc
//@WithMockUser
//class SoruTestResourceIT {
//
//    private static final String DEFAULT_TES_BASLIK = "AAAAAAAAAA";
//    private static final String UPDATED_TES_BASLIK = "BBBBBBBBBB";
//
//    private static final String DEFAULT_TEST_PDF = "AAAAAAAAAA";
//    private static final String UPDATED_TEST_PDF = "BBBBBBBBBB";
//
//    private static final byte[] DEFAULT_TEST_FOTO = TestUtil.createByteArray(1, "0");
//    private static final byte[] UPDATED_TEST_FOTO = TestUtil.createByteArray(1, "1");
//    private static final String DEFAULT_TEST_FOTO_CONTENT_TYPE = "image/jpg";
//    private static final String UPDATED_TEST_FOTO_CONTENT_TYPE = "image/png";
//
//    private static final String DEFAULT_CEVAPLAR = "AAAAAAAAAA";
//    private static final String UPDATED_CEVAPLAR = "BBBBBBBBBB";
//
//    private static final byte[] DEFAULT_SORU_PDF_FILE = TestUtil.createByteArray(1, "0");
//    private static final byte[] UPDATED_SORU_PDF_FILE = TestUtil.createByteArray(1, "1");
//    private static final String DEFAULT_SORU_PDF_FILE_CONTENT_TYPE = "image/jpg";
//    private static final String UPDATED_SORU_PDF_FILE_CONTENT_TYPE = "image/png";
//
//    private static final Integer DEFAULT_SORU_SAYISI = 1;
//    private static final Integer UPDATED_SORU_SAYISI = 2;
//
//    private static final String DEFAULT_SEVIYE = "AAAAAAAAAA";
//    private static final String UPDATED_SEVIYE = "BBBBBBBBBB";
//
//    private static final String ENTITY_API_URL = "/api/soru-tests";
//    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
//
//    private static Random random = new Random();
//    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
//
//    @Autowired
//    private SoruTestRepository soruTestRepository;
//
//    @Mock
//    private SoruTestRepository soruTestRepositoryMock;
//
//    @Autowired
//    private EntityManager em;
//
//    @Autowired
//    private MockMvc restSoruTestMockMvc;
//
//    private SoruTest soruTest;
//
//    /**
//     * Create an entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static SoruTest createEntity(EntityManager em) {
//        SoruTest soruTest = new SoruTest()
//            .tesBaslik(DEFAULT_TES_BASLIK)
//            .testPdf(DEFAULT_TEST_PDF)
//            .testFoto(DEFAULT_TEST_FOTO)
//            .testFotoContentType(DEFAULT_TEST_FOTO_CONTENT_TYPE)
//            .cevaplar(DEFAULT_CEVAPLAR)
//            .soruPdfFile(DEFAULT_SORU_PDF_FILE)
//            .soruPdfFileContentType(DEFAULT_SORU_PDF_FILE_CONTENT_TYPE)
//            .soruSayisi(DEFAULT_SORU_SAYISI)
//            .seviye(DEFAULT_SEVIYE);
//        return soruTest;
//    }
//
//    /**
//     * Create an updated entity for this test.
//     *
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which requires the current entity.
//     */
//    public static SoruTest createUpdatedEntity(EntityManager em) {
//        SoruTest soruTest = new SoruTest()
//            .tesBaslik(UPDATED_TES_BASLIK)
//            .testPdf(UPDATED_TEST_PDF)
//            .testFoto(UPDATED_TEST_FOTO)
//            .testFotoContentType(UPDATED_TEST_FOTO_CONTENT_TYPE)
//            .cevaplar(UPDATED_CEVAPLAR)
//            .soruPdfFile(UPDATED_SORU_PDF_FILE)
//            .soruPdfFileContentType(UPDATED_SORU_PDF_FILE_CONTENT_TYPE)
//            .soruSayisi(UPDATED_SORU_SAYISI)
//            .seviye(UPDATED_SEVIYE);
//        return soruTest;
//    }
//
//    @BeforeEach
//    public void initTest() {
//        soruTest = createEntity(em);
//    }
//
//    @Test
//    @Transactional
//    void createSoruTest() throws Exception {
//        int databaseSizeBeforeCreate = soruTestRepository.findAll().size();
//        // Create the SoruTest
//        restSoruTestMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(soruTest)))
//            .andExpect(status().isCreated());
//
//        // Validate the SoruTest in the database
//        List<SoruTest> soruTestList = soruTestRepository.findAll();
//        assertThat(soruTestList).hasSize(databaseSizeBeforeCreate + 1);
//        SoruTest testSoruTest = soruTestList.get(soruTestList.size() - 1);
//        assertThat(testSoruTest.getTesBaslik()).isEqualTo(DEFAULT_TES_BASLIK);
//        assertThat(testSoruTest.getTestPdf()).isEqualTo(DEFAULT_TEST_PDF);
//        assertThat(testSoruTest.getTestFoto()).isEqualTo(DEFAULT_TEST_FOTO);
//        assertThat(testSoruTest.getTestFotoContentType()).isEqualTo(DEFAULT_TEST_FOTO_CONTENT_TYPE);
//        assertThat(testSoruTest.getCevaplar()).isEqualTo(DEFAULT_CEVAPLAR);
//        assertThat(testSoruTest.getSoruPdfFile()).isEqualTo(DEFAULT_SORU_PDF_FILE);
//        assertThat(testSoruTest.getSoruPdfFileContentType()).isEqualTo(DEFAULT_SORU_PDF_FILE_CONTENT_TYPE);
//        assertThat(testSoruTest.getSoruSayisi()).isEqualTo(DEFAULT_SORU_SAYISI);
//        assertThat(testSoruTest.getSeviye()).isEqualTo(DEFAULT_SEVIYE);
//    }
//
//    @Test
//    @Transactional
//    void createSoruTestWithExistingId() throws Exception {
//        // Create the SoruTest with an existing ID
//        soruTest.setId(1L);
//
//        int databaseSizeBeforeCreate = soruTestRepository.findAll().size();
//
//        // An entity with an existing ID cannot be created, so this API call must fail
//        restSoruTestMockMvc
//            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(soruTest)))
//            .andExpect(status().isBadRequest());
//
//        // Validate the SoruTest in the database
//        List<SoruTest> soruTestList = soruTestRepository.findAll();
//        assertThat(soruTestList).hasSize(databaseSizeBeforeCreate);
//    }
//
//    @Test
//    @Transactional
//    void getAllSoruTests() throws Exception {
//        // Initialize the database
//        soruTestRepository.saveAndFlush(soruTest);
//
//        // Get all the soruTestList
//        restSoruTestMockMvc
//            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(soruTest.getId().intValue())))
//            .andExpect(jsonPath("$.[*].tesBaslik").value(hasItem(DEFAULT_TES_BASLIK)))
//            .andExpect(jsonPath("$.[*].testPdf").value(hasItem(DEFAULT_TEST_PDF)))
//            .andExpect(jsonPath("$.[*].testFotoContentType").value(hasItem(DEFAULT_TEST_FOTO_CONTENT_TYPE)))
//            .andExpect(jsonPath("$.[*].testFoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_TEST_FOTO))))
//            .andExpect(jsonPath("$.[*].cevaplar").value(hasItem(DEFAULT_CEVAPLAR)))
//            .andExpect(jsonPath("$.[*].soruPdfFileContentType").value(hasItem(DEFAULT_SORU_PDF_FILE_CONTENT_TYPE)))
//            .andExpect(jsonPath("$.[*].soruPdfFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_SORU_PDF_FILE))))
//            .andExpect(jsonPath("$.[*].soruSayisi").value(hasItem(DEFAULT_SORU_SAYISI)))
//            .andExpect(jsonPath("$.[*].seviye").value(hasItem(DEFAULT_SEVIYE)));
//    }
//
//    @SuppressWarnings({ "unchecked" })
//    void getAllSoruTestsWithEagerRelationshipsIsEnabled() throws Exception {
//        when(soruTestRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
//
//        restSoruTestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());
//
//        verify(soruTestRepositoryMock, times(1)).findAllWithEagerRelationships(any());
//    }
//
//    @SuppressWarnings({ "unchecked" })
//    void getAllSoruTestsWithEagerRelationshipsIsNotEnabled() throws Exception {
//        when(soruTestRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
//
//        restSoruTestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());
//
//        verify(soruTestRepositoryMock, times(1)).findAllWithEagerRelationships(any());
//    }
//
//    @Test
//    @Transactional
//    void getSoruTest() throws Exception {
//        // Initialize the database
//        soruTestRepository.saveAndFlush(soruTest);
//
//        // Get the soruTest
//        restSoruTestMockMvc
//            .perform(get(ENTITY_API_URL_ID, soruTest.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//            .andExpect(jsonPath("$.id").value(soruTest.getId().intValue()))
//            .andExpect(jsonPath("$.tesBaslik").value(DEFAULT_TES_BASLIK))
//            .andExpect(jsonPath("$.testPdf").value(DEFAULT_TEST_PDF))
//            .andExpect(jsonPath("$.testFotoContentType").value(DEFAULT_TEST_FOTO_CONTENT_TYPE))
//            .andExpect(jsonPath("$.testFoto").value(Base64Utils.encodeToString(DEFAULT_TEST_FOTO)))
//            .andExpect(jsonPath("$.cevaplar").value(DEFAULT_CEVAPLAR))
//            .andExpect(jsonPath("$.soruPdfFileContentType").value(DEFAULT_SORU_PDF_FILE_CONTENT_TYPE))
//            .andExpect(jsonPath("$.soruPdfFile").value(Base64Utils.encodeToString(DEFAULT_SORU_PDF_FILE)))
//            .andExpect(jsonPath("$.soruSayisi").value(DEFAULT_SORU_SAYISI))
//            .andExpect(jsonPath("$.seviye").value(DEFAULT_SEVIYE));
//    }
//
//    @Test
//    @Transactional
//    void getNonExistingSoruTest() throws Exception {
//        // Get the soruTest
//        restSoruTestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    void putNewSoruTest() throws Exception {
//        // Initialize the database
//        soruTestRepository.saveAndFlush(soruTest);
//
//        int databaseSizeBeforeUpdate = soruTestRepository.findAll().size();
//
//        // Update the soruTest
//        SoruTest updatedSoruTest = soruTestRepository.findById(soruTest.getId()).get();
//        // Disconnect from session so that the updates on updatedSoruTest are not directly saved in db
//        em.detach(updatedSoruTest);
//        updatedSoruTest
//            .tesBaslik(UPDATED_TES_BASLIK)
//            .testPdf(UPDATED_TEST_PDF)
//            .testFoto(UPDATED_TEST_FOTO)
//            .testFotoContentType(UPDATED_TEST_FOTO_CONTENT_TYPE)
//            .cevaplar(UPDATED_CEVAPLAR)
//            .soruPdfFile(UPDATED_SORU_PDF_FILE)
//            .soruPdfFileContentType(UPDATED_SORU_PDF_FILE_CONTENT_TYPE)
//            .soruSayisi(UPDATED_SORU_SAYISI)
//            .seviye(UPDATED_SEVIYE);
//
//        restSoruTestMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, updatedSoruTest.getId())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(updatedSoruTest))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the SoruTest in the database
//        List<SoruTest> soruTestList = soruTestRepository.findAll();
//        assertThat(soruTestList).hasSize(databaseSizeBeforeUpdate);
//        SoruTest testSoruTest = soruTestList.get(soruTestList.size() - 1);
//        assertThat(testSoruTest.getTesBaslik()).isEqualTo(UPDATED_TES_BASLIK);
//        assertThat(testSoruTest.getTestPdf()).isEqualTo(UPDATED_TEST_PDF);
//        assertThat(testSoruTest.getTestFoto()).isEqualTo(UPDATED_TEST_FOTO);
//        assertThat(testSoruTest.getTestFotoContentType()).isEqualTo(UPDATED_TEST_FOTO_CONTENT_TYPE);
//        assertThat(testSoruTest.getCevaplar()).isEqualTo(UPDATED_CEVAPLAR);
//        assertThat(testSoruTest.getSoruPdfFile()).isEqualTo(UPDATED_SORU_PDF_FILE);
//        assertThat(testSoruTest.getSoruPdfFileContentType()).isEqualTo(UPDATED_SORU_PDF_FILE_CONTENT_TYPE);
//        assertThat(testSoruTest.getSoruSayisi()).isEqualTo(UPDATED_SORU_SAYISI);
//        assertThat(testSoruTest.getSeviye()).isEqualTo(UPDATED_SEVIYE);
//    }
//
//    @Test
//    @Transactional
//    void putNonExistingSoruTest() throws Exception {
//        int databaseSizeBeforeUpdate = soruTestRepository.findAll().size();
//        soruTest.setId(count.incrementAndGet());
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restSoruTestMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, soruTest.getId())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(soruTest))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the SoruTest in the database
//        List<SoruTest> soruTestList = soruTestRepository.findAll();
//        assertThat(soruTestList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithIdMismatchSoruTest() throws Exception {
//        int databaseSizeBeforeUpdate = soruTestRepository.findAll().size();
//        soruTest.setId(count.incrementAndGet());
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restSoruTestMockMvc
//            .perform(
//                put(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(soruTest))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the SoruTest in the database
//        List<SoruTest> soruTestList = soruTestRepository.findAll();
//        assertThat(soruTestList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void putWithMissingIdPathParamSoruTest() throws Exception {
//        int databaseSizeBeforeUpdate = soruTestRepository.findAll().size();
//        soruTest.setId(count.incrementAndGet());
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restSoruTestMockMvc
//            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(soruTest)))
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the SoruTest in the database
//        List<SoruTest> soruTestList = soruTestRepository.findAll();
//        assertThat(soruTestList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void partialUpdateSoruTestWithPatch() throws Exception {
//        // Initialize the database
//        soruTestRepository.saveAndFlush(soruTest);
//
//        int databaseSizeBeforeUpdate = soruTestRepository.findAll().size();
//
//        // Update the soruTest using partial update
//        SoruTest partialUpdatedSoruTest = new SoruTest();
//        partialUpdatedSoruTest.setId(soruTest.getId());
//
//        partialUpdatedSoruTest
//            .tesBaslik(UPDATED_TES_BASLIK)
//            .testPdf(UPDATED_TEST_PDF)
//            .cevaplar(UPDATED_CEVAPLAR)
//            .soruPdfFile(UPDATED_SORU_PDF_FILE)
//            .soruPdfFileContentType(UPDATED_SORU_PDF_FILE_CONTENT_TYPE)
//            .soruSayisi(UPDATED_SORU_SAYISI)
//            .seviye(UPDATED_SEVIYE);
//
//        restSoruTestMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedSoruTest.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSoruTest))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the SoruTest in the database
//        List<SoruTest> soruTestList = soruTestRepository.findAll();
//        assertThat(soruTestList).hasSize(databaseSizeBeforeUpdate);
//        SoruTest testSoruTest = soruTestList.get(soruTestList.size() - 1);
//        assertThat(testSoruTest.getTesBaslik()).isEqualTo(UPDATED_TES_BASLIK);
//        assertThat(testSoruTest.getTestPdf()).isEqualTo(UPDATED_TEST_PDF);
//        assertThat(testSoruTest.getTestFoto()).isEqualTo(DEFAULT_TEST_FOTO);
//        assertThat(testSoruTest.getTestFotoContentType()).isEqualTo(DEFAULT_TEST_FOTO_CONTENT_TYPE);
//        assertThat(testSoruTest.getCevaplar()).isEqualTo(UPDATED_CEVAPLAR);
//        assertThat(testSoruTest.getSoruPdfFile()).isEqualTo(UPDATED_SORU_PDF_FILE);
//        assertThat(testSoruTest.getSoruPdfFileContentType()).isEqualTo(UPDATED_SORU_PDF_FILE_CONTENT_TYPE);
//        assertThat(testSoruTest.getSoruSayisi()).isEqualTo(UPDATED_SORU_SAYISI);
//        assertThat(testSoruTest.getSeviye()).isEqualTo(UPDATED_SEVIYE);
//    }
//
//    @Test
//    @Transactional
//    void fullUpdateSoruTestWithPatch() throws Exception {
//        // Initialize the database
//        soruTestRepository.saveAndFlush(soruTest);
//
//        int databaseSizeBeforeUpdate = soruTestRepository.findAll().size();
//
//        // Update the soruTest using partial update
//        SoruTest partialUpdatedSoruTest = new SoruTest();
//        partialUpdatedSoruTest.setId(soruTest.getId());
//
//        partialUpdatedSoruTest
//            .tesBaslik(UPDATED_TES_BASLIK)
//            .testPdf(UPDATED_TEST_PDF)
//            .testFoto(UPDATED_TEST_FOTO)
//            .testFotoContentType(UPDATED_TEST_FOTO_CONTENT_TYPE)
//            .cevaplar(UPDATED_CEVAPLAR)
//            .soruPdfFile(UPDATED_SORU_PDF_FILE)
//            .soruPdfFileContentType(UPDATED_SORU_PDF_FILE_CONTENT_TYPE)
//            .soruSayisi(UPDATED_SORU_SAYISI)
//            .seviye(UPDATED_SEVIYE);
//
//        restSoruTestMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, partialUpdatedSoruTest.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSoruTest))
//            )
//            .andExpect(status().isOk());
//
//        // Validate the SoruTest in the database
//        List<SoruTest> soruTestList = soruTestRepository.findAll();
//        assertThat(soruTestList).hasSize(databaseSizeBeforeUpdate);
//        SoruTest testSoruTest = soruTestList.get(soruTestList.size() - 1);
//        assertThat(testSoruTest.getTesBaslik()).isEqualTo(UPDATED_TES_BASLIK);
//        assertThat(testSoruTest.getTestPdf()).isEqualTo(UPDATED_TEST_PDF);
//        assertThat(testSoruTest.getTestFoto()).isEqualTo(UPDATED_TEST_FOTO);
//        assertThat(testSoruTest.getTestFotoContentType()).isEqualTo(UPDATED_TEST_FOTO_CONTENT_TYPE);
//        assertThat(testSoruTest.getCevaplar()).isEqualTo(UPDATED_CEVAPLAR);
//        assertThat(testSoruTest.getSoruPdfFile()).isEqualTo(UPDATED_SORU_PDF_FILE);
//        assertThat(testSoruTest.getSoruPdfFileContentType()).isEqualTo(UPDATED_SORU_PDF_FILE_CONTENT_TYPE);
//        assertThat(testSoruTest.getSoruSayisi()).isEqualTo(UPDATED_SORU_SAYISI);
//        assertThat(testSoruTest.getSeviye()).isEqualTo(UPDATED_SEVIYE);
//    }
//
//    @Test
//    @Transactional
//    void patchNonExistingSoruTest() throws Exception {
//        int databaseSizeBeforeUpdate = soruTestRepository.findAll().size();
//        soruTest.setId(count.incrementAndGet());
//
//        // If the entity doesn't have an ID, it will throw BadRequestAlertException
//        restSoruTestMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, soruTest.getId())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(soruTest))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the SoruTest in the database
//        List<SoruTest> soruTestList = soruTestRepository.findAll();
//        assertThat(soruTestList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithIdMismatchSoruTest() throws Exception {
//        int databaseSizeBeforeUpdate = soruTestRepository.findAll().size();
//        soruTest.setId(count.incrementAndGet());
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restSoruTestMockMvc
//            .perform(
//                patch(ENTITY_API_URL_ID, count.incrementAndGet())
//                    .contentType("application/merge-patch+json")
//                    .content(TestUtil.convertObjectToJsonBytes(soruTest))
//            )
//            .andExpect(status().isBadRequest());
//
//        // Validate the SoruTest in the database
//        List<SoruTest> soruTestList = soruTestRepository.findAll();
//        assertThat(soruTestList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void patchWithMissingIdPathParamSoruTest() throws Exception {
//        int databaseSizeBeforeUpdate = soruTestRepository.findAll().size();
//        soruTest.setId(count.incrementAndGet());
//
//        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//        restSoruTestMockMvc
//            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(soruTest)))
//            .andExpect(status().isMethodNotAllowed());
//
//        // Validate the SoruTest in the database
//        List<SoruTest> soruTestList = soruTestRepository.findAll();
//        assertThat(soruTestList).hasSize(databaseSizeBeforeUpdate);
//    }
//
//    @Test
//    @Transactional
//    void deleteSoruTest() throws Exception {
//        // Initialize the database
//        soruTestRepository.saveAndFlush(soruTest);
//
//        int databaseSizeBeforeDelete = soruTestRepository.findAll().size();
//
//        // Delete the soruTest
//        restSoruTestMockMvc
//            .perform(delete(ENTITY_API_URL_ID, soruTest.getId()).accept(MediaType.APPLICATION_JSON))
//            .andExpect(status().isNoContent());
//
//        // Validate the database contains one less item
//        List<SoruTest> soruTestList = soruTestRepository.findAll();
//        assertThat(soruTestList).hasSize(databaseSizeBeforeDelete - 1);
//    }
//}
