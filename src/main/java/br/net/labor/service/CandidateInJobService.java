package br.net.labor.service;

import br.net.labor.model.candidateApplication.ApplicationStatus;
import br.net.labor.model.candidateApplication.CandidateApplication;
import br.net.labor.model.dto.likeJobs.LikeInJobsResponseDTO;
import br.net.labor.model.jobs.JobVacancies;
import br.net.labor.model.typeUser.Candidate;
import br.net.labor.repository.ApplicationRepository;
import br.net.labor.repository.CandidateRepository;
import br.net.labor.repository.JobVacanciesRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CandidateInJobService {

    private final JobVacanciesRepository jobVacanciesRepository;
    private final CandidateRepository candidateRepository;
    private final ApplicationRepository applicationRepository;

    public CandidateInJobService(JobVacanciesRepository jobVacanciesRepository, CandidateRepository candidateRepository, ApplicationRepository applicationRepository) {
        this.jobVacanciesRepository = jobVacanciesRepository;
        this.candidateRepository = candidateRepository;
        this.applicationRepository = applicationRepository;
    }

    public LikeInJobsResponseDTO likeJobs(String email, UUID id) {
        Candidate candidate = candidateRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        JobVacancies jobVacancies = jobVacanciesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));
        CandidateApplication application = new CandidateApplication();

        application.setCandidate(candidate);
        application.setJob(jobVacancies);
        application.setStatus(ApplicationStatus.APPLIED);
        application.setAppliedAt(LocalDateTime.now());
        applicationRepository.save(application);

        return new LikeInJobsResponseDTO(
                jobVacancies.getTitle(),
                candidate.getUsername()
        );

    }

}
