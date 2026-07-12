package br.net.labor.repository;

import br.net.labor.model.candidateApplication.ApplicationStatus;
import br.net.labor.model.candidateApplication.CandidateApplication;
import br.net.labor.model.jobs.JobVacancies;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<CandidateApplication, UUID> {


    List<CandidateApplication> findByJobAndStatus(
            JobVacancies job,
            ApplicationStatus status
    );
}
