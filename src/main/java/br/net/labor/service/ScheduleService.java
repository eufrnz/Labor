package br.net.labor.service;

import br.net.labor.model.candidateApplication.ApplicationStatus;
import br.net.labor.model.candidateApplication.CandidateApplication;
import br.net.labor.model.dto.jobs.JobsVacanciesResponseWithCandidatesDTO;
import br.net.labor.model.dto.jobs.JobsVacanciesResponseWithOutCandidatesDTO;
import br.net.labor.model.dto.likeJobs.CandidateInJobDTO;
import br.net.labor.model.dto.schedule.JobWithSelectedsResponseDTO;
import br.net.labor.model.dto.schedule.ScheduleForCandidate;
import br.net.labor.model.jobs.JobVacancies;
import br.net.labor.model.schedule.Schedule;
import br.net.labor.model.typeUser.Candidate;
import br.net.labor.model.typeUser.Company;
import br.net.labor.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ScheduleService {

    private final ApplicationRepository applicationRepository;
    private final JobVacanciesRepository jobVacanciesRepository;
    private final ScheduleRepository scheduleRepository;
    private final CandidateRepository candidateRepository;
    private final CompanyRepository companyRepository;
    private final ChatService chatService;

    public ScheduleService(ApplicationRepository applicationRepository, JobVacanciesRepository jobVacanciesRepository, ScheduleRepository scheduleRepository, CandidateRepository candidateRepository, CompanyRepository companyRepository, ChatService chatService) {
        this.applicationRepository = applicationRepository;
        this.jobVacanciesRepository = jobVacanciesRepository;
        this.scheduleRepository = scheduleRepository;
        this.candidateRepository = candidateRepository;
        this.companyRepository = companyRepository;
        this.chatService = chatService;
    }

    public JobWithSelectedsResponseDTO generateSchedule(UUID id){
        JobVacancies job = jobVacanciesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));
        List<CandidateApplication> selected = applicationRepository.findByJobAndStatus(job, ApplicationStatus.SELECTED);

        List<Schedule> schedules = selected.stream()
                .map( application -> {
                            Schedule schedule = new Schedule();
                            schedule.getCandidates().add(application.getCandidate());
                            schedule.setJob(job);
                            schedule.setCompany(job.getCompany());
                            schedule.setWorkDate(job.getDateJob());
                            schedule.setEndTime(job.getEndTime());
                            schedule.setPayment(job.getPayValue());
                            return schedule;
                        }
                ).toList();
        scheduleRepository.saveAll(schedules);
        Candidate candidate = new Candidate();
        candidate.setSchedules(schedules);
        candidateRepository.save(candidate);
        Company company = new Company();
        company.setSchedules(schedules);
        companyRepository.save(company);
        selected.forEach(application ->
                        chatService.generateChat(
                                application.getCandidate().getUser().getEmail()
                        )
                );


        List<CandidateInJobDTO> candidates = selected.stream()
                .map(application ->
                        new CandidateInJobDTO(
                                application.getCandidate().getId(),
                                application.getCandidate().getUsername(),
                                application.getStatus()
                        )
                )
                .toList();


        JobsVacanciesResponseWithOutCandidatesDTO jobDTO =
                new JobsVacanciesResponseWithOutCandidatesDTO(
                        job.getId(),
                        job.getTitle(),
                        job.getAbility(),
                        job.getPayValue(),
                        job.getInitTime(),
                        job.getEndTime(),
                        job.getDateJob(),
                        job.getDescription(),
                        job.getCompany().getCompanyName()
                );


        return new JobWithSelectedsResponseDTO(
                jobDTO,
                candidates
        );
    }

//    //precisa fazer DTO
//    public List<ScheduleForCandidate> findByCandidate(UUID id){
//
//        Candidate candidate = candidateRepository.findById(id)
//                .orElseThrow();
//        return new ScheduleForCandidate(
//                new JobsVacanciesResponseWithCandidatesDTO(
//                        new JobsVacanciesResponseWithOutCandidatesDTO()
//                )
//        )
//    }
    //precisa fazer DTO
    public List<Schedule> findByJob(UUID id){

        JobVacancies job = jobVacanciesRepository.findById(id)
                .orElseThrow();

        return scheduleRepository.findByJob(job);
    }
}
