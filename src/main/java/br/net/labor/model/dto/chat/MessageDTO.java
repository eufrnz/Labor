package br.net.labor.model.dto.chat;

import java.util.UUID;

public record MessageDTO(
        UUID chatId,
        UUID senderId,
        String content
) {
}
