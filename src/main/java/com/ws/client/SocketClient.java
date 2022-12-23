package com.ws.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

public class SocketClient {
    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    private final static WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

    public static void main(String[] args) throws Exception {
        SocketClient socketClient = new SocketClient();

        ListenableFuture<StompSession> f = socketClient.connect();
        StompSession stompSession = f.get();

        logger.info("Subscribing to message topic using session " + stompSession);
        socketClient.subscribeMessages(stompSession);
        logger.info("Sending message" + stompSession);
        socketClient.sendAllowed(stompSession);
    }

    public ListenableFuture<StompSession> connect() {

        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());


        WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
        webSocketHttpHeaders.add("Authorization", "Bearer 0002XD6YVxBYhOSlb8rsyJAgRA1y4Lv6RGk57DkLT0YHrbz2Cb0cgbeL2BK4NlxQ");
        webSocketHttpHeaders.add("PKey", "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEFx3lO7obkSWGWWCZtwX94qGbRayXqJYzUKohY76Fmiq8yHbQbpAQf9afcjBkxmkk6aTg494kry4TMN53hLTVQg==");
        webSocketHttpHeaders.add("TS", "+oqAlHq21tXDh9IPuDL3TA==");
        webSocketHttpHeaders.add("deviceId", "3965feb6d4e-30e3-51c9-b03a-d47f60f11d8f");

        String url = "ws://{host}:{port}/ws";
        return stompClient.connect(url, webSocketHttpHeaders, new StompClientHandler(), "localhost", 7084);
    }

    public void subscribeMessages(StompSession stompSession) throws ExecutionException, InterruptedException {
        stompSession.subscribe("/topic/messages", new StompFrameHandler() {

            public Type getPayloadType(StompHeaders stompHeaders) {
                return byte[].class;
            }

            public void handleFrame(StompHeaders stompHeaders, Object o) {
                logger.info("Received messages " + new String((byte[]) o));
            }
        });
    }

    public void sendAllowed(StompSession stompSession) {
        String json = "{ \"action\" : \"allowed\" }";
        stompSession.send("/app/ws", json.getBytes());
    }

}