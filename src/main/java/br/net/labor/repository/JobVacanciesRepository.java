package br.net.labor.repository;

import br.net.labor.model.jobs.JobVacancies;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobVacanciesRepository extends JpaRepository<JobVacancies, UUID> {
    List<JobVacancies> findByCompanyUserEmail(String email);
}
