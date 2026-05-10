package com.nexchat.NexChat.service;

import com.nexchat.NexChat.modal.entity.Message;
import com.nexchat.NexChat.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SummarizeService {

    private final MessageRepository messageRepository;
    private final GeminiService geminiService;

    public SummarizeService(MessageRepository messageRepository, GeminiService geminiService) {
        this.messageRepository = messageRepository;
        this.geminiService = geminiService;
    }

    public String summarizeGroup(Long groupId) {
        List<Message> messages = messageRepository.findTop50ByChatRoomIdOrderBySentAtDesc(groupId);

        Collections.reverse(messages);

        if (messages.isEmpty()) {
            return "No messages found in this group to summarize.";
        }

        StringBuilder formatted = new StringBuilder();
        for (Message msg : messages) {
            formatted.append(msg.getSender())
                    .append(": ")
                    .append(msg.getContent())
                    .append("\n");
        }

        return geminiService.summarize(formatted.toString());
    }
}