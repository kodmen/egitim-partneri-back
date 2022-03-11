package com.hanrideb.web.rest;

import com.hanrideb.domain.Yorum;
import com.hanrideb.repository.YorumRepository;
import com.hanrideb.service.YorumService;
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
 * REST controller for managing {@link com.hanrideb.domain.Yorum}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class YorumResource {

    private final Logger log = LoggerFactory.getLogger(YorumResource.class);

    private static final String ENTITY_NAME = "yorum";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final YorumRepository yorumRepository;
    private final YorumService yorumService;

    public YorumResource(YorumRepository yorumRepository, YorumService yorumService) {
        this.yorumRepository = yorumRepository;
        this.yorumService = yorumService;
    }

    /**
     * {@code POST  /yorums} : Create a new yorum.
     *
     * @param yorum the yorum to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new yorum, or with status {@code 400 (Bad Request)} if the yorum has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/yorums")
    public ResponseEntity<Yorum> createYorum(@Valid @RequestBody Yorum yorum) throws URISyntaxException {
        log.debug("REST request to save Yorum : {}", yorum);
        if (yorum.getId() != null) {
            throw new BadRequestAlertException("A new yorum cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Yorum result = yorumRepository.save(yorum);
        return ResponseEntity
            .created(new URI("/api/yorums/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /yorums/:id} : Updates an existing yorum.
     *
     * @param id the id of the yorum to save.
     * @param yorum the yorum to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated yorum,
     * or with status {@code 400 (Bad Request)} if the yorum is not valid,
     * or with status {@code 500 (Internal Server Error)} if the yorum couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/yorums/{id}")
    public ResponseEntity<Yorum> updateYorum(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Yorum yorum)
        throws URISyntaxException {
        log.debug("REST request to update Yorum : {}, {}", id, yorum);
        if (yorum.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, yorum.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!yorumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Yorum result = yorumRepository.save(yorum);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, yorum.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /yorums/:id} : Partial updates given fields of an existing yorum, field will ignore if it is null
     *
     * @param id the id of the yorum to save.
     * @param yorum the yorum to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated yorum,
     * or with status {@code 400 (Bad Request)} if the yorum is not valid,
     * or with status {@code 404 (Not Found)} if the yorum is not found,
     * or with status {@code 500 (Internal Server Error)} if the yorum couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/yorums/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Yorum> partialUpdateYorum(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Yorum yorum
    ) throws URISyntaxException {
        log.debug("REST request to partial update Yorum partially : {}, {}", id, yorum);
        if (yorum.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, yorum.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!yorumRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Yorum> result = yorumRepository
            .findById(yorum.getId())
            .map(existingYorum -> {
                if (yorum.getYazi() != null) {
                    existingYorum.setYazi(yorum.getYazi());
                }
                if (yorum.getDate() != null) {
                    existingYorum.setDate(yorum.getDate());
                }

                return existingYorum;
            })
            .map(yorumRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, yorum.getId().toString())
        );
    }

    /**
     * {@code GET  /yorums} : get all the yorums.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of yorums in body.
     */
    @GetMapping("/yorums")
    public List<Yorum> getAllYorums() {
        log.debug("REST request to get all Yorums");
        return yorumRepository.findAll();
    }

    /**
     * {@code GET  /yorums/:id} : get the "id" yorum.
     *
     * @param id the id of the yorum to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the yorum, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/yorums/{id}")
    public ResponseEntity<Yorum> getYorum(@PathVariable Long id) {
        log.debug("REST request to get Yorum : {}", id);
        Optional<Yorum> yorum = yorumRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(yorum);
    }

    /**
     * {@code DELETE  /yorums/:id} : delete the "id" yorum.
     *
     * @param id the id of the yorum to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/yorums/{id}")
    public ResponseEntity<Void> deleteYorum(@PathVariable Long id) {
        log.debug("REST request to delete Yorum : {}", id);
        yorumRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/yorums/form/{id}")
    public List<Yorum> getListYorumByForum(@PathVariable Long id) {
        log.debug("REST request to get Yorum : {}", id);
        //        Optional<List<Yorum>> yorum = Optional.of(yorumService.getByFormIdAllYorum(id));
        return yorumService.getByFormIdAllYorum(id);
    }
}
