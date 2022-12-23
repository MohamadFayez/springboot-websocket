package com.ws.config;

import com.ws.constant.Constants;
import com.ws.payload.ApigeePOJO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeHandler;

import java.util.Map;

@Service
public class WebsocketHandshakeHandler implements HandshakeHandler {

    @Autowired
    private ApigeeAuthenticationClient apigeeAuthClient;

    @Override
    public boolean doHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws HandshakeFailureException {

        HttpHeaders httpHeaders = request.getHeaders();
        // retrieve the opaque token from the headers
        String authorization = httpHeaders.getFirst(Constants.Authorization);
        // retrieve the PKey from the headers
        String pKey = httpHeaders.getFirst(Constants.PKey);
        // retrieve the TS from the headers
        String ts = httpHeaders.getFirst(Constants.TS);
        // retrieve the DeviceId from the headers
        String deviceId = httpHeaders.getFirst(Constants.DeviceId);

        try {
            ApigeePOJO apigeePOJO = (ApigeePOJO) apigeeAuthClient.authUsingApigee(authorization, deviceId, pKey, ts);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


}
