package br.net.labor.controller;

import br.net.labor.model.chat.Message;
import br.net.labor.model.dto.chat.MessageDTO;
import br.net.labor.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Controller
public class ChatWebSocketController {


    private final SimpMessagingTemplate template;
    private final MessageService messageService;


    public ChatWebSocketController(SimpMessagingTemplate template, MessageService messageService) {
        this.template = template;
        this.messageService = messageService;
    }

    @MessageMapping("/chat/send")
    public void sendMessage(MessageDTO dto){

        Message message =
                messageService.save(dto);


        template.convertAndSend(
                "/topic/chat/" + dto.chatId(),
                message
        );
    }

    @GetMapping("/{chatId}")
    public List<Message> findMessages(
            @PathVariable UUID chatId
    ){
        return messageService.findByChat(chatId);
    }
}
