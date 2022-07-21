package com.etherofgodd.extended.config;

public interface ApplicationUrl {
    String BASE_CONTEXT_URL = "/api/v1/interns";
    String INTERS_ALL = "/all";

    String GET_INTERNS_BY_ID = "/{internId}";

    String CREATE_INTERN = "/initiate";
}
