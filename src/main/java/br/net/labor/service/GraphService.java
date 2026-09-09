package br.net.labor.service;

import br.net.labor.model.candidateApplication.ApplicationStatus;
import br.net.labor.model.dto.jobs.JobsVacanciesResponseWithCandidatesDTO;
import br.net.labor.model.jobs.JobVacancies;
import br.net.labor.repository.ApplicationRepository;
import br.net.labor.repository.JobVacanciesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GraphService {

    private final JobVacanciesRepository jobVacanciesRepository;
    private final JobVacanciesService jobVacanciesService;
    private final ApplicationRepository applicationRepository;

    public GraphService(JobVacanciesRepository jobVacanciesRepository, ApplicationRepository applicationRepository, JobVacanciesService jobVacanciesService) {
        this.jobVacanciesRepository = jobVacanciesRepository;
        this.applicationRepository = applicationRepository;
        this.jobVacanciesService = jobVacanciesService;
    }

    public Long countCandidatesInJob(UUID jobId){
        return applicationRepository.countByJobIdAndStatus(jobId, ApplicationStatus.SELECTED);
    }

    public double calculateEnterpriseExpenses(String email){
        List<JobVacancies> jobVacancies = jobVacanciesRepository.findByCompanyUserEmail(email);
        double total = 0D;
        for (JobVacancies job : jobVacancies){
            var candidates = applicationRepository.countByJobAndStatus(job, ApplicationStatus.SELECTED);
            total = job.getPayValue() * candidates;
        }
        return total;
    }

    public int countJobs(String email){
        List<JobVacancies> jobVacancies = jobVacanciesRepository.findByCompanyUserEmail(email);
        return jobVacancies.size();
    }
}
