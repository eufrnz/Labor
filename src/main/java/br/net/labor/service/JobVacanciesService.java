package br.net.labor.service;

import br.net.labor.model.dto.jobs.JobsVacanciesRequestDTO;
import br.net.labor.model.dto.jobs.JobsVacanciesResponseWithCandidatesDTO;
import br.net.labor.model.dto.jobs.JobsVacanciesResponseWithOutCandidatesDTO;
import br.net.labor.model.dto.likeJobs.CandidateInJobDTO;
import br.net.labor.model.jobs.JobVacancies;
import br.net.labor.model.schedule.Schedule;
import br.net.labor.model.typeUser.Company;
import br.net.labor.repository.CompanyRepository;
import br.net.labor.repository.JobVacanciesRepository;
import br.net.labor.repository.ScheduleRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class JobVacanciesService {

    private final JobVacanciesRepository jobVacanciesRepository;
    private final CompanyRepository companyRepository;
    private final ScheduleRepository scheduleRepository;

    public JobVacanciesService(JobVacanciesRepository jobVacanciesRepository, CompanyRepository companyRepository, ScheduleRepository scheduleRepository) {
        this.jobVacanciesRepository = jobVacanciesRepository;
        this.companyRepository = companyRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public JobsVacanciesResponseWithOutCandidatesDTO createJobsVacancies(JobsVacanciesRequestDTO jobsVacanciesRequestDTO, String email) {
        Company company = companyRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada para este usuário logado."));
        JobVacancies jobVacancies = getJobVacancies(jobsVacanciesRequestDTO, company);
        var savedJob = jobVacanciesRepository.save(jobVacancies);

        return new JobsVacanciesResponseWithOutCandidatesDTO(
                savedJob.getId(),
                savedJob.getTitle(),
                savedJob.getAbility(),
                savedJob.getPayValue(),
                savedJob.getInitTime(),
                savedJob.getEndTime(),
                savedJob.getDateJob(),
                savedJob.getDescription(),
                savedJob.getCompany().getCompanyName()
        );
    }


    public List<JobsVacanciesResponseWithCandidatesDTO> getAll() {
        return jobVacanciesRepository.findAll()
                .stream()
                .map(job -> new JobsVacanciesResponseWithCandidatesDTO(
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
                        ),
                        job.getApplications()
                                .stream()
                                .map(application -> new CandidateInJobDTO(
                                        application.getCandidate().getId(),
                                        application.getCandidate().getUsername(),
                                        application.getStatus()
                                ))
                                .toList()
                ))
                .toList();
    }


    public void deleteById(UUID id) {
        jobVacanciesRepository.deleteById(id);
    }


    private static @NonNull JobVacancies getJobVacancies(JobsVacanciesRequestDTO jobsVacanciesRequestDTO, Company company) {
        JobVacancies jobVacancies = new JobVacancies();

        jobVacancies.setTitle(jobsVacanciesRequestDTO.title());
        jobVacancies.setAbility(jobsVacanciesRequestDTO.ability());
        jobVacancies.setPayValue(jobsVacanciesRequestDTO.payValue());

        if (jobsVacanciesRequestDTO.dateJob().isBefore(LocalDate.now())) {
            throw new RuntimeException("Date invalid");
        }

        if (!jobsVacanciesRequestDTO.endTime()
                .isAfter(jobsVacanciesRequestDTO.initTime())) {
            throw new RuntimeException("End time must be after start time");
        }

        if (jobsVacanciesRequestDTO.dateJob().equals(LocalDate.now())
                && jobsVacanciesRequestDTO.endTime().isBefore(LocalTime.now())) {
            throw new RuntimeException("Job already finished");
        }

        jobVacancies.setInitTime(jobsVacanciesRequestDTO.initTime());
        jobVacancies.setEndTime(jobsVacanciesRequestDTO.endTime());
        jobVacancies.setDateJob(jobsVacanciesRequestDTO.dateJob());

        jobVacancies.setDescription(jobsVacanciesRequestDTO.description());
        jobVacancies.setCompany(company);

        return jobVacancies;
    }

    @Scheduled(fixedRate = 60000)
    private void finishJobVacancies() {
        List<JobVacancies> jobVacancies = jobVacanciesRepository.findAll();

        for (JobVacancies vacancy : jobVacancies) {
            LocalTime currentHour = LocalTime.now();
            LocalDate currentDay = LocalDate.now();
            if (!vacancy.getEndTime().isAfter(currentHour) && !vacancy.getDateJob().isAfter(currentDay)) {
                List<Schedule> schedules = scheduleRepository.findByJob(vacancy);
                scheduleRepository.deleteAll(schedules);
                jobVacanciesRepository.delete(vacancy);
            }
        }

    }

}
