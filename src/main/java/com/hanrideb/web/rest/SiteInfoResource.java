package com.hanrideb.web.rest;

import com.hanrideb.domain.SiteInfo;
import com.hanrideb.repository.SiteInfoRepository;
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
 * REST controller for managing {@link com.hanrideb.domain.SiteInfo}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SiteInfoResource {

    private final Logger log = LoggerFactory.getLogger(SiteInfoResource.class);

    private static final String ENTITY_NAME = "siteInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SiteInfoRepository siteInfoRepository;

    public SiteInfoResource(SiteInfoRepository siteInfoRepository) {
        this.siteInfoRepository = siteInfoRepository;
    }

    /**
     * {@code POST  /site-infos} : Create a new siteInfo.
     *
     * @param siteInfo the siteInfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new siteInfo, or with status {@code 400 (Bad Request)} if the siteInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/site-infos")
    public ResponseEntity<SiteInfo> createSiteInfo(@RequestBody SiteInfo siteInfo) throws URISyntaxException {
        log.debug("REST request to save SiteInfo : {}", siteInfo);
        if (siteInfo.getId() != null) {
            throw new BadRequestAlertException("A new siteInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SiteInfo result = siteInfoRepository.save(siteInfo);
        return ResponseEntity
            .created(new URI("/api/site-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /site-infos/:id} : Updates an existing siteInfo.
     *
     * @param id the id of the siteInfo to save.
     * @param siteInfo the siteInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated siteInfo,
     * or with status {@code 400 (Bad Request)} if the siteInfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the siteInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/site-infos/{id}")
    public ResponseEntity<SiteInfo> updateSiteInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SiteInfo siteInfo
    ) throws URISyntaxException {
        log.debug("REST request to update SiteInfo : {}, {}", id, siteInfo);
        if (siteInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, siteInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!siteInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SiteInfo result = siteInfoRepository.save(siteInfo);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, siteInfo.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /site-infos/:id} : Partial updates given fields of an existing siteInfo, field will ignore if it is null
     *
     * @param id the id of the siteInfo to save.
     * @param siteInfo the siteInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated siteInfo,
     * or with status {@code 400 (Bad Request)} if the siteInfo is not valid,
     * or with status {@code 404 (Not Found)} if the siteInfo is not found,
     * or with status {@code 500 (Internal Server Error)} if the siteInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/site-infos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SiteInfo> partialUpdateSiteInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SiteInfo siteInfo
    ) throws URISyntaxException {
        log.debug("REST request to partial update SiteInfo partially : {}, {}", id, siteInfo);
        if (siteInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, siteInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!siteInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SiteInfo> result = siteInfoRepository
            .findById(siteInfo.getId())
            .map(existingSiteInfo -> {
                if (siteInfo.getBaslik() != null) {
                    existingSiteInfo.setBaslik(siteInfo.getBaslik());
                }

                return existingSiteInfo;
            })
            .map(siteInfoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, siteInfo.getId().toString())
        );
    }

    /**
     * {@code GET  /site-infos} : get all the siteInfos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of siteInfos in body.
     */
    @GetMapping("/site-infos")
    public List<SiteInfo> getAllSiteInfos() {
        log.debug("REST request to get all SiteInfos");
        return siteInfoRepository.findAll();
    }

    /**
     * {@code GET  /site-infos/:id} : get the "id" siteInfo.
     *
     * @param id the id of the siteInfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the siteInfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/site-infos/{id}")
    public ResponseEntity<SiteInfo> getSiteInfo(@PathVariable Long id) {
        log.debug("REST request to get SiteInfo : {}", id);
        Optional<SiteInfo> siteInfo = siteInfoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(siteInfo);
    }

    /**
     * {@code DELETE  /site-infos/:id} : delete the "id" siteInfo.
     *
     * @param id the id of the siteInfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/site-infos/{id}")
    public ResponseEntity<Void> deleteSiteInfo(@PathVariable Long id) {
        log.debug("REST request to delete SiteInfo : {}", id);
        siteInfoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
