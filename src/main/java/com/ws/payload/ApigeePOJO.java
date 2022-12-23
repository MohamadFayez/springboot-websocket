package com.ws.payload;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ApigeePOJO {
    public int code;
    public ArrayList<String> message;
    public String reason;
    public JwtDTO data;
}
