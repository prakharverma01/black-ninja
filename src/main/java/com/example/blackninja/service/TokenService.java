package com.example.blackninja.service;

import com.example.blackninja.common.GeneralUtils;
import com.example.blackninja.exceptions.BadRequestException;
import com.example.blackninja.exceptions.UnauthorizedException;
import com.example.blackninja.model.Company;
import com.example.blackninja.model.JsonModel;
import com.example.blackninja.model.User;
import com.example.blackninja.model.UserCompanyEntitlement;
import com.example.blackninja.repository.CompanyRepository;
import com.example.blackninja.repository.UserCompanyEntitlementRepository;
import com.example.blackninja.service.security.KeyHelper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.OptionalLong;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private final KeyHelper keyHelper;
    private final UserService userService;
    private final UserCompanyEntitlementRepository userCompanyEntitlementRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public TokenService(KeyHelper keyHelper, UserService userService, UserCompanyEntitlementRepository userCompanyEntitlementRepository, CompanyRepository companyRepository) {
        this.keyHelper = keyHelper;
        this.userService = userService;
        this.userCompanyEntitlementRepository = userCompanyEntitlementRepository;
        this.companyRepository = companyRepository;
    }

    public String refreshToken(String refreshToken, String accessToken) {
        decodeToken(accessToken);
        JsonModel claims = decodeToken(refreshToken);
        if(!isRefreshToken(claims)) {
            throw new BadRequestException("this is not a refresh token");
        }
        if(!isTokenExpired(claims)) {
            String userReferenceId = claims.getAsString("userReferenceId");
            String companyName = claims.getAsString("partner");
            User user = userService.getUserByReferenceId(userReferenceId);
            Company company = companyRepository.findByCompanyNameIgnoreCase(companyName).orElseThrow(() -> new BadRequestException("cannot find company with name " + companyName));
            List<UserCompanyEntitlement> entitlements = userCompanyEntitlementRepository.findByUserAndCompany(user, company);
            List<String> roles = entitlements.stream()
                    .map(r -> r.getRole().label)
                    .collect(Collectors.toList());
            return userService.getAuthToken(
                    user.getFirstname(),
                    user.getLastname(),
                    GeneralUtils.getFullName(user.getFirstname(), user.getLastname()),
                    user.getEmail(),
                    user.getReference(),
                    roles,
                    company.getCompanyName(),
                    1800,
                    3600,
                    keyHelper
            );
        }
        throw new UnauthorizedException("token has expired please login again");
    }

    private JsonModel decodeToken(String refreshToken) {
        Jwt jwt;
        try {
            jwt = JwtHelper.decodeAndVerify(refreshToken, keyHelper.getVerifier());
            String claimsJson = jwt.getClaims();
            return new JsonModel(new Gson(), claimsJson);
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }

    }

    private boolean isRefreshToken(JsonModel claims) {
        return claims.has("ati");
    }

    private boolean isTokenExpired(JsonModel claims) {
        if(!claims.has("exp")) { throw new BadRequestException("invalid expiry time"); }
        OptionalLong exp = claims.getAsLong("exp");
        if(!exp.isPresent()) { throw new BadRequestException("Invalid expiry time"); }
        Date expiration = new Date(exp.getAsLong() * 1000L);
        return System.currentTimeMillis() > expiration.getTime();
    }
}
