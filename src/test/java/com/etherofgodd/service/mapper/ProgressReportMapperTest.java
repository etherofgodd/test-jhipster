package com.etherofgodd.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProgressReportMapperTest {

    private ProgressReportMapper progressReportMapper;

    @BeforeEach
    public void setUp() {
        progressReportMapper = new ProgressReportMapperImpl();
    }
}
