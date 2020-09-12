package com.example.blackninja.dtos.common;

import com.example.blackninja.service.security.KeyHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.jwt.JwtHelper;

import java.util.List;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor
public class AuthModel {
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String userReferenceId;
    private List<String> role;
    private String partner;
    private int accessTokenValiditySeconds;
    private int refreshTokenValiditySeconds;
    private String accessTokenJti;
    private String refreshTokenJti;

    public AuthModel(String firstName, String lastName, String fullName, String email, String userReferenceId, List<String> role, String partner, int accessTokenValiditySeconds, int refreshTokenValiditySeconds) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.email = email;
        this.userReferenceId = userReferenceId;
        this.role = role;
        this.partner = partner;
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
        this.accessTokenJti = UUID.randomUUID().toString();
        this.refreshTokenJti = UUID.randomUUID().toString();
    }

    public String toJson(KeyHelper keyHelper) {
        Gson gson = new Gson();
        JsonObject obj = new JsonObject();
        String accessToken = getAccessTokenJson(gson);
        String refreshToken = getRefreshToken(gson);
        obj.addProperty("accessToken", JwtHelper.encode(accessToken, keyHelper.getSigner()).getEncoded());
        obj.addProperty("refreshToken", JwtHelper.encode(refreshToken, keyHelper.getSigner()).getEncoded());
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
        obj.addProperty("roles", StringUtils.join(role, ','));
        obj.add("authorities", array(role));
        obj.addProperty("partner", this.partner);
        obj.addProperty("jti", this.accessTokenJti);
        obj.addProperty("exp", currentEpochSeconds + this.accessTokenValiditySeconds);
        return gson.toJson(obj);
    }

    private String getRefreshToken(Gson gson) {
        long currentEpochSeconds = System.currentTimeMillis() / 1000L;
        JsonObject obj = new JsonObject();
        obj.addProperty("firstname", this.firstName);
        obj.addProperty("lastname", this.lastName);
        obj.addProperty("email", this.email);
        obj.addProperty("name", this.fullName);
        obj.addProperty("userReferenceId", this.userReferenceId);
        obj.addProperty("roles", StringUtils.join(role, ','));
        obj.add("authorities", array(role));
        obj.addProperty("partner", this.partner);
        obj.addProperty("jti", this.refreshTokenJti);
        obj.addProperty("ati", this.refreshTokenJti);
        obj.addProperty("exp", currentEpochSeconds + this.refreshTokenValiditySeconds);
        return gson.toJson(obj);
    }

    private JsonArray array(List<String> values) {
        JsonArray arr = new JsonArray();
        for (String value : values) {
            arr.add(value);
        }
        return arr;
    }
}
