package com.hanrideb.web.rest;

import com.hanrideb.domain.Bolum;
import com.hanrideb.repository.BolumRepository;
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
 * REST controller for managing {@link com.hanrideb.domain.Bolum}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BolumResource {

    private final Logger log = LoggerFactory.getLogger(BolumResource.class);

    private static final String ENTITY_NAME = "bolum";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BolumRepository bolumRepository;

    public BolumResource(BolumRepository bolumRepository) {
        this.bolumRepository = bolumRepository;
    }

    /**
     * {@code POST  /bolums} : Create a new bolum.
     *
     * @param bolum the bolum to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bolum, or with status {@code 400 (Bad Request)} if the bolum has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bolums")
    public ResponseEntity<Bolum> createBolum(@Valid @RequestBody Bolum bolum) throws URISyntaxException {
        log.debug("REST request to save Bolum : {}", bolum);
        if (bolum.getId() != null) {
            throw new BadRequestAlertException("A new bolum cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Bolum result = bolumRepository.save(bolum);
        return ResponseEntity
            .created(new URI("/api/bolums/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bolums/:id} : Updates an existing bolum.
     *
     * @param id the id of the bolum to save.
     * @param bolum the bolum to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bolum,
     * or with status {@code 400 (Bad Request)} if the bolum is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bolum couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bolums/{id}")
    public ResponseEntity<Bolum> updateBolum(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Bolum bolum)
        throws URISyntaxException {
        log.debug("REST request to update Bolum : {}, {}", id, bolum);
        if (bolum.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bolum.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bolumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Bolum result = bolumRepository.save(bolum);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bolum.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bolums/:id} : Partial updates given fields of an existing bolum, field will ignore if it is null
     *
     * @param id the id of the bolum to save.
     * @param bolum the bolum to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bolum,
     * or with status {@code 400 (Bad Request)} if the bolum is not valid,
     * or with status {@code 404 (Not Found)} if the bolum is not found,
     * or with status {@code 500 (Internal Server Error)} if the bolum couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bolums/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Bolum> partialUpdateBolum(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Bolum bolum
    ) throws URISyntaxException {
        log.debug("REST request to partial update Bolum partially : {}, {}", id, bolum);
        if (bolum.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bolum.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bolumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Bolum> result = bolumRepository
            .findById(bolum.getId())
            .map(existingBolum -> {
                if (bolum.getBolumBaslik() != null) {
                    existingBolum.setBolumBaslik(bolum.getBolumBaslik());
                }
                if (bolum.getDokuman() != null) {
                    existingBolum.setDokuman(bolum.getDokuman());
                }
                if (bolum.getPuan() != null) {
                    existingBolum.setPuan(bolum.getPuan());
                }
                if (bolum.getVideoLink() != null) {
                    existingBolum.setVideoLink(bolum.getVideoLink());
                }
                if (bolum.getSure() != null) {
                    existingBolum.setSure(bolum.getSure());
                }

                return existingBolum;
            })
            .map(bolumRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bolum.getId().toString())
        );
    }

    /**
     * {@code GET  /bolums} : get all the bolums.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bolums in body.
     */
    @GetMapping("/bolums")
    public List<Bolum> getAllBolums() {
        log.debug("REST request to get all Bolums");
        return bolumRepository.findAll();
    }

    /**
     * {@code GET  /bolums/:id} : get the "id" bolum.
     *
     * @param id the id of the bolum to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bolum, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bolums/{id}")
    public ResponseEntity<Bolum> getBolum(@PathVariable Long id) {
        log.debug("REST request to get Bolum : {}", id);
        Optional<Bolum> bolum = bolumRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bolum);
    }

    /**
     * {@code DELETE  /bolums/:id} : delete the "id" bolum.
     *
     * @param id the id of the bolum to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bolums/{id}")
    public ResponseEntity<Void> deleteBolum(@PathVariable Long id) {
        log.debug("REST request to delete Bolum : {}", id);
        bolumRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
