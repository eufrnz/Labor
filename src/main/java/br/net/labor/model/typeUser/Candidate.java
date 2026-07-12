package br.net.labor.model.typeUser;

import br.net.labor.model.candidateApplication.CandidateApplication;
import br.net.labor.model.chat.ChatModel;
import br.net.labor.model.jobs.JobVacancies;
import br.net.labor.model.schedule.Schedule;
import br.net.labor.model.user.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "candidate")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;
    private String cpf;
    private LocalDate birthDate;
    private String userPhoto;
    private String status;
    private String realName;
    @OneToMany(mappedBy = "candidate")
    private List<CandidateApplication> applications = new ArrayList<>();
    @ManyToMany(mappedBy = "candidates")
    private List<Schedule> schedules = new ArrayList<>();
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
    private List<ChatModel> chats = new ArrayList<>();

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

    public List<CandidateApplication> getApplications() {
        return applications;
    }

    public void setApplications(List<CandidateApplication> applications) {
        this.applications = applications;
    }

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
