package com.etherofgodd.service.mapper;

import com.etherofgodd.domain.Intern;
import com.etherofgodd.service.dto.InternDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Intern} and its DTO {@link InternDTO}.
 */
@Mapper(componentModel = "spring")
public interface InternMapper extends EntityMapper<InternDTO, Intern> {}
