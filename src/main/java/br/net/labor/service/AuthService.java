package br.net.labor.service;

import br.net.labor.config.TokenService;
import br.net.labor.model.dto.typeUsers.LoginRequest;
import br.net.labor.model.dto.typeUsers.LoginResponse;
import br.net.labor.model.dto.typeUsers.candidate.CandidateRequest;
import br.net.labor.model.dto.typeUsers.candidate.CandidateResponse;
import br.net.labor.model.dto.typeUsers.company.CompanyRequestDTO;
import br.net.labor.model.dto.typeUsers.company.CompanyResponseDTO;
import br.net.labor.model.typeUser.Candidate;
import br.net.labor.model.typeUser.Company;
import br.net.labor.model.user.User;
import br.net.labor.model.user.enums.RolesEnumType;
import br.net.labor.repository.CandidateRepository;
import br.net.labor.repository.CompanyRepository;
import br.net.labor.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final CompanyRepository companyRepository;

    public AuthService(
            UserRepository userRepository, CandidateRepository candidateRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            TokenService tokenService, CompanyRepository companyRepository) {

        this.userRepository = userRepository;
        this.candidateRepository = candidateRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.companyRepository = companyRepository;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );
        User user = (User) authentication.getPrincipal();
        String token = tokenService.generateToken(user);
        return new LoginResponse(user.getEmail(), token);
    }

    public CompanyResponseDTO registerCompany(CompanyRequestDTO companyRequestDTO) {
        if (userRepository.findByEmail(companyRequestDTO.email()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        User user = new User();
        user.setEmail(companyRequestDTO.email());
        user.setPhoneNumber(companyRequestDTO.phoneNumber());
        user.setPassword(passwordEncoder.encode(companyRequestDTO.password()));
        user.setRole(RolesEnumType.ROLE_COMPANY);

        user = userRepository.save(user);

        Company company = new Company();
        company.setCompanyName(companyRequestDTO.companyName());
        company.setCnpj(companyRequestDTO.cnpj());
        company.setDescription(companyRequestDTO.description());
        company.setIndustry(companyRequestDTO.industry());
        company.setCompanyPhoto(companyRequestDTO.companyPhoto());

        company.setStatus("ACTIVE");

        company.setUser(user);

        company = companyRepository.save(company);

        return new CompanyResponseDTO(
                company.getCompanyName(),
                user.getEmail()
        );
    }

    public CandidateResponse registerCandidate(CandidateRequest candidateRequest) {
        if (userRepository.findByEmail(candidateRequest.email()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(candidateRequest.email());
        user.setPhoneNumber(candidateRequest.phoneNumber());
        user.setPassword(passwordEncoder.encode(candidateRequest.password()));
        user.setRole(RolesEnumType.ROLE_CANDIDATE);

        userRepository.save(user);

        Candidate candidate = new Candidate();
        candidate.setUsername(candidateRequest.username());
        candidate.setCpf(candidateRequest.cpf());
        candidate.setBirthDate(candidateRequest.birthDate());
        candidate.setUserPhoto(candidateRequest.userPhoto());
        candidate.setStatus(candidateRequest.status());
        candidate.setRealName(candidateRequest.realName());
        candidate.setUser(user);

        candidateRepository.save(candidate);

        return new CandidateResponse(
                candidate.getUsername(),
                user.getEmail()
        );
    }


    public void cadAdm() {
        User user = new User();
        user.setEmail("adm@gmail.com");
        user.setPassword(passwordEncoder.encode("admin123"));
        user.setRole(RolesEnumType.ROLE_ADMIN);
        userRepository.save(user);
    }


}
