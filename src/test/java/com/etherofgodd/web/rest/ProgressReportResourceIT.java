package com.etherofgodd.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.etherofgodd.IntegrationTest;
import com.etherofgodd.domain.ProgressReport;
import com.etherofgodd.repository.ProgressReportRepository;
import com.etherofgodd.service.dto.ProgressReportDTO;
import com.etherofgodd.service.mapper.ProgressReportMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProgressReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProgressReportResourceIT {

    private static final String DEFAULT_DATE = "AAAAAAAAAA";
    private static final String UPDATED_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/progress-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProgressReportRepository progressReportRepository;

    @Autowired
    private ProgressReportMapper progressReportMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProgressReportMockMvc;

    private ProgressReport progressReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProgressReport createEntity(EntityManager em) {
        ProgressReport progressReport = new ProgressReport().date(DEFAULT_DATE).status(DEFAULT_STATUS).remarks(DEFAULT_REMARKS);
        return progressReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProgressReport createUpdatedEntity(EntityManager em) {
        ProgressReport progressReport = new ProgressReport().date(UPDATED_DATE).status(UPDATED_STATUS).remarks(UPDATED_REMARKS);
        return progressReport;
    }

    @BeforeEach
    public void initTest() {
        progressReport = createEntity(em);
    }

    @Test
    @Transactional
    void createProgressReport() throws Exception {
        int databaseSizeBeforeCreate = progressReportRepository.findAll().size();
        // Create the ProgressReport
        ProgressReportDTO progressReportDTO = progressReportMapper.toDto(progressReport);
        restProgressReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(progressReportDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProgressReport in the database
        List<ProgressReport> progressReportList = progressReportRepository.findAll();
        assertThat(progressReportList).hasSize(databaseSizeBeforeCreate + 1);
        ProgressReport testProgressReport = progressReportList.get(progressReportList.size() - 1);
        assertThat(testProgressReport.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testProgressReport.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProgressReport.getRemarks()).isEqualTo(DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void createProgressReportWithExistingId() throws Exception {
        // Create the ProgressReport with an existing ID
        progressReport.setId(1L);
        ProgressReportDTO progressReportDTO = progressReportMapper.toDto(progressReport);

        int databaseSizeBeforeCreate = progressReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgressReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(progressReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgressReport in the database
        List<ProgressReport> progressReportList = progressReportRepository.findAll();
        assertThat(progressReportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = progressReportRepository.findAll().size();
        // set the field null
        progressReport.setDate(null);

        // Create the ProgressReport, which fails.
        ProgressReportDTO progressReportDTO = progressReportMapper.toDto(progressReport);

        restProgressReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(progressReportDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProgressReport> progressReportList = progressReportRepository.findAll();
        assertThat(progressReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProgressReports() throws Exception {
        // Initialize the database
        progressReportRepository.saveAndFlush(progressReport);

        // Get all the progressReportList
        restProgressReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(progressReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)));
    }

    @Test
    @Transactional
    void getProgressReport() throws Exception {
        // Initialize the database
        progressReportRepository.saveAndFlush(progressReport);

        // Get the progressReport
        restProgressReportMockMvc
            .perform(get(ENTITY_API_URL_ID, progressReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(progressReport.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS));
    }

    @Test
    @Transactional
    void getNonExistingProgressReport() throws Exception {
        // Get the progressReport
        restProgressReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProgressReport() throws Exception {
        // Initialize the database
        progressReportRepository.saveAndFlush(progressReport);

        int databaseSizeBeforeUpdate = progressReportRepository.findAll().size();

        // Update the progressReport
        ProgressReport updatedProgressReport = progressReportRepository.findById(progressReport.getId()).get();
        // Disconnect from session so that the updates on updatedProgressReport are not directly saved in db
        em.detach(updatedProgressReport);
        updatedProgressReport.date(UPDATED_DATE).status(UPDATED_STATUS).remarks(UPDATED_REMARKS);
        ProgressReportDTO progressReportDTO = progressReportMapper.toDto(updatedProgressReport);

        restProgressReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, progressReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(progressReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProgressReport in the database
        List<ProgressReport> progressReportList = progressReportRepository.findAll();
        assertThat(progressReportList).hasSize(databaseSizeBeforeUpdate);
        ProgressReport testProgressReport = progressReportList.get(progressReportList.size() - 1);
        assertThat(testProgressReport.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testProgressReport.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProgressReport.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void putNonExistingProgressReport() throws Exception {
        int databaseSizeBeforeUpdate = progressReportRepository.findAll().size();
        progressReport.setId(count.incrementAndGet());

        // Create the ProgressReport
        ProgressReportDTO progressReportDTO = progressReportMapper.toDto(progressReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgressReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, progressReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(progressReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgressReport in the database
        List<ProgressReport> progressReportList = progressReportRepository.findAll();
        assertThat(progressReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProgressReport() throws Exception {
        int databaseSizeBeforeUpdate = progressReportRepository.findAll().size();
        progressReport.setId(count.incrementAndGet());

        // Create the ProgressReport
        ProgressReportDTO progressReportDTO = progressReportMapper.toDto(progressReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgressReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(progressReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgressReport in the database
        List<ProgressReport> progressReportList = progressReportRepository.findAll();
        assertThat(progressReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProgressReport() throws Exception {
        int databaseSizeBeforeUpdate = progressReportRepository.findAll().size();
        progressReport.setId(count.incrementAndGet());

        // Create the ProgressReport
        ProgressReportDTO progressReportDTO = progressReportMapper.toDto(progressReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgressReportMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(progressReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProgressReport in the database
        List<ProgressReport> progressReportList = progressReportRepository.findAll();
        assertThat(progressReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProgressReportWithPatch() throws Exception {
        // Initialize the database
        progressReportRepository.saveAndFlush(progressReport);

        int databaseSizeBeforeUpdate = progressReportRepository.findAll().size();

        // Update the progressReport using partial update
        ProgressReport partialUpdatedProgressReport = new ProgressReport();
        partialUpdatedProgressReport.setId(progressReport.getId());

        partialUpdatedProgressReport.remarks(UPDATED_REMARKS);

        restProgressReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgressReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProgressReport))
            )
            .andExpect(status().isOk());

        // Validate the ProgressReport in the database
        List<ProgressReport> progressReportList = progressReportRepository.findAll();
        assertThat(progressReportList).hasSize(databaseSizeBeforeUpdate);
        ProgressReport testProgressReport = progressReportList.get(progressReportList.size() - 1);
        assertThat(testProgressReport.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testProgressReport.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProgressReport.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void fullUpdateProgressReportWithPatch() throws Exception {
        // Initialize the database
        progressReportRepository.saveAndFlush(progressReport);

        int databaseSizeBeforeUpdate = progressReportRepository.findAll().size();

        // Update the progressReport using partial update
        ProgressReport partialUpdatedProgressReport = new ProgressReport();
        partialUpdatedProgressReport.setId(progressReport.getId());

        partialUpdatedProgressReport.date(UPDATED_DATE).status(UPDATED_STATUS).remarks(UPDATED_REMARKS);

        restProgressReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgressReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProgressReport))
            )
            .andExpect(status().isOk());

        // Validate the ProgressReport in the database
        List<ProgressReport> progressReportList = progressReportRepository.findAll();
        assertThat(progressReportList).hasSize(databaseSizeBeforeUpdate);
        ProgressReport testProgressReport = progressReportList.get(progressReportList.size() - 1);
        assertThat(testProgressReport.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testProgressReport.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProgressReport.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void patchNonExistingProgressReport() throws Exception {
        int databaseSizeBeforeUpdate = progressReportRepository.findAll().size();
        progressReport.setId(count.incrementAndGet());

        // Create the ProgressReport
        ProgressReportDTO progressReportDTO = progressReportMapper.toDto(progressReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgressReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, progressReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(progressReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgressReport in the database
        List<ProgressReport> progressReportList = progressReportRepository.findAll();
        assertThat(progressReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProgressReport() throws Exception {
        int databaseSizeBeforeUpdate = progressReportRepository.findAll().size();
        progressReport.setId(count.incrementAndGet());

        // Create the ProgressReport
        ProgressReportDTO progressReportDTO = progressReportMapper.toDto(progressReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgressReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(progressReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgressReport in the database
        List<ProgressReport> progressReportList = progressReportRepository.findAll();
        assertThat(progressReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProgressReport() throws Exception {
        int databaseSizeBeforeUpdate = progressReportRepository.findAll().size();
        progressReport.setId(count.incrementAndGet());

        // Create the ProgressReport
        ProgressReportDTO progressReportDTO = progressReportMapper.toDto(progressReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgressReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(progressReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProgressReport in the database
        List<ProgressReport> progressReportList = progressReportRepository.findAll();
        assertThat(progressReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProgressReport() throws Exception {
        // Initialize the database
        progressReportRepository.saveAndFlush(progressReport);

        int databaseSizeBeforeDelete = progressReportRepository.findAll().size();

        // Delete the progressReport
        restProgressReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, progressReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProgressReport> progressReportList = progressReportRepository.findAll();
        assertThat(progressReportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
