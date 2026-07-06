package br.net.labor.model.dto.schedule;

import br.net.labor.model.dto.jobs.JobsVacanciesResponseWithCandidatesDTO;
import br.net.labor.model.dto.likeJobs.CandidateInJobDTO;

import java.util.List;

public record ScheduleForCandidate(
        JobsVacanciesResponseWithCandidatesDTO jobsVacanciesResponseWithCandidatesDTO

) {
}
