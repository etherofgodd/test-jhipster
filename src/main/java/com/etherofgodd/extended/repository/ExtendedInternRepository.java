package com.etherofgodd.extended.repository;

import com.etherofgodd.repository.InternRepository;
import com.etherofgodd.service.dto.InternDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtendedInternRepository extends InternRepository {
    List<InternDTO> findInternByInternId(String internId);
}
