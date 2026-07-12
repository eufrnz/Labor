package br.net.labor.repository;

import br.net.labor.model.jobs.JobVacancies;
import br.net.labor.model.schedule.Schedule;
import br.net.labor.model.typeUser.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    List<Schedule> findByCandidatesContaining(Candidate candidate);

    List<Schedule> findByJob(JobVacancies job);
}
