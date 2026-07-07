package br.net.labor.controller;

import br.net.labor.config.JWTUserData;
import br.net.labor.model.dto.chat.ChatResponseDTO;
import br.net.labor.service.ChatService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public List<ChatResponseDTO>     findUserChat(@AuthenticationPrincipal JWTUserData userData){
        if(userData == null){
            throw new RuntimeException("Usuário não autenticado");
        }
        String userLogged = userData.email();
        return chatService.findUserChat(userLogged);
    }

}
