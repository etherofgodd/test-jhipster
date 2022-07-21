package com.etherofgodd.domain;

import com.etherofgodd.domain.enumeration.Company;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Intern.
 */
@Entity
@Table(name = "intern")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Intern implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "intern_id", nullable = false, unique = true)
    private String internId;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @NotNull
    @Column(name = "dob", nullable = false)
    private String dob;

    @NotNull
    @Column(name = "school", nullable = false)
    private String school;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "department", nullable = false)
    private Company department;

    @OneToMany(mappedBy = "intern")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "intern" }, allowSetters = true)
    private Set<ProgressReport> progressReports = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Intern id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Intern firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getInternId() {
        return this.internId;
    }

    public Intern internId(String internId) {
        this.setInternId(internId);
        return this;
    }

    public void setInternId(String internId) {
        this.internId = internId;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Intern lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Intern phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDob() {
        return this.dob;
    }

    public Intern dob(String dob) {
        this.setDob(dob);
        return this;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getSchool() {
        return this.school;
    }

    public Intern school(String school) {
        this.setSchool(school);
        return this;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Company getDepartment() {
        return this.department;
    }

    public Intern department(Company department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(Company department) {
        this.department = department;
    }

    public Set<ProgressReport> getProgressReports() {
        return this.progressReports;
    }

    public void setProgressReports(Set<ProgressReport> progressReports) {
        if (this.progressReports != null) {
            this.progressReports.forEach(i -> i.setIntern(null));
        }
        if (progressReports != null) {
            progressReports.forEach(i -> i.setIntern(this));
        }
        this.progressReports = progressReports;
    }

    public Intern progressReports(Set<ProgressReport> progressReports) {
        this.setProgressReports(progressReports);
        return this;
    }

    public Intern addProgressReport(ProgressReport progressReport) {
        this.progressReports.add(progressReport);
        progressReport.setIntern(this);
        return this;
    }

    public Intern removeProgressReport(ProgressReport progressReport) {
        this.progressReports.remove(progressReport);
        progressReport.setIntern(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Intern)) {
            return false;
        }
        return id != null && id.equals(((Intern) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Intern{" +
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
