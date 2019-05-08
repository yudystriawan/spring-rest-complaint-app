package com.yudystriawan.springrestcomplaintapp.securities;

import lombok.Data;

@Data
public class JwtResponse {

    private String token;
    private String type = "Bearer";

    public JwtResponse(String accessToken) {
        this.token = accessToken;
    }
}
