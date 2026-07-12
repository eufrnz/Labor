package br.net.labor.service;

import br.net.labor.model.dto.rate.RateRequestDTO;
import br.net.labor.model.dto.rate.RatingResponseDTO;
import br.net.labor.model.hate.Rating;

import br.net.labor.model.typeUser.Candidate;

import br.net.labor.model.typeUser.Company;
import br.net.labor.model.user.User;
import br.net.labor.model.user.enums.RolesEnumType;
import br.net.labor.repository.CandidateRepository;
import br.net.labor.repository.CompanyRepository;
import br.net.labor.repository.RatingRepository;
import br.net.labor.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RatingService {

    public final RatingRepository ratingRepository;
    public final CompanyRepository companyRepository;
    private final CandidateRepository candidateRepository;
    private final UserRepository userRepository;

    public RatingService(RatingRepository ratingRepository, CompanyRepository companyRepository, CandidateRepository candidateRepository, UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.companyRepository = companyRepository;
        this.candidateRepository = candidateRepository;
        this.userRepository = userRepository;
    }

    public RatingResponseDTO ratingCompany(RateRequestDTO rateRequestDTO, String email, UUID idUser) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User userLogged = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Rating rating = new Rating();
        rating.setRating(rateRequestDTO.rating());
        rating.setRatingDescription(rateRequestDTO.ratingDescription());

        //candidate rating company
        if (userLogged.getRole() == RolesEnumType.ROLE_CANDIDATE) {
            if (userLogged.getId() == user.getId()) {
                throw new RuntimeException("You cannot self evaluate");
            }
            if (userLogged.getRole() == user.getRole()) {
                throw new RuntimeException("You cannot rate a candidate");
            }
            Candidate candidate = candidateRepository.findByUserEmail(email)
                    .orElseThrow(() -> new RuntimeException("Candidate not found"));
            rating.setUser(user);
            rating.setSentBy(candidate.getUsername());
            userRepository.save(user);
            var savedRateCompany = ratingRepository.save(rating);
            return new RatingResponseDTO(
                    savedRateCompany.getId(),
                    savedRateCompany.getRating(),
                    savedRateCompany.getRatingDescription(),
                    candidate.getUsername(),
                    user.getUsername()
            );
        }
        //company ranting candidate
        if (userLogged.getRole() == RolesEnumType.ROLE_COMPANY) {
            if (userLogged.getId() == user.getId()) {
                throw new RuntimeException("You cannot self evaluate");
            }
            if (userLogged.getRole() == user.getRole()) {
                throw new RuntimeException("You cannot rate a company");
            }
            Company company = companyRepository.findByUserEmail(email)
                    .orElseThrow(() -> new RuntimeException("company not found"));
            rating.setUser(user);
            rating.setSentBy(company.getCompanyName());
            userRepository.save(user);
            var savedRateForCandidate = ratingRepository.save(rating);
            return new RatingResponseDTO(
                    savedRateForCandidate.getId(),
                    savedRateForCandidate.getRating(),
                    savedRateForCandidate.getRatingDescription(),
                    company.getCompanyName(),
                    user.getUsername()
            );
        }
        throw new RuntimeException("Invalid role.");
    }

    public List<RatingResponseDTO> getAll() {
        return ratingRepository.findAll().stream()
                .map(rating -> new RatingResponseDTO(
                        rating.getId(),
                        rating.getRating(),
                        rating.getRatingDescription(),
                        rating.getSentBy(),
                        rating.getUser().getUsername()
                )).toList();
    }

}
