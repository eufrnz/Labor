package br.net.labor.service;


import br.net.labor.model.chat.ChatModel;
import br.net.labor.model.dto.chat.ChatResponseDTO;
import br.net.labor.model.dto.typeUsers.candidate.CandidateResponse;
import br.net.labor.model.dto.typeUsers.company.CompanyResponseDTO;
import br.net.labor.model.schedule.Schedule;
import br.net.labor.model.typeUser.Candidate;
import br.net.labor.model.typeUser.Company;
import br.net.labor.model.user.User;
import br.net.labor.model.user.enums.RolesEnumType;
import br.net.labor.repository.CandidateRepository;
import br.net.labor.repository.ChatRepository;
import br.net.labor.repository.CompanyRepository;
import br.net.labor.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    private final CandidateRepository candidateRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public ChatService(CandidateRepository candidateRepository, ChatRepository chatRepository, UserRepository userRepository, CompanyRepository companyRepository) {
        this.candidateRepository = candidateRepository;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    public void generateChat(String email){
        Candidate candidate = candidateRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Candidate not found."));
        for (Schedule schedule : candidate.getSchedules()) {
            ChatModel chat = new ChatModel();
            chat.setCandidate(candidate);
            chat.setCompany(schedule.getCompany());
            chatRepository.save(chat);
        }
    }

    public List<ChatResponseDTO> findUserChat(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
       if(user.getRole() == RolesEnumType.ROLE_CANDIDATE){
           Candidate candidate = candidateRepository.findByUserEmail(email)
                   .orElseThrow(() -> new RuntimeException("Candidate not found"));
           return chatRepository.findByCandidate(candidate)
                   .stream()
                   .map(chat -> new ChatResponseDTO(
                           chat.getId(),
                           new CandidateResponse(
                                   candidate.getUsername(),
                                   candidate.getUser().getEmail()
                           ),
                           new CompanyResponseDTO(
                                   chat.getCompany().getCompanyName(),
                                   chat.getCompany().getUser().getEmail()
                           )
                   ))
                   .toList();
       }

        Company company = companyRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        return chatRepository.findByCompany(company)
                .stream()
                .map(chat -> new ChatResponseDTO(
                        chat.getId(),
                        new CandidateResponse(
                                chat.getCandidate().getUsername(),
                                chat.getCandidate().getUser().getEmail()
                        ),
                        new CompanyResponseDTO(
                                company.getCompanyName(),
                                company.getUser().getEmail()
                        )
                ))
                .toList();
    }

}
