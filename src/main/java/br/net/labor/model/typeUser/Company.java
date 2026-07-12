package br.net.labor.model.typeUser;

import br.net.labor.model.chat.ChatModel;
import br.net.labor.model.jobs.JobVacancies;
import br.net.labor.model.schedule.Schedule;
import br.net.labor.model.user.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String companyName;
    private String cnpj;
    private String description;
    private String industry;
    private String companyPhoto;
    private String status;
    @OneToMany(mappedBy = "company")
    private List<JobVacancies> jobVacancies = new ArrayList<>();
    @OneToMany(mappedBy = "company")
    private List<Schedule> schedules = new ArrayList<>();
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<ChatModel> chats = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCompanyPhoto() {
        return companyPhoto;
    }

    public void setCompanyPhoto(String companyPhoto) {
        this.companyPhoto = companyPhoto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<JobVacancies> getJobVacancies() {
        return jobVacancies;
    }

    public void setJobVacancies(List<JobVacancies> jobVacancies) {
        this.jobVacancies = jobVacancies;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public List<ChatModel> getChats() {
        return chats;
    }

    public void setChats(List<ChatModel> chats) {
        this.chats = chats;
    }
}
