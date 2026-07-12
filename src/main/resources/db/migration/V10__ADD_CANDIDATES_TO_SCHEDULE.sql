ALTER TABLE schedule
    ADD CONSTRAINT fk_schedule_company
        FOREIGN KEY (company_id)
            REFERENCES company(id)
            ON DELETE CASCADE;


ALTER TABLE schedule
    ADD CONSTRAINT fk_schedule_candidate
        FOREIGN KEY (candidate_id)
            REFERENCES candidate(id)
            ON DELETE CASCADE;


ALTER TABLE schedule
    ADD CONSTRAINT fk_schedule_job
        FOREIGN KEY (job_id)
            REFERENCES job_vacancies(id)
            ON DELETE CASCADE;