package com.example.blackninja.dtos.common;

import com.example.blackninja.service.security.KeyHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.jwt.JwtHelper;

@Getter @Setter @NoArgsConstructor
public class AuthModel {
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String userReferenceId;
    private String role;
    private String partner;
    private int accessTokenValiditySeconds;

    public AuthModel(String firstName, String lastName, String fullName, String email, String userReferenceId, String role, String partner, int accessTokenValiditySeconds) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.email = email;
        this.userReferenceId = userReferenceId;
        this.role = role;
        this.partner = partner;
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    public String toJson(KeyHelper keyHelper) {
        Gson gson = new Gson();
        JsonObject obj = new JsonObject();
        String accessToken = getAccessTokenJson(gson);
        obj.addProperty("accessToken", JwtHelper.encode(accessToken, keyHelper.getSigner()).getEncoded());
        obj.addProperty("tokenType", "bearer");
        obj.addProperty("expiresIn", this.accessTokenValiditySeconds);
        return gson.toJson(obj);
    }

    private String getAccessTokenJson(Gson gson) {
        long currentEpochSeconds = System.currentTimeMillis() / 1000L;
        JsonObject obj = new JsonObject();
        obj.addProperty("firstname", this.firstName);
        obj.addProperty("lastname", this.lastName);
        obj.addProperty("email", this.email);
        obj.addProperty("name", this.fullName);
        obj.addProperty("userReferenceId", this.userReferenceId);
        obj.addProperty("role", this.role);
        obj.addProperty("partner", this.partner);
        obj.addProperty("exp", currentEpochSeconds + this.accessTokenValiditySeconds);
        return gson.toJson(obj);
    }
}
