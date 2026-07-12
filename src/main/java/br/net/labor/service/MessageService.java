package br.net.labor.service;

import br.net.labor.model.chat.Message;
import br.net.labor.model.chat.ChatModel;
import br.net.labor.model.dto.chat.MessageDTO;
import br.net.labor.model.user.User;
import br.net.labor.repository.ChatRepository;
import br.net.labor.repository.MessageRepository;
import br.net.labor.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;


    public MessageService(
            MessageRepository messageRepository,
            ChatRepository chatRepository,
            UserRepository userRepository
    ) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }


    public Message save(MessageDTO dto){

        ChatModel chat = chatRepository.findById(dto.chatId())
                .orElseThrow(() ->
                        new RuntimeException("Chat not found")
                );


        User sender = userRepository.findById(dto.senderId())
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );


        Message message = new Message();

        message.setChat(chat);
        message.setSender(sender);
        message.setContent(dto.content());
        message.setSentAt(LocalDateTime.now());


        return messageRepository.save(message);
    }


    public List<Message> findByChat(UUID chatId){

        return messageRepository.findByChatId(chatId);

    }
}