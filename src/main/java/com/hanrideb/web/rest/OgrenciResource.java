package com.hanrideb.web.rest;

import com.hanrideb.domain.Ogrenci;
import com.hanrideb.repository.OgrenciRepository;
import com.hanrideb.service.OgrenciService;
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
 * REST controller for managing {@link com.hanrideb.domain.Ogrenci}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OgrenciResource {

    private final Logger log = LoggerFactory.getLogger(OgrenciResource.class);

    private static final String ENTITY_NAME = "ogrenci";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OgrenciRepository ogrenciRepository;

    private final OgrenciService ogrenciService;

    public OgrenciResource(OgrenciRepository ogrenciRepository, OgrenciService ogrenciService) {
        this.ogrenciRepository = ogrenciRepository;
        this.ogrenciService = ogrenciService;
    }

    /**
     * {@code POST  /ogrencis} : Create a new ogrenci.
     *
     * @param ogrenci the ogrenci to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ogrenci, or with status {@code 400 (Bad Request)} if the ogrenci has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ogrencis")
    public ResponseEntity<Ogrenci> createOgrenci(@RequestBody Ogrenci ogrenci) throws URISyntaxException {
        log.debug("REST request to save Ogrenci : {}", ogrenci);
        if (ogrenci.getId() != null) {
            throw new BadRequestAlertException("A new ogrenci cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ogrenci result = ogrenciRepository.save(ogrenci);
        return ResponseEntity
            .created(new URI("/api/ogrencis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ogrencis/:id} : Updates an existing ogrenci.
     *
     * @param id the id of the ogrenci to save.
     * @param ogrenci the ogrenci to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ogrenci,
     * or with status {@code 400 (Bad Request)} if the ogrenci is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ogrenci couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ogrencis/{id}")
    public ResponseEntity<Ogrenci> updateOgrenci(@PathVariable(value = "id", required = false) final Long id, @RequestBody Ogrenci ogrenci)
        throws URISyntaxException {
        log.debug("REST request to update Ogrenci : {}, {}", id, ogrenci);
        if (ogrenci.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ogrenci.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ogrenciRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ogrenci result = ogrenciRepository.save(ogrenci);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ogrenci.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ogrencis/:id} : Partial updates given fields of an existing ogrenci, field will ignore if it is null
     *
     * @param id the id of the ogrenci to save.
     * @param ogrenci the ogrenci to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ogrenci,
     * or with status {@code 400 (Bad Request)} if the ogrenci is not valid,
     * or with status {@code 404 (Not Found)} if the ogrenci is not found,
     * or with status {@code 500 (Internal Server Error)} if the ogrenci couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ogrencis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ogrenci> partialUpdateOgrenci(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Ogrenci ogrenci
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ogrenci partially : {}, {}", id, ogrenci);
        if (ogrenci.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ogrenci.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ogrenciRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ogrenci> result = ogrenciRepository
            .findById(ogrenci.getId())
            .map(existingOgrenci -> {
                if (ogrenci.getLevel() != null) {
                    existingOgrenci.setLevel(ogrenci.getLevel());
                }
                if (ogrenci.getAciklama() != null) {
                    existingOgrenci.setAciklama(ogrenci.getAciklama());
                }
                if (ogrenci.getToplamPuan() != null) {
                    existingOgrenci.setToplamPuan(ogrenci.getToplamPuan());
                }

                return existingOgrenci;
            })
            .map(ogrenciRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ogrenci.getId().toString())
        );
    }

    /**
     * {@code GET  /ogrencis} : get all the ogrencis.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ogrencis in body.
     */
    @GetMapping("/ogrencis")
    public List<Ogrenci> getAllOgrencis(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Ogrencis");
        return ogrenciRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /ogrencis} : get all the ogrencis.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ogrencis in body.
     */
    @GetMapping("/ogrenci")
    public Ogrenci getActiveOgrenci() throws Exception {
        log.debug("REST request to get all Ogrencis");
        try {
            return ogrenciService.getByUserId();
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/ogrenci/listele")
    public List<Ogrenci> getTop10Ogrenci() throws Exception {
        log.debug("REST request to get all Ogrencis");
        try {
            return ogrenciService.ilkOnOgrListele();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * {@code GET  /ogrencis/:id} : get the "id" ogrenci.
     *
     * @param id the id of the ogrenci to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ogrenci, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ogrencis/{id}")
    public ResponseEntity<Ogrenci> getOgrenci(@PathVariable Long id) {
        log.debug("REST request to get Ogrenci : {}", id);
        Optional<Ogrenci> ogrenci = ogrenciRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(ogrenci);
    }

    /**
     * {@code DELETE  /ogrencis/:id} : delete the "id" ogrenci.
     *
     * @param id the id of the ogrenci to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ogrencis/{id}")
    public ResponseEntity<Void> deleteOgrenci(@PathVariable Long id) {
        log.debug("REST request to delete Ogrenci : {}", id);
        ogrenciRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
