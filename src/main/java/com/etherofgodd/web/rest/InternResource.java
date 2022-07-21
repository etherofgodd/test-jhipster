package com.etherofgodd.web.rest;

import com.etherofgodd.repository.InternRepository;
import com.etherofgodd.service.InternService;
import com.etherofgodd.service.dto.InternDTO;
import com.etherofgodd.web.rest.errors.BadRequestAlertException;
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
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.etherofgodd.domain.Intern}.
 */
@RestController
@RequestMapping("/api")
public class InternResource {

    private final Logger log = LoggerFactory.getLogger(InternResource.class);

    private static final String ENTITY_NAME = "testMsIntern";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InternService internService;

    private final InternRepository internRepository;

    public InternResource(InternService internService, InternRepository internRepository) {
        this.internService = internService;
        this.internRepository = internRepository;
    }

    /**
     * {@code POST  /interns} : Create a new intern.
     *
     * @param internDTO the internDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new internDTO, or with status {@code 400 (Bad Request)} if the intern has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/interns")
    public ResponseEntity<InternDTO> createIntern(@Valid @RequestBody InternDTO internDTO) throws URISyntaxException {
        log.debug("REST request to save Intern : {}", internDTO);
        if (internDTO.getId() != null) {
            throw new BadRequestAlertException("A new intern cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InternDTO result = internService.save(internDTO);
        return ResponseEntity
            .created(new URI("/api/interns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /interns/:id} : Updates an existing intern.
     *
     * @param id the id of the internDTO to save.
     * @param internDTO the internDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated internDTO,
     * or with status {@code 400 (Bad Request)} if the internDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the internDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/interns/{id}")
    public ResponseEntity<InternDTO> updateIntern(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InternDTO internDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Intern : {}, {}", id, internDTO);
        if (internDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, internDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!internRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InternDTO result = internService.update(internDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, internDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /interns/:id} : Partial updates given fields of an existing intern, field will ignore if it is null
     *
     * @param id the id of the internDTO to save.
     * @param internDTO the internDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated internDTO,
     * or with status {@code 400 (Bad Request)} if the internDTO is not valid,
     * or with status {@code 404 (Not Found)} if the internDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the internDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/interns/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InternDTO> partialUpdateIntern(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InternDTO internDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Intern partially : {}, {}", id, internDTO);
        if (internDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, internDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!internRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InternDTO> result = internService.partialUpdate(internDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, internDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /interns} : get all the interns.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of interns in body.
     */
    @GetMapping("/interns")
    public List<InternDTO> getAllInterns() {
        log.debug("REST request to get all Interns");
        return internService.findAll();
    }

    /**
     * {@code GET  /interns/:id} : get the "id" intern.
     *
     * @param id the id of the internDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the internDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/interns/{id}")
    public ResponseEntity<InternDTO> getIntern(@PathVariable Long id) {
        log.debug("REST request to get Intern : {}", id);
        Optional<InternDTO> internDTO = internService.findOne(id);
        return ResponseUtil.wrapOrNotFound(internDTO);
    }

    /**
     * {@code DELETE  /interns/:id} : delete the "id" intern.
     *
     * @param id the id of the internDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/interns/{id}")
    public ResponseEntity<Void> deleteIntern(@PathVariable Long id) {
        log.debug("REST request to delete Intern : {}", id);
        internService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
