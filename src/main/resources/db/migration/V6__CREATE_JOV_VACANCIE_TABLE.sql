CREATE TABLE job_vacancies (
                                id UUID PRIMARY KEY,
                                title VARCHAR(255) NOT NULL,
                                ability VARCHAR(255),
                                pay_value NUMERIC(10, 2),
                                init_time TIME,
                                end_time TIME,
                                date_job DATE,
                                description TEXT,
                                company_id UUID,

                                CONSTRAINT fk_job_vacancies_company
                                    FOREIGN KEY (company_id)
                                        REFERENCES company(id)
                                        ON DELETE SET NULL
);