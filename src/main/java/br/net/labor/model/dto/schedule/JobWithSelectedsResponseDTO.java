package br.net.labor.model.dto.schedule;

import br.net.labor.model.dto.jobs.JobsVacanciesResponseWithOutCandidatesDTO;
import br.net.labor.model.dto.likeJobs.CandidateInJobDTO;

import java.util.List;

public record JobWithSelectedsResponseDTO(
        JobsVacanciesResponseWithOutCandidatesDTO jobsVacanciesResponseWithOutCandidatesDTO,
        List<CandidateInJobDTO> candidates
) {
}
