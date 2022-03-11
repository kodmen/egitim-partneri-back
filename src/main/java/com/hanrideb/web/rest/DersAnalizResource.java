package com.hanrideb.web.rest;

import com.hanrideb.domain.DersAnaliz;
import com.hanrideb.repository.DersAnalizRepository;
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
 * REST controller for managing {@link com.hanrideb.domain.DersAnaliz}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DersAnalizResource {

    private final Logger log = LoggerFactory.getLogger(DersAnalizResource.class);

    private static final String ENTITY_NAME = "dersAnaliz";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DersAnalizRepository dersAnalizRepository;

    public DersAnalizResource(DersAnalizRepository dersAnalizRepository) {
        this.dersAnalizRepository = dersAnalizRepository;
    }

    /**
     * {@code POST  /ders-analizs} : Create a new dersAnaliz.
     *
     * @param dersAnaliz the dersAnaliz to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dersAnaliz, or with status {@code 400 (Bad Request)} if the dersAnaliz has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ders-analizs")
    public ResponseEntity<DersAnaliz> createDersAnaliz(@RequestBody DersAnaliz dersAnaliz) throws URISyntaxException {
        log.debug("REST request to save DersAnaliz : {}", dersAnaliz);
        if (dersAnaliz.getId() != null) {
            throw new BadRequestAlertException("A new dersAnaliz cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DersAnaliz result = dersAnalizRepository.save(dersAnaliz);
        return ResponseEntity
            .created(new URI("/api/ders-analizs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ders-analizs/:id} : Updates an existing dersAnaliz.
     *
     * @param id the id of the dersAnaliz to save.
     * @param dersAnaliz the dersAnaliz to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dersAnaliz,
     * or with status {@code 400 (Bad Request)} if the dersAnaliz is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dersAnaliz couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ders-analizs/{id}")
    public ResponseEntity<DersAnaliz> updateDersAnaliz(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DersAnaliz dersAnaliz
    ) throws URISyntaxException {
        log.debug("REST request to update DersAnaliz : {}, {}", id, dersAnaliz);
        if (dersAnaliz.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dersAnaliz.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dersAnalizRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DersAnaliz result = dersAnalizRepository.save(dersAnaliz);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dersAnaliz.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ders-analizs/:id} : Partial updates given fields of an existing dersAnaliz, field will ignore if it is null
     *
     * @param id the id of the dersAnaliz to save.
     * @param dersAnaliz the dersAnaliz to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dersAnaliz,
     * or with status {@code 400 (Bad Request)} if the dersAnaliz is not valid,
     * or with status {@code 404 (Not Found)} if the dersAnaliz is not found,
     * or with status {@code 500 (Internal Server Error)} if the dersAnaliz couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ders-analizs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DersAnaliz> partialUpdateDersAnaliz(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DersAnaliz dersAnaliz
    ) throws URISyntaxException {
        log.debug("REST request to partial update DersAnaliz partially : {}, {}", id, dersAnaliz);
        if (dersAnaliz.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dersAnaliz.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dersAnalizRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DersAnaliz> result = dersAnalizRepository
            .findById(dersAnaliz.getId())
            .map(existingDersAnaliz -> {
                if (dersAnaliz.getToplamYanlis() != null) {
                    existingDersAnaliz.setToplamYanlis(dersAnaliz.getToplamYanlis());
                }
                if (dersAnaliz.getToplamDogru() != null) {
                    existingDersAnaliz.setToplamDogru(dersAnaliz.getToplamDogru());
                }
                if (dersAnaliz.getCozulenSoru() != null) {
                    existingDersAnaliz.setCozulenSoru(dersAnaliz.getCozulenSoru());
                }
                if (dersAnaliz.getTamamlandi() != null) {
                    existingDersAnaliz.setTamamlandi(dersAnaliz.getTamamlandi());
                }

                return existingDersAnaliz;
            })
            .map(dersAnalizRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dersAnaliz.getId().toString())
        );
    }

    /**
     * {@code GET  /ders-analizs} : get all the dersAnalizs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dersAnalizs in body.
     */
    @GetMapping("/ders-analizs")
    public List<DersAnaliz> getAllDersAnalizs() {
        log.debug("REST request to get all DersAnalizs");
        return dersAnalizRepository.findAll();
    }

    /**
     * {@code GET  /ders-analizs/:id} : get the "id" dersAnaliz.
     *
     * @param id the id of the dersAnaliz to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dersAnaliz, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ders-analizs/{id}")
    public ResponseEntity<DersAnaliz> getDersAnaliz(@PathVariable Long id) {
        log.debug("REST request to get DersAnaliz : {}", id);
        Optional<DersAnaliz> dersAnaliz = dersAnalizRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dersAnaliz);
    }

    /**
     * {@code DELETE  /ders-analizs/:id} : delete the "id" dersAnaliz.
     *
     * @param id the id of the dersAnaliz to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ders-analizs/{id}")
    public ResponseEntity<Void> deleteDersAnaliz(@PathVariable Long id) {
        log.debug("REST request to delete DersAnaliz : {}", id);
        dersAnalizRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
