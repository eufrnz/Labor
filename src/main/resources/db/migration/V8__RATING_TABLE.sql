CREATE TABLE rating (
    id UUID PRIMARY KEY,
    rating INTEGER DEFAULT 0,
    rating_description VARCHAR(255),
    sent_by VARCHAR(255),
    user_id UUID NOT NULL,

    CONSTRAINT fk_rating_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE
);