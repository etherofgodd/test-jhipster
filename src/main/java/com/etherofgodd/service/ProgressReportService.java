package com.etherofgodd.service;

import com.etherofgodd.domain.ProgressReport;
import com.etherofgodd.repository.ProgressReportRepository;
import com.etherofgodd.service.dto.ProgressReportDTO;
import com.etherofgodd.service.mapper.ProgressReportMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProgressReport}.
 */
@Service
@Transactional
public class ProgressReportService {

    private final Logger log = LoggerFactory.getLogger(ProgressReportService.class);

    private final ProgressReportRepository progressReportRepository;

    private final ProgressReportMapper progressReportMapper;

    public ProgressReportService(ProgressReportRepository progressReportRepository, ProgressReportMapper progressReportMapper) {
        this.progressReportRepository = progressReportRepository;
        this.progressReportMapper = progressReportMapper;
    }

    /**
     * Save a progressReport.
     *
     * @param progressReportDTO the entity to save.
     * @return the persisted entity.
     */
    public ProgressReportDTO save(ProgressReportDTO progressReportDTO) {
        log.debug("Request to save ProgressReport : {}", progressReportDTO);
        ProgressReport progressReport = progressReportMapper.toEntity(progressReportDTO);
        progressReport = progressReportRepository.save(progressReport);
        return progressReportMapper.toDto(progressReport);
    }

    /**
     * Update a progressReport.
     *
     * @param progressReportDTO the entity to save.
     * @return the persisted entity.
     */
    public ProgressReportDTO update(ProgressReportDTO progressReportDTO) {
        log.debug("Request to save ProgressReport : {}", progressReportDTO);
        ProgressReport progressReport = progressReportMapper.toEntity(progressReportDTO);
        progressReport = progressReportRepository.save(progressReport);
        return progressReportMapper.toDto(progressReport);
    }

    /**
     * Partially update a progressReport.
     *
     * @param progressReportDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProgressReportDTO> partialUpdate(ProgressReportDTO progressReportDTO) {
        log.debug("Request to partially update ProgressReport : {}", progressReportDTO);

        return progressReportRepository
            .findById(progressReportDTO.getId())
            .map(existingProgressReport -> {
                progressReportMapper.partialUpdate(existingProgressReport, progressReportDTO);

                return existingProgressReport;
            })
            .map(progressReportRepository::save)
            .map(progressReportMapper::toDto);
    }

    /**
     * Get all the progressReports.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProgressReportDTO> findAll() {
        log.debug("Request to get all ProgressReports");
        return progressReportRepository
            .findAll()
            .stream()
            .map(progressReportMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one progressReport by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProgressReportDTO> findOne(Long id) {
        log.debug("Request to get ProgressReport : {}", id);
        return progressReportRepository.findById(id).map(progressReportMapper::toDto);
    }

    /**
     * Delete the progressReport by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProgressReport : {}", id);
        progressReportRepository.deleteById(id);
    }
}
