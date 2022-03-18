package com.hanrideb.web.rest;

import com.hanrideb.domain.ImageModel;
import com.hanrideb.repository.ImageModelRepository;
import com.hanrideb.service.ImageService;
import com.hanrideb.web.rest.errors.BadRequestAlertException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.imageio.ImageIO;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hanrideb.domain.ImageModel}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ImageModelResource {

    private final Logger log = LoggerFactory.getLogger(ImageModelResource.class);

    private static final String ENTITY_NAME = "imageModel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImageModelRepository imageModelRepository;
    private final ImageService imageService;

    public ImageModelResource(ImageModelRepository imageModelRepository, ImageService imageService) {
        this.imageModelRepository = imageModelRepository;
        this.imageService = imageService;
    }

    /**
     * {@code POST  /image-models} : Create a new imageModel.
     *
     * @param imageModel the imageModel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new imageModel, or with status {@code 400 (Bad Request)} if the imageModel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/image-models")
    public ResponseEntity<ImageModel> createImageModel(@RequestBody ImageModel imageModel) throws URISyntaxException {
        log.debug("REST request to save ImageModel : {}", imageModel);
        if (imageModel.getId() != null) {
            throw new BadRequestAlertException("A new imageModel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ImageModel result = imageService.Save(imageModel);
        return ResponseEntity
            .created(new URI("/api/image-models/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /image-models/:id} : Updates an existing imageModel.
     *
     * @param id the id of the imageModel to save.
     * @param imageModel the imageModel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageModel,
     * or with status {@code 400 (Bad Request)} if the imageModel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the imageModel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/image-models/{id}")
    public ResponseEntity<ImageModel> updateImageModel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ImageModel imageModel
    ) throws URISyntaxException {
        log.debug("REST request to update ImageModel : {}, {}", id, imageModel);
        if (imageModel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageModel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ImageModel result = imageModelRepository.save(imageModel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imageModel.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /image-models/:id} : Partial updates given fields of an existing imageModel, field will ignore if it is null
     *
     * @param id the id of the imageModel to save.
     * @param imageModel the imageModel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageModel,
     * or with status {@code 400 (Bad Request)} if the imageModel is not valid,
     * or with status {@code 404 (Not Found)} if the imageModel is not found,
     * or with status {@code 500 (Internal Server Error)} if the imageModel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/image-models/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ImageModel> partialUpdateImageModel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ImageModel imageModel
    ) throws URISyntaxException {
        log.debug("REST request to partial update ImageModel partially : {}, {}", id, imageModel);
        if (imageModel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageModel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImageModel> result = imageModelRepository
            .findById(imageModel.getId())
            .map(existingImageModel -> {
                if (imageModel.getName() != null) {
                    existingImageModel.setName(imageModel.getName());
                }
                if (imageModel.getType() != null) {
                    existingImageModel.setType(imageModel.getType());
                }
                if (imageModel.getImg() != null) {
                    existingImageModel.setImg(imageModel.getImg());
                }
                if (imageModel.getImgContentType() != null) {
                    existingImageModel.setImgContentType(imageModel.getImgContentType());
                }

                return existingImageModel;
            })
            .map(imageModelRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imageModel.getId().toString())
        );
    }

    /**
     * {@code GET  /image-models} : get all the imageModels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of imageModels in body.
     */
    @GetMapping("/image-models")
    public List<ImageModel> getAllImageModels() {
        log.debug("REST request to get all ImageModels");
        return imageModelRepository.findAll();
    }

    /**
     * {@code GET  /image-models/:id} : get the "id" imageModel.
     *
     * @param id the id of the imageModel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the imageModel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/image-models/{id}")
    public ResponseEntity<ImageModel> getImageModel(@PathVariable Long id) {
        log.debug("REST request to get ImageModel : {}", id);
        Optional<ImageModel> imageModel = imageModelRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(imageModel);
    }

    /**
     * {@code DELETE  /image-models/:id} : delete the "id" imageModel.
     *
     * @param id the id of the imageModel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/image-models/{id}")
    public ResponseEntity<Void> deleteImageModel(@PathVariable Long id) {
        log.debug("REST request to delete ImageModel : {}", id);
        imageModelRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    //    // burası güzel çalışıyor
    //    @GetMapping(value = "/image")
    //    public @ResponseBody ResponseEntity<byte[]> getImage(@RequestParam(required = true) String name) throws IOException {
    //        byte[] image = imageService.getByName(name).getImg();
    //        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    //    }

    // burası güzel çalışıyor http://localhost:8080/api/image?name=2
    @GetMapping(value = "/image")
    public @ResponseBody ResponseEntity<byte[]> getImage1(@RequestParam(required = true) String name) throws IOException {
        ImageModel imageModel = imageService.getByName(name);
        byte[] image = imageModel.getImg();
        return ResponseEntity.ok().contentType(MediaType.valueOf(imageModel.getImgContentType())).body(image);
    }
}
