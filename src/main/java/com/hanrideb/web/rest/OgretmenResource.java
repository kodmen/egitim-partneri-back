package com.hanrideb.web.rest;

import com.hanrideb.domain.Ogretmen;
import com.hanrideb.repository.OgretmenRepository;
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
 * REST controller for managing {@link com.hanrideb.domain.Ogretmen}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OgretmenResource {

    private final Logger log = LoggerFactory.getLogger(OgretmenResource.class);

    private static final String ENTITY_NAME = "ogretmen";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OgretmenRepository ogretmenRepository;

    public OgretmenResource(OgretmenRepository ogretmenRepository) {
        this.ogretmenRepository = ogretmenRepository;
    }

    /**
     * {@code POST  /ogretmen} : Create a new ogretmen.
     *
     * @param ogretmen the ogretmen to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ogretmen, or with status {@code 400 (Bad Request)} if the ogretmen has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ogretmen")
    public ResponseEntity<Ogretmen> createOgretmen(@RequestBody Ogretmen ogretmen) throws URISyntaxException {
        log.debug("REST request to save Ogretmen : {}", ogretmen);
        if (ogretmen.getId() != null) {
            throw new BadRequestAlertException("A new ogretmen cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ogretmen result = ogretmenRepository.save(ogretmen);
        return ResponseEntity
            .created(new URI("/api/ogretmen/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ogretmen/:id} : Updates an existing ogretmen.
     *
     * @param id the id of the ogretmen to save.
     * @param ogretmen the ogretmen to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ogretmen,
     * or with status {@code 400 (Bad Request)} if the ogretmen is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ogretmen couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ogretmen/{id}")
    public ResponseEntity<Ogretmen> updateOgretmen(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Ogretmen ogretmen
    ) throws URISyntaxException {
        log.debug("REST request to update Ogretmen : {}, {}", id, ogretmen);
        if (ogretmen.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ogretmen.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ogretmenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ogretmen result = ogretmenRepository.save(ogretmen);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ogretmen.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ogretmen/:id} : Partial updates given fields of an existing ogretmen, field will ignore if it is null
     *
     * @param id the id of the ogretmen to save.
     * @param ogretmen the ogretmen to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ogretmen,
     * or with status {@code 400 (Bad Request)} if the ogretmen is not valid,
     * or with status {@code 404 (Not Found)} if the ogretmen is not found,
     * or with status {@code 500 (Internal Server Error)} if the ogretmen couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ogretmen/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ogretmen> partialUpdateOgretmen(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Ogretmen ogretmen
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ogretmen partially : {}, {}", id, ogretmen);
        if (ogretmen.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ogretmen.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ogretmenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ogretmen> result = ogretmenRepository
            .findById(ogretmen.getId())
            .map(existingOgretmen -> {
                if (ogretmen.getAciklama() != null) {
                    existingOgretmen.setAciklama(ogretmen.getAciklama());
                }

                return existingOgretmen;
            })
            .map(ogretmenRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ogretmen.getId().toString())
        );
    }

    /**
     * {@code GET  /ogretmen} : get all the ogretmen.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ogretmen in body.
     */
    @GetMapping("/ogretmen")
    public List<Ogretmen> getAllOgretmen() {
        log.debug("REST request to get all Ogretmen");
        return ogretmenRepository.findAll();
    }

    /**
     * {@code GET  /ogretmen/:id} : get the "id" ogretmen.
     *
     * @param id the id of the ogretmen to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ogretmen, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ogretmen/{id}")
    public ResponseEntity<Ogretmen> getOgretmen(@PathVariable Long id) {
        log.debug("REST request to get Ogretmen : {}", id);
        Optional<Ogretmen> ogretmen = ogretmenRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ogretmen);
    }

    /**
     * {@code DELETE  /ogretmen/:id} : delete the "id" ogretmen.
     *
     * @param id the id of the ogretmen to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ogretmen/{id}")
    public ResponseEntity<Void> deleteOgretmen(@PathVariable Long id) {
        log.debug("REST request to delete Ogretmen : {}", id);
        ogretmenRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
