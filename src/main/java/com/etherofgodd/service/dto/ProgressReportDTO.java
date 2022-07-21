package com.etherofgodd.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.etherofgodd.domain.ProgressReport} entity.
 */
public class ProgressReportDTO implements Serializable {

    private Long id;

    @NotNull
    private String date;

    private String status;

    private String remarks;

    private InternDTO intern;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public InternDTO getIntern() {
        return intern;
    }

    public void setIntern(InternDTO intern) {
        this.intern = intern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProgressReportDTO)) {
            return false;
        }

        ProgressReportDTO progressReportDTO = (ProgressReportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, progressReportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProgressReportDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", intern=" + getIntern() +
            "}";
    }
}
