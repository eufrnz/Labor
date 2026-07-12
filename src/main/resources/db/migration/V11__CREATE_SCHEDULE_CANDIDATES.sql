CREATE TABLE schedule_candidates (
                                     schedule_id UUID NOT NULL,
                                     candidate_id UUID NOT NULL,

                                     CONSTRAINT fk_schedule_candidates_schedule
                                         FOREIGN KEY (schedule_id)
                                             REFERENCES schedule(id)
                                             ON DELETE CASCADE,

                                     CONSTRAINT fk_schedule_candidates_candidate
                                         FOREIGN KEY (candidate_id)
                                             REFERENCES candidate(id)
                                             ON DELETE CASCADE,

                                     CONSTRAINT pk_schedule_candidates
                                         PRIMARY KEY (schedule_id, candidate_id)
);