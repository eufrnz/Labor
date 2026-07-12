package br.net.labor.model.jobs;

import br.net.labor.model.candidateApplication.CandidateApplication;
import br.net.labor.model.schedule.Schedule;
import br.net.labor.model.typeUser.Candidate;
import br.net.labor.model.typeUser.Company;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "job_vacancies")
public class JobVacancies {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private String ability;
    private Double payValue;
    private LocalTime initTime;
    private LocalTime endTime;
    private LocalDate dateJob;
    private String description;
    @OneToMany(mappedBy = "job")
    private List<CandidateApplication> applications = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonIgnoreProperties("jobVacancies")
    private Company company;
    @OneToMany(mappedBy = "job")
    private List<Schedule> schedules = new ArrayList<>();

    public List<CandidateApplication> getApplications() {
        return applications;
    }

    public void setApplications(List<CandidateApplication> applications) {
        this.applications = applications;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public Double getPayValue() {
        return payValue;
    }

    public void setPayValue(Double payValue) {
        this.payValue = payValue;
    }

    public LocalTime getInitTime() {
        return initTime;
    }

    public void setInitTime(LocalTime initTime) {
        this.initTime = initTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalDate getDateJob() {
        return dateJob;
    }

    public void setDateJob(LocalDate dateJob) {
        this.dateJob = dateJob;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }
}
