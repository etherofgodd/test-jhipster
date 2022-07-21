package com.etherofgodd.service.dto;

import com.etherofgodd.domain.enumeration.Company;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.etherofgodd.domain.Intern} entity.
 */
public class InternDTO implements Serializable {

    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String internId;

    @NotNull
    private String lastName;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String dob;

    @NotNull
    private String school;

    @NotNull
    private Company department;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getInternId() {
        return internId;
    }

    public void setInternId(String internId) {
        this.internId = internId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Company getDepartment() {
        return department;
    }

    public void setDepartment(Company department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InternDTO)) {
            return false;
        }

        InternDTO internDTO = (InternDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, internDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InternDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", internId='" + getInternId() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", dob='" + getDob() + "'" +
            ", school='" + getSchool() + "'" +
            ", department='" + getDepartment() + "'" +
            "}";
    }
}
