package com.hanrideb.web.rest;

import com.hanrideb.domain.Kayit;
import com.hanrideb.repository.KayitRepository;
import com.hanrideb.service.KayitService;
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
 * REST controller for managing {@link com.hanrideb.domain.Kayit}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class KayitResource {

    private final Logger log = LoggerFactory.getLogger(KayitResource.class);

    private static final String ENTITY_NAME = "kayit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KayitRepository kayitRepository;
    private final KayitService kayitService;

    public KayitResource(KayitRepository kayitRepository, KayitService kayitService) {
        this.kayitRepository = kayitRepository;
        this.kayitService = kayitService;
    }

    /**
     * {@code POST  /kayits} : Create a new kayit.
     *
     * @param kayit the kayit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new kayit, or with status {@code 400 (Bad Request)} if the kayit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/kayits")
    public ResponseEntity<Kayit> createKayit(@RequestBody Kayit kayit) throws URISyntaxException {
        log.debug("REST request to save Kayit : {}", kayit);
        if (kayit.getId() != null) {
            throw new BadRequestAlertException("A new kayit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Kayit result = kayitRepository.save(kayit);
        return ResponseEntity
            .created(new URI("/api/kayits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/kayit")
    public ResponseEntity<Kayit> createKayitByDersId(@RequestBody long dersId) throws Exception {
        log.debug("REST request to save Kayit : {}", dersId);

        Kayit result = kayitService.save(dersId);
        return ResponseEntity
            .created(new URI("/api/kayit/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /kayits/:id} : Updates an existing kayit.
     *
     * @param id the id of the kayit to save.
     * @param kayit the kayit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kayit,
     * or with status {@code 400 (Bad Request)} if the kayit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the kayit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/kayits/{id}")
    public ResponseEntity<Kayit> updateKayit(@PathVariable(value = "id", required = false) final Long id, @RequestBody Kayit kayit)
        throws URISyntaxException {
        log.debug("REST request to update Kayit : {}, {}", id, kayit);
        if (kayit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kayit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kayitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Kayit result = kayitRepository.save(kayit);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kayit.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /kayits/:id} : Partial updates given fields of an existing kayit, field will ignore if it is null
     *
     * @param id the id of the kayit to save.
     * @param kayit the kayit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kayit,
     * or with status {@code 400 (Bad Request)} if the kayit is not valid,
     * or with status {@code 404 (Not Found)} if the kayit is not found,
     * or with status {@code 500 (Internal Server Error)} if the kayit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/kayits/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Kayit> partialUpdateKayit(@PathVariable(value = "id", required = false) final Long id, @RequestBody Kayit kayit)
        throws URISyntaxException {
        log.debug("REST request to partial update Kayit partially : {}, {}", id, kayit);
        if (kayit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kayit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kayitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Kayit> result = kayitRepository
            .findById(kayit.getId())
            .map(existingKayit -> {
                if (kayit.getPuan() != null) {
                    existingKayit.setPuan(kayit.getPuan());
                }
                if (kayit.getKayitTarih() != null) {
                    existingKayit.setKayitTarih(kayit.getKayitTarih());
                }

                return existingKayit;
            })
            .map(kayitRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kayit.getId().toString())
        );
    }

    /**
     * {@code GET  /kayits} : get all the kayits.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of kayits in body.
     */
    @GetMapping("/kayits")
    public List<Kayit> getAllKayits(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Kayits");
        return kayitRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /kayits/:id} : get the "id" kayit.
     *
     * @param id the id of the kayit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the kayit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/kayits/{id}")
    public ResponseEntity<Kayit> getKayit(@PathVariable Long id) {
        log.debug("REST request to get Kayit : {}", id);
        Optional<Kayit> kayit = kayitRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(kayit);
    }

    /**
     * {@code DELETE  /kayits/:id} : delete the "id" kayit.
     *
     * @param id the id of the kayit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/kayits/{id}")
    public ResponseEntity<Void> deleteKayit(@PathVariable Long id) {
        log.debug("REST request to delete Kayit : {}", id);
        kayitRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
