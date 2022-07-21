package com.etherofgodd.extended.service.impl;

import com.etherofgodd.domain.Intern;
import com.etherofgodd.extended.repository.ExtendedInternRepository;
import com.etherofgodd.extended.service.ExtendedInternService;
import com.etherofgodd.extended.utils.GenerateInternId;
import com.etherofgodd.service.dto.InternDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExtendedInternServiceImpl implements ExtendedInternService {
    private final ExtendedInternRepository internRepository;

    @Autowired
    public ExtendedInternServiceImpl(ExtendedInternRepository internRepository) {
        this.internRepository = internRepository;
    }

    @Override
    public void createIntern(InternDTO internDTO) {
        Intern intern = new Intern();
        intern.setDepartment(internDTO.getDepartment());
        intern.setInternId(GenerateInternId.generate());
        intern.setDob(internDTO.getDob());
        intern.setFirstName(internDTO.getFirstName());
        intern.setLastName(internDTO.getLastName());
        intern.setPhoneNumber(internDTO.getPhoneNumber());
        intern.setSchool(internDTO.getSchool());
        internRepository.save(intern);
    }

    @Override
    public List<Intern> findAllInterns() {
        return internRepository.findAll();
    }

    @Override
    public List<InternDTO> getInternsByInternId(String internId) {
        return internRepository.findInternByInternId(internId);
    }
}
