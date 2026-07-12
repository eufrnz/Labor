CREATE TABLE chat (
                      id UUID PRIMARY KEY,
                      company_id UUID NOT NULL,
                      candidate_id UUID NOT NULL,

                      CONSTRAINT fk_chat_company
                          FOREIGN KEY (company_id)
                              REFERENCES company(id)
                              ON DELETE CASCADE,

                      CONSTRAINT fk_chat_candidate
                          FOREIGN KEY (candidate_id)
                              REFERENCES candidate(id)
                              ON DELETE CASCADE
);

CREATE TABLE message (
                         id UUID PRIMARY KEY,
                         chat_id UUID NOT NULL,
                         content TEXT NOT NULL,
                         sent_at TIMESTAMP NOT NULL,

                         CONSTRAINT fk_message_chat
                             FOREIGN KEY (chat_id)
                                 REFERENCES chat(id)
                                 ON DELETE CASCADE
);