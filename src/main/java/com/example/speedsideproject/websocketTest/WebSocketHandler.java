//package com.example.speedsideproject.websocketTest;
//
//
//import com.sun.xml.bind.Utils;
//import lombok.extern.slf4j.Slf4j;
//import lombok.var;
//
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Slf4j
//public class WebSocketHandler extends TextWebSocketHandler {
//    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
//        var sessionId = session.getId();
//        sessions.put(sessionId, session);
//
//        Message message = Message.builder().sender(sessionId).receiver("all").build();
//        message.newConnect();
//
//        sessions.values().forEach(s -> {
//            try {
//                if(!s.getId().equals(sessionId)) {
//                    s.sendMessage(new TextMessage(Utils.ge));
//                }
//            }
//            catch (Exception e){
//
//            }
//        });
//    }
//}
