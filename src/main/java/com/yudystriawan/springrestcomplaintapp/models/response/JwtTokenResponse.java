package com.yudystriawan.springrestcomplaintapp.models.response;

import lombok.Data;

@Data
public class JwtTokenResponse {

    private String acessToken;
    private String type = "Bearer";

    public JwtTokenResponse(String accessToken) {
        this.acessToken = accessToken;
    }
}
