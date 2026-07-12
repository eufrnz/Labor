package br.net.labor.service;

import br.net.labor.model.candidateApplication.ApplicationStatus;
import br.net.labor.model.candidateApplication.CandidateApplication;
import br.net.labor.model.dto.likeJobs.CandidateInJobDTO;
import br.net.labor.model.jobs.JobVacancies;
import br.net.labor.repository.ApplicationRepository;
import br.net.labor.repository.CandidateRepository;
import br.net.labor.repository.CompanyRepository;
import br.net.labor.repository.JobVacanciesRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class CompanyLikeCandidates {

    private final CandidateRepository candidateRepository;
    private final JobVacanciesRepository jobVacanciesRepository;
    private final CompanyRepository companyRepository;
    private final ApplicationRepository applicationRepository;

    public CompanyLikeCandidates(CandidateRepository candidateRepository, JobVacanciesRepository jobVacanciesRepository, CompanyRepository companyRepository, ApplicationRepository applicationRepository) {
        this.candidateRepository = candidateRepository;
        this.jobVacanciesRepository = jobVacanciesRepository;
        this.companyRepository = companyRepository;
        this.applicationRepository = applicationRepository;
    }

    public String likeCandidate(UUID idApplication) {
        CandidateApplication application = applicationRepository.findById(idApplication)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        application.setStatus(ApplicationStatus.SELECTED);
        applicationRepository.save(application);

        return "candidate selected";

    }

    public List<CandidateInJobDTO> selectedCandidates() {
        List<CandidateInJobDTO> result = new ArrayList<>();

        List<JobVacancies> jobs = jobVacanciesRepository.findAll();
        for (JobVacancies jobVacancies : jobs) {
            for (CandidateApplication application : jobVacancies.getApplications()) {
                if (application.getStatus() == ApplicationStatus.SELECTED) {
                    result.add(
                            new CandidateInJobDTO(
                                    application.getCandidate().getId(),
                                    application.getCandidate().getUsername(),
                                    application.getStatus()
                            )
                    );
                }
            }
        }
        return result;
    }


}
