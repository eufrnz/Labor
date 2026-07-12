ALTER TABLE message
    sender_id UUID NOT NULL,
    CONSTRAINT fk_message_sender
    FOREIGN KEY(sender_id)
    REFERENCES users(id)