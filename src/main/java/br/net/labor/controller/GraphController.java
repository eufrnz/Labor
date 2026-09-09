package br.net.labor.controller;

import br.net.labor.config.JWTUserData;
import br.net.labor.service.GraphService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/graph")
public class GraphController {

    private final GraphService graphService;

    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    @GetMapping("/count-candidates-in-job")
    public ResponseEntity<Long> countCandidatesInJob(UUID jobId){
       return ResponseEntity.ok(graphService.countCandidatesInJob(jobId));
    }

    @GetMapping("/calculate-expenses")
    public ResponseEntity<Double> calculateEnterpriseExpenses(@AuthenticationPrincipal JWTUserData userData){
        if(userData == null){
            throw new RuntimeException("User not logged");
        }
        String emailFromLoggeduser = userData.email();
        return ResponseEntity.ok(graphService.calculateEnterpriseExpenses(emailFromLoggeduser));
    }

    @GetMapping("/count-jobs")
    public ResponseEntity<Integer> countJobs(@AuthenticationPrincipal JWTUserData userData){
        if(userData == null){
            throw new RuntimeException("User not logged");
        }
        String emailFromLoggeduser = userData.email();
        return ResponseEntity.ok(graphService.countJobs(emailFromLoggeduser));
    }
}
