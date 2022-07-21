package com.etherofgodd.web.rest;

import com.etherofgodd.repository.ProgressReportRepository;
import com.etherofgodd.service.ProgressReportService;
import com.etherofgodd.service.dto.ProgressReportDTO;
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
 * REST controller for managing {@link com.etherofgodd.domain.ProgressReport}.
 */
@RestController
@RequestMapping("/api")
public class ProgressReportResource {

    private final Logger log = LoggerFactory.getLogger(ProgressReportResource.class);

    private static final String ENTITY_NAME = "testMsProgressReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProgressReportService progressReportService;

    private final ProgressReportRepository progressReportRepository;

    public ProgressReportResource(ProgressReportService progressReportService, ProgressReportRepository progressReportRepository) {
        this.progressReportService = progressReportService;
        this.progressReportRepository = progressReportRepository;
    }

    /**
     * {@code POST  /progress-reports} : Create a new progressReport.
     *
     * @param progressReportDTO the progressReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new progressReportDTO, or with status {@code 400 (Bad Request)} if the progressReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/progress-reports")
    public ResponseEntity<ProgressReportDTO> createProgressReport(@Valid @RequestBody ProgressReportDTO progressReportDTO)
        throws URISyntaxException {
        log.debug("REST request to save ProgressReport : {}", progressReportDTO);
        if (progressReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new progressReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProgressReportDTO result = progressReportService.save(progressReportDTO);
        return ResponseEntity
            .created(new URI("/api/progress-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /progress-reports/:id} : Updates an existing progressReport.
     *
     * @param id the id of the progressReportDTO to save.
     * @param progressReportDTO the progressReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated progressReportDTO,
     * or with status {@code 400 (Bad Request)} if the progressReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the progressReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/progress-reports/{id}")
    public ResponseEntity<ProgressReportDTO> updateProgressReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProgressReportDTO progressReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProgressReport : {}, {}", id, progressReportDTO);
        if (progressReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, progressReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!progressReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProgressReportDTO result = progressReportService.update(progressReportDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, progressReportDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /progress-reports/:id} : Partial updates given fields of an existing progressReport, field will ignore if it is null
     *
     * @param id the id of the progressReportDTO to save.
     * @param progressReportDTO the progressReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated progressReportDTO,
     * or with status {@code 400 (Bad Request)} if the progressReportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the progressReportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the progressReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/progress-reports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProgressReportDTO> partialUpdateProgressReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProgressReportDTO progressReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProgressReport partially : {}, {}", id, progressReportDTO);
        if (progressReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, progressReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!progressReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProgressReportDTO> result = progressReportService.partialUpdate(progressReportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, progressReportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /progress-reports} : get all the progressReports.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of progressReports in body.
     */
    @GetMapping("/progress-reports")
    public List<ProgressReportDTO> getAllProgressReports() {
        log.debug("REST request to get all ProgressReports");
        return progressReportService.findAll();
    }

    /**
     * {@code GET  /progress-reports/:id} : get the "id" progressReport.
     *
     * @param id the id of the progressReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the progressReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/progress-reports/{id}")
    public ResponseEntity<ProgressReportDTO> getProgressReport(@PathVariable Long id) {
        log.debug("REST request to get ProgressReport : {}", id);
        Optional<ProgressReportDTO> progressReportDTO = progressReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(progressReportDTO);
    }

    /**
     * {@code DELETE  /progress-reports/:id} : delete the "id" progressReport.
     *
     * @param id the id of the progressReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/progress-reports/{id}")
    public ResponseEntity<Void> deleteProgressReport(@PathVariable Long id) {
        log.debug("REST request to delete ProgressReport : {}", id);
        progressReportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
