package com.etherofgodd.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InternMapperTest {

    private InternMapper internMapper;

    @BeforeEach
    public void setUp() {
        internMapper = new InternMapperImpl();
    }
}
