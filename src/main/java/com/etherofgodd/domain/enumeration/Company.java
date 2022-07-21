package com.etherofgodd.domain.enumeration;

/**
 * The Company enumeration.
 */
public enum Company {
    STSL("SYSTEM_SPECS_LIMITED"),
    RPSL("REMITA_PAYMENT_SERVICE_LIMITED"),
    SYSTEMSPECS("SYSTEM_SPECS");

    private final String value;

    Company(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
