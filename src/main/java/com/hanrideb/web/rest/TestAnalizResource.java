package com.hanrideb.web.rest;

import com.hanrideb.domain.DersAnaliz;
import com.hanrideb.domain.TestAnaliz;
import com.hanrideb.repository.TestAnalizRepository;
import com.hanrideb.service.DersAnalizService;
import com.hanrideb.service.TestAnalizService;
import com.hanrideb.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hanrideb.domain.TestAnaliz}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TestAnalizResource {

    private final Logger log = LoggerFactory.getLogger(TestAnalizResource.class);

    private static final String ENTITY_NAME = "testAnaliz";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestAnalizRepository testAnalizRepository;
    private final TestAnalizService testAnalizService;
    private final DersAnalizService dersAnalizService;

    public TestAnalizResource(
        TestAnalizRepository testAnalizRepository,
        TestAnalizService testAnalizService,
        DersAnalizService dersAnalizService
    ) {
        this.testAnalizRepository = testAnalizRepository;
        this.testAnalizService = testAnalizService;
        this.dersAnalizService = dersAnalizService;
    }

    /**
     * {@code POST  /test-analizs} : Create a new testAnaliz.
     *
     * @param testAnaliz the testAnaliz to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testAnaliz, or with status {@code 400 (Bad Request)} if the testAnaliz has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/test-analizs")
    public ResponseEntity<TestAnaliz> createTestAnaliz(@RequestBody TestAnaliz testAnaliz) throws URISyntaxException {
        log.debug("REST request to save TestAnaliz : {}", testAnaliz);
        if (testAnaliz.getId() != null) {
            throw new BadRequestAlertException("A new testAnaliz cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TestAnaliz result = testAnalizRepository.save(testAnaliz);
        return ResponseEntity
            .created(new URI("/api/test-analizs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /test-analizs/:id} : Updates an existing testAnaliz.
     *
     * @param id the id of the testAnaliz to save.
     * @param testAnaliz the testAnaliz to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testAnaliz,
     * or with status {@code 400 (Bad Request)} if the testAnaliz is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testAnaliz couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/test-analizs/{id}")
    public ResponseEntity<TestAnaliz> updateTestAnaliz(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestAnaliz testAnaliz
    ) throws URISyntaxException {
        log.debug("REST request to update TestAnaliz : {}, {}", id, testAnaliz);
        if (testAnaliz.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testAnaliz.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testAnalizRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TestAnaliz result = testAnalizRepository.save(testAnaliz);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testAnaliz.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /test-analizs/:id} : Partial updates given fields of an existing testAnaliz, field will ignore if it is null
     *
     * @param id the id of the testAnaliz to save.
     * @param testAnaliz the testAnaliz to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testAnaliz,
     * or with status {@code 400 (Bad Request)} if the testAnaliz is not valid,
     * or with status {@code 404 (Not Found)} if the testAnaliz is not found,
     * or with status {@code 500 (Internal Server Error)} if the testAnaliz couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/test-analizs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestAnaliz> partialUpdateTestAnaliz(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestAnaliz testAnaliz
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestAnaliz partially : {}, {}", id, testAnaliz);
        if (testAnaliz.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testAnaliz.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testAnalizRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestAnaliz> result = testAnalizRepository
            .findById(testAnaliz.getId())
            .map(existingTestAnaliz -> {
                if (testAnaliz.getDogru() != null) {
                    existingTestAnaliz.setDogru(testAnaliz.getDogru());
                }
                if (testAnaliz.getYanlis() != null) {
                    existingTestAnaliz.setYanlis(testAnaliz.getYanlis());
                }
                if (testAnaliz.getBos() != null) {
                    existingTestAnaliz.setBos(testAnaliz.getBos());
                }
                if (testAnaliz.getNet() != null) {
                    existingTestAnaliz.setNet(testAnaliz.getNet());
                }
                if (testAnaliz.getTamamlandi() != null) {
                    existingTestAnaliz.setTamamlandi(testAnaliz.getTamamlandi());
                }
                if (testAnaliz.getTestId() != null) {
                    existingTestAnaliz.setTestId(testAnaliz.getTestId());
                }

                return existingTestAnaliz;
            })
            .map(testAnalizRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testAnaliz.getId().toString())
        );
    }

    /**
     * {@code GET  /test-analizs} : get all the testAnalizs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testAnalizs in body.
     */
    @GetMapping("/test-analizs")
    public List<TestAnaliz> getAllTestAnalizs() {
        log.debug("REST request to get all TestAnalizs");
        return testAnalizRepository.findAll();
    }

    /**
     * {@code GET  /test-analizs/:id} : get the "id" testAnaliz.
     *
     * @param id the id of the testAnaliz to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testAnaliz, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-analizs/{id}")
    public ResponseEntity<TestAnaliz> getTestAnaliz(@PathVariable Long id) {
        log.debug("REST request to get TestAnaliz : {}", id);
        Optional<TestAnaliz> testAnaliz = testAnalizRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(testAnaliz);
    }

    /**
     * {@code GET  /test-analizs/:id} : get the "id" testAnaliz.
     *
     * @param id the id of the testAnaliz to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testAnaliz, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/test-analizs/bolum/{bolum}")
    public ResponseEntity<List<TestAnaliz>> getTestAnalizByBolum(@PathVariable String bolum) throws Exception {
        //        DersAnaliz dersAnaliz = dersAnalizService.getByBolumBaslik(bolum);
        //        List<TestAnaliz> testAnalizList2 = testAnalizService.findAllByDersAnalizId(dersAnaliz.getId());
        List<TestAnaliz> testAnalizList = testAnalizService.getUserTestAnalizInDers(bolum);

        return ResponseEntity.ok(testAnalizList);
        //        log.debug("REST request to get TestAnaliz : {}", id);
        //        Optional<TestAnaliz> testAnaliz = testAnalizRepository.findById(id);
        //        return ResponseUtil.wrapOrNotFound(testAnaliz);
    }

    /**
     * {@code DELETE  /test-analizs/:id} : delete the "id" testAnaliz.
     *
     * @param id the id of the testAnaliz to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/test-analizs/{id}")
    public ResponseEntity<Void> deleteTestAnaliz(@PathVariable Long id) {
        log.debug("REST request to delete TestAnaliz : {}", id);
        testAnalizRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
