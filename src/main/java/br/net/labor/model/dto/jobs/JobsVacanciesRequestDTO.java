package br.net.labor.model.dto.jobs;

import br.net.labor.model.typeUser.Company;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

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
