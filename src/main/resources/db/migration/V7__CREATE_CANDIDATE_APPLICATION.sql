CREATE TABLE candidate_applications (
    id UUID PRIMARY KEY,
    candidate_id UUID NOT NULL,
    job_id UUID NOT NULL,
    status VARCHAR(50) DEFAULT 'APPLIED',
    applied_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_candidate_application_candidate
        FOREIGN KEY (candidate_id)
            REFERENCES candidate(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_candidate_application_job
        FOREIGN KEY (job_id)
            REFERENCES job_vacancies(id)
            ON DELETE CASCADE,

    CONSTRAINT uk_candidate_application_unique
        UNIQUE (candidate_id, job_id)
);
