package com.ws.config;
import com.ws.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

public class WebsocketChannelInterceptor  implements ChannelInterceptor {

    @Autowired
    private ApigeeAuthenticationClient apigeeAuthClient;

    // Processes a message before sending it
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        Object response = null;
        MessageHeaders messageHeaders = message.getHeaders();
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);


        // Instantiate an object for retrieving the STOMP headers
        // Check that the object is not null
        assert accessor != null;
        // If the frame is a CONNECT frame
        if (accessor.getCommand() == StompCommand.CONNECT) {

            // retrieve the opaque token from the headers
            String authorization = accessor.getFirstNativeHeader(Constants.Authorization);
            // retrieve the PKey from the headers
            String pKey = accessor.getFirstNativeHeader(Constants.PKey);
            // retrieve the TS from the headers
            String ts = accessor.getFirstNativeHeader(Constants.TS);
            // retrieve the DeviceId from the headers
            String deviceId = accessor.getFirstNativeHeader(Constants.DeviceId);

            try {
                response = apigeeAuthClient.authUsingApigee(authorization, deviceId, pKey, ts);
            } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
            }
        }
        return message;
    }
}