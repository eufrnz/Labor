package br.net.labor.controller;

import br.net.labor.model.dto.jobs.JobsVacanciesRequestDTO;
import br.net.labor.model.dto.jobs.JobsVacanciesResponseWithCandidatesDTO;
import br.net.labor.model.dto.jobs.JobsVacanciesResponseWithOutCandidatesDTO;
import br.net.labor.service.JobVacanciesService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import br.net.labor.config.JWTUserData;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/jobsVacancies")
public class JobsVacanciesController {

    private final JobVacanciesService jobVacanciesService;

    public JobsVacanciesController(JobVacanciesService jobVacanciesService) {
        this.jobVacanciesService = jobVacanciesService;
    }

    @PostMapping
    public ResponseEntity<JobsVacanciesResponseWithOutCandidatesDTO> createJobs(@AuthenticationPrincipal JWTUserData userData, @RequestBody JobsVacanciesRequestDTO jobsVacanciesRequestDTO){
        if (userData == null) {
            throw new RuntimeException("Usuário não autenticado");
        }

        String emailFromLoggeduser = userData.email();

        JobsVacanciesResponseWithOutCandidatesDTO response = jobVacanciesService.createJobsVacancies(jobsVacanciesRequestDTO, emailFromLoggeduser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<JobsVacanciesResponseWithCandidatesDTO> getAll(){
        return jobVacanciesService.getAll();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable UUID id){
        jobVacanciesService.deleteById(id);
    }

}
