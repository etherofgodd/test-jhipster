package com.etherofgodd.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.etherofgodd.IntegrationTest;
import com.etherofgodd.domain.Intern;
import com.etherofgodd.domain.enumeration.Company;
import com.etherofgodd.repository.InternRepository;
import com.etherofgodd.service.dto.InternDTO;
import com.etherofgodd.service.mapper.InternMapper;
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
 * Integration tests for the {@link InternResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InternResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_INTERN_ID = "AAAAAAAAAA";
    private static final String UPDATED_INTERN_ID = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DOB = "AAAAAAAAAA";
    private static final String UPDATED_DOB = "BBBBBBBBBB";

    private static final String DEFAULT_SCHOOL = "AAAAAAAAAA";
    private static final String UPDATED_SCHOOL = "BBBBBBBBBB";

    private static final Company DEFAULT_DEPARTMENT = Company.STSL;
    private static final Company UPDATED_DEPARTMENT = Company.RPSL;

    private static final String ENTITY_API_URL = "/api/interns";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InternRepository internRepository;

    @Autowired
    private InternMapper internMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInternMockMvc;

    private Intern intern;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intern createEntity(EntityManager em) {
        Intern intern = new Intern()
            .firstName(DEFAULT_FIRST_NAME)
            .internId(DEFAULT_INTERN_ID)
            .lastName(DEFAULT_LAST_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .dob(DEFAULT_DOB)
            .school(DEFAULT_SCHOOL)
            .department(DEFAULT_DEPARTMENT);
        return intern;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intern createUpdatedEntity(EntityManager em) {
        Intern intern = new Intern()
            .firstName(UPDATED_FIRST_NAME)
            .internId(UPDATED_INTERN_ID)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .dob(UPDATED_DOB)
            .school(UPDATED_SCHOOL)
            .department(UPDATED_DEPARTMENT);
        return intern;
    }

    @BeforeEach
    public void initTest() {
        intern = createEntity(em);
    }

    @Test
    @Transactional
    void createIntern() throws Exception {
        int databaseSizeBeforeCreate = internRepository.findAll().size();
        // Create the Intern
        InternDTO internDTO = internMapper.toDto(intern);
        restInternMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internDTO)))
            .andExpect(status().isCreated());

        // Validate the Intern in the database
        List<Intern> internList = internRepository.findAll();
        assertThat(internList).hasSize(databaseSizeBeforeCreate + 1);
        Intern testIntern = internList.get(internList.size() - 1);
        assertThat(testIntern.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testIntern.getInternId()).isEqualTo(DEFAULT_INTERN_ID);
        assertThat(testIntern.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testIntern.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testIntern.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testIntern.getSchool()).isEqualTo(DEFAULT_SCHOOL);
        assertThat(testIntern.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
    }

    @Test
    @Transactional
    void createInternWithExistingId() throws Exception {
        // Create the Intern with an existing ID
        intern.setId(1L);
        InternDTO internDTO = internMapper.toDto(intern);

        int databaseSizeBeforeCreate = internRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInternMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Intern in the database
        List<Intern> internList = internRepository.findAll();
        assertThat(internList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = internRepository.findAll().size();
        // set the field null
        intern.setFirstName(null);

        // Create the Intern, which fails.
        InternDTO internDTO = internMapper.toDto(intern);

        restInternMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internDTO)))
            .andExpect(status().isBadRequest());

        List<Intern> internList = internRepository.findAll();
        assertThat(internList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInternIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = internRepository.findAll().size();
        // set the field null
        intern.setInternId(null);

        // Create the Intern, which fails.
        InternDTO internDTO = internMapper.toDto(intern);

        restInternMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internDTO)))
            .andExpect(status().isBadRequest());

        List<Intern> internList = internRepository.findAll();
        assertThat(internList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = internRepository.findAll().size();
        // set the field null
        intern.setLastName(null);

        // Create the Intern, which fails.
        InternDTO internDTO = internMapper.toDto(intern);

        restInternMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internDTO)))
            .andExpect(status().isBadRequest());

        List<Intern> internList = internRepository.findAll();
        assertThat(internList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = internRepository.findAll().size();
        // set the field null
        intern.setPhoneNumber(null);

        // Create the Intern, which fails.
        InternDTO internDTO = internMapper.toDto(intern);

        restInternMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internDTO)))
            .andExpect(status().isBadRequest());

        List<Intern> internList = internRepository.findAll();
        assertThat(internList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDobIsRequired() throws Exception {
        int databaseSizeBeforeTest = internRepository.findAll().size();
        // set the field null
        intern.setDob(null);

        // Create the Intern, which fails.
        InternDTO internDTO = internMapper.toDto(intern);

        restInternMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internDTO)))
            .andExpect(status().isBadRequest());

        List<Intern> internList = internRepository.findAll();
        assertThat(internList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSchoolIsRequired() throws Exception {
        int databaseSizeBeforeTest = internRepository.findAll().size();
        // set the field null
        intern.setSchool(null);

        // Create the Intern, which fails.
        InternDTO internDTO = internMapper.toDto(intern);

        restInternMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internDTO)))
            .andExpect(status().isBadRequest());

        List<Intern> internList = internRepository.findAll();
        assertThat(internList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDepartmentIsRequired() throws Exception {
        int databaseSizeBeforeTest = internRepository.findAll().size();
        // set the field null
        intern.setDepartment(null);

        // Create the Intern, which fails.
        InternDTO internDTO = internMapper.toDto(intern);

        restInternMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internDTO)))
            .andExpect(status().isBadRequest());

        List<Intern> internList = internRepository.findAll();
        assertThat(internList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInterns() throws Exception {
        // Initialize the database
        internRepository.saveAndFlush(intern);

        // Get all the internList
        restInternMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(intern.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].internId").value(hasItem(DEFAULT_INTERN_ID)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB)))
            .andExpect(jsonPath("$.[*].school").value(hasItem(DEFAULT_SCHOOL)))
            .andExpect(jsonPath("$.[*].department").value(hasItem(DEFAULT_DEPARTMENT.toString())));
    }

    @Test
    @Transactional
    void getIntern() throws Exception {
        // Initialize the database
        internRepository.saveAndFlush(intern);

        // Get the intern
        restInternMockMvc
            .perform(get(ENTITY_API_URL_ID, intern.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(intern.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.internId").value(DEFAULT_INTERN_ID))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB))
            .andExpect(jsonPath("$.school").value(DEFAULT_SCHOOL))
            .andExpect(jsonPath("$.department").value(DEFAULT_DEPARTMENT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingIntern() throws Exception {
        // Get the intern
        restInternMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIntern() throws Exception {
        // Initialize the database
        internRepository.saveAndFlush(intern);

        int databaseSizeBeforeUpdate = internRepository.findAll().size();

        // Update the intern
        Intern updatedIntern = internRepository.findById(intern.getId()).get();
        // Disconnect from session so that the updates on updatedIntern are not directly saved in db
        em.detach(updatedIntern);
        updatedIntern
            .firstName(UPDATED_FIRST_NAME)
            .internId(UPDATED_INTERN_ID)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .dob(UPDATED_DOB)
            .school(UPDATED_SCHOOL)
            .department(UPDATED_DEPARTMENT);
        InternDTO internDTO = internMapper.toDto(updatedIntern);

        restInternMockMvc
            .perform(
                put(ENTITY_API_URL_ID, internDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(internDTO))
            )
            .andExpect(status().isOk());

        // Validate the Intern in the database
        List<Intern> internList = internRepository.findAll();
        assertThat(internList).hasSize(databaseSizeBeforeUpdate);
        Intern testIntern = internList.get(internList.size() - 1);
        assertThat(testIntern.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testIntern.getInternId()).isEqualTo(UPDATED_INTERN_ID);
        assertThat(testIntern.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testIntern.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testIntern.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testIntern.getSchool()).isEqualTo(UPDATED_SCHOOL);
        assertThat(testIntern.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void putNonExistingIntern() throws Exception {
        int databaseSizeBeforeUpdate = internRepository.findAll().size();
        intern.setId(count.incrementAndGet());

        // Create the Intern
        InternDTO internDTO = internMapper.toDto(intern);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInternMockMvc
            .perform(
                put(ENTITY_API_URL_ID, internDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(internDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intern in the database
        List<Intern> internList = internRepository.findAll();
        assertThat(internList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIntern() throws Exception {
        int databaseSizeBeforeUpdate = internRepository.findAll().size();
        intern.setId(count.incrementAndGet());

        // Create the Intern
        InternDTO internDTO = internMapper.toDto(intern);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInternMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(internDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intern in the database
        List<Intern> internList = internRepository.findAll();
        assertThat(internList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIntern() throws Exception {
        int databaseSizeBeforeUpdate = internRepository.findAll().size();
        intern.setId(count.incrementAndGet());

        // Create the Intern
        InternDTO internDTO = internMapper.toDto(intern);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInternMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(internDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Intern in the database
        List<Intern> internList = internRepository.findAll();
        assertThat(internList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInternWithPatch() throws Exception {
        // Initialize the database
        internRepository.saveAndFlush(intern);

        int databaseSizeBeforeUpdate = internRepository.findAll().size();

        // Update the intern using partial update
        Intern partialUpdatedIntern = new Intern();
        partialUpdatedIntern.setId(intern.getId());

        partialUpdatedIntern.firstName(UPDATED_FIRST_NAME);

        restInternMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntern.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIntern))
            )
            .andExpect(status().isOk());

        // Validate the Intern in the database
        List<Intern> internList = internRepository.findAll();
        assertThat(internList).hasSize(databaseSizeBeforeUpdate);
        Intern testIntern = internList.get(internList.size() - 1);
        assertThat(testIntern.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testIntern.getInternId()).isEqualTo(DEFAULT_INTERN_ID);
        assertThat(testIntern.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testIntern.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testIntern.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testIntern.getSchool()).isEqualTo(DEFAULT_SCHOOL);
        assertThat(testIntern.getDepartment()).isEqualTo(DEFAULT_DEPARTMENT);
    }

    @Test
    @Transactional
    void fullUpdateInternWithPatch() throws Exception {
        // Initialize the database
        internRepository.saveAndFlush(intern);

        int databaseSizeBeforeUpdate = internRepository.findAll().size();

        // Update the intern using partial update
        Intern partialUpdatedIntern = new Intern();
        partialUpdatedIntern.setId(intern.getId());

        partialUpdatedIntern
            .firstName(UPDATED_FIRST_NAME)
            .internId(UPDATED_INTERN_ID)
            .lastName(UPDATED_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .dob(UPDATED_DOB)
            .school(UPDATED_SCHOOL)
            .department(UPDATED_DEPARTMENT);

        restInternMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntern.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIntern))
            )
            .andExpect(status().isOk());

        // Validate the Intern in the database
        List<Intern> internList = internRepository.findAll();
        assertThat(internList).hasSize(databaseSizeBeforeUpdate);
        Intern testIntern = internList.get(internList.size() - 1);
        assertThat(testIntern.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testIntern.getInternId()).isEqualTo(UPDATED_INTERN_ID);
        assertThat(testIntern.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testIntern.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testIntern.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testIntern.getSchool()).isEqualTo(UPDATED_SCHOOL);
        assertThat(testIntern.getDepartment()).isEqualTo(UPDATED_DEPARTMENT);
    }

    @Test
    @Transactional
    void patchNonExistingIntern() throws Exception {
        int databaseSizeBeforeUpdate = internRepository.findAll().size();
        intern.setId(count.incrementAndGet());

        // Create the Intern
        InternDTO internDTO = internMapper.toDto(intern);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInternMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, internDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(internDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intern in the database
        List<Intern> internList = internRepository.findAll();
        assertThat(internList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIntern() throws Exception {
        int databaseSizeBeforeUpdate = internRepository.findAll().size();
        intern.setId(count.incrementAndGet());

        // Create the Intern
        InternDTO internDTO = internMapper.toDto(intern);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInternMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(internDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intern in the database
        List<Intern> internList = internRepository.findAll();
        assertThat(internList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIntern() throws Exception {
        int databaseSizeBeforeUpdate = internRepository.findAll().size();
        intern.setId(count.incrementAndGet());

        // Create the Intern
        InternDTO internDTO = internMapper.toDto(intern);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInternMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(internDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Intern in the database
        List<Intern> internList = internRepository.findAll();
        assertThat(internList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIntern() throws Exception {
        // Initialize the database
        internRepository.saveAndFlush(intern);

        int databaseSizeBeforeDelete = internRepository.findAll().size();

        // Delete the intern
        restInternMockMvc
            .perform(delete(ENTITY_API_URL_ID, intern.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Intern> internList = internRepository.findAll();
        assertThat(internList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
