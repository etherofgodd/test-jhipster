package com.etherofgodd.service;

import com.etherofgodd.domain.Intern;
import com.etherofgodd.repository.InternRepository;
import com.etherofgodd.service.dto.InternDTO;
import com.etherofgodd.service.mapper.InternMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Intern}.
 */
@Service
@Transactional
public class InternService {

    private final Logger log = LoggerFactory.getLogger(InternService.class);

    private final InternRepository internRepository;

    private final InternMapper internMapper;

    public InternService(InternRepository internRepository, InternMapper internMapper) {
        this.internRepository = internRepository;
        this.internMapper = internMapper;
    }

    /**
     * Save a intern.
     *
     * @param internDTO the entity to save.
     * @return the persisted entity.
     */
    public InternDTO save(InternDTO internDTO) {
        log.debug("Request to save Intern : {}", internDTO);
        Intern intern = internMapper.toEntity(internDTO);
        intern = internRepository.save(intern);
        return internMapper.toDto(intern);
    }

    /**
     * Update a intern.
     *
     * @param internDTO the entity to save.
     * @return the persisted entity.
     */
    public InternDTO update(InternDTO internDTO) {
        log.debug("Request to save Intern : {}", internDTO);
        Intern intern = internMapper.toEntity(internDTO);
        intern = internRepository.save(intern);
        return internMapper.toDto(intern);
    }

    /**
     * Partially update a intern.
     *
     * @param internDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InternDTO> partialUpdate(InternDTO internDTO) {
        log.debug("Request to partially update Intern : {}", internDTO);

        return internRepository
            .findById(internDTO.getId())
            .map(existingIntern -> {
                internMapper.partialUpdate(existingIntern, internDTO);

                return existingIntern;
            })
            .map(internRepository::save)
            .map(internMapper::toDto);
    }

    /**
     * Get all the interns.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<InternDTO> findAll() {
        log.debug("Request to get all Interns");
        return internRepository.findAll().stream().map(internMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one intern by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InternDTO> findOne(Long id) {
        log.debug("Request to get Intern : {}", id);
        return internRepository.findById(id).map(internMapper::toDto);
    }

    /**
     * Delete the intern by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Intern : {}", id);
        internRepository.deleteById(id);
    }
}
