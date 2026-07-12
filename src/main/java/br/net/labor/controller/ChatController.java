package br.net.labor.controller;

import br.net.labor.config.JWTUserData;
import br.net.labor.model.dto.chat.ChatResponseDTO;
import br.net.labor.model.dto.typeUsers.candidate.CandidateResponse;
import br.net.labor.model.dto.typeUsers.company.CompanyResponseDTO;
import br.net.labor.repository.ChatRepository;
import br.net.labor.service.ChatService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatRepository chatRepository;

    public ChatController(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @GetMapping("/user/{userId}")
    public List<ChatResponseDTO> findChats(
            @PathVariable UUID userId
    ){

        return chatRepository.findChatsByUser(userId)
                .stream()
                .map(chat -> new ChatResponseDTO(
                        chat.getId(),
                        new CandidateResponse(
                                chat.getCandidate().getUsername(),
                                chat.getCandidate().getUser().getEmail()
                        ),
                        new CompanyResponseDTO(
                                chat.getCompany().getCompanyName(),
                                chat.getCompany().getUser().getEmail()
                        )
                ))
                .toList();
    }

}
