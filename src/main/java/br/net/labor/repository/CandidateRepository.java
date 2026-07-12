package br.net.labor.repository;

import br.net.labor.model.typeUser.Candidate;
import br.net.labor.model.typeUser.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CandidateRepository extends JpaRepository<Candidate, UUID> {
    Optional<Candidate> findByUserEmail(String email);

    UUID id(UUID id);
}
