package br.net.labor.repository;

import br.net.labor.model.chat.ChatModel;
import br.net.labor.model.typeUser.Candidate;
import br.net.labor.model.typeUser.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<ChatModel, UUID> {
    List<ChatModel> findByCandidate(Candidate candidate);

    List<ChatModel> findByCompany(Company company);
}
