package com.ws.config;

import com.ws.constant.Constants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Service
public class WebsocketHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        HttpHeaders httpHeaders = request.getHeaders();
        // retrieve the opaque token from the headers
        String authorization = httpHeaders.getFirst(Constants.Authorization);
        // retrieve the PKey from the headers
        String pKey = httpHeaders.getFirst(Constants.PKey);
        // retrieve the TS from the headers
        String ts = httpHeaders.getFirst(Constants.TS);
        // retrieve the DeviceId from the headers
        String deviceId = httpHeaders.getFirst(Constants.DeviceId);
        return checkNoNull(authorization, pKey, ts, deviceId);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }


    private boolean checkNoNull(Object... objects) {
        for (Object obj : objects) {
            if (obj == null) {
                return false;
            }
        }
        return true;
    }
}
