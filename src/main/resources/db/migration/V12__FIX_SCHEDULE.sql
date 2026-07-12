-- Remove a coluna antiga do relacionamento direto
ALTER TABLE schedule
DROP COLUMN IF EXISTS candidate_id;


-- Remove constraints antigas caso existam
ALTER TABLE schedule
DROP CONSTRAINT IF EXISTS fk_schedule_candidate;


-- Cria tabela intermediária do ManyToMany
CREATE TABLE IF NOT EXISTS schedule_candidates (
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