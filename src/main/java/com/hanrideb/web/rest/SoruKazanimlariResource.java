package com.hanrideb.web.rest;

import com.hanrideb.domain.SoruKazanimlari;
import com.hanrideb.repository.SoruKazanimlariRepository;
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
 * REST controller for managing {@link com.hanrideb.domain.SoruKazanimlari}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SoruKazanimlariResource {

    private final Logger log = LoggerFactory.getLogger(SoruKazanimlariResource.class);

    private static final String ENTITY_NAME = "soruKazanimlari";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SoruKazanimlariRepository soruKazanimlariRepository;

    public SoruKazanimlariResource(SoruKazanimlariRepository soruKazanimlariRepository) {
        this.soruKazanimlariRepository = soruKazanimlariRepository;
    }

    /**
     * {@code POST  /soru-kazanimlaris} : Create a new soruKazanimlari.
     *
     * @param soruKazanimlari the soruKazanimlari to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new soruKazanimlari, or with status {@code 400 (Bad Request)} if the soruKazanimlari has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/soru-kazanimlaris")
    public ResponseEntity<SoruKazanimlari> createSoruKazanimlari(@RequestBody SoruKazanimlari soruKazanimlari) throws URISyntaxException {
        log.debug("REST request to save SoruKazanimlari : {}", soruKazanimlari);
        if (soruKazanimlari.getId() != null) {
            throw new BadRequestAlertException("A new soruKazanimlari cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SoruKazanimlari result = soruKazanimlariRepository.save(soruKazanimlari);
        return ResponseEntity
            .created(new URI("/api/soru-kazanimlaris/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /soru-kazanimlaris/:id} : Updates an existing soruKazanimlari.
     *
     * @param id the id of the soruKazanimlari to save.
     * @param soruKazanimlari the soruKazanimlari to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated soruKazanimlari,
     * or with status {@code 400 (Bad Request)} if the soruKazanimlari is not valid,
     * or with status {@code 500 (Internal Server Error)} if the soruKazanimlari couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/soru-kazanimlaris/{id}")
    public ResponseEntity<SoruKazanimlari> updateSoruKazanimlari(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SoruKazanimlari soruKazanimlari
    ) throws URISyntaxException {
        log.debug("REST request to update SoruKazanimlari : {}, {}", id, soruKazanimlari);
        if (soruKazanimlari.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, soruKazanimlari.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!soruKazanimlariRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SoruKazanimlari result = soruKazanimlariRepository.save(soruKazanimlari);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, soruKazanimlari.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /soru-kazanimlaris/:id} : Partial updates given fields of an existing soruKazanimlari, field will ignore if it is null
     *
     * @param id the id of the soruKazanimlari to save.
     * @param soruKazanimlari the soruKazanimlari to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated soruKazanimlari,
     * or with status {@code 400 (Bad Request)} if the soruKazanimlari is not valid,
     * or with status {@code 404 (Not Found)} if the soruKazanimlari is not found,
     * or with status {@code 500 (Internal Server Error)} if the soruKazanimlari couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/soru-kazanimlaris/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SoruKazanimlari> partialUpdateSoruKazanimlari(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SoruKazanimlari soruKazanimlari
    ) throws URISyntaxException {
        log.debug("REST request to partial update SoruKazanimlari partially : {}, {}", id, soruKazanimlari);
        if (soruKazanimlari.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, soruKazanimlari.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!soruKazanimlariRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SoruKazanimlari> result = soruKazanimlariRepository
            .findById(soruKazanimlari.getId())
            .map(existingSoruKazanimlari -> {
                if (soruKazanimlari.getKazanim() != null) {
                    existingSoruKazanimlari.setKazanim(soruKazanimlari.getKazanim());
                }

                return existingSoruKazanimlari;
            })
            .map(soruKazanimlariRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, soruKazanimlari.getId().toString())
        );
    }

    /**
     * {@code GET  /soru-kazanimlaris} : get all the soruKazanimlaris.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of soruKazanimlaris in body.
     */
    @GetMapping("/soru-kazanimlaris")
    public List<SoruKazanimlari> getAllSoruKazanimlaris() {
        log.debug("REST request to get all SoruKazanimlaris");
        return soruKazanimlariRepository.findAll();
    }

    /**
     * {@code GET  /soru-kazanimlaris/:id} : get the "id" soruKazanimlari.
     *
     * @param id the id of the soruKazanimlari to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the soruKazanimlari, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/soru-kazanimlaris/{id}")
    public ResponseEntity<SoruKazanimlari> getSoruKazanimlari(@PathVariable Long id) {
        log.debug("REST request to get SoruKazanimlari : {}", id);
        Optional<SoruKazanimlari> soruKazanimlari = soruKazanimlariRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(soruKazanimlari);
    }

    /**
     * {@code DELETE  /soru-kazanimlaris/:id} : delete the "id" soruKazanimlari.
     *
     * @param id the id of the soruKazanimlari to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/soru-kazanimlaris/{id}")
    public ResponseEntity<Void> deleteSoruKazanimlari(@PathVariable Long id) {
        log.debug("REST request to delete SoruKazanimlari : {}", id);
        soruKazanimlariRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
