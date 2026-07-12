package br.net.labor.repository;

import br.net.labor.model.chat.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findByChatId(UUID chatId);

}
