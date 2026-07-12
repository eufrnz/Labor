package br.net.labor.controller;

import br.net.labor.model.candidateApplication.CandidateApplication;
import br.net.labor.model.dto.likeJobs.CandidateInJobDTO;
import br.net.labor.model.user.User;
import br.net.labor.service.CompanyLikeCandidates;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/likeInCandidates")
public class LikeInCandidatesController {

    private final CompanyLikeCandidates companyLikeCandidates;

    public LikeInCandidatesController(CompanyLikeCandidates companyLikeCandidates) {
        this.companyLikeCandidates = companyLikeCandidates;
    }

    @PostMapping("/{id}")
   public String like(@PathVariable UUID id){
    return companyLikeCandidates.likeCandidate(id);
    }

    @GetMapping
    public List<CandidateInJobDTO> selectedCandidates(){
        return companyLikeCandidates.selectedCandidates();
    }


}