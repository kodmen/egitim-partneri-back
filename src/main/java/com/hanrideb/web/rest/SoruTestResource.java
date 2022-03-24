package com.hanrideb.web.rest;

import com.hanrideb.domain.SoruTest;
import com.hanrideb.repository.SoruTestRepository;
import com.hanrideb.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hanrideb.domain.SoruTest}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SoruTestResource {

    private final Logger log = LoggerFactory.getLogger(SoruTestResource.class);

    private static final String ENTITY_NAME = "soruTest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SoruTestRepository soruTestRepository;

    public SoruTestResource(SoruTestRepository soruTestRepository) {
        this.soruTestRepository = soruTestRepository;
    }

    /**
     * {@code POST  /soru-tests} : Create a new soruTest.
     *
     * @param soruTest the soruTest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new soruTest, or with status {@code 400 (Bad Request)} if the soruTest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/soru-tests")
    public ResponseEntity<SoruTest> createSoruTest(@Valid @RequestBody SoruTest soruTest) throws URISyntaxException {
        log.debug("REST request to save SoruTest : {}", soruTest);
        if (soruTest.getId() != null) {
            throw new BadRequestAlertException("A new soruTest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SoruTest result = soruTestRepository.save(soruTest);
        return ResponseEntity
            .created(new URI("/api/soru-tests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /soru-tests/:id} : Updates an existing soruTest.
     *
     * @param id the id of the soruTest to save.
     * @param soruTest the soruTest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated soruTest,
     * or with status {@code 400 (Bad Request)} if the soruTest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the soruTest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/soru-tests/{id}")
    public ResponseEntity<SoruTest> updateSoruTest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SoruTest soruTest
    ) throws URISyntaxException {
        log.debug("REST request to update SoruTest : {}, {}", id, soruTest);
        if (soruTest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, soruTest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!soruTestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SoruTest result = soruTestRepository.save(soruTest);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, soruTest.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /soru-tests/:id} : Partial updates given fields of an existing soruTest, field will ignore if it is null
     *
     * @param id the id of the soruTest to save.
     * @param soruTest the soruTest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated soruTest,
     * or with status {@code 400 (Bad Request)} if the soruTest is not valid,
     * or with status {@code 404 (Not Found)} if the soruTest is not found,
     * or with status {@code 500 (Internal Server Error)} if the soruTest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/soru-tests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SoruTest> partialUpdateSoruTest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SoruTest soruTest
    ) throws URISyntaxException {
        log.debug("REST request to partial update SoruTest partially : {}, {}", id, soruTest);
        if (soruTest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, soruTest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!soruTestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SoruTest> result = soruTestRepository
            .findById(soruTest.getId())
            .map(existingSoruTest -> {
                if (soruTest.getTesBaslik() != null) {
                    existingSoruTest.setTesBaslik(soruTest.getTesBaslik());
                }
                if (soruTest.getTestPdf() != null) {
                    existingSoruTest.setTestPdf(soruTest.getTestPdf());
                }
                if (soruTest.getTestFoto() != null) {
                    existingSoruTest.setTestFoto(soruTest.getTestFoto());
                }
                if (soruTest.getTestFotoContentType() != null) {
                    existingSoruTest.setTestFotoContentType(soruTest.getTestFotoContentType());
                }
                if (soruTest.getCevaplar() != null) {
                    existingSoruTest.setCevaplar(soruTest.getCevaplar());
                }
                if (soruTest.getSoruPdfFile() != null) {
                    existingSoruTest.setSoruPdfFile(soruTest.getSoruPdfFile());
                }
                if (soruTest.getSoruPdfFileContentType() != null) {
                    existingSoruTest.setSoruPdfFileContentType(soruTest.getSoruPdfFileContentType());
                }
                if (soruTest.getSoruSayisi() != null) {
                    existingSoruTest.setSoruSayisi(soruTest.getSoruSayisi());
                }

                return existingSoruTest;
            })
            .map(soruTestRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, soruTest.getId().toString())
        );
    }

    /**
     * {@code GET  /soru-tests} : get all the soruTests.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of soruTests in body.
     */
    @GetMapping("/soru-tests")
    public List<SoruTest> getAllSoruTests(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all SoruTests");
        return soruTestRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /soru-tests/:id} : get the "id" soruTest.
     *
     * @param id the id of the soruTest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the soruTest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/soru-tests/{id}")
    public ResponseEntity<SoruTest> getSoruTest(@PathVariable Long id) {
        log.debug("REST request to get SoruTest : {}", id);
        Optional<SoruTest> soruTest = soruTestRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(soruTest);
    }

    /**
     * {@code DELETE  /soru-tests/:id} : delete the "id" soruTest.
     *
     * @param id the id of the soruTest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/soru-tests/{id}")
    public ResponseEntity<Void> deleteSoruTest(@PathVariable Long id) {
        log.debug("REST request to delete SoruTest : {}", id);
        soruTestRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
