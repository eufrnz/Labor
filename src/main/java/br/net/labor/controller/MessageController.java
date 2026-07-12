package br.net.labor.controller;

import br.net.labor.model.chat.Message;
import br.net.labor.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/messages")
public class MessageController {


    private final MessageService messageService;


    public MessageController(
            MessageService messageService
    ){
        this.messageService = messageService;
    }


    @GetMapping("/{chatId}")
    public List<Message> findMessages(
            @PathVariable UUID chatId
    ){

        return messageService.findByChat(chatId);
    }
}