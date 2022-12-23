package com.ws.config;

import com.ws.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ApigeeAuthenticationClient {
    private static final Logger logger = LoggerFactory.getLogger(ApigeeAuthenticationClient.class);
    public Object authUsingApigee(String authorization,String deviceId, String pKey, String ts) throws Exception{

        RestTemplate restTemplate = new RestTemplate();
        String uri
                = "http://apigee.dev.apps.apigee.dev.mdi:9005/api/v1/device-management/websocket/auth";
        HttpHeaders headers = new HttpHeaders();
        headers.add(Constants.Authorization, authorization);
        headers.add(Constants.DeviceId, deviceId);
        headers.add(Constants.PKey, pKey);
        headers.add(Constants.TS, ts);

        Object response = null;
        try {
            ResponseEntity<Object> responseEntity
                    = restTemplate.exchange
                    (uri, HttpMethod.GET, new HttpEntity<>(headers), Object.class);

            response = responseEntity.getBody();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return response;
    }
}
