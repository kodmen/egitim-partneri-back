package com.hanrideb.web.rest;

import com.hanrideb.domain.Mufredat;
import com.hanrideb.repository.MufredatRepository;
import com.hanrideb.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.hanrideb.domain.Mufredat}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MufredatResource {

    private final Logger log = LoggerFactory.getLogger(MufredatResource.class);

    private static final String ENTITY_NAME = "mufredat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MufredatRepository mufredatRepository;

    public MufredatResource(MufredatRepository mufredatRepository) {
        this.mufredatRepository = mufredatRepository;
    }

    /**
     * {@code POST  /mufredats} : Create a new mufredat.
     *
     * @param mufredat the mufredat to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mufredat, or with status {@code 400 (Bad Request)} if the mufredat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mufredats")
    public ResponseEntity<Mufredat> createMufredat(@Valid @RequestBody Mufredat mufredat) throws URISyntaxException {
        log.debug("REST request to save Mufredat : {}", mufredat);
        if (mufredat.getId() != null) {
            throw new BadRequestAlertException("A new mufredat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Mufredat result = mufredatRepository.save(mufredat);
        return ResponseEntity
            .created(new URI("/api/mufredats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mufredats/:id} : Updates an existing mufredat.
     *
     * @param id the id of the mufredat to save.
     * @param mufredat the mufredat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mufredat,
     * or with status {@code 400 (Bad Request)} if the mufredat is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mufredat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mufredats/{id}")
    public ResponseEntity<Mufredat> updateMufredat(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Mufredat mufredat
    ) throws URISyntaxException {
        log.debug("REST request to update Mufredat : {}, {}", id, mufredat);
        if (mufredat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mufredat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mufredatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Mufredat result = mufredatRepository.save(mufredat);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mufredat.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mufredats/:id} : Partial updates given fields of an existing mufredat, field will ignore if it is null
     *
     * @param id the id of the mufredat to save.
     * @param mufredat the mufredat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mufredat,
     * or with status {@code 400 (Bad Request)} if the mufredat is not valid,
     * or with status {@code 404 (Not Found)} if the mufredat is not found,
     * or with status {@code 500 (Internal Server Error)} if the mufredat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mufredats/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Mufredat> partialUpdateMufredat(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Mufredat mufredat
    ) throws URISyntaxException {
        log.debug("REST request to partial update Mufredat partially : {}, {}", id, mufredat);
        if (mufredat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mufredat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mufredatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Mufredat> result = mufredatRepository
            .findById(mufredat.getId())
            .map(existingMufredat -> {
                if (mufredat.getMufredatBaslik() != null) {
                    existingMufredat.setMufredatBaslik(mufredat.getMufredatBaslik());
                }
                if (mufredat.getToplamSure() != null) {
                    existingMufredat.setToplamSure(mufredat.getToplamSure());
                }
                if (mufredat.getBolumSayi() != null) {
                    existingMufredat.setBolumSayi(mufredat.getBolumSayi());
                }

                return existingMufredat;
            })
            .map(mufredatRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mufredat.getId().toString())
        );
    }

    /**
     * {@code GET  /mufredats} : get all the mufredats.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mufredats in body.
     */
    @GetMapping("/mufredats")
    public List<Mufredat> getAllMufredats(
        @RequestParam(required = false) String filter,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        if ("mufredatders-is-null".equals(filter)) {
            log.debug("REST request to get all Mufredats where mufredatDers is null");
            return StreamSupport
                .stream(mufredatRepository.findAll().spliterator(), false)
                .filter(mufredat -> mufredat.getMufredatDers() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Mufredats");
        return mufredatRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /mufredats/:id} : get the "id" mufredat.
     *
     * @param id the id of the mufredat to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mufredat, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mufredats/{id}")
    public ResponseEntity<Mufredat> getMufredat(@PathVariable Long id) {
        log.debug("REST request to get Mufredat : {}", id);
        Optional<Mufredat> mufredat = mufredatRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(mufredat);
    }

    /**
     * {@code DELETE  /mufredats/:id} : delete the "id" mufredat.
     *
     * @param id the id of the mufredat to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mufredats/{id}")
    public ResponseEntity<Void> deleteMufredat(@PathVariable Long id) {
        log.debug("REST request to delete Mufredat : {}", id);
        mufredatRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
