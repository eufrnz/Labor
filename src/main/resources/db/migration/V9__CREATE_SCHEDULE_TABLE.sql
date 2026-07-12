CREATE TABLE IF NOT EXISTS schedule(
    id UUID PRIMARY KEY,
    company_id UUID,
    candidate_id UUID,
    job_id UUID,
    work_date DATE,
    start_time TIME,
    end_time TIME,
    payment NUMERIC(10, 2),

    CONSTRAINT fk_schedule_company
        FOREIGN KEY (company_id)
            REFERENCES company(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_schedule_candidate
        FOREIGN KEY (candidate_id)
            REFERENCES candidate(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_schedule_job
        FOREIGN KEY (job_id)
            REFERENCES job_vacancies(id)
            ON DELETE CASCADE
);

