package br.net.labor.model.dto.jobs;

import br.net.labor.model.dto.likeJobs.CandidateInJobDTO;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public record JobsVacanciesResponseWithCandidatesDTO(
                                       JobsVacanciesResponseWithOutCandidatesDTO jobsVacanciesResponseWithOutCandidatesDTO,
                                       List<CandidateInJobDTO> candidates

) {
}
