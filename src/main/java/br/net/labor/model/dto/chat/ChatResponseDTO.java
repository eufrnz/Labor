package br.net.labor.model.dto.chat;

import br.net.labor.model.dto.typeUsers.candidate.CandidateResponse;
import br.net.labor.model.dto.typeUsers.company.CompanyResponseDTO;

import java.util.UUID;

public record ChatResponseDTO(
        UUID chatId,
        CandidateResponse candidate,
        CompanyResponseDTO company
) {}