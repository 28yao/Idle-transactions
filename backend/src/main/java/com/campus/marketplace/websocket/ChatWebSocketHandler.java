package com.campus.marketplace.websocket;

import com.campus.marketplace.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    // userId -> WebSocketSession
    private static final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long userId = extractUserId(session);
        if (userId == null) {
            session.close(CloseStatus.NOT_ACCEPTABLE);
            return;
        }
        sessions.put(userId, session);
        log.info("WebSocket connected: userId={}", userId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long userId = extractUserId(session);
        if (userId != null) {
            sessions.remove(userId);
            log.info("WebSocket disconnected: userId={}", userId);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 心跳响应
        try {
            Map<String, Object> payload = objectMapper.readValue(message.getPayload(), Map.class);
            if ("ping".equals(payload.get("type"))) {
                session.sendMessage(new TextMessage("{\"type\":\"pong\"}"));
            }
        } catch (Exception e) {
            log.warn("Invalid WebSocket message: {}", e.getMessage());
        }
    }

    public void sendToUser(Long userId, Object message) {
        WebSocketSession session = sessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                String json = objectMapper.writeValueAsString(message);
                session.sendMessage(new TextMessage(json));
            } catch (IOException e) {
                log.error("Failed to send WebSocket message to userId={}: {}", userId, e.getMessage());
            }
        }
    }

    public boolean isOnline(Long userId) {
        WebSocketSession session = sessions.get(userId);
        return session != null && session.isOpen();
    }

    private Long extractUserId(WebSocketSession session) {
        // 从 URL 参数提取 token: ws://localhost:8080/ws/chat?token=xxx
        String query = session.getUri() != null ? session.getUri().getQuery() : null;
        if (query != null && query.startsWith("token=")) {
            String token = query.substring(6);
            try {
                return jwtUtil.getUserId(token);
            } catch (Exception e) {
                log.warn("Invalid WebSocket token: {}", e.getMessage());
            }
        }
        return null;
    }
}
