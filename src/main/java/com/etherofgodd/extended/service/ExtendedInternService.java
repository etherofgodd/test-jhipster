package com.etherofgodd.extended.service;

import com.etherofgodd.domain.Intern;
import com.etherofgodd.service.dto.InternDTO;

import java.util.List;


public interface ExtendedInternService {
    void createIntern(InternDTO internDTO);
    List<Intern> findAllInterns();
    List<InternDTO> getInternsByInternId(String internId);
}
