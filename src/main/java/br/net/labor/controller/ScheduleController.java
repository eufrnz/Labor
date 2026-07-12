package br.net.labor.controller;

import br.net.labor.model.candidateApplication.CandidateApplication;
import br.net.labor.model.dto.schedule.JobWithSelectedsResponseDTO;
import br.net.labor.model.schedule.Schedule;
import br.net.labor.service.ScheduleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/{id}")
    public JobWithSelectedsResponseDTO generateSchedule(@PathVariable UUID id){
        return scheduleService.generateSchedule(id);
    }
    //precisa fazer DTO
//    @GetMapping("/candidate/{id}")
//    public List<Schedule> findByCandidate(@PathVariable UUID id){
//        return scheduleService.findByCandidate(id);
//    }
    //precisa fazer DTO
    @GetMapping("/job/{id}")
    public List<Schedule> findByJob(@PathVariable UUID id){
        return scheduleService.findByJob(id);
    }
}
