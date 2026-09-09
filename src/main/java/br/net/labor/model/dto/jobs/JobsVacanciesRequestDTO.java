package br.net.labor.model.dto.jobs;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record JobsVacanciesRequestDTO(String title,
                                      String ability,
                                      Double payValue,
                                      @JsonFormat(pattern = "HH:mm:ss")
                                      LocalTime initTime,
                                      @JsonFormat(pattern = "HH:mm:ss")
                                      LocalTime endTime,
                                      LocalDate dateJob,
                                      String description
) {
}
