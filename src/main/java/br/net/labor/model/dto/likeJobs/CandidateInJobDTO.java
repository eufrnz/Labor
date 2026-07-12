package br.net.labor.model.dto.likeJobs;

import br.net.labor.model.candidateApplication.ApplicationStatus;

import java.util.UUID;

public record CandidateInJobDTO(
        UUID id,
        String username,
        ApplicationStatus status
) {
}