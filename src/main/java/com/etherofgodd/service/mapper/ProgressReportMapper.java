package com.etherofgodd.service.mapper;

import com.etherofgodd.domain.Intern;
import com.etherofgodd.domain.ProgressReport;
import com.etherofgodd.service.dto.InternDTO;
import com.etherofgodd.service.dto.ProgressReportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProgressReport} and its DTO {@link ProgressReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProgressReportMapper extends EntityMapper<ProgressReportDTO, ProgressReport> {
    @Mapping(target = "intern", source = "intern", qualifiedByName = "internId")
    ProgressReportDTO toDto(ProgressReport s);

    @Named("internId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InternDTO toDtoInternId(Intern intern);
}
